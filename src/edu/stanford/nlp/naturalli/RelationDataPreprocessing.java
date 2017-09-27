package edu.stanford.nlp.naturalli;

import edu.stanford.nlp.ie.machinereading.structure.Span;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.naturalli.bean.Location;
import edu.stanford.nlp.naturalli.bean.Senetnce;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.process.ListProcessor;
import edu.stanford.nlp.process.TSVSentenceProcessor;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.semgraph.semgrex.SemgrexMatcher;
import edu.stanford.nlp.semgraph.semgrex.SemgrexPattern;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.ArgumentParser;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Pair;
import edu.stanford.nlp.util.logging.Redwood;
import org.apache.commons.lang3.StringEscapeUtils;
import org.omg.CORBA.Request;

import java.sql.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static edu.stanford.nlp.util.logging.Redwood.Util.endTrack;
import static edu.stanford.nlp.util.logging.Redwood.Util.forceTrack;
import static edu.stanford.nlp.util.logging.Redwood.Util.log;

public class RelationDataPreprocessing implements TSVSentenceProcessor {
    /** A logger for this class */
    private static Redwood.RedwoodChannels log = Redwood.channels(CreateClauseDataset.class);

    @ArgumentParser.Option(name="in", gloss="The input to read from")
    private static InputStream in = System.in;

    public RelationDataPreprocessing() {
    }


    private static Span toSpan(List<? extends HasIndex> chunk) {
        int min = Integer.MAX_VALUE;
        int max = -1;
        for (HasIndex word : chunk) {
            min = Math.min(word.index() - 1, min);
            max = Math.max(word.index(), max);
        }
        assert min >= 0;
        assert max < Integer.MAX_VALUE && max > 0;
        return new Span(min, max);
    }

    @Override
    public void process(long id, Annotation doc) {
        CoreMap sentence = doc.get(CoreAnnotations.SentencesAnnotation.class).get(0);
        SemanticGraph depparse = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
        log.info("| " + sentence.get(CoreAnnotations.TextAnnotation.class));

        // Get all valid subject spans
        BitSet consumedAsSubjects = new BitSet();
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") List<Span> subjectSpans = new ArrayList<>();
        NEXTNODE: for (IndexedWord head : depparse.topologicalSort()) {
            // Check if the node is a noun/pronoun
            if (head.tag().startsWith("N") || head.tag().equals("PRP")) {
                // Try to get the NP chunk
                Optional<List<IndexedWord>> subjectChunk = segmenter.getValidChunk(depparse, head, segmenter.VALID_SUBJECT_ARCS, Optional.empty(), true);
                if (subjectChunk.isPresent()) {
                    // Make sure it's not already a member of a larger NP
                    for (IndexedWord tok : subjectChunk.get()) {
                        if (consumedAsSubjects.get(tok.index())) {
                            continue NEXTNODE;  // Already considered. Continue to the next node.
                        }
                    }
                    // Register it as an NP
                    for (IndexedWord tok : subjectChunk.get()) {
                        consumedAsSubjects.set(tok.index());
                    }
                    // Add it as a subject
                    subjectSpans.add(toSpan(subjectChunk.get()));
                }
            }
        }
    }


    /**
     * The pattern for traces which are potential subjects
     */
    private static Pattern TRACE_TARGET_PATTERN = Pattern.compile("(NP-.*)-([0-9]+)");

    /**
     * The pattern for trace markers.
     */
    private static Pattern TRACE_SOURCE_PATTERN = Pattern.compile(".*\\*-([0-9]+)");

    /**
     * The converter from constituency to dependency trees.
     */
    private static UniversalEnglishGrammaticalStructureFactory parser = new UniversalEnglishGrammaticalStructureFactory();

    /**
     * The OpenIE segmenter to use.
     */
    private static RelationTripleSegmenter segmenter = new RelationTripleSegmenter();

    /**
     * The natural logic annotator for marking polarity.
     */
    private static NaturalLogicAnnotator natlog = new NaturalLogicAnnotator();

    /**
     * Parse a given constituency tree into a dependency graph.
     *
     * @param tree The constituency tree, in Penn Treebank style.
     * @return The dependency graph for the tree.
     */
    private static SemanticGraph parse(Tree tree) {
        return new SemanticGraph(parser.newGrammaticalStructure(tree).typedDependenciesCollapsed());
    }

    public static SemanticGraph parse2dependency(Tree tree) {
        return new SemanticGraph(parser.newGrammaticalStructure(tree).typedDependenciesCollapsed());
    }

    /**
     * Create a dataset of subject/object pairs, such that a sequence of splits that segments this
     * subject and object is a correct sequence.
     *
     * @param depparse The dependency parse of the sentence.
     * @param traceTargets The set of spans corresponding to targets of traces.
     * @param traceSources The set of indices in a sentence corresponding to the sources of traces.
     * @return A dataset of subject/object spans.
     */
    @SuppressWarnings("UnusedParameters")
    private static Collection<Pair<Span, Span>> subjectObjectPairs(SemanticGraph depparse,
                                                                   List<CoreLabel> tokens,
                                                                   Map<Integer, Span> traceTargets,
                                                                   Map<Integer, Integer> traceSources) {
//    log(StringUtils.join(tokens.stream().map(CoreLabel::word), " "));
        List<Pair<Span, Span>> data = new ArrayList<>();
        for (SemgrexPattern vpPattern : segmenter.VP_PATTERNS) {
            SemgrexMatcher matcher = vpPattern.matcher(depparse);
            while (matcher.find()) {
                // Get the verb and object
                IndexedWord verb = matcher.getNode("verb");
                IndexedWord object = matcher.getNode("object");
                if (verb != null && object != null) {
                    // See if there is already a subject attached
                    boolean hasSubject = false;
                    for (SemanticGraphEdge edge : depparse.outgoingEdgeIterable(verb)) {
                        if (edge.getRelation().toString().contains("subj")) {
                            hasSubject = true;
                        }
                    }
                    for (SemanticGraphEdge edge : depparse.outgoingEdgeIterable(object)) {
                        if (edge.getRelation().toString().contains("subj")) {
                            hasSubject = true;
                        }
                    }
                    if (!hasSubject) {
                        // Get the spans for the verb and object
                        Optional<List<IndexedWord>> verbChunk = segmenter.getValidChunk(depparse, verb, segmenter.VALID_ADVERB_ARCS, Optional.empty(), true);
                        Optional<List<IndexedWord>> objectChunk = segmenter.getValidChunk(depparse, object, segmenter.VALID_OBJECT_ARCS, Optional.empty(), true);
                        if (verbChunk.isPresent() && objectChunk.isPresent()) {
                            Collections.sort(verbChunk.get(), (a, b) -> a.index() - b.index());
                            Collections.sort(objectChunk.get(), (a, b) -> a.index() - b.index());
                            // Find a trace
                            int traceId = -1;
                            Span verbSpan = toSpan(verbChunk.get());
                            Span traceSpan = Span.fromValues(verbSpan.start() - 1, verbSpan.end() + 1);
                            for (Map.Entry<Integer, Integer> entry : traceSources.entrySet()) {
                                if (traceSpan.contains(entry.getValue())) {
                                    traceId = entry.getKey();
                                }
                            }
                            //noinspection StatementWithEmptyBody
                            if (traceId < 0) {
                                // Register the VP as an unknown VP
//                List<CoreLabel> vpChunk = new ArrayList<>();
//                vpChunk.addAll(verbChunk.get());
//                vpChunk.addAll(objectChunk.get());
//                Collections.sort(vpChunk, (a, b) -> a.index() - b.index());
//                debug("could not find trace for " + vpChunk);
                            } else {
                                // Add the obj chunk
                                Span subjectSpan = traceTargets.get(traceId);
                                Span objectSpan = toSpan(objectChunk.get());
                                if (subjectSpan != null) {
//                  debug("(" +
//                      StringUtils.join(tokens.subList(subjectSpan.start(), subjectSpan.end()).stream().map(CoreLabel::word), " ") + "; " +
//                      verb.word() + "; " +
//                      StringUtils.join(tokens.subList(objectSpan.start(), objectSpan.end()).stream().map(CoreLabel::word), " ") +
//                      ")");
                                    data.add(Pair.makePair(subjectSpan, objectSpan));
                                }
                            }
                        }
                    }
                }
            }
        }

        // Run vanilla pattern splits
        for (SemgrexPattern vpPattern : segmenter.VERB_PATTERNS) {
            SemgrexMatcher matcher = vpPattern.matcher(depparse);
            while (matcher.find()) {
                // Get the verb and object
                IndexedWord subject = matcher.getNode("subject");
                IndexedWord object = matcher.getNode("object");
                if (subject != null && object != null) {
                    Optional<List<IndexedWord>> subjectChunk = segmenter.getValidChunk(depparse, subject, segmenter.VALID_SUBJECT_ARCS, Optional.empty(), true);
                    Optional<List<IndexedWord>> objectChunk = segmenter.getValidChunk(depparse, object, segmenter.VALID_OBJECT_ARCS, Optional.empty(), true);
                    if (subjectChunk.isPresent() && objectChunk.isPresent()) {
                        Span subjectSpan = toSpan(subjectChunk.get());
                        Span objectSpan = toSpan(objectChunk.get());
                        data.add(Pair.makePair(subjectSpan, objectSpan));
                    }
                }
            }
        }

        return data;
    }

    /**
     * Collect all the possible targets for traces. This is limited to NP-style traces.
     *
     * @param root The tree to search in. This is a recursive function.
     * @return The set of trace targets. The key is the id of the trace, the value is the span of the target of the trace.
     */
    private static Map<Integer, Span> findTraceTargets(Tree root) {
        Map<Integer, Span> spansInTree = new HashMap<>(4);

        Matcher m = TRACE_TARGET_PATTERN.matcher(root.label().value() == null ? "NULL" : root.label().value());
        if (m.matches()) {
            int index = Integer.parseInt(m.group(2));
            spansInTree.put(index, Span.fromPair(root.getSpan()).toExclusive());
        }
        for (Tree child : root.children()) {
            spansInTree.putAll(findTraceTargets(child));
        }
        return spansInTree;
    }

    /**
     * Collect all the trace markers in the sentence.
     *
     * @param root The tree to search in. This is a recursive function.
     * @return A map of trace sources. The key is hte id of the trace, the value is the index of the trace's source in the sentence.
     */
    private static Map<Integer, Integer> findTraceSources(Tree root) {
        Map<Integer, Integer> spansInTree = new HashMap<>(4);

        Matcher m = TRACE_SOURCE_PATTERN.matcher(root.label().value() == null ? "NULL" : root.label().value());
        if (m.matches()) {
            int index = Integer.parseInt(m.group(1));
            spansInTree.put(index, ((CoreLabel) root.label()).index() - 1);
        }
        for (Tree child : root.children()) {
            spansInTree.putAll(findTraceSources(child));
        }
        return spansInTree;
    }

    /**
     * Count the number of extractions in the given dataset. That is, the sum count of the pair spans
     * for each sentence.
     *
     * @param data The dataset.
     * @return The number of extractions in the datasets..
     */
    private static int countDatums(List<Pair<CoreMap, Collection<Pair<Span,Span>>>> data) {
        int count = 0;
        for (Pair<CoreMap, Collection<Pair<Span, Span>>> datum : data) {
            count += datum.second.size();
        }
        return count;
    }


    private static List<Pair<CoreMap, Collection<Pair<Span, Span>>>> processTrees(List<String> sentences, List<Tree> parse_trees)throws Exception{
        forceTrack("Processing Parse Trees");

        // Prepare the files to iterate over
        int numTreesProcessed = 0;
        List<Pair<CoreMap, Collection<Pair<Span, Span>>>> trainingData = new ArrayList<>(1024);
        for(int index = 0; index < parse_trees.size(); index ++){
                Tree tree = parse_trees.get(index);
                // Prepare the tree
                tree.indexSpans();
                tree.setSpans();

                // Get relevant information from sentence
                List<CoreLabel> tokens = tree.getLeaves().stream()
                        .map(leaf -> (CoreLabel) leaf.label())
//            .filter(leaf -> !TRACE_SOURCE_PATTERN.matcher(leaf.word()).matches() && !leaf.tag().equals("-NONE-"))
                        .collect(Collectors.toList());
                SemanticGraph graph = parse(tree);
                Map<Integer, Span> targets = findTraceTargets(tree);
                Map<Integer, Integer> sources = findTraceSources(tree);

                // Create a sentence object
                CoreMap sentence = new ArrayCoreMap(4) {{
                    set(CoreAnnotations.TokensAnnotation.class, tokens);
                    set(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class, graph);
                    set(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class, graph);
                    set(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class, graph);
                }};
                natlog.doOneSentence(null, sentence);

                // Generate training data
                Collection<Pair<Span, Span>> trainingDataFromSentence = subjectObjectPairs(graph, tokens, targets, sources);
                trainingData.add(Pair.makePair(sentence, trainingDataFromSentence));

                // Debug print
                numTreesProcessed += 1;
                if (numTreesProcessed % 100 == 0) {
                    log("[" + new DecimalFormat("00000").format(numTreesProcessed) + "] " + countDatums(trainingData) + " known extractions");
                }
        }

        // End
        log("" + numTreesProcessed + " trees processed yielding " + countDatums(trainingData) + " known extractions");
        endTrack("End Processing Parse Trees" );
        return trainingData;
    }

    private static List<Tree> sent2trees(List<String> sentences) throws Exception{
        String grammar = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
        String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
        LexicalizedParser lp = LexicalizedParser.loadModel(grammar,options);
        TreebankLanguagePack tlp = lp.getOp().langpack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        List<Tree> trees = new ArrayList<>();

        for(String sentence : sentences){
            System.out.println("Processing sentence : \n" + sentence);
            Tokenizer<? extends HasWord> tokenizer = tlp.getTokenizerFactory().getTokenizer(new StringReader(sentence));
            List<? extends HasWord> sent2word = tokenizer.tokenize();
            trees.add(lp.parse(sent2word));
        }
        return trees;
    }

    public static List<String> loadAnnotaedSentences(String url) throws IOException{
        ArrayList<String> annotatedSentences = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(url));
        reader.readLine();
        String line = "";
        while ((line = reader.readLine()) != null){
            String segment = "";
            Pattern pattern = Pattern.compile("\"(.*?)\"");
            Matcher matcher = pattern.matcher(line);
            int counter = 0;
            for(int i = 0; i < 5; i++){
                matcher.find();
            }
            segment = matcher.group();
            segment = segment.substring(1,segment.length()-1);
            segment = decodeHtml(segment);
            annotatedSentences.add(segment);
        }
        return annotatedSentences;
    }

    public static Connection conn2database()throws Exception{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = null;
        //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataset?user=root&password=rootpass123!@#");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_dataset?user=root&password=rootpass123!@#");

//        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataset?user=root&password=20080808qwejkl");
        if(connection != null){
            System.out.println("Connect to mysql/nlp422 successfully!");
        }else {
            System.out.println("invalid connect to mysql/nlp422");
        }
        return connection;
    }

    public static void savedataset(List<String> sentences, List<Pair<CoreMap, Collection<Pair<Span, Span>>>> dataset, List<Tree> parse_trees)throws Exception{
        if(dataset.isEmpty())
            throw new Exception("dataset is empty");
        if(sentences.size() != dataset.size())
            throw new Exception("dataset doesn't match sentences");
        Connection connection = conn2database();
        PreparedStatement stmt = null;
        String sql_s = "INSERT INTO sentence(sentence.sid, sentence.sentence, sentence.parse_tree) VALUES(?,?,?)";
        String sql_l = "INSERT INTO location(location.sid, location.start_left, location.start_right, location.end_left, location.end_right) VALUES(?,?,?,?,?)";
        String sql_a = "SELECT COUNT(*) column_count FROM sentence";
        String sql_c = "SELECT COUNT(*) target_count FROM sentence WHERE sentence.sid = ?";
        //iterate all sentences
        for(int i = 0; i < sentences.size(); i++){

            int rows = 0;
            String sentence_num = "S#" + i;
            Tree parse_tree = parse_trees.get(i);
            Collection<Pair<Span, Span>> locations = dataset.get(i).second;
            ResultSet rs = null;
            stmt = connection.prepareStatement(sql_c);
            stmt.setString(1, sentence_num);
            rs = stmt.executeQuery();
            int target_count = 0;
            while (rs.next()){
                target_count = rs.getInt("target_count");
            }
            if(target_count != 0){
                stmt.close();
                stmt = connection.prepareStatement(sql_a);
                rs = stmt.executeQuery();
                int column_count = 0;
                while (rs.next()){
                    column_count = rs.getInt("column_count");
                }
                sentence_num = "S#" + column_count;
                stmt.close();
            }

            //saving sentence's all attributes to database
            stmt = connection.prepareStatement(sql_s);
            stmt.setString(2,sentences.get(i));
            stmt.setString(1,sentence_num);
            stmt.setString(3,parse_tree.toString());
            rows = stmt.executeUpdate();

            if(rows > 0){
                System.out.println("insert sentence "+ sentence_num + " successfully!");
            }

            stmt.close();
            rows = 0;

            //saving location of this sentence
            for(Pair<Span, Span> location : locations){
                Span left = location.first;
                Span right = location.second;
                stmt = connection.prepareStatement(sql_l);
                stmt.setString(1,sentence_num);
                stmt.setInt(2,left.start());
                stmt.setInt(3,right.start());
                stmt.setInt(4,left.end());
                stmt.setInt(5,right.end());
                rows += stmt.executeUpdate();
            }
            if(rows > 0){
                System.out.println("insert "+ rows + " relations into sentence "+ sentence_num+ " successfully!");
            }
            stmt.close();
        }
        connection.close();
    }

    public static List<Pair<CoreMap, Collection<Pair<Span, Span>>>> loadDataSetfromDataBase()throws Exception{
        List<Pair<CoreMap, Collection<Pair<Span, Span>>>> dataset = new ArrayList<Pair<CoreMap, Collection<Pair<Span, Span>>>>();
        ArrayList<Senetnce> sentences = new ArrayList<>();
        Connection connection = conn2database();
        String sql_s = "SELECT * FROM sentence";
        String sql_l = "SELECT * FROM location WHERE sid = ?";
        PreparedStatement preparedStatement = null;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql_s);
        while (rs.next()){
            Senetnce senetnce = new Senetnce();
            senetnce.setSentence(rs.getString(1));
            senetnce.setSid(rs.getString(2));
            senetnce.setParse_tree(rs.getString(3));
            sentences.add(senetnce);
        }
        stmt.close();
        rs.close();
        for(int i = 0; i < sentences.size(); i ++){
            System.out.println("Processing sentence : S#" + i);
            Senetnce senetnce = sentences.get(i);
            Tree tree = Tree.valueOf(senetnce.getParse_tree(), new LabeledScoredTreeReaderFactory());
            tree.indexSpans();
            tree.setSpans();
            List<CoreLabel> tokens = tree.getLeaves().stream().map(leaf -> (CoreLabel) leaf.label()).collect(Collectors.toList());
            SemanticGraph graph = parse(tree);

            // Create a sentence object
            CoreMap coreMap = new ArrayCoreMap(4) {{
                set(CoreAnnotations.TokensAnnotation.class, tokens);
                set(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class, graph);
                set(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class, graph);
                set(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class, graph);
            }};
            natlog.doOneSentence(null, coreMap);
            sentences.get(i).setCoreMap(coreMap);
        }
        for(Senetnce senetnce : sentences){
            Pair<CoreMap, Collection<Pair<Span, Span>>> datapair = new Pair<>();
            ArrayList<Pair<Span,Span>> locations = new ArrayList<>();
            preparedStatement = connection.prepareStatement(sql_l);
            preparedStatement.setString(1,senetnce.getSid());
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                Pair<Span,Span> pair = new Pair<>();
                pair.first = new Span(rs.getInt(3), rs.getInt(5));
                pair.second = new Span(rs.getInt(4), rs.getInt(6));
                locations.add(pair);
            }
            datapair.first = senetnce.getCoreMap();
            datapair.second = locations;
            dataset.add(datapair);
            preparedStatement.close();
            rs.close();
        }
        connection.close();
        return dataset;
    }

    public static List<Pair<CoreMap, Collection<Pair<Span, Span>>>> getDataSet(List<String> senetnces) throws Exception{
        if (senetnces.isEmpty())
            throw new Exception("Input sentence data is empty,please try to check your data");
        List<Pair<CoreMap, Collection<Pair<Span, Span>>>> trainingData = null;
        List<Tree> parse_trees = sent2trees(senetnces);
        trainingData = processTrees(senetnces, parse_trees);
        forceTrack("Saving dataset to database");
        savedataset(senetnces, trainingData, parse_trees);
        endTrack("Saving dataset to database");
        return trainingData;
    }

    public static String decodeHtml(String str) {
        str = StringEscapeUtils.unescapeHtml4(str);
        if (str.contains("&"))
            str = StringEscapeUtils.unescapeHtml4(str);
        return str;
    }

    public static Pair<String, String> getSubobjPair(SemanticGraph senetnce, Pair<Span, Span> spanPair){
        Pair<String, String> subobjpair = new Pair<String, String>();
        List<IndexedWord> words = senetnce.vertexListSorted();
        String sub_text = "";
        String obj_text = "";
        Span sub_span = spanPair.first;
        Span obj_span = spanPair.second;
        for(int sub_index = sub_span.start(); sub_index < sub_span.end(); sub_index++){
            sub_text += words.get(sub_index).word() + " ";
        }
        for(int obj_index = obj_span.start(); obj_index < obj_span.end(); obj_index++){
            obj_text += words.get(obj_index).word() + " ";
        }
        sub_text = sub_text.substring(0, sub_text.length() - 1);
        obj_text = obj_text.substring(0, obj_text.length() - 1);
        subobjpair.first = sub_text;
        subobjpair.second = obj_text;
        return subobjpair;
    }

    public static Pair<String, String> revert2governor(RelationTriple extraction){
        Pair<String, String> governor = new Pair<String, String>();
        List<CoreLabel> subject = extraction.subject;
        List<CoreLabel> object = extraction.object;
        String subText = "";
        String objText = "";
        for(CoreLabel label : subject){
            subText += label.value() + " ";
        }
        subText = subText.substring(0, subText.length() - 1);
        for(CoreLabel label : object){
            objText += label.value() +  " ";
        }
        objText = objText.substring(0, objText.length() - 1);
        governor.first = subText;
        governor.second = objText;
        return governor;
    }

    public static List<Pair<String, String>> getKnowledgeBase(){
        ArrayList<Pair<String, String>> kb = new ArrayList<>();
        try{
            Connection connection = conn2database();
            String sql = "select entity, slotValue from knowledgebase";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String entity = resultSet.getString(1);
                String slotValue = resultSet.getString(2);
                Pair<String, String> tuple = new Pair<>();
                tuple.first = entity;
                tuple.second = slotValue;
                kb.add(tuple);
            }
            resultSet.close();
            statement.close();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return kb;
    }

}
