DROP TABLE IF EXISTS player cascade;
DROP TABLE IF EXISTS inventory cascade;
DROP TABLE IF EXISTS game_state cascade;


CREATE TABLE player (
    id SERIAL PRIMARY KEY,
    player_name text NOT NULL,
    health integer NOT NULL,
    armor integer NOT NULL ,
    damage integer NOT NULL
);


CREATE TABLE inventory (
    id serial PRIMARY KEY,
    player_id INTEGER REFERENCES player(id),
    item_name text NOT NULL
);

CREATE TABLE game_state (
    id serial PRIMARY KEY,
    current_map text NOT NULL,
    saved_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    player_id integer NOT NULL
);


ALTER TABLE ONLY game_state
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES player(id);


ALTER TABLE ONLY inventory
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES player(id);