DROP DATABASE IF EXISTS db_livraria_solutis;
CREATE DATABASE IF NOT EXISTS db_livraria_solutis;
USE db_livraria_solutis;


CREATE TABLE livro
(
    id      INT AUTO_INCREMENT,
    titulo  VARCHAR(255)                    NOT NULL,
    autores VARCHAR(255)                    NOT NULL,
    editora VARCHAR(255)                    NOT NULL,
    preco   DOUBLE                          NOT NULL,
    tipo    ENUM ('IMPRESSO', 'ELETRONICO') NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE impresso
(
    id      INT,
    frete   DOUBLE NOT NULL,
    estoque INT    NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES livro (id) ON DELETE CASCADE
);

CREATE TABLE eletronico
(
    id      INT,
    tamanho DOUBLE NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES livro (id) ON DELETE CASCADE
);

CREATE TABLE venda
(
    id      INT AUTO_INCREMENT,
    cliente VARCHAR(255) NOT NULL,
    valor   DOUBLE       NOT NULL,

    PRIMARY KEY (ID)
);

CREATE TABLE venda_livro
(
    venda_id INT,
    livro_id INT,

    PRIMARY KEY (venda_id, livro_id),

    FOREIGN KEY (venda_id) REFERENCES venda (id) ON DELETE CASCADE,
    FOREIGN KEY (livro_id) REFERENCES livro (id) ON DELETE CASCADE
);


SELECT *
FROM livro;