USE Studio_nagran;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS Sessions;

CREATE TABLE Sessions (
    id int NOT NULL AUTO_INCREMENT,
    session_name varchar(255) NOT NULL,
    band_name varchar(255),
    start_date timestamp NOT NULL,
    end_date timestamp NOT NULL,
    duration int NOT NULL,
    Clients_id int NOT NULL,
    Engineer_id int NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY session_name_unique (session_name)
);

ALTER TABLE Sessions
    ADD (
        CONSTRAINT Sessions_fk_1 FOREIGN KEY (Clients_id)
            REFERENCES User_accounts (account_id) ON DELETE CASCADE,
        CONSTRAINT Sessions_fk_2 FOREIGN KEY (Engineer_id)
            REFERENCES User_accounts (account_id) ON DELETE CASCADE
        );

SET FOREIGN_KEY_CHECKS = 1;