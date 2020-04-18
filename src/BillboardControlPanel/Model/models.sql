Drop DATABASE IF EXISTS cab302;
CREATE DATABASE IF NOT EXISTS cab302;

USE cab302;

--alter hash according to encrpytion method

CREATE TABLE IF NOT EXISTS user (
    id int UNSIGNED AUTO_INCREMENT,
    email varchar(255) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    create_billboards boolean NOT NULL,
    edit_billboard boolean NOT NULL,
    schedule_billboards boolean NOT NULL,
    edit_users boolean NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (email)
);

CREATE TABLE If NOT EXISTS salts(
    id int UNSIGNED AUTO_INCREMENT,
    user_email varchar(255) UNIQUE NOT NULL,
    salt varbinary(16) NOT NULL,
    PRIMARY KEY (id)
);

--complete as per requirements
CREATE TABLE IF NOT EXISTS billboard(
    id int UNSIGNED AUTO_INCREMENT NOT NULL ,
    user_id int UNSIGNED NOT NULL,
    schedule_id int UNSIGNED NOT NULL,
    background_colour varchar(255) default'#0000FF',
    message varchar(1024) default '',
    message_colour varchar(255) default '#FFFF00',
    image_url varchar(1024),
    image_data longblob,
    information varchar(1024),
    information_colour varchar(255) default '#00FFFF',
    PRIMARY KEY (id),
    CONSTRAINT
        FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE IF NOT EXISTS schedule(
    id int UNSIGNED AUTO_INCREMENT NOT NULL ,
    user_id int UNSIGNED NOT NULL,
    billboard_id int UNSIGNED NOT NULL,
    start_time timestamp default current_timestamp,
    end_time timestamp default current_timestamp,
    PRIMARY KEY (id),
    CONSTRAINT
        FOREIGN KEY (user_id) REFERENCES user (id),
        FOREIGN KEY (billboard_id) REFERENCES billboard (id)
);

INSERT INTO user VALUES (1, 'ADMIN', 'pass', TRUE, TRUE, TRUE, TRUE);