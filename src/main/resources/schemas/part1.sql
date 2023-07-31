DROP TABLE IF EXISTS p2p CASCADE;
DROP TABLE IF EXISTS transferredpoints CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS recommendations CASCADE;
DROP TABLE IF EXISTS timetracking CASCADE;
DROP TABLE IF EXISTS verter CASCADE;
DROP TABLE IF EXISTS xp CASCADE;
DROP TABLE IF EXISTS tasks CASCADE ;
DROP TABLE IF EXISTS peers CASCADE ;
DROP TABLE IF EXISTS checks CASCADE ;
DROP SCHEMA IF EXISTS public CASCADE ;

CREATE SCHEMA IF NOT EXISTS public;

CREATE TYPE STATUS AS ENUM ('Start', 'Success', 'Failure');

CREATE TABLE Peers
(
    nickname VARCHAR PRIMARY KEY,
    birthday DATE
);

CREATE TABLE Tasks
(
    title       VARCHAR PRIMARY KEY,
    parent_task VARCHAR NULL REFERENCES Tasks (Title) CHECK ( parent_task != title ),
    max_xp      BIGINT NOT NULL
);

CREATE TABLE Checks
(
    id   SERIAL PRIMARY KEY,
    peer VARCHAR NOT NULL REFERENCES Peers (nickname),
    task VARCHAR NOT NULL REFERENCES Tasks (title),
    date DATE NOT NULL
);

CREATE  TABLE P2P
(
    id            SERIAL PRIMARY KEY,
    check_id      BIGINT NOT NULL REFERENCES Checks (id),
    checking_peer VARCHAR NOT NULL REFERENCES Peers (nickname),
    state         STATUS  NOT NULL,
    time          TIME,
    CONSTRAINT uk_p2p UNIQUE (check_id, state)
);

CREATE TABLE Verter
(
    id       SERIAL NOT NULL PRIMARY KEY,
    check_id BIGINT NOT NULL REFERENCES Checks(id),
    state    STATUS NOT NULL,
    time     TIME
);

CREATE TABLE XP
(
    id        SERIAL PRIMARY KEY,
    check_id  BIGINT NOT NULL REFERENCES Checks(id),
    xp_amount BIGINT NOT NULL
);

CREATE TABLE TransferredPoints
(
    id            SERIAL PRIMARY KEY,
    checking_peer VARCHAR NOT NULL REFERENCES Peers (nickname),
    checked_peer  VARCHAR NOT NULL REFERENCES Peers (nickname) CHECK ( checked_peer != checking_peer ),
    points_amount BIGINT NOT NULL
);

CREATE TABLE Friends
(
    id    SERIAL PRIMARY KEY,
    peer1 VARCHAR NOT NULL REFERENCES Peers (nickname),
    peer2 VARCHAR NOT NULL REFERENCES Peers (nickname) CHECK ( peer2 != peer1 ),
    CONSTRAINT uk_friends UNIQUE (peer1, peer2)
);

CREATE TABLE Recommendations
(
    id               SERIAL PRIMARY KEY,
    peer             VARCHAR NOT NULL REFERENCES Peers (nickname),
    recommended_peer VARCHAR NOT NULL CHECK ( peer != recommended_peer ),
    CONSTRAINT uk_recommendations UNIQUE (peer, recommended_peer)
);

CREATE TABLE TimeTracking
(
    id    SERIAL PRIMARY KEY,
    peer  VARCHAR NOT NULL REFERENCES Peers (nickname),
    date  DATE NOT NULL DEFAULT CURRENT_DATE,
    time  TIME NOT NULL DEFAULT CURRENT_TIME,
    state INT CHECK ( state in (1,2))
);

-- Conditions for p2p:  1) 'Start' is first record
--                      2) Maximum 2 records 'Start'+'Success' or 'Start'+'Failure'
--                      4) Same peer check
--                      3) Time order
CREATE OR REPLACE FUNCTION fnc_trg_p2p_insert_check() RETURNS trigger AS $trg_p2p_insert_check$
    BEGIN
        IF (SELECT count(*) FROM p2p WHERE check_id = NEW.check_id) >= 2 THEN
            RAISE EXCEPTION 'p2p insert or update error: tab already has 2 rows with such check_id';
        END IF;
        IF (NEW.state = 'Success' OR NEW.state = 'Failure') THEN
            IF NOT EXISTS (SELECT * FROM p2p WHERE check_id = NEW.check_id) THEN
                RAISE EXCEPTION 'p2p insert or update error: check with such check_id did not start';
            END IF;
            IF (SELECT checking_peer FROM p2p WHERE check_id = NEW.check_id) != NEW.checking_peer THEN
                RAISE EXCEPTION 'p2p insert or update error: two different peers detected for one check_id';
            END IF;
            IF (SELECT time FROM p2p WHERE check_id = NEW.check_id) > NEW.time THEN
                RAISE EXCEPTION 'p2p insert or update error: invalid time order';
            END IF;
        END IF;
    RETURN NEW;
    END;
$trg_p2p_insert_check$ LANGUAGE plpgsql;

CREATE TRIGGER trg_p2p_insert_check BEFORE INSERT OR UPDATE ON p2p
    FOR EACH ROW EXECUTE FUNCTION fnc_trg_p2p_insert_check();

-- Conditions for Verter:  1) 'Start' is first record
--                         2) Maximum 2 records 'Start'+'Success' or 'Start'+'Failure'
--                         3) Need successful P2P check before start verter
--                         4) time order
CREATE OR REPLACE FUNCTION fnc_trg_verter_insert_check() RETURNS trigger AS $trg_verter_insert_check$
    BEGIN
        IF (SELECT count(*) FROM verter WHERE check_id = NEW.check_id) >= 2 THEN
            RAISE EXCEPTION 'Verter insert or update error: tab already has 2 rows with such check_id';
        END IF;
        IF NOT EXISTS (SELECT * FROM p2p
            WHERE check_id = NEW.check_id AND state = 'Success') THEN
                RAISE EXCEPTION 'Verter insert or update error: no Success P2P check found';
        END IF;
        IF (NEW.state = 'Success' OR NEW.state = 'Failure') THEN
            IF NOT EXISTS (SELECT * FROM verter WHERE check_id = NEW.check_id) THEN
                RAISE EXCEPTION 'Verter insert or update error: check with such check_id did not start';
            END IF;
            IF (SELECT time FROM verter WHERE check_id = NEW.check_id) > NEW.time THEN
                RAISE EXCEPTION 'p2p insert or update error: invalid time order';
            END IF;
        END IF;
        RETURN NEW;
    END;
$trg_verter_insert_check$ LANGUAGE plpgsql;

CREATE TRIGGER trg_verter_insert_check BEFORE INSERT OR UPDATE ON verter
    FOR EACH ROW EXECUTE FUNCTION fnc_trg_verter_insert_check();

CREATE OR REPLACE FUNCTION fnc_trg_p2p() RETURNS TRIGGER AS $p2p$
DECLARE
    checked_peer_from_checks VARCHAR;
BEGIN
    IF (NEW.state = 'Start') THEN
        checked_peer_from_checks := (SELECT peer FROM checks WHERE id = NEW.check_id);
        IF EXISTS (SELECT id
                   FROM transferredpoints
                   WHERE  checking_peer = NEW.checking_peer
                     AND checked_peer = checked_peer_from_checks) THEN
            UPDATE transferredpoints
            SET points_amount = points_amount + 1
            WHERE checking_peer = NEW.checking_peer
              AND checked_peer = checked_peer_from_checks;
        ELSE
            INSERT INTO transferredpoints (checking_peer, checked_peer, points_amount)
            VALUES (NEW.checking_peer, checked_peer_from_checks, 1);
        END IF;
    END IF;
    RETURN NULL;
END;
$p2p$ LANGUAGE plpgsql;

-- part 2, 3) Написать триггер: после добавления записи со статутом "начало" в таблицу P2P, изменить соответствующую запись в таблице TransferredPoints

CREATE TRIGGER trg_p2p AFTER INSERT ON p2p
    FOR EACH ROW EXECUTE FUNCTION fnc_trg_p2p();

CREATE OR REPLACE PROCEDURE friends_insert(peer_1 text, peer_2 text)
LANGUAGE sql AS $$
    INSERT INTO friends (peer1, peer2) VALUES (peer_1, peer_2);
    INSERT INTO friends (peer1, peer2) VALUES (peer_2, peer_1);
$$;

CREATE OR REPLACE PROCEDURE insert_check_success(peer varchar, peer_ch varchar, task varchar, date date, time_ TIME)
LANGUAGE plpgsql AS $$
DECLARE
    last_id BIGINT;
BEGIN
    INSERT INTO Checks (peer, task, date)
        VALUES (peer, task, date);
        last_id := (SELECT id FROM Checks ORDER BY id DESC LIMIT 1);
    INSERT INTO P2P (check_id, checking_peer, state, time)
        VALUES  (last_id, peer_ch, 'Start', time_),
                (last_id, peer_ch, 'Success', time_ + '00:25:00');
    INSERT INTO Verter (check_id, state, time)
        VALUES  (last_id,  'Start', time_ + '00:30:00'),
                (last_id,  'Success', time_ + '00:35:00');
END;
$$;

CREATE OR REPLACE PROCEDURE insert_check_success(peer varchar, peer_ch varchar, task varchar, date date, time_ TIME, xp int)
LANGUAGE plpgsql AS $$
DECLARE
    last_id BIGINT;
    xp_max INT;
BEGIN
    xp_max := (SELECT max_xp FROM Tasks WHERE title = task);
    INSERT INTO Checks (peer, task, date)
        VALUES (peer, task, date);
    last_id := (SELECT id FROM Checks ORDER BY id DESC LIMIT 1);
    INSERT INTO P2P (check_id, checking_peer, state, time)
        VALUES  (last_id, peer_ch, 'Start', time_),
                (last_id, peer_ch, 'Success', time_ + '00:25:00');
    INSERT INTO Verter (check_id, state, time)
        VALUES  (last_id,  'Start', time_ + '00:30:00'),
                (last_id,  'Success', time_ + '00:35:00');
    INSERT INTO XP (check_id, xp_amount)
        VALUES  (last_id,  xp_max * xp / 100);
END;
$$;

CREATE OR REPLACE PROCEDURE insert_check_fail_p2p(peer varchar, peer_ch varchar, task varchar, date date, time_ TIME)
LANGUAGE plpgsql AS $$
DECLARE
    last_id BIGINT;
BEGIN
    INSERT INTO Checks (peer, task, date)
        VALUES (peer, task, date);
        last_id := (SELECT id FROM Checks ORDER BY id DESC LIMIT 1);
    INSERT INTO P2P (check_id, checking_peer, state, time)
        VALUES  (last_id, peer_ch, 'Start', time_),
                (last_id, peer_ch, 'Failure', time_ + '00:25:00');
END;
$$;

CREATE OR REPLACE PROCEDURE insert_check_fail_verter(peer varchar, peer_ch varchar, task varchar, date date, time_ TIME)
LANGUAGE plpgsql AS $$
DECLARE
    last_id BIGINT;
BEGIN
    INSERT INTO Checks (peer, task, date)
        VALUES (peer, task, date);
        last_id := (SELECT id FROM Checks ORDER BY id DESC LIMIT 1);
    INSERT INTO P2P (check_id, checking_peer, state, time)
        VALUES  (last_id, peer_ch, 'Start', time_),
                (last_id, peer_ch, 'Success', time_ + '00:25:00');
    INSERT INTO Verter (check_id, state, time)
        VALUES  (last_id,  'Start', time_ + '00:30:00'),
                (last_id,  'Failure', time_ + '00:35:00');
END;
$$;

-- IMPORT AND EXPORT PROCEDURES
CREATE OR REPLACE PROCEDURE exportdb(path text, delim "char")
LANGUAGE plpgsql AS $$
DECLARE
    tabs_list VARCHAR[] := ARRAY['Peers', 'Tasks', 'Checks', 'P2P', 'Verter', 'XP',
                                 'TransferredPoints', 'Friends', 'Recommendations', 'TimeTracking'];
    tab_name VARCHAR;
BEGIN
    FOREACH tab_name IN ARRAY tabs_list
    LOOP
        EXECUTE 'COPY ' || tab_name ||' to '''|| path || '/' || tab_name || '.csv'' WITH (FORMAT csv, DELIMITER'
                ||  quote_literal(delim) || ')';
    END LOOP;
END;
$$;

-- to Call export procedure
-- CALL exportdb('/tmp','|');

CREATE OR REPLACE PROCEDURE importdb(path text, delim "char")
LANGUAGE plpgsql AS $$
DECLARE
    tabs_list VARCHAR[] := ARRAY['Peers', 'Tasks', 'Checks', 'P2P', 'Verter', 'XP',
                                 'TransferredPoints', 'Friends', 'Recommendations', 'TimeTracking'];
    tab_name VARCHAR;
BEGIN
    FOREACH tab_name IN ARRAY tabs_list
    LOOP
        EXECUTE 'COPY ' || tab_name ||' from '''|| path || '/' || tab_name || '.csv'' WITH (FORMAT csv, DELIMITER'
                ||  quote_literal(delim) || ')';
    END LOOP;
END;
$$;

-- to Call import procedure
-- CALL importdb('/tmp', '|');

INSERT INTO Peers
VALUES ('maslynem', '1998-06-30'),
       ('azathotp', '2004-01-01'),
       ('gwynesse', '1986-11-04'),
       ('arthurdent', '1984-01-14'),
       ('fordprefect', '1982-02-13'),
       ('zaphodbeeblebrox', '1980-03-12'),
       ('marvin', '2007-05-10'),
       ('trillian', '1988--01-30'),
       ('slartibartfast', '1990-02-05');

INSERT INTO Tasks
VALUES  ('C2_s21_SimpleBashUtils', NULL, 250),
        ('C3_s21_string+', 'C2_s21_SimpleBashUtils', 500),
        ('C4_s21_math', 'C2_s21_SimpleBashUtils', 300),
        ('C5_s21_decimal', 'C2_s21_SimpleBashUtils', 350),
        ('C6_s21_matrix', 'C5_s21_decimal', 200),
        ('C7_s21_SmartCalc_v1.0', 'C6_s21_matrix', 500),
        ('C8_s21_3DViewer_v1.0', 'C7_s21_SmartCalc_v1.0', 750),

        ('DO1_s21_Linux', 'C3_s21_string+', 300),
        ('DO2_s21_Linux_Network', 'DO1_s21_Linux', 250),
        ('DO3_s21_LinuxMonitoring v1.0', 'DO2_s21_Linux_Network', 350),
        ('DO4_s21_LinuxMonitoring v2.0', 'DO3_s21_LinuxMonitoring v1.0', 350),
        ('DO5_s21_SimpleDocker', 'DO3_s21_LinuxMonitoring v1.0', 300),
        ('DO6_s21_CICD', 'DO5_s21_SimpleDocker', 300),

        ('CPP1_s21_matrix+', 'C8_s21_3DViewer_v1.0', 300),
        ('CPP2_s21_containers', 'CPP1_s21_matrix+', 350),
        ('CPP3_s21_SmartCalc_v2.0', 'CPP2_s21_containers', 600),
        ('CPP4_s21_3DViewer_v2.0', 'CPP3_s21_SmartCalc_v2.0', 750),
        ('CPP5_s21_3DViewer_v2.1', 'CPP4_s21_3DViewer_v2.0', 600),
        ('CPP6_s21_3DViewer_v2.2', 'CPP4_s21_3DViewer_v2.0', 800),
        ('CPP7_s21_MLP', 'CPP4_s21_3DViewer_v2.0', 700),
        ('CPP8_s21_PhotoLab_v1.0', 'CPP4_s21_3DViewer_v2.0', 450),
        ('CPP9_s21_MonitoringSystem', 'CPP4_s21_3DViewer_v2.0', 1000),

        ('A1_s21_Maze', 'CPP4_s21_3DViewer_v2.0', 300),
        ('A2_s21_SimpleNavigator v1.0', 'A1_s21_Maze', 400),
        ('A3_s21_Parallels', 'A2_s21_SimpleNavigator v1.0', 300),
        ('A4_s21_Crypto', 'A2_s21_SimpleNavigator v1.0', 350),
        ('A5_s21_memory', 'A2_s21_SimpleNavigator v1.0', 400),
        ('A6_s21_Transactions', 'A2_s21_SimpleNavigator v1.0', 700),
        ('A7_s21_DNA Analyzer', 'A2_s21_SimpleNavigator v1.0', 800),
        ('A8_s21_Algorithmic trading', 'A2_s21_SimpleNavigator v1.0', 800),

        ('SQL2_s21_Info21 v1.0', 'C8_s21_3DViewer_v1.0', 500),
        ('SQL3_s21_RetailAnalitycs v1.0', 'SQL2_s21_Info21 v1.0', 600);

-- maslynem C
CALL insert_check_success('maslynem', 'marvin', 'C2_s21_SimpleBashUtils', '2023-01-01', '10:00:00', 100);
CALL insert_check_success('maslynem', 'trillian', 'C3_s21_string+', '2023-01-01', '18:00:00', 100);
CALL insert_check_success('maslynem', 'slartibartfast', 'C4_s21_math', '2023-01-20', '07:45:00', 60);
CALL insert_check_success('maslynem', 'azathotp', 'C5_s21_decimal', '2023-02-05', '16:20:00', 100);
CALL insert_check_fail_p2p('maslynem', 'marvin', 'C6_s21_matrix', '2023-02-20', '09:00:00');
CALL insert_check_success('maslynem', 'gwynesse', 'C6_s21_matrix', '2023-02-20', '11:00:00', 80);
CALL insert_check_success('maslynem', 'zaphodbeeblebrox', 'C7_s21_SmartCalc_v1.0', '2023-03-01', '8:00:00', 100);
CALL insert_check_success('maslynem', 'marvin', 'C8_s21_3DViewer_v1.0', '2023-03-05', '19:55:00', 100);

-- maslynem CPP
CALL insert_check_success('maslynem', 'gwynesse', 'CPP1_s21_matrix+', '2023-03-09', '12:30:00', 70);
CALL insert_check_success('maslynem', 'slartibartfast', 'CPP2_s21_containers', '2023-03-14', '06:45:00', 100);
CALL insert_check_success('maslynem', 'marvin', 'CPP3_s21_SmartCalc_v2.0', '2023-03-21', '15:20:00', 100);
CALL insert_check_success('maslynem', 'gwynesse', 'CPP7_s21_MLP', '2023-04-05', '11:00:00', 90);

-- azathotp C
CALL insert_check_success('azathotp', 'slartibartfast', 'C2_s21_SimpleBashUtils', '2023-01-01', '12:00:00', 100);
CALL insert_check_success('azathotp', 'trillian', 'C3_s21_string+', '2023-01-15', '18:00:00', 100);
CALL insert_check_success('azathotp', 'marvin', 'C4_s21_math', '2023-01-18', '09:45:00', 100);
CALL insert_check_fail_verter('azathotp', 'slartibartfast', 'C5_s21_decimal', '2023-01-24', '18:20:00');
CALL insert_check_success('azathotp', 'trillian', 'C5_s21_decimal', '2023-01-25', '18:20:00', 100);
CALL insert_check_success('azathotp', 'gwynesse', 'C6_s21_matrix', '2023-02-05', '11:00:00', 60);
CALL insert_check_success('azathotp', 'zaphodbeeblebrox', 'C7_s21_SmartCalc_v1.0', '2023-02-16', '18:00:00', 100);
CALL insert_check_success('azathotp', 'marvin', 'C8_s21_3DViewer_v1.0', '2023-02-17', '09:55:00', 100);

-- azathotp DO
CALL insert_check_success('azathotp', 'zaphodbeeblebrox', 'DO1_s21_Linux', '2023-02-26', '12:00:00', 100);
CALL insert_check_success('azathotp', 'slartibartfast', 'DO2_s21_Linux_Network', '2023-03-02', '18:00:00', 70);
CALL insert_check_success('azathotp', 'marvin', 'DO3_s21_LinuxMonitoring v1.0', '2023-03-10', '09:45:00', 100);
CALL insert_check_success('azathotp', 'trillian', 'DO4_s21_LinuxMonitoring v2.0', '2023-03-15', '18:20:00', 100);
CALL insert_check_fail_p2p('azathotp', 'gwynesse', 'DO5_s21_SimpleDocker', '2023-03-20', '15:00:00');
CALL insert_check_success('azathotp', 'fordprefect', 'DO5_s21_SimpleDocker', '2023-03-21', '11:00:00', 70);
CALL insert_check_success('azathotp', 'maslynem', 'DO6_s21_CICD', '2023-04-01', '18:00:00', 100);

-- gwynesse C
CALL insert_check_fail_verter('gwynesse', 'marvin', 'C2_s21_SimpleBashUtils', '2023-01-14', '20:00:00');
CALL insert_check_success('gwynesse', 'azathotp', 'C2_s21_SimpleBashUtils', '2023-01-15', '19:00:00', 100);
CALL insert_check_success('gwynesse', 'maslynem', 'C3_s21_string+', '2023-01-31', '18:00:00', 100);
CALL insert_check_success('gwynesse', 'arthurdent', 'C4_s21_math', '2023-02-20', '07:30:00', 100);
CALL insert_check_success('gwynesse', 'trillian', 'C5_s21_decimal', '2023-02-25', '18:55:00', 70);
CALL insert_check_success('gwynesse', 'fordprefect', 'C6_s21_matrix', '2023-02-05', '11:00:00', 100);
CALL insert_check_success('gwynesse', 'zaphodbeeblebrox', 'C7_s21_SmartCalc_v1.0', '2023-02-16', '22:00:00', 100);
CALL insert_check_success('gwynesse', 'maslynem', 'C8_s21_3DViewer_v1.0', '2023-02-27', '06:55:00', 100);

-- gwynesse SQL
CALL insert_check_success('gwynesse', 'zaphodbeeblebrox', 'SQL2_s21_Info21 v1.0', '2023-03-15', '09:00:00', 70);
CALL insert_check_success('gwynesse', 'arthurdent', 'SQL3_s21_RetailAnalitycs v1.0', '2023-03-31', '18:00:00', 100);

-- arthurdent C
CALL insert_check_success('arthurdent', 'fordprefect', 'C2_s21_SimpleBashUtils', '2023-01-14', '19:30:00', 100);
CALL insert_check_success('arthurdent', 'zaphodbeeblebrox', 'C3_s21_string+', '2023-01-30', '08:00:00', 100);
CALL insert_check_success('arthurdent', 'marvin', 'C4_s21_math', '2023-02-20', '15:15:00', 100);
CALL insert_check_success('arthurdent', 'trillian', 'C5_s21_decimal', '2023-02-26', '18:55:00', 70);

-- arthurdent DO
CALL insert_check_success('arthurdent', 'zaphodbeeblebrox', 'DO1_s21_Linux', '2023-03-01', '14:00:00', 100);
CALL insert_check_success('arthurdent', 'slartibartfast', 'DO2_s21_Linux_Network', '2023-03-15', '08:00:00', 100);

-- fordprefect C
CALL insert_check_success('fordprefect', 'maslynem', 'C2_s21_SimpleBashUtils', '2023-01-14', '15:30:00', 100);
CALL insert_check_success('fordprefect', 'zaphodbeeblebrox', 'C3_s21_string+', '2023-01-30', '08:05:00', 70);
CALL insert_check_success('fordprefect', 'marvin', 'C4_s21_math', '2023-02-12', '15:25:00', 100);
CALL insert_check_success('fordprefect', 'trillian', 'C5_s21_decimal', '2023-02-18', '18:15:00', 100);

-- fordprefect DO
CALL insert_check_success('fordprefect', 'arthurdent', 'DO1_s21_Linux', '2023-02-26', '12:00:00', 100);
CALL insert_check_success('fordprefect', 'zaphodbeeblebrox', 'DO2_s21_Linux_Network', '2023-03-01', '18:00:00', 100);
CALL insert_check_success('fordprefect', 'gwynesse', 'DO3_s21_LinuxMonitoring v1.0', '2023-03-10', '09:45:00', 100);
CALL insert_check_success('fordprefect', 'marvin', 'DO4_s21_LinuxMonitoring v2.0', '2023-03-14', '18:20:00', 70);
CALL insert_check_success('fordprefect', 'slartibartfast', 'DO5_s21_SimpleDocker', '2023-03-20', '11:00:00', 100);
CALL insert_check_success('fordprefect', 'trillian', 'DO6_s21_CICD', '2023-04-01', '18:00:00', 100);

-- zaphodbeeblebrox C
CALL insert_check_success('zaphodbeeblebrox', 'marvin', 'C2_s21_SimpleBashUtils', '2023-01-30', '15:00:00', 100);
CALL insert_check_success('zaphodbeeblebrox', 'slartibartfast', 'C3_s21_string+', '2023-02-25', '07:05:00', 100);
CALL insert_check_success('zaphodbeeblebrox', 'arthurdent', 'C4_s21_math', '2023-03-19', '15:00:00', 70);

-- marvin C
CALL insert_check_success('marvin', 'gwynesse', 'C2_s21_SimpleBashUtils', '2023-01-05', '16:00:00', 100);
CALL insert_check_success('marvin', 'arthurdent', 'C3_s21_string+', '2023-01-12', '17:00:00', 70);
CALL insert_check_success('marvin', 'fordprefect', 'C4_s21_math', '2023-01-20', '15:15:00', 100);
CALL insert_check_success('marvin', 'zaphodbeeblebrox', 'C5_s21_decimal', '2023-01-25', '15:55:00', 100);
CALL insert_check_success('marvin', 'fordprefect', 'C6_s21_matrix', '2023-02-05', '20:50:00', 100);
CALL insert_check_success('marvin', 'zaphodbeeblebrox', 'C7_s21_SmartCalc_v1.0', '2023-02-09', '12:00:00', 100);
CALL insert_check_success('marvin', 'trillian', 'C8_s21_3DViewer_v1.0', '2023-02-17', '06:35:00', 70);

-- marvin CPP
CALL insert_check_success('marvin', 'slartibartfast', 'CPP1_s21_matrix+', '2023-02-27', '02:30:00', 100);
CALL insert_check_success('marvin', 'maslynem', 'CPP2_s21_containers', '2023-03-04', '16:45:00', 100);
CALL insert_check_success('marvin', 'azathotp', 'CPP3_s21_SmartCalc_v2.0', '2023-03-11', '15:50:00', 100);
CALL insert_check_success('marvin', 'gwynesse', 'CPP7_s21_MLP', '2023-03-21', '11:40:00', 70);

-- marvin SQL
CALL insert_check_success('marvin', 'arthurdent', 'SQL2_s21_Info21 v1.0', '2023-03-26', '19:00:00', 100);
CALL insert_check_success('marvin', 'fordprefect', 'SQL3_s21_RetailAnalitycs v1.0', '2023-04-01', '10:00:00', 100);

-- trillian C
CALL insert_check_success('trillian', 'maslynem', 'C2_s21_SimpleBashUtils', '2023-01-15', '09:00:00', 100);
CALL insert_check_fail_p2p('trillian', 'azathotp', 'C3_s21_string+', '2023-01-30', '18:00:00');
CALL insert_check_success('trillian', 'maslynem', 'C3_s21_string+', '2023-01-31', '17:00:00', 100);
CALL insert_check_success('trillian', 'gwynesse', 'C4_s21_math', '2023-02-20', '15:00:00', 100);
CALL insert_check_success('trillian', 'azathotp', 'C5_s21_decimal', '2023-03-14', '17:55:00', 60);
CALL insert_check_success('trillian', 'fordprefect', 'C5_s21_decimal', '2023-03-15', '17:55:00', 80);
CALL insert_check_success('trillian', 'gwynesse', 'C5_s21_decimal', '2023-03-16', '17:55:00', 60);
CALL insert_check_success('trillian', 'fordprefect', 'C6_s21_matrix', '2023-03-30', '11:30:00', 100);

-- slartibartfast
CALL insert_check_success('slartibartfast', 'gwynesse', 'C2_s21_SimpleBashUtils', '2023-01-18', '09:00:00', 100);
CALL insert_check_fail_verter('slartibartfast', 'arthurdent', 'C3_s21_string+', '2023-02-05', '14:00:00');
CALL insert_check_success('slartibartfast', 'maslynem', 'C3_s21_string+', '2023-02-06', '22:00:00', 100);
CALL insert_check_fail_p2p('slartibartfast', 'fordprefect', 'C4_s21_math', '2023-03-13', '09:00:00');
CALL insert_check_success('slartibartfast', 'arthurdent', 'C4_s21_math', '2023-03-15', '09:00:00', 100);

CALL friends_insert('maslynem', 'arthurdent');
CALL friends_insert('arthurdent', 'azathotp');
CALL friends_insert('arthurdent', 'gwynesse');
CALL friends_insert('fordprefect', 'azathotp');
CALL friends_insert('zaphodbeeblebrox', 'maslynem');
CALL friends_insert('zaphodbeeblebrox', 'gwynesse');
CALL friends_insert('arthurdent', 'zaphodbeeblebrox');
CALL friends_insert('trillian', 'maslynem');
CALL friends_insert('arthurdent', 'trillian');
CALL friends_insert('trillian', 'zaphodbeeblebrox');
CALL friends_insert('slartibartfast', 'maslynem');
CALL friends_insert('slartibartfast', 'gwynesse');
CALL friends_insert('slartibartfast', 'azathotp');
CALL friends_insert('slartibartfast', 'zaphodbeeblebrox');
CALL friends_insert('slartibartfast', 'trillian');

INSERT INTO Recommendations (peer, recommended_peer)
VALUES  ('maslynem', 'azathotp'),
        ('maslynem', 'arthurdent'),
        ('maslynem', 'fordprefect'),
        ('maslynem', 'zaphodbeeblebrox'),
        ('maslynem', 'marvin' ),
        ('maslynem', 'slartibartfast'),

        ('gwynesse', 'maslynem'),
        ('gwynesse', 'azathotp'),
        ('gwynesse', 'arthurdent'),
        ('gwynesse', 'zaphodbeeblebrox'),

        ('arthurdent', 'maslynem'),
        ('arthurdent', 'azathotp'),
        ('arthurdent', 'fordprefect'),
        ('arthurdent', 'zaphodbeeblebrox'),

        ('fordprefect', 'maslynem'),
        ('fordprefect', 'arthurdent'),

        ('zaphodbeeblebrox', 'maslynem'),
        ('zaphodbeeblebrox', 'arthurdent'),
        ('zaphodbeeblebrox', 'fordprefect'),
        ('zaphodbeeblebrox', 'marvin'),

        ('marvin', 'azathotp'),
        ('marvin', 'arthurdent'),
        ('marvin', 'fordprefect'),

        ('trillian', 'azathotp');

INSERT INTO TimeTracking (peer, date, time, state)
VALUES  ('azathotp', '2023-02-15', '12:00:00', 1),
        ('azathotp', '2023-02-15', '18:00:00', 2),
        ('maslynem', '2023-02-20', '09:00:00', 1),
        ('maslynem', '2023-02-20', '20:00:00', 2),
        ('maslynem', '2023-02-22', '08:00:00', 1),
        ('maslynem', '2023-02-22', '21:00:00', 2),
        ('maslynem', '2023-02-23', '07:30:00', 1),
        ('maslynem', '2023-02-23', '17:00:00', 2),
        ('fordprefect', '2023-02-24', '08:30:00', 1),
        ('fordprefect', '2023-02-24', '17:00:00', 2),
        ('gwynesse', '2023-02-25', '18:00:00', 1),
        ('gwynesse', '2023-02-25', '19:00:00', 2),
        ('zaphodbeeblebrox', '2023-03-25', '15:00:00', 1),
        ('zaphodbeeblebrox', '2023-03-25', '19:00:00', 2),
        ('azathotp', '2023-04-01', '09:00:00', 1),
        ('azathotp', '2023-04-01', '09:45:00', 2),
        ('azathotp', '2023-04-01', '10:05:00', 1),
        ('azathotp', '2023-04-01', '11:50:00', 2),
        ('azathotp', '2023-04-01', '14:00:00', 1),
        ('azathotp', '2023-04-01', '16:15:00', 2),
        ('azathotp', '2023-04-01', '18:05:00', 1),
        ('azathotp', '2023-04-01', '21:15:00', 2),
        ('arthurdent', '2023-01-01', '12:11:00', 1),
        ('arthurdent', '2023-01-01', '17:39:00', 2),
        ('arthurdent', '2023-01-02', '12:32:00', 1),
        ('arthurdent', '2023-01-02', '19:39:00', 2),
        ('arthurdent', '2023-01-03', '12:42:00', 1),
        ('arthurdent', '2023-01-03', '17:39:00', 2),
        ('arthurdent', '2023-01-04', '12:45:00', 1),
        ('arthurdent', '2023-01-04', '16:39:00', 2),
        ('arthurdent', '2023-01-05', '15:05:00', 1),
        ('arthurdent', '2023-01-05', '16:39:00', 2),
        ('arthurdent', '2023-01-15', '8:00:00', 1),
        ('arthurdent', '2023-01-15', '18:39:00', 2),
        ('arthurdent', '2023-01-16', '8:15:00', 1),
        ('arthurdent', '2023-01-16', '19:54:00', 2),
        ('arthurdent', '2023-01-17', '8:47:00', 1),
        ('arthurdent', '2023-01-17', '20:13:00', 2),
        ('arthurdent', '2023-01-18', '8:43:00', 1),
        ('arthurdent', '2023-01-18', '20:25:00', 2),
        ('arthurdent', '2023-01-19', '8:59:00', 1),
        ('arthurdent', '2023-01-19', '21:12:00', 2),
        ('arthurdent', '2023-01-20', '8:29:00', 1),
        ('arthurdent', '2023-01-20', '17:28:00', 2),
        ('arthurdent', '2023-01-21', '8:35:00', 1),
        ('arthurdent', '2023-01-21', '18:49:00', 2),
        ('marvin', '2022-05-01', '12:59:00', 1),
        ('marvin', '2022-05-01', '19:40:00', 2),
        ('marvin', '2022-05-02', '16:00:00', 1),
        ('marvin', '2022-05-02', '19:20:00', 2),
        ('marvin', '2022-05-15', '7:00:00', 1),
        ('marvin', '2022-05-15', '19:39:00', 2),
        ('marvin', '2022-05-16', '7:15:00', 1),
        ('marvin', '2022-05-16', '20:54:00', 2),
        ('marvin', '2022-05-17', '7:47:00', 1),
        ('marvin', '2022-05-17', '21:13:00', 2),
        ('marvin', '2022-05-18', '7:43:00', 1),
        ('marvin', '2022-05-18', '21:25:00', 2),
        ('marvin', '2022-05-19', '7:59:00', 1),
        ('marvin', '2022-05-19', '22:12:00', 2),
        ('marvin', '2022-05-20', '7:29:00', 1),
        ('marvin', '2022-05-20', '18:28:00', 2),
        ('marvin', '2022-05-21', '7:35:00', 1),
        ('marvin', '2022-05-21', '19:49:00', 2),
        ('marvin', '2022-05-22', '7:35:00', 1),
        ('marvin', '2022-05-22', '19:49:00', 2),
        ('marvin', '2022-05-23', '7:35:00', 1),
        ('marvin', '2022-05-23', '19:49:00', 2),
        ('marvin', '2023-04-19', '8:59:00', 1),
        ('marvin', '2023-04-19', '21:12:00', 2),
        ('marvin', '2023-04-20', '8:29:00', 1),
        ('marvin', '2023-04-20', '17:28:00', 2),
        ('marvin', '2023-04-21', '8:35:00', 1),
        ('marvin', '2023-04-21', '18:49:00', 2),
        ('trillian', '2022-06-01', '12:24:00', 1),
        ('trillian', '2022-06-01', '14:39:00', 2),
        ('trillian', '2022-06-02', '12:15:00', 1),
        ('trillian', '2022-06-02', '15:54:00', 2),
        ('trillian', '2022-06-03', '12:47:00', 1),
        ('trillian', '2022-06-03', '13:13:00', 2),
        ('trillian', '2022-06-04', '12:43:00', 1),
        ('trillian', '2022-06-04', '16:25:00', 2),
        ('trillian', '2022-06-15', '2:00:00', 1),
        ('trillian', '2022-06-15', '14:39:00', 2),
        ('trillian', '2022-06-16', '2:15:00', 1),
        ('trillian', '2022-06-16', '15:54:00', 2),
        ('trillian', '2022-06-17', '2:47:00', 1),
        ('trillian', '2022-06-17', '11:13:00', 2),
        ('trillian', '2022-06-18', '2:43:00', 1),
        ('trillian', '2022-06-18', '16:25:00', 2),
        ('trillian', '2022-06-19', '2:59:00', 1),
        ('trillian', '2022-06-19', '14:12:00', 2),
        ('trillian', '2022-06-20', '2:29:00', 1),
        ('trillian', '2022-06-20', '15:28:00', 2),
        ('trillian', '2022-06-21', '2:35:00', 1),
        ('trillian', '2022-06-21', '18:49:00', 2),
        ('slartibartfast', '2023-04-01', '08:24:00', 1),
        ('slartibartfast', '2023-04-01', '08:39:00', 2),
        ('slartibartfast', '2023-04-01', '09:15:00', 1),
        ('slartibartfast', '2023-04-01', '09:54:00', 2),
        ('slartibartfast', '2023-04-01', '10:47:00', 1),
        ('slartibartfast', '2023-04-01', '11:13:00', 2),
        ('slartibartfast', '2023-04-01', '11:43:00', 1),
        ('slartibartfast', '2023-04-01', '12:25:00', 2),
        ('slartibartfast', '2023-04-02', '11:43:00', 1),
        ('slartibartfast', '2023-04-02', '12:25:00', 2),
        ('slartibartfast', '2023-04-02', '12:43:00', 1),
        ('slartibartfast', '2023-04-02', '13:25:00', 2),
        ('slartibartfast', '2023-04-03', '7:43:00', 1),
        ('slartibartfast', '2023-04-03', '8:25:00', 2),
        ('slartibartfast', '2023-04-03', '8:43:00', 1),
        ('slartibartfast', '2023-04-03', '9:25:00', 2),
        ('slartibartfast', '2023-04-03', '9:43:00', 1),
        ('slartibartfast', '2023-04-03', '10:25:00', 2),
        ('slartibartfast', '2023-04-04', '7:43:00', 1),
        ('slartibartfast', '2023-04-04', '8:25:00', 2),
        ('slartibartfast', '2023-04-04', '8:43:00', 1),
        ('slartibartfast', '2023-04-04', '9:25:00', 2),
        ('slartibartfast', '2023-04-04', '9:43:00', 1),
        ('slartibartfast', '2023-04-04', '10:25:00', 2),
        ('slartibartfast', '2023-04-04', '10:43:00', 1),
        ('slartibartfast', '2023-04-04', '11:25:00', 2),
        ('slartibartfast', '2023-04-04', '11:43:00', 1),
        ('slartibartfast', '2023-04-04', '12:25:00', 2);

