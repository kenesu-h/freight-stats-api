use cs3200Project;

SET GLOBAL local_infile = true;
truncate covid_data;
LOAD DATA LOCAL INFILE 'ABSOLUTE/PATH/TO/SHIPMENT/covid_data.csv' INTO TABLE covid_data
-- Place the path to your file: /Users/__YOUR_USERNAME_/Downloads/covid_data.csv on macos
FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n';
