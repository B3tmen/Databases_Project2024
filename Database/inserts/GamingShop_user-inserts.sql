#ALTER TABLE `user` AUTO_INCREMENT = 1;

# lozinka: sifra1
CALL usp_AddAdministrator ('admin1', '5624c771a8e2e0c12fb764264e91b321fafaf81178de928864f843bdf194e155', 'John', 'Doe', 'john@gmail.com', 1);

#lozinka: password
CALL usp_AddEmployee ('employee1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Petar', 'Petrovic', 'petar@gmail.com', 1, 'Prodavac', 1100.00);

#lozinka: 12345
CALL usp_AddCustomer ('customer1', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5', 'Marko', 'Markovic', 'marko@yahoo.com', 1, 1, '066/123-456');

