USE Studio_nagran;

DROP TABLE IF EXISTS Equipment;

create table Equipment (
    id int not null auto_increment,
    name varchar(64) not null,
    type varchar(64),
    quantity int not null,
    backline bool not null,
    primary key (id),
    unique key name_unique (name)
);

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