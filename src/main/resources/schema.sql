CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login VARCHAR(60) NOT NULL,
    name VARCHAR(60) NOT NULL,
    email VARCHAR(60) NOT NULL,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT NOT NULL REFERENCES users(id),
    friend_id BIGINT NOT NULL REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS mpa (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    mpa VARCHAR(60) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS genre (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    genre VARCHAR(60) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS films (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(255) NOT NULL,
    releaseDate DATE NOT NULL,
    duration INTEGER NOT NULL,
    mpa_id BIGINT NOT NULL REFERENCES mpa(id)
    );

CREATE TABLE IF NOT EXISTS genres (
    id_film BIGINT NOT NULL REFERENCES films(id),
    id_genre BIGINT NOT NULL REFERENCES genre(id)
    );

CREATE TABLE IF NOT EXISTS filmlikes (
    id_film BIGINT NOT NULL REFERENCES films(id),
    id_user BIGINT NOT NULL REFERENCES users(id)
    );