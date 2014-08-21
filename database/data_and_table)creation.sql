CREATE DATABASE `NLP` /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE TABLE `sent_lexicon_1` (
  `type` char(40) DEFAULT NULL,
  `word` char(100) DEFAULT NULL,
  `pos` char(30) DEFAULT NULL,
  `polarity` char(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
