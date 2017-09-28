/*
Navicat MySQL Data Transfer

Source Server         : nlp422
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : test_dataset

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-09-28 14:12:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for annotation
-- ----------------------------
DROP TABLE IF EXISTS `annotation`;
CREATE TABLE `annotation` (
  `relation_root` int(11) NOT NULL,
  `sid` varchar(255) NOT NULL,
  `aid` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of annotation
-- ----------------------------
INSERT INTO `annotation` VALUES ('11', 'S#0', '1');
INSERT INTO `annotation` VALUES ('29', 'S#1', '2');
INSERT INTO `annotation` VALUES ('3', 'S#10', '3');
INSERT INTO `annotation` VALUES ('10', 'S#10', '4');
INSERT INTO `annotation` VALUES ('20', 'S#2', '5');
INSERT INTO `annotation` VALUES ('14', 'S#2', '6');
INSERT INTO `annotation` VALUES ('46', 'S#2', '7');
INSERT INTO `annotation` VALUES ('46', 'S#2', '8');
INSERT INTO `annotation` VALUES ('5', 'S#3', '9');
INSERT INTO `annotation` VALUES ('7', 'S#3', '10');
INSERT INTO `annotation` VALUES ('24', 'S#3', '11');
INSERT INTO `annotation` VALUES ('24', 'S#3', '12');
INSERT INTO `annotation` VALUES ('8', 'S#4', '13');
INSERT INTO `annotation` VALUES ('58', 'S#5', '14');
INSERT INTO `annotation` VALUES ('20', 'S#5', '15');
INSERT INTO `annotation` VALUES ('19', 'S#6', '16');
INSERT INTO `annotation` VALUES ('3', 'S#7', '17');
INSERT INTO `annotation` VALUES ('12', 'S#7', '18');
INSERT INTO `annotation` VALUES ('37', 'S#8', '19');
INSERT INTO `annotation` VALUES ('45', 'S#9', '20');
INSERT INTO `annotation` VALUES ('10', 'S#9', '21');
INSERT INTO `annotation` VALUES ('42', 'S#9', '22');
INSERT INTO `annotation` VALUES ('62', 'S#9', '23');

-- ----------------------------
-- Table structure for knowledgebase
-- ----------------------------
DROP TABLE IF EXISTS `knowledgebase`;
CREATE TABLE `knowledgebase` (
  `kbid` int(11) NOT NULL AUTO_INCREMENT,
  `slotValueCharOffsetEnd` int(11) NOT NULL,
  `slotValueCharOffsetBegin` int(11) NOT NULL,
  `entityCharOffsetEnd` int(11) NOT NULL,
  `entityCharOffsetBegin` int(11) NOT NULL,
  `slotValue` varchar(1000) NOT NULL,
  `entity` varchar(1000) NOT NULL,
  `sid` varchar(255) NOT NULL,
  PRIMARY KEY (`kbid`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of knowledgebase
-- ----------------------------
INSERT INTO `knowledgebase` VALUES ('1', '64', '57', '20', '0', ' Edward', 'Alexandra of Denmark', 'S#0');
INSERT INTO `knowledgebase` VALUES ('2', '97', '85', '36', '23', ' Big Machine', ' Taylor Swift', 'S#1');
INSERT INTO `knowledgebase` VALUES ('3', '209', '203', '160', '142', ' Italy', ' Francesco Cossiga', 'S#2');
INSERT INTO `knowledgebase` VALUES ('4', '83', '71', '144', '139', ' BAE Systems', ' MBDA', 'S#3');
INSERT INTO `knowledgebase` VALUES ('5', '36', '26', '53', '48', ' Universal', ' Deus', 'S#4');
INSERT INTO `knowledgebase` VALUES ('6', '181', '173', '118', '106', ' Denmark', ' August Blom', 'S#5');
INSERT INTO `knowledgebase` VALUES ('7', '102', '95', '63', '57', ' Moscow', ' Yukos', 'S#6');
INSERT INTO `knowledgebase` VALUES ('8', '159', '149', '25', '0', ' Rotherham', 'Wath Comprehensive School', 'S#7');
INSERT INTO `knowledgebase` VALUES ('9', '213', '194', '47', '44', ' Harvard Law School', ' my', 'S#8');
INSERT INTO `knowledgebase` VALUES ('10', '206', '199', '27', '21', ' Malawi', ' Banda', 'S#9');
INSERT INTO `knowledgebase` VALUES ('11', '63', '58', '2', '0', ' King', 'He', 'S#10');
INSERT INTO `knowledgebase` VALUES ('12', '102', '97', '13', '0', ' Navy', 'David Henshaw', 'S#11');

-- ----------------------------
-- Table structure for location
-- ----------------------------
DROP TABLE IF EXISTS `location`;
CREATE TABLE `location` (
  `sid` varchar(255) NOT NULL,
  `lid` int(255) NOT NULL AUTO_INCREMENT,
  `start_left` int(11) NOT NULL,
  `start_right` int(11) NOT NULL,
  `end_left` int(11) NOT NULL,
  `end_right` int(11) NOT NULL,
  PRIMARY KEY (`lid`),
  KEY `sid` (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=286 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of location
-- ----------------------------
INSERT INTO `location` VALUES ('S#0', '267', '0', '12', '1', '18');
INSERT INTO `location` VALUES ('S#0', '268', '0', '9', '1', '18');
INSERT INTO `location` VALUES ('S#1', '269', '26', '32', '27', '35');
INSERT INTO `location` VALUES ('S#1', '270', '26', '1', '27', '4');
INSERT INTO `location` VALUES ('S#1', '271', '26', '0', '27', '35');
INSERT INTO `location` VALUES ('S#1', '272', '26', '29', '27', '31');
INSERT INTO `location` VALUES ('S#2', '273', '1', '14', '5', '15');
INSERT INTO `location` VALUES ('S#2', '274', '1', '12', '5', '13');
INSERT INTO `location` VALUES ('S#3', '275', '3', '1', '4', '3');
INSERT INTO `location` VALUES ('S#3', '276', '3', '6', '4', '15');
INSERT INTO `location` VALUES ('S#3', '277', '3', '6', '4', '15');
INSERT INTO `location` VALUES ('S#5', '278', '49', '51', '50', '55');
INSERT INTO `location` VALUES ('S#6', '279', '1', '20', '4', '27');
INSERT INTO `location` VALUES ('S#6', '280', '1', '27', '4', '28');
INSERT INTO `location` VALUES ('S#6', '281', '1', '18', '4', '28');
INSERT INTO `location` VALUES ('S#9', '282', '5', '1', '6', '4');
INSERT INTO `location` VALUES ('S#10', '283', '0', '4', '1', '7');
INSERT INTO `location` VALUES ('S#11', '284', '0', '21', '2', '22');
INSERT INTO `location` VALUES ('S#11', '285', '0', '15', '2', '22');

-- ----------------------------
-- Table structure for sentence
-- ----------------------------
DROP TABLE IF EXISTS `sentence`;
CREATE TABLE `sentence` (
  `sentence` varchar(2000) NOT NULL,
  `sid` varchar(255) NOT NULL,
  `parse_tree` varchar(18000) NOT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sentence
-- ----------------------------
INSERT INTO `sentence` VALUES ('Alexandra of Denmark ( 1844 – 1925 ) was Queen Consort to Edward VII of the United Kingdom and thus Empress of India during her husband\'s reign .', 'S#0', '(ROOT (S (NP (NP (NP (NNP Alexandra)) (PP (IN of) (NP (NNP Denmark)))) (PRN (-LRB- -LRB-) (NP (NP (CD 1844)) (: --) (NP (CD 1925))) (-RRB- -RRB-))) (VP (VBD was) (NP (NP (NNP Queen) (NNP Consort)) (PP (PP (TO to) (NP (NP (NNP Edward) (NNP VII)) (PP (IN of) (NP (DT the) (NNP United) (NNP Kingdom))))) (CC and) (ADVP (RB thus)) (PP (NP (NP (NNP Empress)) (PP (IN of) (NP (NNP India)))) (IN during) (NP (NP (PRP$ her) (NN husband) (POS \'s)) (NN reign)))))) (. .)))');
INSERT INTO `sentence` VALUES ('With her two albums , \" Taylor Swift \" in 2006 and \" Fearless \" in 2008 ( both on the Big Machine label ) , Swift has built her career by way of Nashville and country radio .', 'S#1', '(ROOT (S (PP (IN With) (NP (NP (PRP$ her) (CD two) (NNS albums)) (, ,) (\'\' \'\') (NP (NP (NP (NNP Taylor) (NNP Swift)) (\'\' \'\') (PP (IN in) (NP (NP (CD 2006) (NNP and) (\'\' \'\') (NNP Fearless)) (\'\' \'\') (PP (IN in) (NP (CD 2008)))))) (PRN (-LRB- -LRB-) (NP (NP (DT both)) (PP (IN on) (NP (DT the) (NNP Big) (NN Machine) (NN label)))) (-RRB- -RRB-))))) (, ,) (NP (NNP Swift)) (VP (VBZ has) (VP (VBN built) (NP (PRP$ her) (NN career)) (PP (IN by) (NP (NP (NN way)) (PP (IN of) (NP (NP (NNP Nashville)) (CC and) (NP (NN country) (NN radio)))))))) (. .)))');
INSERT INTO `sentence` VALUES ('He eventually went to New York City , and made records for King Records under the name Al Grant ( one in particular , \" Cabaret \" , appeared in the Variety magazine charts ) .', 'S#10', '(ROOT (S (NP (PRP He)) (ADVP (RB eventually)) (VP (VP (VBD went) (PP (TO to) (NP (NNP New) (NNP York) (NNP City)))) (, ,) (CC and) (VP (VBD made) (NP (NP (NNS records)) (PP (IN for) (NP (NNP King) (NNPS Records)))) (PP (IN under) (NP (NP (NP (DT the) (NN name)) (NP (NNP Al) (NNP Grant))) (PRN (-LRB- -LRB-) (NP (NP (CD one)) (PP (IN in) (NP (NP (JJ particular)) (, ,) (\'\' \'\') (NP (NP (NN Cabaret) (\'\' \'\')) (, ,) (VP (VBN appeared) (PP (IN in) (NP (DT the) (NNP Variety) (NN magazine) (NNS charts)))))))) (-RRB- -RRB-)))))) (. .)))');
INSERT INTO `sentence` VALUES ('David Henshaw ( April 2 , 1791 – November 11 , 1852 ) was the 14th United States Secretary of the Navy .', 'S#11', '(ROOT (S (NP (NP (NNP David) (NNP Henshaw)) (PRN (-LRB- -LRB-) (NP (NP (NNP April) (CD 2) (, ,) (CD 1791)) (: --) (NP (NNP November) (CD 11) (, ,) (CD 1852))) (-RRB- -RRB-))) (VP (VBD was) (NP (NP (DT the) (JJ 14th) (NNP United) (NNPS States) (NNP Secretary)) (PP (IN of) (NP (DT the) (NNP Navy))))) (. .)))');
INSERT INTO `sentence` VALUES ('The so-called \" historic compromise \" between the Christian Democrats and the PCI was abandoned : The Italian Government led by Prime Minister Francesco Cossiga ( a member of the extreme right faction of Italy\'s Christian Democrat party , a pro-NATO atlantist was also suspected of involvement in the killing of Aldo Moro ) .', 'S#2', '(ROOT (S (S (NP (NP (NP (DT The) (JJ so-called) (\'\' \'\') (NN historic) (NN compromise)) (\'\' \'\') (PP (IN between) (NP (DT the) (NNP Christian) (NNPS Democrats)))) (CC and) (NP (DT the) (NNP PCI))) (VP (VBD was) (VP (VBN abandoned)))) (: :) (S (NP (DT The) (JJ Italian) (NN Government)) (VP (VBD led) (PP (IN by) (NP (NP (NNP Prime) (NNP Minister) (NNP Francesco) (NNP Cossiga)) (PRN (-LRB- -LRB-) (S (NP (NP (DT a) (NN member)) (PP (IN of) (NP (NP (DT the) (JJ extreme) (NN right) (NN faction)) (PP (IN of) (NP (NP (NP (NNP Italy) (POS \'s)) (NNP Christian) (NNP Democrat) (NN party)) (, ,) (NP (DT a) (JJ pro-NATO) (NN atlantist))))))) (VP (VBD was) (ADVP (RB also)) (VP (VBN suspected) (PP (IN of) (NP (NN involvement))) (PP (IN in) (NP (NP (DT the) (NN killing)) (PP (IN of) (NP (NNP Aldo) (NNP Moro)))))))) (-RRB- -RRB-)))))) (. .)))');
INSERT INTO `sentence` VALUES ('In April 2001 EADS agreed to merge its missile businesses with those of BAE Systems and Alenia Marconi Systems ( BAE/Finmeccanica ) to form MBDA .', 'S#3', '(ROOT (S (PP (IN In) (NP (NNP April) (CD 2001))) (NP (NNS EADS)) (VP (VBD agreed) (S (VP (TO to) (VP (VB merge) (NP (PRP$ its) (NN missile) (NNS businesses)) (PP (IN with) (NP (NP (DT those)) (PP (IN of) (NP (NP (NNP BAE) (NNPS Systems) (CC and) (NNP Alenia) (NNP Marconi) (NNP Systems)) (PRN (-LRB- -LRB-) (NP (NNP BAE/Finmeccanica)) (-RRB- -RRB-)))))) (S (VP (TO to) (VP (VB form) (NP (NNP MBDA))))))))) (. .)))');
INSERT INTO `sentence` VALUES ('Eu intuo que seja a Igreja Universal do Reino de Deus — que goza de isenções fiscais , como todo mundo sabe.Esse mercador de exorcismos — sua igreja continua imbatível na tarefa de tirar o demônio do corpo de iletrados — foi longe , não é ?', 'S#4', '(ROOT (NP (NP (NP (NNP Eu) (NN intuo) (NN que) (NN seja)) (SBAR (S (NP (DT a) (NNP Igreja) (NNP Universal)) (VP (VBP do) (NP (NNP Reino) (FW de) (NNP Deus)))))) (: --) (NP (NP (NN que) (NN goza)) (PP (IN de) (NP (JJ isenções) (NNS fiscais) (, ,) (NN como) (NN todo) (NN mundo) (NN sabe.Esse) (NN mercador) (JJ de) (NNS exorcismos)))) (: --) (NP (NP (NP (NN sua) (NN igreja) (NN continua)) (ADJP (JJ imbatível) (S (VP (TO na) (VP (VB tarefa) (NP (NP (IN de) (JJ tirar) (NN o) (NN demônio)) (SBAR (S (VP (VBP do) (NP (NP (NN corpo)) (IN de) (NP (NNS iletrados)))))))))))) (: --) (NP (NP (NN foi) (NN longe)) (, ,) (NP (NN não) (NN é)))) (. ?)))');
INSERT INTO `sentence` VALUES ('With The Last Victim of the White Slave Trade ( Den Hvide Slavehandels Sidste Offer ) ( 1911 ) Directed by August Blom One of the many \" white slave trade \" films popular in Denmark , ( which dealt with young women being sold to brothels ) the film is wonderfully exciting , especially the climactic fight on a rooftop .', 'S#5', '(ROOT (S (PP (IN With) (S (NP (NP (DT The) (JJ Last) (NN Victim)) (PP (IN of) (NP (NP (NP (DT the) (NNP White) (NNP Slave) (NNP Trade)) (PRN (-LRB- -LRB-) (NP (NNP Den) (NNP Hvide) (NNP Slavehandels) (NNP Sidste) (NNP Offer)) (-RRB- -RRB-))) (PRN (-LRB- -LRB-) (NP (CD 1911)) (-RRB- -RRB-))))) (VP (VBG Directed) (PP (IN by) (NP (NP (NNP August) (NNP Blom) (NNP One)) (PP (IN of) (NP (NP (DT the) (JJ many) (\'\' \'\') (NN white) (NN slave) (NN trade) (\'\' \'\') (NNS films)) (ADJP (JJ popular) (PP (IN in) (NP (NNP Denmark))))))))))) (, ,) (PRN (-LRB- -LRB-) (SBAR (WHNP (WDT which)) (S (VP (VBN dealt) (PP (IN with) (NP (NP (JJ young) (NNS women)) (VP (VBG being) (VP (VBN sold) (PP (TO to) (NP (NNS brothels)))))))))) (-RRB- -RRB-)) (NP (DT the) (NN film)) (VP (VBZ is) (ADJP (RB wonderfully) (JJ exciting)) (, ,) (ADVP (RB especially)) (NP (NP (DT the) (JJ climactic) (NN fight)) (PP (IN on) (NP (DT a) (NN rooftop))))) (. .)))');
INSERT INTO `sentence` VALUES ('The last remaining assets of bankrupt Russian oil company Yukos , including its headquarters in Moscow , were sold at auction for nearly 3.9 billion U.S. dollars Friday .', 'S#6', '(ROOT (S (NP (NP (DT The) (JJ last) (JJ remaining) (NNS assets)) (PP (IN of) (NP (JJ bankrupt) (JJ Russian) (NN oil) (NN company) (NNS Yukos))) (, ,) (PP (VBG including) (NP (NP (PRP$ its) (NN headquarters)) (PP (IN in) (NP (NNP Moscow))))) (, ,)) (VP (VBD were) (VP (VBN sold) (PP (IN at) (NP (NP (NN auction)) (PP (IN for) (NP (QP (RB nearly) (CD 3.9) (CD billion)) (NNP U.S.) (NNS dollars))))) (NP-TMP (NNP Friday)))) (. .)))');
INSERT INTO `sentence` VALUES ('Wath Comprehensive School : A Language College is a co-educational secondary school on Sandygate in Wath-upon-Dearne , in the Metropolitan Borough of Rotherham , South Yorkshire , England .', 'S#7', '(ROOT (NP (NP (NNP Wath) (NNP Comprehensive) (NNP School)) (: :) (S (NP (DT A) (NNP Language) (NNP College)) (VP (VBZ is) (NP (NP (DT a) (JJ co-educational) (JJ secondary) (NN school)) (PP (IN on) (NP (NP (NNP Sandygate)) (PP (IN in) (NP (NNP Wath-upon-Dearne) (PRN (, ,) (PP (IN in) (NP (NP (DT the) (NNP Metropolitan) (NNP Borough)) (PP (IN of) (NP (NNP Rotherham) (, ,) (NNP South) (NNP Yorkshire))))) (, ,)) (NNP England)))))))) (. .)))');
INSERT INTO `sentence` VALUES ('\" There was a part of me that began to doubt my own abilities and to ignore my own truth , what I knew to be true about me , \" said Obama , who ultimately graduated from Princeton University and Harvard Law School .', 'S#8', '(ROOT (S (\'\' \'\') (S (NP (EX There)) (VP (VBD was) (NP (NP (DT a) (NN part)) (PP (IN of) (NP (PRP me))) (SBAR (WHNP (WDT that)) (S (VP (VBD began) (S (VP (VP (TO to) (VP (VB doubt) (NP (PRP$ my) (JJ own) (NNS abilities)))) (CC and) (VP (TO to) (VP (VB ignore) (NP (NP (PRP$ my) (JJ own) (NN truth)) (, ,) (SBAR (WHNP (WP what)) (S (NP (PRP I)) (VP (VBD knew) (S (VP (TO to) (VP (VB be) (ADJP (JJ true) (PP (IN about) (NP (PRP me))))))))))))))))))))) (, ,) (\'\' \'\') (VP (VBD said) (NP (NP (NNP Obama)) (, ,) (SBAR (WHNP (WP who)) (S (ADVP (RB ultimately)) (VP (VBN graduated) (PP (IN from) (NP (NNP Princeton) (NNP University) (CC and) (NNP Harvard)))))))) (NP (NNP Law) (NNP School)) (. .)))');
INSERT INTO `sentence` VALUES ('For almost 30 years , Banda ruled firmly , suppressing opposition to his party and ensuring that he had no personal opposition.Cutter , Africa 2006 , p. 143 Despite his political severity , however , Malawi\'s economy while Banda was president was often cited as an example of how a poor , landlocked , heavily populated , mineral-poor country could achieve progress in both agriculture and industrial development .', 'S#9', '(ROOT (S (S (PP (IN For) (NP (RB almost) (CD 30) (NNS years))) (, ,) (NP (NNP Banda)) (VP (VBD ruled) (ADVP (RB firmly)) (, ,) (S (VP (VP (VBG suppressing) (NP (NN opposition)) (PP (TO to) (NP (PRP$ his) (NN party)))) (CC and) (VP (VBG ensuring) (SBAR (IN that) (S (NP (PRP he)) (VP (VBD had) (NP (DT no) (JJ personal) (NN opposition.Cutter)))))))))) (, ,) (S (NP (NP (NNP Africa) (CD 2006) (, ,) (CD p.)) (ADVP (RB 143))) (PP (IN Despite) (NP (PRP$ his) (JJ political) (NN severity))) (, ,) (ADVP (RB however))) (, ,) (S (NP (NP (NNP Malawi) (POS \'s)) (NN economy) (SBAR (IN while) (S (NP (NNP Banda)) (VP (VBD was) (NP (NN president)))))) (VP (VBD was) (ADVP (RB often)) (VP (VBN cited) (PP (IN as) (NP (NP (DT an) (NN example)) (PP (IN of) (SBAR (WHADVP (WRB how)) (S (NP (DT a) (ADJP (JJ poor) (, ,) (JJ landlocked) (, ,) (RB heavily) (JJ populated) (, ,)) (JJ mineral-poor) (NN country)) (VP (MD could) (VP (VB achieve) (NP (NN progress)) (PP (IN in) (NP (NP (DT both) (NN agriculture)) (CC and) (NP (JJ industrial) (NN development)))))))))))))) (. .)))');
