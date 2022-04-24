use cs3200Project;

SET GLOBAL local_infile = true;
truncate shipment;
LOAD DATA LOCAL INFILE 'ABSOLUTE/PATH/TO/SHIPMENT/shipment.csv' INTO TABLE shipment
-- IE: /Users/__YOUR_USERNAME_/Downloads/shipment.csv on macos
FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n';
