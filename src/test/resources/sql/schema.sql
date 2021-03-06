CREATE TABLE sector (
  id     BIGINT,
  name   VARCHAR(255),
  parent BIGINT,
  created TIMESTAMP DEFAULT CURRENT_DATE NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE user_data (
  id   BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1),
  name VARCHAR(255),
  created TIMESTAMP DEFAULT CURRENT_DATE NOT NULL,
  modified TIMESTAMP DEFAULT CURRENT_DATE NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE user_sector (
  id   BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1),
  user_id   BIGINT NOT NULL,
  sector_id BIGINT NOT NULL,
  created TIMESTAMP DEFAULT CURRENT_DATE NOT NULL,
  PRIMARY KEY (id)
);