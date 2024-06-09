INSERT INTO genre (genre) SELECT  'Комедия'
    WHERE NOT EXISTS (SELECT  * FROM genre WHERE   genre  = 'Комедия');

INSERT INTO genre (genre) SELECT  'Драма'
    WHERE NOT EXISTS (SELECT  * FROM genre WHERE   genre  = 'Драма');

INSERT INTO genre (genre) SELECT  'Мультфильм'
    WHERE NOT EXISTS (SELECT  * FROM genre WHERE   genre  = 'Мультфильм');

INSERT INTO genre (genre) SELECT  'Триллер'
    WHERE NOT EXISTS (SELECT  * FROM genre WHERE   genre  = 'Триллер');

INSERT INTO genre (genre) SELECT  'Документальный'
    WHERE NOT EXISTS (SELECT  * FROM genre WHERE   genre  = 'Документальный');

INSERT INTO genre (genre) SELECT  'Боевик'
    WHERE NOT EXISTS (SELECT  * FROM genre WHERE   genre  = 'Боевик');

INSERT INTO mpa (mpa) SELECT  'G'
    WHERE NOT EXISTS (SELECT  * FROM mpa WHERE   mpa  = 'G');

INSERT INTO mpa (mpa) SELECT  'PG'
    WHERE NOT EXISTS (SELECT  * FROM mpa WHERE   mpa  = 'PG');

INSERT INTO mpa (mpa) SELECT  'PG-13'
    WHERE NOT EXISTS (SELECT  * FROM mpa WHERE   mpa  = 'PG-13');

INSERT INTO mpa (mpa) SELECT  'R'
    WHERE NOT EXISTS (SELECT  * FROM mpa WHERE   mpa  = 'R');

INSERT INTO mpa (mpa) SELECT  'NC-17'
    WHERE NOT EXISTS (SELECT  * FROM mpa WHERE   mpa  = 'NC-17');


