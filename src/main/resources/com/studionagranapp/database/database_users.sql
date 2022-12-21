USE Studio_nagran;

DROP TABLE IF EXISTS User_accounts;

CREATE TABLE User_accounts (
    account_id int NOT NULL AUTO_INCREMENT,
    first_name varchar(32) NOT NULL,
    last_name varchar(32) NOT NULL,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    role varchar(16) NOT NULL,
    email varchar(255) NOT NULL,
    phone_number varchar(16) NOT NULL,
    PRIMARY KEY (account_id),
    UNIQUE KEY account_id_unique (account_id),
    UNIQUE KEY username_unique (username)
);

LOCK TABLES User_accounts WRITE;
ALTER TABLE User_accounts DISABLE KEYS;
INSERT INTO User_accounts VALUES
    (1, 'Mateusz', 'Muzioł', 'Matias3004', 'dupa', 'OWNER', 'matias3004@gmail.com', '790652360'),
    (2, 'Łukasz', 'Rudkiewicz', 'luki', 'kochammondeo', 'ENGINEER', 'leszcz@vp.pl', '123456789'),
    (3, 'Kacper', 'Kulios', 'sejtenyst', '666szatan', 'CLIENT', 'parufczakkacper@gmail.com', '987654321');
ALTER TABLE User_accounts ENABLE KEYS;
UNLOCK TABLES;