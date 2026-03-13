INSERT INTO film (titel, varighed, alders_graense, plakat_url)
VALUES ('Interstellar', 169, 11, 'https://image.tmdb.org/t/p/w300/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg');

INSERT INTO film (titel, varighed, alders_graense, plakat_url)
VALUES ('The Dark Knight', 152, 15, 'https://image.tmdb.org/t/p/w300/qJ2tW6WMUDux911r6m7haRef0WH.jpg');

INSERT INTO sal (navn)
VALUES ('Sal 1');

INSERT INTO sal (navn)
VALUES ('Sal 2');

INSERT INTO forestilling (start_tid, film_id, sal_id)
VALUES ('2026-03-10 19:30:00', 1, 1);

INSERT INTO forestilling (start_tid, film_id, sal_id)
VALUES ('2026-03-10 20:00:00', 2, 2);