DROP DATABASE IF EXISTS db_livraria_solutis;
CREATE DATABASE IF NOT EXISTS db_livraria_solutis;
USE db_livraria_solutis;

# Rascunho da modelagem em MySQL
CREATE TABLE livro
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    titulo  VARCHAR(255)                    NOT NULL,
    autores VARCHAR(255)                    NOT NULL,
    editora VARCHAR(255)                    NOT NULL,
    preco   DOUBLE                          NOT NULL,
    tipo    ENUM ('IMPRESSO', 'ELETRONICO') NOT NULL
);

CREATE TABLE impresso
(
    id      INT PRIMARY KEY,
    frete   DOUBLE NOT NULL,
    estoque INT    NOT NULL,
    FOREIGN KEY (id) REFERENCES livro (id) ON DELETE CASCADE
);

CREATE TABLE eletronico
(
    id      INT PRIMARY KEY,
    tamanho DOUBLE NOT NULL,
    FOREIGN KEY (id) REFERENCES livro (id) ON DELETE CASCADE
);

CREATE TABLE venda
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    cliente VARCHAR(255) NOT NULL,
    valor   DOUBLE       NOT NULL
);

CREATE TABLE venda_livro
(
    venda_id INT,
    livro_id INT,
    PRIMARY KEY (venda_id, livro_id),
    FOREIGN KEY (venda_id) REFERENCES venda (id) ON DELETE CASCADE,
    FOREIGN KEY (livro_id) REFERENCES livro (id) ON DELETE CASCADE
);


SELECT * FROM livro;