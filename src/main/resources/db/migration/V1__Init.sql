
CREATE DATABASE IF NOT EXISTS `test` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `test`;

CREATE TABLE `address` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `city` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8; 

INSERT INTO `address` VALUES(1,'Surat');
INSERT INTO `address` VALUES(2,'Rajkot');
INSERT INTO `address` VALUES(3,'Pune');

CREATE TABLE `student` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `birthDate` DATE NULL,
  `address` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_student_1_idx` (`address` ASC),
  CONSTRAINT `fk_student_1`
    FOREIGN KEY (`address`)
    REFERENCES `address` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;

INSERT INTO `student` VALUES(1,'Hiren','1992-1-20',1);
INSERT INTO `student` VALUES(2,'Nikunj','1996-1-20',2);
INSERT INTO `student` VALUES(3,'Hiral','1992-2-20',3);

CREATE TABLE `subject` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;

INSERT INTO `subject` VALUES(1,'DS');
INSERT INTO `subject` VALUES(2,'DBMS');
INSERT INTO `subject` VALUES(3,'RDBMS');

CREATE TABLE `student_subject` (
  `studentid` BIGINT(20) NULL,
  `subjectid` BIGINT(20) NULL,
  INDEX `fk_student_subject_1_idx` (`studentid` ASC),
  INDEX `fk_student_subject_2_idx` (`subjectid` ASC),
  CONSTRAINT `fk_student_subject_1`
    FOREIGN KEY (`studentid`)
    REFERENCES `student` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_student_subject_2`
    FOREIGN KEY (`subjectid`)
    REFERENCES `subject` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;

 
