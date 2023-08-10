CREATE table TableNameTest1
(
    temp INT
);

CREATE table TableNameTest2
(
    temp INT
);

CREATE table part4_ex02_table
(
    temp INT
);

INSERT INTO part4_ex02_table
VALUES (1),
       (2);


CREATE OR REPLACE FUNCTION part4_tmp1(OUT tmp INT) AS
$$
SELECT count(*)
FROM tablenametest2;
$$ LANGUAGE sql;

CREATE OR REPLACE FUNCTION part4_tmp2() RETURNS INT AS
$$
SELECT count(*)
FROM tablenametest2;
$$ LANGUAGE sql;

CREATE OR REPLACE FUNCTION part4_tmp3(tmp INT) RETURNS INT AS
$$
SELECT count(*)
FROM tablenametest2;
$$ LANGUAGE sql;

CREATE OR REPLACE FUNCTION part4_tmp4(tmp INT)
    RETURNS table
            (
                temp1 DATE,
                tmp2  INT
            )
AS
$$
SELECT CURRENT_DATE, *
from part4_ex02_table;
$$ LANGUAGE sql;

CREATE OR REPLACE FUNCTION part4_tmp5() RETURNS INT AS
$$
BEGIN
    SELECT count(*)
    FROM tablenametest2;
END ;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION part4_tmp6(tmp INT) RETURNS INT AS
$$
BEGIN
    SELECT count(*)
    FROM tablenametest2;
END ;
$$ LANGUAGE plpgsql;


--ex01

CREATE OR REPLACE PROCEDURE part4_ex01()
AS
$$
DECLARE
    row record;
BEGIN
    FOR row IN SELECT table_name
               FROM information_schema.tables
               WHERE table_schema = 'public'
                 AND table_type = 'BASE TABLE'
                 AND table_name LIKE 'tablename%'
        LOOP
            EXECUTE 'DROP TABLE ' || quote_ident(row.table_name) || ' CASCADE ';
            RAISE INFO 'Dropped table: %', quote_ident(row.table_name);
        END LOOP;
END;
$$ LANGUAGE plpgsql;

CALL part4_ex01();

--ex02

CREATE OR REPLACE PROCEDURE part4_ex02(OUT result INT)
AS
$$
DECLARE
    row     RECORD;
    counter INT := 0;
BEGIN
    FOR row IN SELECT p.proname AS fnc_name, pg_get_function_identity_arguments(p.oid) AS fnc_args
               FROM pg_catalog.pg_proc AS p
                        LEFT JOIN pg_namespace pn on p.pronamespace = pn.oid
                        LEFT JOIN information_schema.routines ir ON ir.routine_name = p.proname
                        LEFT JOIN pg_type pt ON p.prorettype = pt.oid
               WHERE pn.nspname = 'public'
                 AND ir.routine_body = 'SQL'
                 AND ir.routine_type = 'FUNCTION'
                 AND pg_get_function_identity_arguments(p.oid) != ''
                 AND pt.typname <> 'record'
                 AND CASE
                         WHEN position('OUT' IN pg_get_function_identity_arguments(p.oid)) = 0 THEN
                             CASE WHEN pt.typname = 'void' THEN false ELSE true END
                         ELSE true END
        LOOP
            counter := counter + 1;
            RAISE INFO '%(%)', row.fnc_name, row.fnc_args;
        END LOOP;
    result := counter;
END;
$$ LANGUAGE plpgsql;

CALL part4_ex02(0);



-- ex03

CREATE OR REPLACE PROCEDURE part4_ex03()
AS
$$
DECLARE
    row record;
BEGIN
    FOR row IN SELECT event_object_table AS table_name, trigger_name
               FROM information_schema.triggers
               GROUP BY table_name, trigger_name
        LOOP
            EXECUTE 'DROP TRIGGER ' || quote_ident(row.trigger_name) || ' ON ' || quote_ident(row.table_name) || ' CASCADE ';
            RAISE INFO 'Dropped TRIGGER: %', quote_ident(row.trigger_name);
        END LOOP;
END;
$$ LANGUAGE plpgsql;

CALL part4_ex03();

--ex04

CREATE OR REPLACE PROCEDURE part4_ex04(str VARCHAR)
AS
$$
DECLARE
    row record;
BEGIN
    FOR row IN SELECT p.proname AS fnc_name, ir.routine_type AS type
               FROM pg_catalog.pg_proc AS p
                        LEFT JOIN pg_namespace pn on p.pronamespace = pn.oid
                        LEFT JOIN information_schema.routines ir ON ir.routine_name = p.proname
                        LEFT JOIN pg_type pt ON p.prorettype = pt.oid
               WHERE pn.nspname = 'public' AND position(str IN p.prosrc) <> 0
        LOOP
            RAISE INFO '% %', row.fnc_name, row.type;
        END LOOP;
END;
$$ LANGUAGE plpgsql;

CALL part4_ex04('DROP');



-- ex02

-- CREATE OR REPLACE PROCEDURE part4_ex02(OUT count INT, cursor REFCURSOR)
-- AS
-- $$
-- BEGIN
--     OPEN cursor FOR
--         WITH fnc_info AS (
--             SELECT p.proname AS fnc_name, pg_get_function_identity_arguments(p.oid) AS fnc_args
--             FROM pg_catalog.pg_proc AS p
--                      LEFT JOIN pg_namespace pn on p.pronamespace = pn.oid
--                      LEFT JOIN information_schema.routines ir ON ir.routine_name = p.proname
--                      LEFT JOIN pg_type pt ON p.prorettype = pt.oid
--             WHERE pn.nspname = 'public'
--               AND ir.routine_body = 'SQL'
--               AND ir.routine_type = 'FUNCTION'
--               AND pg_get_function_identity_arguments(p.oid) != ''
--               AND pt.typname <> 'record'
--               AND CASE
--                       WHEN position('OUT' IN pg_get_function_identity_arguments(p.oid)) = 0 THEN
--                           CASE WHEN pt.typname = 'void' THEN false ELSE true END
--                       ELSE true END)
--         SELECT format('%s(%s)', fnc_name, fnc_args)
--         FROM fnc_info;
--     count := count(*)
--              FROM pg_catalog.pg_proc AS p
--                       LEFT JOIN pg_namespace pn on p.pronamespace = pn.oid
--                       LEFT JOIN information_schema.routines ir ON ir.routine_name = p.proname
--                       LEFT JOIN pg_type pt ON p.prorettype = pt.oid
--              WHERE pn.nspname = 'public'
--                AND ir.routine_body = 'SQL'
--                AND ir.routine_type = 'FUNCTION'
--                AND pg_get_function_identity_arguments(p.oid) != ''
--                AND pt.typname <> 'record'
--                AND CASE
--                        WHEN position('OUT' IN pg_get_function_identity_arguments(p.oid)) = 0 THEN
--                            CASE WHEN pt.typname = 'void' THEN false ELSE true END
--                        ELSE true END;
-- END;
-- $$ LANGUAGE plpgsql;

-- BEGIN;
-- CALL part4_ex02(0, 'ref');
-- FETCH ALL IN "ref";
-- END;


--ex 04
-- CREATE OR REPLACE PROCEDURE part4_ex04(str VARCHAR, cursor REFCURSOR)
-- AS
-- $$
-- BEGIN
--     OPEN cursor FOR
--
--     SELECT p.proname AS fnc_name, ir.routine_type AS type
--                FROM pg_catalog.pg_proc AS p
--                         LEFT JOIN pg_namespace pn on p.pronamespace = pn.oid
--                         LEFT JOIN information_schema.routines ir ON ir.routine_name = p.proname
--                         LEFT JOIN pg_type pt ON p.prorettype = pt.oid
--                WHERE pn.nspname = 'public' AND position(str IN p.prosrc) <> 0;
-- END;
-- $$ LANGUAGE plpgsql;
--
-- BEGIN;
-- CALL part4_ex04('INSERT', 'ref');
-- FETCH ALL IN "ref";
-- END;