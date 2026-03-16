DROP TABLE IF EXISTS Service_Request;
DROP TABLE IF EXISTS Mechanic;
DROP TABLE IF EXISTS Cars;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Address;


CREATE TABLE Address (addr_id numeric (5, 0), 
                      street text, 
                      city text, 
                      state text, 
                      zipcode numeric (5, 0), 
                      PRIMARY KEY (addr_id));
COPY Address
FROM '/home/csmajs/vzamb003/project_1/address.txt' WITH DELIMITER',';

CREATE TABLE Customer (cust_id numeric (5, 0) NOT NULL, 
                       firstName text, 
                       lastName text, 
                       phone numeric (10, 0), 
                       addr_id numeric (5, 0), 
                       PRIMARY KEY (cust_id), 
                       FOREIGN KEY (addr_id) REFERENCES Address);
COPY Customer
FROM '/home/csmajs/vzamb003/project_1/customer.txt' WITH DELIMITER',';

CREATE TABLE Cars (vin numeric (5, 0), 
                   year numeric (4, 0), 
                   make text, 
                   model text, 
                   cust_id numeric (5, 0), 
                   PRIMARY KEY (vin), 
                   FOREIGN KEY (cust_id) REFERENCES Customer);
COPY Cars
FROM '/home/csmajs/vzamb003/project_1/cars.txt' WITH DELIMITER',';

CREATE TABLE Mechanic (mech_id numeric (5, 0), 
                      firstName text, 
                      lastName text, 
                      experience numeric (2, 0), 
                      PRIMARY KEY (mech_id));
COPY Mechanic
FROM '/home/csmajs/vzamb003/project_1/mechanic.txt' WITH DELIMITER',';

CREATE TABLE Service_Request (req_id numeric (5, 0), 
                              mech_id numeric (5, 0), 
                              vin numeric (5, 0), 
                              inDate numeric (6, 0), 
                              problem text, 
                              openDate numeric (6, 0), 
                              closeDate numeric (6, 0), 
                              comments text, 
                              bill numeric (7, 2), 
                              odometer numeric (6, 0), 
                              PRIMARY KEY (req_id), 
                              FOREIGN KEY (mech_id) REFERENCES Mechanic, 
                              FOREIGN KEY (vin) REFERENCES Cars);
COPY Service_Request
FROM '/home/csmajs/vzamb003/project_1/service_request.txt' WITH (NULL '', DELIMITER',');