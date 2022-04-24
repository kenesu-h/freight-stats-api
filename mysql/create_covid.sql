use cs3200Project;

DROP TABLE IF EXISTS covid_data;
CREATE TABLE covid_data (
  covid_data_id int PRIMARY KEY,
  state SMALLINT NOT NULL,
  covid_cases int DEFAULT 0,
  covid_deaths int DEFAULT 0,
  cases_month DATE NOT NULL,
  CONSTRAINT fk_state FOREIGN KEY (state) REFERENCES state (state_id)
);