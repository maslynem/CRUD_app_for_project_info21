-- 1) Написать функцию, возвращающую таблицу TransferredPoints в более человекочитаемом виде

CREATE OR REPLACE FUNCTION ex01()
    RETURNS TABLE
            (
                Peer1        VARCHAR,
                Peer2        VARCHAR,
                PointsAmount BIGINT
            )
AS
$$
SELECT t1_checking_peer, t1_checked_peer, sum(t1_sum)
FROM (SELECT checking_peer      AS t1_checking_peer,
             checked_peer       AS t1_checked_peer,
             sum(points_amount) AS t1_sum
      FROM transferred_points AS t1
      GROUP BY checking_peer, checked_peer
      UNION ALL
      SELECT checked_peer           AS t1_checked_peer,
             checking_peer          AS t1_checking_peer,
             0 - sum(points_amount) AS t1_sum
      FROM transferred_points AS t2
      GROUP BY checked_peer, checking_peer) AS t1
GROUP BY t1_checking_peer, t1_checked_peer;
$$
    LANGUAGE SQL;

SELECT *
from ex01();


-- 2) Написать функцию, которая возвращает таблицу вида: ник пользователя, название проверенного задания, кол-во полученного XP

CREATE OR REPLACE FUNCTION ex02()
    RETURNS TABLE
            (
                Peer VARCHAR,
                Task VARCHAR,
                XP   BIGINT
            )
AS
$$
SELECT peer, task, xp_amount
FROM checks
         LEFT JOIN XP xp ON checks.id = xp.check_id
WHERE checks.id IN (SELECT check_id FROM verter WHERE state = 'Success');
$$
    LANGUAGE SQL;

SELECT *
FROM ex02();

-- 3) Написать функцию, определяющую пиров, которые не выходили из кампуса в течение всего дня

CREATE OR REPLACE FUNCTION ex03(day DATE)
    RETURNS SETOF VARCHAR
AS
$$
SELECT peer
FROM (SELECT peer, count(state) AS in_out_count
      FROM time_tracking
      WHERE date = day
      GROUP BY peer, date) AS t1
WHERE t1.in_out_count = 2

$$
    LANGUAGE SQL;

SELECT *
FROM ex03('2023-02-15');

--4) Посчитать изменение в количестве пир поинтов каждого пира по таблице TransferredPoints

CREATE OR REPLACE FUNCTION ex04()
    RETURNS TABLE
            (
                Peer         VARCHAR,
                PointsChange BIGINT
            )
AS
$$
SELECT t.checking_peer AS Peer, SUM(t.sum) AS PointsChange
FROM (SELECT checking_peer,
             sum(points_amount) AS sum
      FROM transferred_points
      GROUP BY checking_peer
      UNION ALL
      SELECT checked_peer,
             0 - sum(points_amount) AS sum
      FROM transferred_points
      GROUP BY checked_peer) AS t
GROUP BY t.checking_peer
ORDER BY PointsChange DESC;
$$
    LANGUAGE SQL;

SELECT *
FROM ex04();


-- 5) Посчитать изменение в количестве пир поинтов каждого пира по таблице, возвращаемой первой функцией из Part 3

CREATE OR REPLACE FUNCTION ex05()
    RETURNS TABLE
            (
                Peer         VARCHAR,
                PointsChange BIGINT
            )
AS
$$
SELECT t.checking_peer, SUM(t.PointsAmount) AS PointsChange
FROM (SELECT Peer1 AS checking_peer, Peer2 AS checked_peer, PointsAmount AS PointsAmount
      FROM ex01()
      UNION
      SELECT Peer2 AS checking_peer, Peer1 AS checked_peer, 0 - PointsAmount AS PointsAmount
      FROM ex01()
      WHERE 0 - PointsAmount < 0) AS t
GROUP BY t.checking_peer
ORDER BY PointsChange DESC;
$$
    LANGUAGE SQL;

SELECT *
FROM ex05();

-- 6) Определить самое часто проверяемое задание за каждый день

CREATE OR REPLACE FUNCTION ex06()
    RETURNS TABLE
            (
                Day  VARCHAR,
                Task VARCHAR
            )
AS
$$
SELECT o.Day AS Day, o.Task AS Task
FROM (SELECT TO_CHAR(date, 'dd.mm.yyyy')                      AS Day,
             substring(task, 0, position('_' in task))        AS Task,
             COUNT(substring(task, 0, position('_' in task))) AS Count
      FROM checks
      GROUP BY Day, Task) AS o
         LEFT JOIN (SELECT TO_CHAR(date, 'dd.mm.yyyy')                      AS Day,
                           substring(task, 0, position('_' in task))        AS Task,
                           COUNT(substring(task, 0, position('_' in task))) AS Count
                    FROM checks
                    GROUP BY Day, Task) AS b ON o.Day = b.Day AND o.Count < b.Count
WHERE b.Count is NULL;
$$
    LANGUAGE SQL;

SELECT *
FROM ex06();

-- 7) Найти всех пиров, выполнивших весь заданный блок задач и дату завершения последнего задания

CREATE OR REPLACE FUNCTION ex07(str VARCHAR)
    RETURNS TABLE
            (
                Peer VARCHAR,
                Day  DATE
            )
AS
$$
DECLARE
    last_task VARCHAR := (SELECT title
                          FROM tasks
                          WHERE title SIMILAR TO FORMAT('%s[[:digit:]]_%%', str)
                          ORDER BY title DESC
                          LIMIT 1);
BEGIN
    RETURN QUERY SELECT c.peer AS Peer, MAX(c.date) AS Day
                 FROM checks AS c
                          LEFT JOIN verter v ON c.id = v.check_id
                          LEFT JOIN tasks t ON c.task = t.title
                 WHERE v.state = 'Success'
                   AND t.title = last_task
                 GROUP BY c.peer;
END ;
$$
    LANGUAGE plpgsql;

SELECT *
FROM ex07('C');


-- 8) Определить, к какому пиру стоит идти на проверку каждому обучающемуся

CREATE OR REPLACE FUNCTION ex08()
    RETURNS TABLE
            (
                Peer            VARCHAR,
                RecommendedPeer VARCHAR
            )
AS
$$
SELECT DISTINCT ON (p.nickname) p.nickname                                                    AS Peer,
                                COALESCE(r.recommended_peer, 'no friends or recommendations') AS RecommendedPeer
FROM peers AS p
         LEFT JOIN friends f ON p.nickname = f.peer1
         LEFT JOIN recommendations r ON f.peer2 = r.peer AND p.nickname <> r.recommended_peer
GROUP BY p.nickname, r.recommended_peer
ORDER BY p.nickname, count(recommended_peer) DESC
$$
    LANGUAGE SQL;

SELECT *
FROM ex08();


-- 9) Определить процент пиров, которые:

CREATE OR REPLACE FUNCTION ex09(IN block1 VARCHAR, IN block2 VARCHAR)
    RETURNS TABLE
            (
                StartedBlock1      NUMERIC,
                StartedBlock2      NUMERIC,
                StartedBothBlocks  NUMERIC,
                DidntStartAnyBlock NUMERIC
            )
AS
$$
DECLARE
    n_peers            NUMERIC;
    StartedBlock1      NUMERIC;
    StartedBlock2      NUMERIC;
    StartedBothBlocks  NUMERIC;
    DidntStartAnyBlock NUMERIC;
BEGIN
    n_peers := (SELECT count(nickname) FROM peers);
    StartedBlock1 := count(t.nickname)
                     FROM (SELECT DISTINCT ON (nickname) nickname
                           FROM peers
                                    LEFT JOIN checks c ON peers.nickname = c.peer
                           WHERE task SIMILAR TO FORMAT('%s[[:digit:]]_%%', block1)
                           EXCEPT
                           SELECT DISTINCT ON (nickname) nickname
                           FROM peers
                                    LEFT JOIN checks c ON peers.nickname = c.peer
                           WHERE task SIMILAR TO FORMAT('%s[[:digit:]]_%%', block2)) AS t;
    StartedBlock1 := floor(StartedBlock1 * 100 / n_peers);

    StartedBlock2 := count(t.nickname)
                     FROM (SELECT DISTINCT ON (nickname) nickname
                           FROM peers
                                    LEFT JOIN checks c ON peers.nickname = c.peer
                           WHERE task SIMILAR TO FORMAT('%s[[:digit:]]_%%', block2)
                           EXCEPT
                           SELECT DISTINCT ON (nickname) nickname
                           FROM peers
                                    LEFT JOIN checks c ON peers.nickname = c.peer
                           WHERE task SIMILAR TO FORMAT('%s[[:digit:]]_%%', block1)) AS t;
    StartedBlock2 := floor(StartedBlock2 * 100 / n_peers);

    StartedBothBlocks := count(t.nickname)
                         FROM (SELECT DISTINCT ON (nickname) nickname
                               FROM peers
                                        LEFT JOIN checks c ON peers.nickname = c.peer
                               WHERE task SIMILAR TO FORMAT('%s[[:digit:]]_%%', block1)
                               INTERSECT
                               SELECT DISTINCT ON (nickname) nickname
                               FROM peers
                                        LEFT JOIN checks c ON peers.nickname = c.peer
                               WHERE task SIMILAR TO FORMAT('%s[[:digit:]]_%%', block2)) AS t;
    StartedBothBlocks := floor(StartedBothBlocks * 100 / n_peers);
    DidntStartAnyBlock := 100 - StartedBlock1 - StartedBlock2 - StartedBothBlocks;
    RETURN QUERY SELECT StartedBlock1, StartedBlock2, StartedBothBlocks, DidntStartAnyBlock;
END;
$$
    LANGUAGE plpgsql;

SELECT *
FROM ex09('SQL', 'CPP');

-- 10) Определить процент пиров, которые когда-либо успешно проходили проверку в свой день рождения
CREATE OR REPLACE FUNCTION ex10()
    RETURNS TABLE
            (
                SuccessfulChecks   NUMERIC,
                UnsuccessfulChecks NUMERIC
            )
AS
$$
DECLARE
    all_checks         INT;
    SuccessfulChecks   NUMERIC;
    UnsuccessfulChecks NUMERIC;
BEGIN
    all_checks := count(t.nickname)
                  FROM (SELECT DISTINCT ON (p.nickname) p.nickname, p.birthday, c.date
                        FROM peers AS p
                                 left join checks c
                                           ON p.nickname = c.peer AND
                                              date_part('month', c.date) = date_part('month', p.birthday) AND
                                              date_part('day', c.date) = date_part('day', p.birthday)
                                 left join p2p p2p2 ON c.id = p2p2.check_id
                        WHERE c.id IS NOT NULL
                          AND p2p2.state = 'Start') AS t;

    SuccessfulChecks := count(t.nickname)
                        FROM (SELECT DISTINCT ON (p.nickname) p.nickname
                              FROM peers AS p
                                       left join checks c
                                                 ON p.nickname = c.peer AND
                                                    date_part('month', c.date) = date_part('month', p.birthday) AND
                                                    date_part('day', c.date) = date_part('day', p.birthday)
                                       left join p2p p2p2 ON c.id = p2p2.check_id
                                       left join verter v on c.id = v.check_id
                              WHERE c.id IS NOT NULL
                                AND p2p2.state = 'Success'
                                AND CASE WHEN v.id IS NULL THEN true ELSE v.state = 'Success' END) AS t;
    SuccessfulChecks := floor(SuccessfulChecks * 100 / CASE WHEN all_checks = 0 THEN 1 ELSE all_checks END);
    UnsuccessfulChecks := 100 - CASE WHEN all_checks = 0 THEN 100 ELSE SuccessfulChecks END;
    RETURN QUERY SELECT SuccessfulChecks, UnsuccessfulChecks;
END;
$$
    LANGUAGE plpgsql;

SELECT *
FROM ex10();

-- 11) Определить всех пиров, которые сдали заданные задания 1 и 2, но не сдали задание 3
CREATE OR REPLACE FUNCTION ex11(task1 VARCHAR, task2 VARCHAR, task3 VARCHAR)
    RETURNS TABLE
            (
                Peer VARCHAR
            )
AS
$$
BEGIN
    RETURN QUERY
        WITH t_with_task1_OR_task2_completed AS (SELECT nickname, task, max(date)
                                                 FROM peers
                                                          LEFT JOIN checks c ON peers.nickname = c.peer
                                                          LEFT JOIN xp x ON c.id = x.check_id
                                                 WHERE x.id IS NOT NULL
                                                   AND (c.task LIKE task1 OR c.task LIKE task2)
                                                 GROUP BY nickname, task),
             peers_with_task1_AND_task2_completed AS (SELECT t1.nickname
                                                      FROM (SELECT t.nickname, count(t.task) AS num
                                                            FROM t_with_task1_OR_task2_completed AS t
                                                            GROUP BY t.nickname) AS t1
                                                      WHERE t1.num = 2),
             t_task3_completed AS (SELECT nickname, max(date)
                                   FROM peers
                                            LEFT JOIN checks c ON peers.nickname = c.peer
                                            LEFT JOIN xp x ON c.id = x.check_id
                                   WHERE x.id IS NOT NULL
                                     AND c.task LIKE task3
                                   GROUP BY nickname, task)
        SELECT nickname AS Peer
        FROM peers_with_task1_AND_task2_completed
        EXCEPT
        SELECT t.nickname
        FROM t_task3_completed AS t;

END;
$$
    LANGUAGE plpgsql;

SELECT *
FROM ex11('D01_s21_Linux', 'D02_s21_Linux Network', 'CPP1_Matrix');


-- 12) Используя рекурсивное обобщенное табличное выражение, для каждой задачи вывести кол-во предшествующих ей задач
CREATE OR REPLACE FUNCTION ex12()
    RETURNS TABLE
            (
                Task      VARCHAR,
                PrevCount INT
            )
AS
$$
WITH RECURSIVE r AS (SELECT title, 0 AS level
                     FROM tasks
                     WHERE tasks.parent_task IS NULL
                     UNION ALL
                     SELECT tasks.title, r.level + 1 AS level
                     FROM tasks
                              JOIN r ON tasks.parent_task = r.title)
SELECT *
FROM r
ORDER BY title;
$$
    LANGUAGE SQL;

SELECT *
FROM ex12();

-- 13) Найти "удачные" для проверок дни. День считается "удачным", если в нем есть хотя бы N идущих подряд успешных проверки

CREATE OR REPLACE FUNCTION ex13(N INT)
    RETURNS TABLE
            (
                Day DATE
            )
AS
$$
WITH temp_table AS (select t.date,
                           row_number() over (partition by t.date, flag order by t.date) -
                           CASE WHEN flag != 0 THEN 1 ELSE 0 END AS counter
                    from (SELECT c.date,
                                 count(case when p1.state <> 'Success' OR v.state <> 'Success' then 1 end)
                                 over (partition by c.date order by p1.time) as flag
                          FROM checks AS c
                                   LEFT JOIN p2p p1 on c.id = p1.check_id AND p1.state <> 'Start'
                                   LEFT JOIN p2p p2 on c.id = p2.check_id AND p2.time < p1.time AND p2.state = 'Start'
                                   LEFT JOIN verter v on c.id = v.check_id AND v.state <> 'Start'
                          ORDER BY date, p2.time) AS t)
SELECT date
From temp_table
GROUP BY date
HAVING max(counter) >= N;
$$
    LANGUAGE SQL;

SELECT *
FROM ex13(3);


-- 14) Определить пира с наибольшим количеством XP

CREATE OR REPLACE FUNCTION ex14()
    RETURNS TABLE
            (
                Peer VARCHAR,
                XP   INT
            )
AS
$$
SELECT t.nickname AS Peer, SUM(t.max_xp_amount) AS XP
FROM (SELECT nickname, max(date), max(xp_amount) AS max_xp_amount
      FROM peers
               LEFT JOIN checks c ON peers.nickname = c.peer
               LEFT JOIN xp x ON c.id = x.check_id
      WHERE xp_amount IS NOT NULL
      GROUP BY nickname, task) AS t
GROUP BY nickname
ORDER BY XP DESC
LIMIT 1;
$$
    LANGUAGE SQL;

SELECT *
FROM ex14();

-- 15) Определить пиров, приходивших раньше заданного времени не менее N раз за всё время
CREATE OR REPLACE FUNCTION ex15(in_time TIME, N INT)
    RETURNS TABLE
            (
                Peer VARCHAR
            )
AS
$$
WITH t_first_in_time AS (SELECT peer, date, min(time) AS time
                         FROM time_tracking
                         WHERE state = 1
                           AND time < in_time
                         GROUP BY peer, date)
SELECT peer
FROM t_first_in_time AS t1
WHERE time < in_time
GROUP BY peer
HAVING count(date) >= N
$$
    LANGUAGE SQL;

SELECT *
FROM ex15('10:00:00', 2);

-- 16) Определить пиров, выходивших за последние N дней из кампуса больше M раз
CREATE OR REPLACE FUNCTION ex16(N INT, M INT)
    RETURNS TABLE
            (
                Peer VARCHAR
            )
AS
$$
SELECT DISTINCT peer
FROM (SELECT peer, count(state) - 1 AS in_out_count
      FROM time_tracking
      WHERE date > CURRENT_DATE - N
        AND state = 2
      GROUP BY peer, date) AS t1
WHERE t1.in_out_count > M

$$
    LANGUAGE SQL;

SELECT *
FROM ex16(3, 1);

-- 17) Определить для каждого месяца процент ранних входов

CREATE OR REPLACE FUNCTION ex17()
    RETURNS TABLE
            (
                Month        VARCHAR,
                EarlyEntries NUMERIC
            )
AS
$$
DECLARE
    months               VARCHAR[12] := '{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"}';
    total_number_entries NUMERIC;
    early_number_entries NUMERIC;
    row                  RECORD;
BEGIN
    FOR i IN 1..12
        LOOP
            total_number_entries := 0;
            early_number_entries := 0;
            FOR row in SELECT peer
                       FROM (SELECT peer, birthday
                             FROM time_tracking
                                      LEFT JOIN peers p2 on time_tracking.peer = p2.nickname
                             WHERE state = 1
                             GROUP BY peer, date, birthday) AS t1
                       WHERE date_part('month', t1.birthday) = i
                       GROUP BY peer
                LOOP
                    total_number_entries := total_number_entries + count(t.peer)
                                            FROM (SELECT peer
                                                  FROM time_tracking
                                                  WHERE state = 1
                                                    AND peer = row.peer
                                                  GROUP BY peer, date) as t;
                    early_number_entries := early_number_entries + count(t.peer)
                                            FROM (SELECT peer
                                                  FROM time_tracking
                                                  WHERE state = 1
                                                    AND peer = row.peer
                                                    AND time < '12:00:00'
                                                  GROUP BY peer, date) as t;
                END LOOP;

            RETURN QUERY SELECT months[i],
                                floor(
                                                early_number_entries * 100 /
                                                CASE
                                                    WHEN total_number_entries = 0 THEN 1
                                                    ELSE total_number_entries END);
        END LOOP;
END;
$$
    LANGUAGE plpgsql;

SELECT *
FROM ex17();
