-- 1) Написать процедуру добавления P2P проверки

CREATE OR REPLACE PROCEDURE add_p2p_check(
    checking_peer_to_add VARCHAR,
    checked_peer_to_add VARCHAR,
    task_title_to_add VARCHAR,
    state_to_add status,
    check_time_to_add TIME
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF state_to_add = 'Start' THEN
        INSERT INTO checks (peer, task, date)
        VALUES (checked_peer_to_add, task_title_to_add, CURRENT_DATE);

        INSERT INTO p2p (check_id, checking_peer, state, time)
        VALUES ((SELECT MAX(id) FROM checks), checking_peer_to_add, state_to_add, check_time_to_add);

    ELSEIF state_to_add != 'Start' THEN

        INSERT INTO p2p (check_id, checking_peer, state, time)
        VALUES ((SELECT checks.id AS id
                 FROM checks
                          JOIN p2p p ON checks.id = p.check_id
                 WHERE p.state = 'Start'
                   AND p.checking_peer = checking_peer_to_add
                   AND checks.peer = checked_peer_to_add
                   AND checks.task = task_title_to_add
                 EXCEPT
                 SELECT checks.id AS id
                 FROM checks
                          JOIN p2p p ON checks.id = p.check_id
                 WHERE (p.state = 'Success' OR p.state = 'Failure')
                   AND p.checking_peer = checking_peer_to_add
                   AND checks.peer = checked_peer_to_add
                   AND checks.task = task_title_to_add
                 ORDER BY id
                 LIMIT 1), checking_peer_to_add, state_to_add, check_time_to_add);
    END IF;
END;
$$;

-- 2) Написать процедуру добавления проверки Verter'ом

CREATE OR REPLACE PROCEDURE add_verter_check(
    checked_peer_to_add VARCHAR,
    task_title_to_add VARCHAR,
    state_to_add STATUS,
    check_time_to_add TIME
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO verter (check_id, state, time)
    VALUES ((SELECT checks.id as id
             FROM checks
                      JOIN p2p p ON checks.id = p.check_id
             WHERE p.state = 'Success'
               AND checks.peer = checked_peer_to_add
               AND checks.task = task_title_to_add
             ORDER BY checks.date DESC, p.time DESC, checks.id DESC
             LIMIT 1), state_to_add, check_time_to_add);
END;
$$;

-- 4) Написать триггер: перед добавлением записи в таблицу XP, проверить корректность добавляемой записи

CREATE OR REPLACE FUNCTION fnc_trg_xp() RETURNS TRIGGER AS
$xp$
BEGIN
    IF (SELECT t.max_xp
        FROM xp
                 JOIN checks c on c.id = xp.check_id
                 JOIN tasks t on t.title = c.task
        WHERE c.id = NEW.check_id
        LIMIT 1) < NEW.xp_amount THEN
        RAISE EXCEPTION 'The amount of XP exceeds the maximum available for the task (%) being checked',
            (SELECT t.title
             FROM xp
                      JOIN checks c on c.id = xp.check_id
                      JOIN tasks t on t.title = c.task
             WHERE c.id = 3
             LIMIT 1);
    END IF;

    IF NOT EXISTS(SELECT *
                  FROM xp
                           JOIN checks c on c.id = xp.check_id
                           JOIN verter v on c.id = v.check_id
                  WHERE c.id = NEW.check_id
                    AND state = 'Success') THEN
        RAISE EXCEPTION 'Peer was not successfully tested by the verter';
    END IF;
    RETURN NULL;
END;
$xp$ LANGUAGE plpgsql;

CREATE TRIGGER trg_xp
    AFTER INSERT
    ON xp
    FOR EACH ROW
EXECUTE FUNCTION fnc_trg_xp();
