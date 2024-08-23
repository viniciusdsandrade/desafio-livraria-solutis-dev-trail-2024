DROP DATABASE IF EXISTS db_livraria_solutis;
CREATE DATABASE IF NOT EXISTS db_livraria_solutis;
USE db_livraria_solutis;

CREATE TABLE livro
(
    id      BIGINT UNSIGNED AUTO_INCREMENT,
    titulo  VARCHAR(255)                    NOT NULL,
    autores VARCHAR(255)                    NOT NULL,
    editora VARCHAR(255)                    NOT NULL,
    preco   DECIMAL(10,2)                   NOT NULL,
    tipo    ENUM('IMPRESSO', 'ELETRONICO') NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE impresso
(
    id      BIGINT UNSIGNED,
    frete   DECIMAL(10,2) NOT NULL,
    estoque INT          NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES livro (id) ON DELETE CASCADE
);

CREATE TABLE eletronico
(
    id      BIGINT UNSIGNED,
    tamanho DECIMAL(10,2) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES livro (id) ON DELETE CASCADE
);

CREATE TABLE venda
(
    id      BIGINT UNSIGNED AUTO_INCREMENT,
    cliente VARCHAR(255) NOT NULL,
    valor   DECIMAL(10,2) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE venda_livro
(
    venda_id BIGINT UNSIGNED,
    livro_id BIGINT UNSIGNED,

    PRIMARY KEY (venda_id, livro_id),

    FOREIGN KEY (venda_id) REFERENCES venda (id) ON DELETE CASCADE,
    FOREIGN KEY (livro_id) REFERENCES livro (id) ON DELETE CASCADE
);

-- Para visualizar os dados da tabela livro
SELECT * FROM livro;
SELECT * FROM impresso;
SELECT * FROM eletronico;
SELECT * FROM venda;