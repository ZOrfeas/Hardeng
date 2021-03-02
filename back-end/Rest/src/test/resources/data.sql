INSERT INTO `admin` VALUES
(1,NULL,'(Business Owner at Location)',NULL,'email1@hardeng.com','$2a$10$TOLDpbQZUmzweuajUGr8w.IyDcemtOt3bBUnKg482GNsUcreXPfyy','user1'), 
(2,NULL,'(Private Residence/Individual)',NULL,'email2@hardeng.com','$2a$10$7y8F6UEq3bC59YaIlxAZF.Elz/E8hhTjgzQxR4nEWFPt/8TWgk6B2','user2'),
(3,NULL,'(Unknown Operator)',NULL,'email3@hardeng.com','$2a$10$38gMG7ZSMz7zJBAMNewBheLkqs01wuUu2jJWFemesAzKd2ssSvW/O','user3'),
(6,NULL,'AGSM Electrify Verona',NULL,'email6@hardeng.com','$2a$10$dLiLFO3c2SsPpo2PotCA6Ooz.1nmXCLsxp50kvET7Amo3zKSBQ39C','user6'),
(50,NULL,'ChargePoint (Coulomb Technologies)','1-888-758-4389','email50@hardeng.com','$2a$10$az3x3tsAahc.93bkBmpBHO6HcP2M.Nxj1glqxMNxSIuxINUygI39C','user50'),
(80,NULL,'E-Car',NULL,'email80@hardeng.com','$2a$10$VCLn.Rbb6Ga9MVeUrA/zduUN8cCUVgahMl2B.mGnkDEH0lmv68r0m','user80'),
(95,NULL,'eins','+49 371 525-5403','email95@hardeng.com','$2a$10$hMjlvvMr4qGynxfw1z9RsOqqMNprGE6OUPkFlSFTSzlC51QpQB12q','user95'),
(126,NULL,'Enel (Italy)',NULL,'email126@hardeng.com','$2a$10$GzMXBM1I6.yN20JbxpDYo.7yhCSa2OhoO7AzEDFL9kD/0FaFU6lJ6','user126'),
(132,NULL,'Enerhub',NULL,'email132@hardeng.com','$2a$10$9azx5eP975HPcOBJFGmiDORUdSqxLpbKg3yYzZUbUgXHQDCjxFvDS','user132'),
(191,NULL,'GreenLots',NULL,'email191@hardeng.com','$2a$10$M5rvgpg.CPK63yh.WjYIVeqTb.VcyuUmJECjG1XtTaiki4rGwi3Qa','user191'),
(207,NULL,'Ionity',NULL,'email207@hardeng.com','$2a$10$pYbsJIrX3jRAy5K.C/t5luT..vogc3cZgH11J87H.PR/vGTiqUsOK','user207'),
(252,NULL,'OpConnect','1-855-885-9571','email252@hardeng.com','$2a$10$mKWS4dWYLt47/SYV8TAMfu3jh4/k62QZ6Jhj.q7aaLenXHodgJD4.','user252'),
(288,NULL,'Repower (Italy)',NULL,'email288@hardeng.com','$2a$10$t9ZD96YvxHVYaxcg7UazMO.e..NCPJ/MsmmdCa.cWLjkTLTeQLnLy','user288'),
(314,NULL,'SODO',NULL,'email314@hardeng.com','$2a$10$Gew.F21aR88BBY.w.sURqOgU7Tqi0Hd0bMveYGMFWQrun9PN18ob.','user314'),
(326,NULL,'Stadtwerke Grevesmühlen','03881 78450','email326@hardeng.com','$2a$10$ZiOgqyedKaXlPi/TYfUGqO9p3w94DQD.jLFuJVbqCAklo0iy/4n9W','user326'),
(367,NULL,'Tonik Energy',NULL,'email367@hardeng.com','$2a$10$LSvxQQWjBNWHg67mI8gFde5xqr3DPq2738t6utRjbL/K674jH6OFu','user367');

INSERT INTO `driver` VALUES
(1,117,4041591182597,'Libbie Shakeshaft','driver1@hardeng.com','$2a$10$UCGU/XII3OQVbA9KYQzDOevon4a15hOBmVaeX1730Iujml5Tu2EgS','driver1',NULL), 
(2,105,341514415129863,'Mommy Castel','driver2@hardeng.com','$2a$10$W5wIXOFICnojByP6qOnhbOuCh.801ZhHiKP6Wkm7guuuaX5t0clDm','driver2',NULL),
(3,182,5321240889021674,'Giuseppe Sheerman','driver3@hardeng.com','$2a$10$TvW/.CpVfcnMMPx725OeTu5FkNTENMWj2Jy2PHmUWfJOAcfgW5RMe','driver3',NULL);

INSERT INTO `energy_provider` VALUES
(1,'Affect Energy',0.117),
(2,'Atlantic',0.113),
(3,'Avro Energy',0.121),
(23,'Extra Energy',0.245),
(26,'Foxglove Energy',0.223),
(43,'Powershop',0.182);

INSERT INTO `car` VALUES 
(1,false,true,true,34,'Ford','Focus electric'),
(2,false,true,true,27,'Kia','Soul'),
(3,false,true,false,13,'Volkswagen','Passat GTE'),
(6,false,true,true,73,'Tesla','Model 3'),
(13,false,true,true,15,'e.GO Mobile','Life 20'),
(65,false,true,true,39,'Hyundai','Kona');

INSERT INTO `car_driver` VALUES 
(24,13,1),
(24,65,1),
(5,65,2),
(15,6,3);

INSERT INTO `price_policy` VALUES 
(1,322,500,1),
(2,474,350,1),
(3,337,200,1),
(4,0,500,2),
(5,211,250,2),
(6,444,250,2),
(7,461,300,2),
(8,331,100,3),
(9,501,250,3),
(10,239,1500,3),
(11,332,150,3),
(12,487,350,3),
(13,404,500,3),
(26,293,800,6),
(381,343,150,80),
(609,313,500,126),
(1007,0,250,207),
(1803,0,1000,367),
(1245,0,200,252),
(1429,287,150,288),
(449,412,350,95),
(642,296,200,132),
(1551,187,150,314),
(1611,205,200,326);


INSERT INTO `price_policy_driver` VALUES 
(1,26),
(1,381),
(1,609),
(1,1007),
(1,1803),
(2,1245),
(2,1429),
(3,449),
(3,642),
(3,1551),
(3,1611);


INSERT INTO `charging_station` VALUES
(1,'2244 Walnut Grove Ave',34.050745,-118.081014,2,191,23),
(2,'3000 Hanover St',37.416434,-122.145904,1,50,43),
(3,'3535 12th Street',33.977163,-117.374653,1,50,26);

INSERT INTO `charging_point` VALUES
(1,1,3,false,1,1),
(2,2,0,false,3,1),
(3,2,3,false,3,2),
(4,2,0,false,3,3);

 INSERT INTO `charging_session` VALUES
(1,112.065,'2018-10-09 00:00:02.000000','credit card','2018-10-08 13:01:34.000000',13,1,1,26),
(2,115.065,'2018-10-09 00:00:02.000000','credit card','2018-10-08 13:01:34.000000',13,1,1,26),
(3,322.845,'2018-10-09 00:00:03.000000','credit card','2018-10-08 13:34:52.000000',13,1,2,26),
(4,425.382,'2018-10-09 00:00:01.000000','credit card','2018-10-08 13:59:16.000000',13,1,3,381),
(5,511.694,'2018-10-09 00:00:03.000000','credit card','2018-10-08 14:01:14.000000',13,1,1,381),
(6,67.534,'2018-10-09 00:00:03.000000','credit card','2018-10-08 14:06:38.000000',13,1,2,381),
(7,720.679,'2018-10-09 00:00:03.000000','credit card','2018-10-08 14:07:27.000000',13,1,3,609),
(8,85.009,'2018-10-09 00:26:14.000000','cash','2018-10-08 14:20:10.000000',13,1,2,609),
(9,98.418,'2018-10-09 00:00:01.000000','credit card','2018-10-08 14:20:19.000000',13,1,3,609),
(10,1017.1,'2018-10-09 00:26:12.000000','cash','2018-10-08 14:27:14.000000',13,1,2,1007),
(11,115.897,'2018-10-09 00:00:01.000000','credit card','2018-10-08 14:32:54.000000',13,1,2,1007),
(12,1215.7,'2018-10-09 00:00:02.000000','credit card','2018-10-08 14:33:38.000000',13,1,1,1007),
(13,1358.94,'2018-10-09 00:09:00.000000','credit card','2018-10-08 14:36:05.000000',13,1,1,1803),
(14,1418.9,'2018-10-09 00:00:01.000000','credit card','2018-10-08 14:43:24.000000',13,1,2,1803),
(15,157.231,'2018-10-09 00:00:00.000000','credit card','2018-10-08 14:44:58.000000',65,2,2,1245),
(16,1614.6,'2018-10-09 00:00:00.000000','credit card','2018-10-08 14:45:25.000000',65,2,3,1245),
(17,1712.57,'2018-10-09 00:00:00.000000','credit card','2018-10-08 14:46:29.000000',65,2,3,1429),
(18,1712.57,'2018-10-09 00:00:00.000000','credit card','2018-10-08 14:46:29.000000',65,2,2,1429),
(19,192.866,'2018-10-09 00:00:02.000000','credit card','2018-10-08 14:51:14.000000',6,3,2,1551),
(20,208.892,'2018-10-09 00:00:01.000000','credit card','2018-10-08 18:01:02.000000',6,3,3,1611);