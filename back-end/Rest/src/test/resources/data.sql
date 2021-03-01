INSERT INTO `admin` VALUES 
(1,NULL,'(Business Owner at Location)',NULL,'email1@hardeng.com','$2a$10$TOLDpbQZUmzweuajUGr8w.IyDcemtOt3bBUnKg482GNsUcreXPfyy','user1'), 
(2,NULL,'(Private Residence/Individual)',NULL,'email2@hardeng.com','$2a$10$7y8F6UEq3bC59YaIlxAZF.Elz/E8hhTjgzQxR4nEWFPt/8TWgk6B2','user2'),
(402,NULL,'Zero Carbon World',NULL,'email402@hardeng.com','$2a$10$0PjzW9Y6715uZo/PjsSvo./I6aB9HVglylJqbbGWB02K43.eVk0jC','user402');

INSERT INTO `driver` VALUES
(1,117,4041591182597,'Libbie Shakeshaft','driver1@hardeng.com','$2a$10$UCGU/XII3OQVbA9KYQzDOevon4a15hOBmVaeX1730Iujml5Tu2EgS','driver1',NULL), 
(2,105,341514415129863,'Mommy Castel','driver2@hardeng.com','$2a$10$W5wIXOFICnojByP6qOnhbOuCh.801ZhHiKP6Wkm7guuuaX5t0clDm','driver2',NULL);

INSERT INTO `energy_provider` VALUES
(43,'Powershop',0.182);

INSERT INTO `charging_station` VALUES
(27134,'7058 Maddox Blvd',37.924371,-75.354259,1,1,43),
(27050,'Am Rasen 2',50.9663592,10.157514900000024,3,1,43);

INSERT INTO `charging_point` VALUES
(42906,2,3,0,13,27134);