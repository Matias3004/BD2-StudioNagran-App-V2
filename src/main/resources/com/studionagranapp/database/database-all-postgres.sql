DROP TRIGGER IF EXISTS Mix_notes_insert ON Mix_notes CASCADE;
DROP TRIGGER IF EXISTS Mix_notes_update ON Mix_notes CASCADE;
DROP TRIGGER IF EXISTS Mix_notes_delete ON Mix_notes CASCADE;
DROP TRIGGER IF EXISTS Mixes_insert ON Mixes CASCADE;
DROP TRIGGER IF EXISTS Mixes_update ON Mixes CASCADE;
DROP TRIGGER IF EXISTS Mixes_delete ON Mixes CASCADE;
DROP TRIGGER IF EXISTS Equipment_insert ON Equipment CASCADE;
DROP TRIGGER IF EXISTS Equipment_update ON Equipment CASCADE;
DROP TRIGGER IF EXISTS Equipment_delete ON Equipment CASCADE;

DROP FUNCTION IF EXISTS log_equipment_changes();
DROP FUNCTION IF EXISTS log_mixes_changes();
DROP FUNCTION IF EXISTS log_mix_notes_changes();

DROP TYPE IF EXISTS modification CASCADE;

DROP TABLE IF EXISTS User_accounts CASCADE;
DROP TABLE IF EXISTS Sessions CASCADE;
DROP TABLE IF EXISTS Equipment CASCADE;
DROP TABLE IF EXISTS Equipment_history CASCADE;
DROP TABLE IF EXISTS Mixes cascade;
DROP TABLE IF EXISTS Mixes_history CASCADE;
DROP TABLE IF EXISTS Mix_notes CASCADE;
DROP TABLE IF EXISTS Mix_notes_history CASCADE;

DROP SCHEMA IF EXISTS RecordingStudio CASCADE;

CREATE SCHEMA RecordingStudio AUTHORIZATION postgres;
SET SEARCH_PATH TO RecordingStudio, public;

CREATE TABLE User_accounts (
    account_id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    first_name varchar(32) NOT NULL,
    last_name varchar(32) NOT NULL,
    username varchar(255) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    role varchar(16) NOT NULL,
    email varchar(255) NOT NULL,
    phone_number varchar(16) NOT NULL
);

CREATE TABLE Sessions (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    session_name varchar(255) UNIQUE NOT NULL,
    band_name varchar(255),
    start_date timestamp NOT NULL,
    end_date timestamp NOT NULL,
    duration int NOT NULL,
    Clients_id int NOT NULL,
    Engineer_id int NOT NULL
);

CREATE TABLE Equipment (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name varchar(64) UNIQUE NOT NULL,
    type varchar(64),
    quantity int NOT NULL,
    backline int NOT NULL
);

-- CREATE TYPE modification AS ENUM ('insert', 'update', 'delete');
--
-- CREATE TABLE Equipment_history (
--     id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
--     action_type modification,
--     action_time timestamp DEFAULT current_timestamp,
--     Equipment_id int,
--     name varchar(64) NOT NULL,
--     type varchar(64),
--     quantity int NOT NULL,
--     backline int NOT NULL
-- );

CREATE TABLE Mixes (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    filename varchar(255) UNIQUE NOT NULL,
    upload_date timestamp DEFAULT current_timestamp NOT NULL,
    path varchar(255) UNIQUE NOT NULL,
    Session_id int NOT NULL
);

-- CREATE TABLE Mixes_history (
--     id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
--     action_type modification,
--     action_time timestamp DEFAULT current_timestamp,
--     Mix_id int,
--     filename varchar(255) NOT NULL,
--     upload_date timestamp DEFAULT current_timestamp NOT NULL,
--     path varchar(255) NOT NULL,
--     Session_id int NOT NULL
-- );

CREATE TABLE Mix_notes (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    upload_date timestamp DEFAULT current_timestamp NOT NULL,
    description varchar(255) NOT NULL,
    Mix_id int NOT NULL
);

-- CREATE TABLE Mix_notes_history (
--     id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
--     action_type modification,
--     action_time timestamp DEFAULT current_timestamp,
--     Mix_note_id int,
--     upload_date timestamp DEFAULT current_timestamp NOT NULL,
--     description varchar(255) NOT NULL,
--     Mix_id int NOT NULL
-- );

ALTER TABLE Sessions
    ADD CONSTRAINT Sessions_fk_1 FOREIGN KEY (Clients_id)
        REFERENCES User_accounts (account_id) ON DELETE CASCADE;

ALTER TABLE Sessions
    ADD CONSTRAINT Sessions_fk_2 FOREIGN KEY (Engineer_id)
        REFERENCES User_accounts (account_id) ON DELETE CASCADE;

ALTER TABLE Mixes
    ADD CONSTRAINT Mixes_fk_1 FOREIGN KEY (Session_id)
        REFERENCES Sessions (id) ON DELETE CASCADE;

-- ALTER TABLE Mixes_history
--     ADD CONSTRAINT Mixes_history_fk_1 FOREIGN KEY (Mix_id)
--         REFERENCES Mixes (id) ON DELETE CASCADE;

ALTER TABLE Mix_notes
    ADD CONSTRAINT Mix_notes_fk_1 FOREIGN KEY (Mix_id)
        REFERENCES Mixes (id) ON DELETE CASCADE;

-- ALTER TABLE Mix_notes_history
--     ADD CONSTRAINT Mix_notes_history_fk_1 FOREIGN KEY (Mix_note_id)
--         REFERENCES Mix_notes (id) ON DELETE CASCADE;
--
-- ALTER TABLE Equipment_history
--     ADD CONSTRAINT Equipment_history_fk_1 FOREIGN KEY (Equipment_id)
--         REFERENCES Equipment (id) ON DELETE CASCADE;


-- CREATE OR REPLACE FUNCTION log_equipment_changes()
-- RETURNS TRIGGER AS $$
-- BEGIN
--     IF (tg_op = 'INSERT') THEN
--         INSERT INTO Equipment_history (
--             action_type, action_time, Equipment_id,
--             name, type, quantity, backline
--         ) VALUES (
--             'insert', now(), new.id, new.name,
--             new.type, new.quantity, new.backline
--         );
--     ELSEIF (tg_op = 'UPDATE') THEN
--         INSERT INTO Equipment_history (
--             action_type, action_time, Equipment_id,
--             name, type, quantity, backline
--         ) VALUES (
--             'update', now(), new.id, new.name,
--             new.type, new.quantity, new.backline
--         );
--     ELSEIF (tg_op = 'DELETE') THEN
--         INSERT INTO Equipment_history (
--             action_type, action_time, Equipment_id,
--             name, type, quantity, backline
--         ) VALUES (
--             'delete', now(), old.id, old.name,
--             old.type, old.quantity, old.backline
--         );
--     END IF;
--     RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;
--
-- CREATE TRIGGER Equipment_insert
--     AFTER INSERT ON Equipment
--     FOR EACH ROW
--     EXECUTE FUNCTION log_equipment_changes();
--
-- CREATE TRIGGER Equipment_update
--     AFTER UPDATE ON Equipment
--     FOR EACH ROW
--     EXECUTE FUNCTION log_equipment_changes();
--
-- CREATE TRIGGER Equipment_delete
--     BEFORE DELETE ON Equipment
--     FOR EACH ROW
--     EXECUTE FUNCTION log_equipment_changes();
--
--
-- CREATE OR REPLACE FUNCTION log_mixes_changes()
--     RETURNS TRIGGER AS $$
-- BEGIN
--     IF (tg_op = 'INSERT') THEN
--         INSERT INTO Mixes_history (
--             action_type, action_time, Mix_id,
--             filename, upload_date, path, Session_id
--         ) VALUES (
--             'insert', now(), new.id, new.filename,
--             new.upload_date, new.path, new.Session_id
--         );
--     ELSEIF (tg_op = 'UPDATE') THEN
--         INSERT INTO Mixes_history (
--             action_type, action_time, Mix_id,
--             filename, upload_date, path, Session_id
--         ) VALUES (
--             'update', now(), new.id, new.filename,
--             new.upload_date, new.path, new.Session_id
--         );
--     ELSEIF (tg_op = 'DELETE') THEN
--         INSERT INTO Mixes_history (
--             action_type, action_time, Mix_id,
--             filename, upload_date, path, Session_id
--         ) VALUES (
--             'delete', now(), old.id, old.filename,
--             old.upload_date, old.path, old.Session_id
--         );
--     END IF;
--     RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;
--
-- CREATE TRIGGER Mixes_insert
--     AFTER INSERT ON Mixes
--     FOR EACH ROW
-- EXECUTE FUNCTION log_mixes_changes();
--
-- CREATE TRIGGER Mixes_update
--     AFTER UPDATE ON Mixes
--     FOR EACH ROW
-- EXECUTE FUNCTION log_mixes_changes();
--
-- CREATE TRIGGER Mixes_delete
--     BEFORE DELETE ON Mixes
--     FOR EACH ROW
-- EXECUTE FUNCTION log_mixes_changes();
--
--
-- CREATE OR REPLACE FUNCTION log_mix_notes_changes()
--     RETURNS TRIGGER AS $$
-- BEGIN
--     IF (tg_op = 'INSERT') THEN
--         INSERT INTO Mix_notes_history (
--             action_type, action_time, Mix_note_id,
--             upload_date, description, Mix_id
--         ) VALUES (
--             'insert', now(), new.id,
--             new.upload_date, new.description, new.Mix_id
--         );
--     ELSEIF (tg_op = 'UPDATE') THEN
--         INSERT INTO Mix_notes_history (
--             action_type, action_time, Mix_note_id,
--             upload_date, description, Mix_id
--         ) VALUES (
--             'update', now(), new.id,
--             new.upload_date, new.description, new.Mix_id
--         );
--     ELSEIF (tg_op = 'DELETE') THEN
--         INSERT INTO Mix_notes_history (
--             action_type, action_time, Mix_note_id,
--             upload_date, description, Mix_id
--         ) VALUES (
--             'delete', now(), old.id,
--             old.upload_date, old.description, old.Mix_id
--         );
--     END IF;
--     RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;
--
-- CREATE TRIGGER Mix_notes_insert
--     AFTER INSERT ON Mix_notes
--     FOR EACH ROW
-- EXECUTE FUNCTION log_mix_notes_changes();
--
-- CREATE TRIGGER Mix_notes_update
--     AFTER UPDATE ON Mix_notes
--     FOR EACH ROW
-- EXECUTE FUNCTION log_mix_notes_changes();
--
-- CREATE TRIGGER Mix_notes_delete
--     BEFORE DELETE ON Mix_notes
--     FOR EACH ROW
-- EXECUTE FUNCTION log_mix_notes_changes();


INSERT INTO User_accounts (first_name, last_name, username, password, role, email, phone_number)
VALUES
    ('Mateusz', 'Muzioł', 'Matias3004', 'dupa', 'OWNER', 'matias3004@gmail.com', '790652360'),
    ('Łukasz', 'Rudkiewicz', 'luki', 'kochammondeo', 'ENGINEER', 'leszcz@vp.pl', '123456789'),
    ('Kacper', 'Kulios', 'sejtenyst', '666szatan', 'CLIENT', 'parufczakkacper@gmail.com', '987654321');

-- Dodawanie kabli XLR
insert into Equipment (name, type, quantity, backline)
values ('XLR 1m', 'Kabel', 40, 0);
insert into Equipment (name, type, quantity, backline)
values ('XLR 3m', 'Kabel', 60, 0);
insert into Equipment (name, type, quantity, backline)
values ('XLR 5m', 'Kabel', 60, 0);
insert into Equipment (name, type, quantity, backline)
values ('XLR 6m', 'Kabel', 60, 0);
insert into Equipment (name, type, quantity, backline)
values ('XLR 8m', 'Kabel', 30, 0);
insert into Equipment (name, type, quantity, backline)
values ('XLR 10m', 'Kabel', 25, 0);
insert into Equipment (name, type, quantity, backline)
values ('XLR 12m', 'Kabel', 25, 0);
insert into Equipment (name, type, quantity, backline)
values ('XLR 15m', 'Kabel', 20, 0);

-- Dodawanie kabli Jack
insert into Equipment (name, type, quantity, backline)
values ('Jack TS 0,5m', 'Kabel', 20, 0);
insert into Equipment (name, type, quantity, backline)
values ('Jack TRS 0,5m', 'Kabel', 20, 0);
insert into Equipment (name, type, quantity, backline)
values ('Jack TS 1,5m', 'Kabel', 20, 0);
insert into Equipment (name, type, quantity, backline)
values ('Jack TRS 1,5m', 'Kabel', 20, 0);
insert into Equipment (name, type, quantity, backline)
values ('Jack TS 3m', 'Kabel', 20, 0);
insert into Equipment (name, type, quantity, backline)
values ('Jack TRS 3m', 'Kabel', 20, 0);
insert into Equipment (name, type, quantity, backline)
values ('Jack TS 6m', 'Kabel', 20, 0);
insert into Equipment (name, type, quantity, backline)
values ('Jack TRS 6m', 'Kabel', 20, 0);
insert into Equipment (name, type, quantity, backline)
values ('Jack TRS 8m', 'Kabel', 20, 0);
insert into Equipment (name, type, quantity, backline)
values ('Jack TRS 10m', 'Kabel', 20, 0);

-- Interfejs audio
insert into Equipment (name, type, quantity, backline)
values ('UAD Apollo x16', 'Interfejs Audio', 4, 0);
-- Preampy
insert into Equipment (name, type, quantity, backline)
values ('API Audio 3124MV', 'Przedwzmacniacz Mikrofonowy', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Focusrite ISA 428 MKII', 'Przedwzmacniacz Mikrofonowy', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Audient ASP 880', 'Przedwzmacniacz Mikrofonowy', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Warm Audio WA273-EQ', 'Przedwzmacniacz Mikrofonowy', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Neve 1073 DPX Dual Preamp & EQ', 'Przedwzmacniacz Mikrofonowy', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Universal Audio 4-710D Twin-Finity', 'Przedwzmacniacz Mikrofonowy', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Audient ASP 800', 'Przedwzmacniacz Mikrofonowy', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Focusrite ISA 828 MKII', 'Przedwzmacniacz Mikrofonowy', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Tube-Tech MP2A', 'Przedwzmacniacz Mikrofonowy', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('SPL Goldmike MK2', 'Przedwzmacniacz Mikrofonowy', 1, 0);
-- Monitory odsłuchowe
insert into Equipment (name, type, quantity, backline)
values ('Neumann KH 120 A', 'Monitor Odsłuchowy', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Adam S3H', 'Monitor Odsłuchowy', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Audient Nero', 'Konsola Odsłuchowa', 1, 0);

-- Kompresory i efekty
insert into Equipment (name, type, quantity, backline)
values ('Empirical Labs EL8X-S Distressor', 'Kompresor', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Looptrotter Monster Compressor 2', 'Kompresor', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Tube-Tech CL2-A', 'Kompresor', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Neve 33609 Limiter / Compressor', 'Kompresor', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Warm Audio WA-2A', 'Kompresor', 4, 0);
insert into Equipment (name, type, quantity, backline)
values ('Warm Audio WA76', 'Kompresor', 4, 0);
insert into Equipment (name, type, quantity, backline)
values ('API Audio 2500+', 'Kompresor', 2, 0);

-- Mikrofony
insert into Equipment (name, type, quantity, backline)
values ('Shure SM57', 'Mikrofon Dynamiczny', 16, 0);
insert into Equipment (name, type, quantity, backline)
values ('Shure Beta 57 A', 'Mikrofon Dynamiczny', 8, 0);
insert into Equipment (name, type, quantity, backline)
values ('Shure SM58 LC', 'Mikrofon Dynamiczny', 14, 0);
insert into Equipment (name, type, quantity, backline)
values ('Shure Beta 58 A', 'Mikrofon Dynamiczny', 6, 0);
insert into Equipment (name, type, quantity, backline)
values ('Shure SM 7 B', 'Mikrofon Dynamiczny', 6, 0);
insert into Equipment (name, type, quantity, backline)
values ('Lewitt LCT 440 PURE', 'Mikrofon Pojemnościowy Wielkomembranowy', 4, 0);
insert into Equipment (name, type, quantity, backline)
values ('Lewitt LCT 1040', 'Mikrofon Pojemnościowy Wielkomembranowy', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Neumann U87 Ai', 'Mikrofon Pojemnościowy Wielkomembranowy', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Neumann TLM 103', 'Mikrofon Pojemnościowy Wielkomembranowy', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Neumann TLM 103 mt', 'Mikrofon Pojemnościowy Wielkomembranowy', 4, 0);
insert into Equipment (name, type, quantity, backline)
values ('Neumann U47 FET', 'Mikrofon Pojemnościowy Wielkomembranowy', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('AKG C414 XLS', 'Mikrofon Pojemnościowy Wielkomembranowy', 6, 0);
insert into Equipment (name, type, quantity, backline)
values ('AKG C214', 'Mikrofon Pojemnościowy Wielkomembranowy', 4, 0);
insert into Equipment (name, type, quantity, backline)
values ('AKG C414 XLII', 'Mikrofon Pojemnościowy Wielkomembranowy', 4, 0);
insert into Equipment (name, type, quantity, backline)
values ('Audio Technica AE 3000', 'Mikrofon Pojemnościowy Wielkomembranowy', 4, 0);
insert into Equipment (name, type, quantity, backline)
values ('Audio Technica AT4050 SM', 'Mikrofon Pojemnościowy Wielkomembranowy', 4, 0);
insert into Equipment (name, type, quantity, backline)
values ('Sony C-800G', 'Mikrofon Pojemnościowy Wielkomembranowy', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Neumann KM184', 'Mikrofon Pojemnościowy Mało-Membranowy', 8, 0);
insert into Equipment (name, type, quantity, backline)
values ('Rode NT5', 'Mikrofon Pojemnościowy Mało-Membranowy', 10, 0);
insert into Equipment (name, type, quantity, backline)
values ('Shure SM81', 'Mikrofon Pojemnościowy Mało-Membranowy', 6, 0);
insert into Equipment (name, type, quantity, backline)
values ('AKG C 451 B', 'Mikrofon Pojemnościowy Mało-Membranowy', 4, 0);
insert into Equipment (name, type, quantity, backline)
values ('Sennheiser MD421-II', 'Mikrofon Dynamiczny', 6, 0);
insert into Equipment (name, type, quantity, backline)
values ('Sennheiser MD441-U', 'Mikrofon Dynamiczny', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('DPA 4099', 'Mikrofon Pojemnościowy Klips', 10, 0);
insert into Equipment (name, type, quantity, backline)
values ('Audio Technica ATM350UL', 'Mikrofon Pojemnościowy Klips', 14, 0);
insert into Equipment (name, type, quantity, backline)
values ('Audix D6', 'Mikrofon Dynamiczny', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Audix D4', 'Mikrofon Dynamiczny', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Shure Beta 52A', 'Mikrofon Dynamiczny', 3, 0);
insert into Equipment (name, type, quantity, backline)
values ('Telefunke M82', 'Mikrofon Dynamiczny', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Shure Beta 91A', 'Mikrofon Pojemnościowy', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('Sennheiser E 902', 'Mikrofon Dynamiczny', 2, 0);
insert into Equipment (name, type, quantity, backline)
values ('AKG D112 MKII', 'Mikrofon Dynamiczny', 3, 0);
insert into Equipment (name, type, quantity, backline)
values ('EV RE20 RE-Series', 'Mikrofon Dynamiczny', 4, 0);
insert into Equipment (name, type, quantity, backline)
values ('Sennheiser E 906', 'Mikrofon Dynamiczny', 6, 0);
insert into Equipment (name, type, quantity, backline)
values ('Sennheiser E609', 'Mikrofon Dynamiczny', 8, 0);
insert into Equipment (name, type, quantity, backline)
values ('Sennheiser E 904', 'Mikrofon Dynamiczny', 6, 0);
insert into Equipment (name, type, quantity, backline)
values ('Audix D2', 'Mikrofon Dynamiczny', 6, 0);
insert into Equipment (name, type, quantity, backline)
values ('Solomon SubKick LoFReQ', 'Subkick', 4, 0);

-- Oprogramowanie
insert into Equipment (name, type, quantity, backline)
values ('Apple Logic Pro X', 'Oprogramowanie DAW', 1,0);
insert into Equipment (name, type, quantity, backline)
values ('Cockos REAPER', 'Oprogramowanie DAW', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Avid Pro Tools Studio', 'Oprogramowanie DAW', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Slate Digital All Access Pass', 'Pluginy Audio', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Waves Mercury', 'Pluginy Audio', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Native Instruments Komplete 14 Ultimate', 'Instrumenty Wirtualne', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Steven Slate Audio SSD 5', 'Instrumenty Wirtualne', 1, 0);
insert into Equipment (name, type, quantity, backline)
values ('Steven Slate Audio Trigger 2 Platinum', 'Instrumenty Wirtualne', 1, 0);

-- Sprzęt dla klientów
insert into Equipment (name, type, quantity, backline)
values ('Sonor AQ1', 'Zestaw perkusyjny', 1, 1);
insert into Equipment (name, type, quantity, backline)
values ('Peavey 5150', 'Wzmacniacz gitarowy', 2, 1);
insert into Equipment (name, type, quantity, backline)
values ('Mesa Boogie Dual Rectifier', 'Wzmacniacz gitarowy', 1, 1);
insert into Equipment (name, type, quantity, backline)
values ('Ampeg SVT-2', 'Wzmacniacz basowy', 1, 1);
