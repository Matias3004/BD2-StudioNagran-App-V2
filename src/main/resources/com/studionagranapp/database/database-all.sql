use Studio_nagran;

drop trigger Mix_notes_insert;
drop trigger Mix_notes_update;
drop trigger Mix_notes_delete;
drop trigger Mixes_insert;
drop trigger Mixes_update;
drop trigger Mixes_delete;
drop trigger Equipment_insert;
drop trigger Equipment_update;
drop trigger Equipment_delete;

set foreign_key_checks = 0;
drop table User_accounts;
drop table Sessions;
drop table Equipment;
drop table Equipment_history;
drop table Mixes;
drop table Mixes_history;
drop table Mix_notes;
drop table Mix_notes_history;
set foreign_key_checks = 1;

set SQL_SAFE_UPDATES = 0;

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

create table Equipment (
    id int not null auto_increment,
    name varchar(64) not null,
    type varchar(64),
    quantity int not null,
    backline bool not null,
    primary key (id),
    unique key name_unique (name)
);

create table Equipment_history (
    id int auto_increment primary key,
    action enum('insert', 'update', 'delete'),
    action_time timestamp default current_timestamp,
    Equipment_id int,
    name varchar(64) not null,
    type varchar(64),
    quantity int not null,
    backline bool not null
);

create table Mixes (
    id int auto_increment primary key,
    filename varchar(255) not null unique,
    upload_date timestamp default current_timestamp not null,
    path varchar(255) not null unique,
    Session_id int not null
);

create table Mixes_history (
    id int auto_increment primary key ,
    action enum('insert', 'update', 'delete'),
    action_time timestamp default current_timestamp,
    Mix_id int,
    filename varchar(255) not null,
    upload_date timestamp default current_timestamp not null,
    path varchar(255) not null,
    Session_id int not null
);

create table Mix_notes (
    id int auto_increment primary key,
    upload_date timestamp default current_timestamp not null,
    description varchar(255) not null,
    Mix_id int not null
);

create table Mix_notes_history (
    id int auto_increment primary key,
    action enum('insert', 'update', 'delete'),
    action_time timestamp default current_timestamp,
    Mix_note_id int,
    upload_date timestamp default current_timestamp not null,
    description varchar(255) not null,
    Mix_id int not null
);

alter table Equipment
add (
    constraint name_u unique (name)
);

alter table Mixes
add (
    constraint filename_u unique (filename),
    constraint path_u unique (path)
);

ALTER TABLE Sessions
    ADD (
        CONSTRAINT Sessions_fk_1 FOREIGN KEY (Clients_id)
            REFERENCES User_accounts (account_id) ON DELETE CASCADE,
        CONSTRAINT Sessions_fk_2 FOREIGN KEY (Engineer_id)
            REFERENCES User_accounts (account_id) ON DELETE CASCADE
        );

alter table Mixes
add constraint Mixes_fk_1 foreign key (Session_id)
    references Sessions (id) on delete cascade;

alter table Mixes_history
add constraint Mixes_history_fk_1 foreign key (Mix_id)
    references Mixes (id);

alter table Mix_notes
add constraint Mix_notes_fk_1 foreign key (Mix_id)
    references Mixes (id) on delete cascade;

alter table Mix_notes_history
add constraint Mix_notes_history_fk_1 foreign key (Mix_note_id)
    references Mix_notes (id);

alter table Equipment_history
add constraint Equipment_history_fk_1 foreign key (Equipment_id)
    references Equipment (id) on delete cascade;
    
alter table Equipment_history
drop foreign key Equipment_history_fk_1;

delimiter $$

create trigger Equipment_insert
    after insert on Equipment
    for each row
begin
    insert into Equipment_history (
        action,
        Equipment_id,
        name,
        type,
        quantity,
        backline
    ) values (
        'insert',
        new.id,
        new.name,
        new.type,
        new.quantity,
        new.backline
    );
end $$

delimiter ;

delimiter $$

create trigger Equipment_update
    after update on Equipment
    for each row
begin
    insert into Equipment_history (
        action,
        Equipment_id,
        name,
        type,
        quantity,
        backline
    ) values (
        'update',
        new.id,
        new.name,
        new.type,
        new.quantity,
        new.backline
    );
end $$

delimiter ;

delimiter $$

create trigger Equipment_delete
    before delete on Equipment
    for each row
begin
    insert into Equipment_history (
        action,
        Equipment_id,
        name,
        type,
        quantity,
        backline
    ) values (
        'delete',
        old.id,
        old.name,
        old.type,
        old.quantity,
        old.backline
    );
end $$

delimiter ;

delimiter $$

create trigger Mixes_insert
    after insert on Mixes
    for each row
begin
    insert into Mixes_history (
        action,
        mix_id,
        filename,
        upload_date,
        path,
        Session_id
    ) values (
        'insert',
        new.id,
        new.filename,
        new.upload_date,
        new.path,
        new.Session_id
    );
end $$

delimiter ;

delimiter $$

create trigger Mixes_update
    after update on Mixes
    for each row
begin
    insert into Mixes_history (
        action,
        Mix_id,
        filename,
        upload_date,
        path,
        Session_id
    ) values (
        'update',
        new.id,
        new.filename,
        new.upload_date,
        new.path,
        new.Session_id
    );
end $$

delimiter ;

delimiter $$

create trigger Mixes_delete
	before delete on Mixes
    for each row
begin
    insert into Mixes_history (
        action,
        Mix_id,
        filename,
        upload_date,
        path,
        Session_id
    ) values (
        'delete',
        old.id,
        old.filename,
        old.upload_date,
        old.path,
        old.Session_id
    );
end $$

delimiter ;

delimiter $$

create trigger Mix_notes_insert
    after insert on Mix_notes
    for each row
begin
    insert into Mix_notes_history (
        action,
        Mix_note_id,
        upload_date,
        description,
        Mix_id
    ) values (
        'insert',
        new.id,
        new.upload_date,
        new.description,
        new.Mix_id
    );
end $$

delimiter ;

delimiter $$

create trigger Mix_notes_update
    after update on Mix_notes
    for each row
begin
    insert into Mix_notes_history (
        action,
        Mix_note_id,
        upload_date,
        description,
        Mix_id
    ) values (
        'update',
        new.id,
        new.upload_date,
        new.description,
        new.Mix_id
    );
end $$

delimiter ;

delimiter $$

create trigger Mix_notes_delete
    before delete on Mix_notes
    for each row
begin
    insert into Mix_notes_history (
        action,
        Mix_note_id,
        upload_date,
        description,
        Mix_id
    ) values (
        'delete',
        old.id,
        old.upload_date,
        old.description,
        old.Mix_id
    );
end $$

delimiter ;

create user 'Owner'@'localhost'
    identified by 'password';

grant all privileges
    on Studio_nagran.*
    to 'Owner'@'localhost'
with grant option;

create user 'Engineer'@'localhost'
    identified by 'password';

grant update, select
    on Studio_nagran.Equipment
    to 'Engineer'@'localhost'
with grant option;

grant insert, update, delete, select
    on Studio_nagran.Mixes
    to 'Engineer'@'localhost'
with grant option;

grant insert, update, delete, select
    on Studio_nagran.Mix_notes
    to 'Engineer'@'localhost'
with grant option;

grant update, select
    on Studio_nagran.Sessions
    to 'Engineer'@'localhost'
with grant option;

create user 'Client'@'localhost'
    identified by 'password';

grant insert, select
    on Studio_nagran.Sessions
    to 'Client'@'localhost'
with grant option;

grant select
    on Studio_nagran.Equipment
    to 'Client'@'localhost'
with grant option;

grant select
    on Studio_nagran.Mixes
    to 'Client'@'localhost'
with grant option;

grant insert, update, delete, select
    on Studio_nagran.Mix_notes
    to 'Client'@'localhost'
with grant option;

LOCK TABLES User_accounts WRITE;
ALTER TABLE User_accounts DISABLE KEYS;
INSERT INTO User_accounts VALUES
    (1, 'Mateusz', 'Muzioł', 'Matias3004', 'dupa', 'OWNER', 'matias3004@gmail.com', '790652360'),
    (2, 'Łukasz', 'Rudkiewicz', 'luki', 'kochammondeo', 'ENGINEER', 'leszcz@vp.pl', '123456789'),
    (3, 'Kacper', 'Kulios', 'sejtenyst', '666szatan', 'CLIENT', 'parufczakkacper@gmail.com', '987654321');
ALTER TABLE User_accounts ENABLE KEYS;
UNLOCK TABLES;

LOCK TABLES Equipment WRITE;
ALTER TABLE Equipment DISABLE KEYS;

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
values ('UAD Apollo x16', 'Interfejs Audio', 4, false);
-- Preampy
insert into Equipment (name, type, quantity, backline)
values ('API Audio 3124MV', 'Przedwzmacniacz Mikrofonowy', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Focusrite ISA 428 MKII', 'Przedwzmacniacz Mikrofonowy', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Audient ASP 880', 'Przedwzmacniacz Mikrofonowy', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Warm Audio WA273-EQ', 'Przedwzmacniacz Mikrofonowy', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Neve 1073 DPX Dual Preamp & EQ', 'Przedwzmacniacz Mikrofonowy', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Universal Audio 4-710D Twin-Finity', 'Przedwzmacniacz Mikrofonowy', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Audient ASP 800', 'Przedwzmacniacz Mikrofonowy', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Focusrite ISA 828 MKII', 'Przedwzmacniacz Mikrofonowy', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Tube-Tech MP2A', 'Przedwzmacniacz Mikrofonowy', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('SPL Goldmike MK2', 'Przedwzmacniacz Mikrofonowy', 1, false);
-- Monitory odsłuchowe
insert into Equipment (name, type, quantity, backline)
values ('Neumann KH 120 A', 'Monitor Odsłuchowy', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Adam S3H', 'Monitor Odsłuchowy', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Audient Nero', 'Konsola Odsłuchowa', 1, false);

-- Kompresory i efekty
insert into Equipment (name, type, quantity, backline)
values ('Empirical Labs EL8X-S Distressor', 'Kompresor', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Looptrotter Monster Compressor 2', 'Kompresor', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Tube-Tech CL2-A', 'Kompresor', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Neve 33609 Limiter / Compressor', 'Kompresor', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Warm Audio WA-2A', 'Kompresor', 4, false);
insert into Equipment (name, type, quantity, backline)
values ('Warm Audio WA76', 'Kompresor', 4, false);
insert into Equipment (name, type, quantity, backline)
values ('API Audio 2500+', 'Kompresor', 2, false);

-- Mikrofony
insert into Equipment (name, type, quantity, backline)
values ('Shure SM57', 'Mikrofon Dynamiczny', 16, false);
insert into Equipment (name, type, quantity, backline)
values ('Shure Beta 57 A', 'Mikrofon Dynamiczny', 8, false);
insert into Equipment (name, type, quantity, backline)
values ('Shure SM58 LC', 'Mikrofon Dynamiczny', 14, false);
insert into Equipment (name, type, quantity, backline)
values ('Shure Beta 58 A', 'Mikrofon Dynamiczny', 6, false);
insert into Equipment (name, type, quantity, backline)
values ('Shure SM 7 B', 'Mikrofon Dynamiczny', 6, false);
insert into Equipment (name, type, quantity, backline)
values ('Lewitt LCT 440 PURE', 'Mikrofon Pojemnościowy Wielkomembranowy', 4, false);
insert into Equipment (name, type, quantity, backline)
values ('Lewitt LCT 1040', 'Mikrofon Pojemnościowy Wielkomembranowy', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Neumann U87 Ai', 'Mikrofon Pojemnościowy Wielkomembranowy', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Neumann TLM 103', 'Mikrofon Pojemnościowy Wielkomembranowy', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Neumann TLM 103 mt', 'Mikrofon Pojemnościowy Wielkomembranowy', 4, false);
insert into Equipment (name, type, quantity, backline)
values ('Neumann U47 FET', 'Mikrofon Pojemnościowy Wielkomembranowy', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('AKG C414 XLS', 'Mikrofon Pojemnościowy Wielkomembranowy', 6, false);
insert into Equipment (name, type, quantity, backline)
values ('AKG C214', 'Mikrofon Pojemnościowy Wielkomembranowy', 4, false);
insert into Equipment (name, type, quantity, backline)
values ('AKG C414 XLII', 'Mikrofon Pojemnościowy Wielkomembranowy', 4, false);
insert into Equipment (name, type, quantity, backline)
values ('Audio Technica AE 3000', 'Mikrofon Pojemnościowy Wielkomembranowy', 4, false);
insert into Equipment (name, type, quantity, backline)
values ('Audio Technica AT4050 SM', 'Mikrofon Pojemnościowy Wielkomembranowy', 4, false);
insert into Equipment (name, type, quantity, backline)
values ('Sony C-800G', 'Mikrofon Pojemnościowy Wielkomembranowy', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Neumann KM184', 'Mikrofon Pojemnościowy Mało-Membranowy', 8, false);
insert into Equipment (name, type, quantity, backline)
values ('Rode NT5', 'Mikrofon Pojemnościowy Mało-Membranowy', 10, false);
insert into Equipment (name, type, quantity, backline)
values ('Shure SM81', 'Mikrofon Pojemnościowy Mało-Membranowy', 6, false);
insert into Equipment (name, type, quantity, backline)
values ('AKG C 451 B', 'Mikrofon Pojemnościowy Mało-Membranowy', 4, false);
insert into Equipment (name, type, quantity, backline)
values ('Sennheiser MD421-II', 'Mikrofon Dynamiczny', 6, false);
insert into Equipment (name, type, quantity, backline)
values ('Sennheiser MD441-U', 'Mikrofon Dynamiczny', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('DPA 4099', 'Mikrofon Pojemnościowy Klips', 10, false);
insert into Equipment (name, type, quantity, backline)
values ('Audio Technica ATM350UL', 'Mikrofon Pojemnościowy Klips', 14, false);
insert into Equipment (name, type, quantity, backline)
values ('Audix D6', 'Mikrofon Dynamiczny', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Audix D4', 'Mikrofon Dynamiczny', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Shure Beta 52A', 'Mikrofon Dynamiczny', 3, false);
insert into Equipment (name, type, quantity, backline)
values ('Telefunke M82', 'Mikrofon Dynamiczny', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Shure Beta 91A', 'Mikrofon Pojemnościowy', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('Sennheiser E 902', 'Mikrofon Dynamiczny', 2, false);
insert into Equipment (name, type, quantity, backline)
values ('AKG D112 MKII', 'Mikrofon Dynamiczny', 3, false);
insert into Equipment (name, type, quantity, backline)
values ('EV RE20 RE-Series', 'Mikrofon Dynamiczny', 4, false);
insert into Equipment (name, type, quantity, backline)
values ('Sennheiser E 906', 'Mikrofon Dynamiczny', 6, false);
insert into Equipment (name, type, quantity, backline)
values ('Sennheiser E609', 'Mikrofon Dynamiczny', 8, false);
insert into Equipment (name, type, quantity, backline)
values ('Sennheiser E 904', 'Mikrofon Dynamiczny', 6, false);
insert into Equipment (name, type, quantity, backline)
values ('Audix D2', 'Mikrofon Dynamiczny', 6, false);
insert into Equipment (name, type, quantity, backline)
values ('Solomon SubKick LoFReQ', 'Subkick', 4, false);

-- Oprogramowanie
insert into Equipment (name, type, quantity, backline)
values ('Apple Logic Pro X', 'Oprogramowanie DAW', 1,false);
insert into Equipment (name, type, quantity, backline)
values ('Cockos REAPER', 'Oprogramowanie DAW', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Avid Pro Tools Studio', 'Oprogramowanie DAW', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Slate Digital All Access Pass', 'Pluginy Audio', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Waves Mercury', 'Pluginy Audio', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Native Instruments Komplete 14 Ultimate', 'Instrumenty Wirtualne', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Steven Slate Audio SSD 5', 'Instrumenty Wirtualne', 1, false);
insert into Equipment (name, type, quantity, backline)
values ('Steven Slate Audio Trigger 2 Platinum', 'Instrumenty Wirtualne', 1, false);

-- Sprzęt dla klientów
insert into Equipment (name, type, quantity, backline)
values ('Sonor AQ1', 'Zestaw perkusyjny', 1, true);
insert into Equipment (name, type, quantity, backline)
values ('Peavey 5150', 'Wzmacniacz gitarowy', 2, true);
insert into Equipment (name, type, quantity, backline)
values ('Mesa Boogie Dual Rectifier', 'Wzmacniacz gitarowy', 1, true);
insert into Equipment (name, type, quantity, backline)
values ('Ampeg SVT-2', 'Wzmacniacz basowy', 1, true);

UNLOCK TABLES;
