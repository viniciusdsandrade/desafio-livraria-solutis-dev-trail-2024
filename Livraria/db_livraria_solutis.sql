DROP DATABASE IF EXISTS db_livraria_solutis;
CREATE DATABASE IF NOT EXISTS db_livraria_solutis;
USE db_livraria_solutis;

CREATE TABLE livro
(
    id      BIGINT UNSIGNED AUTO_INCREMENT  NOT NULL,
    titulo  VARCHAR(255)                    NOT NULL,
    autores VARCHAR(255)                    NOT NULL,
    editora VARCHAR(255)                    NOT NULL,
    preco   DECIMAL(10, 2)                  NOT NULL,
    tipo    ENUM ('IMPRESSO', 'ELETRONICO') NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE impresso
(
    id      BIGINT UNSIGNED NOT NULL,
    frete   DECIMAL(10, 2)  NOT NULL,
    estoque INT UNSIGNED    NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES livro (id) ON DELETE CASCADE
);

CREATE TABLE eletronico
(
    id      BIGINT UNSIGNED NOT NULL,
    tamanho BIGINT UNSIGNED NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES livro (id) ON DELETE CASCADE
);

CREATE TABLE venda
(
    id      BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
    cliente VARCHAR(255)                   NOT NULL,
    valor   DECIMAL(10, 2)                 NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE venda_livro
(
    venda_id BIGINT UNSIGNED NOT NULL,
    livro_id BIGINT UNSIGNED NOT NULL,

    PRIMARY KEY (venda_id, livro_id),

    FOREIGN KEY (venda_id) REFERENCES venda (id) ON DELETE CASCADE,
    FOREIGN KEY (livro_id) REFERENCES livro (id) ON DELETE CASCADE
);

-- Para visualizar os dados da tabela livro
SELECT *
FROM livro;
SELECT *
FROM impresso;
SELECT *
FROM eletronico;
SELECT *
FROM venda;
SELECT *
FROM venda_livro;

-- Consulta para visualizar todos os livros com estoque, considerando livros impressos e eletrônicos
SELECT l.id,
       l.titulo,
       l.preco,
       CASE
           WHEN l.tipo = 'IMPRESSO' THEN COALESCE(i.estoque, 0)
           WHEN l.tipo = 'ELETRONICO' THEN NULL
           END                                    AS estoque,
       IF(l.tipo = 'ELETRONICO', e.tamanho, NULL) AS tamanho_kb,
       CASE
           WHEN l.tipo = 'IMPRESSO' THEN 'IMPRESSO'
           WHEN l.tipo = 'ELETRONICO' THEN 'ELETRONICO'
           END                                    AS tipo
FROM livro l
         LEFT JOIN impresso i ON l.id = i.id
         LEFT JOIN eletronico e ON l.id = e.id;

-- Contar o total de livros impressos
SELECT COUNT(*) AS total_livro_impresso
FROM impresso;

-- Contar o total de livros eletrônicos
SELECT COUNT(*) AS total_livro_eletronico
FROM eletronico;

-- Contar o total de vendas
SELECT COUNT(*) AS total_venda
FROM venda;

-- Inserindo 10 livros impressos
INSERT INTO livro (titulo, autores, editora, preco, tipo)
VALUES ('Dom Quixote', 'Miguel de Cervantes', 'Companhia das Letras', 49.90, 'IMPRESSO'),
       ('O Pequeno Príncipe', 'Antoine de Saint-Exupéry', 'Record', 29.90, 'IMPRESSO'),
       ('1984', 'George Orwell', 'Editora Nova Fronteira', 39.90, 'IMPRESSO'),
       ('O Senhor dos Anéis', 'J.R.R. Tolkien', 'HarperCollins', 79.90, 'IMPRESSO'),
       ('Harry Potter e a Pedra Filosofal', 'J.K. Rowling', 'Rocco', 34.90, 'IMPRESSO'),
       ('O Hobbit', 'J.R.R. Tolkien', 'HarperCollins', 49.90, 'IMPRESSO'),
       ('Orgulho e Preconceito', 'Jane Austen', 'Penguin', 29.90, 'IMPRESSO'),
       ('Cem Anos de Solidão', 'Gabriel García Márquez', 'Companhia das Letras', 59.90, 'IMPRESSO'),
       ('Crime e Castigo', 'Fiodor Dostoiévski', 'L&PM', 39.90, 'IMPRESSO');

INSERT INTO impresso (id, frete, estoque)
VALUES (1, 10.00, 100),
       (2, 8.00, 50),
       (3, 12.00, 20),
       (4, 15.00, 30),
       (5, 9.00, 80),
       (6, 11.00, 60),
       (7, 7.00, 40),
       (8, 13.00, 70),
       (9, 10.00, 90);

-- Inserindo 19 livros eletrônicos
INSERT INTO livro (titulo, autores, editora, preco, tipo)
VALUES ('O Alquimista', 'Paulo Coelho', 'HarperCollins', 19.90, 'ELETRONICO'),
       ('A Menina que Roubava Livros', 'Markus Zusak', 'Companhia das Letras', 24.90, 'ELETRONICO'),
       ('O Código Da Vinci', 'Dan Brown', 'Record', 29.90, 'ELETRONICO'),
       ('A Garota no Trem', 'Paula Hawkins', 'Intrínseca', 22.90, 'ELETRONICO'),
       ('O Silêncio dos Inocentes', 'Thomas Harris', 'Objetiva', 18.90, 'ELETRONICO'),
       ('A Culpa é das Estrelas', 'John Green', 'Intrínseca', 21.90, 'ELETRONICO'),
       ('Divergente', 'Veronica Roth', 'Galera Record', 23.90, 'ELETRONICO'),
       ('Jogos Vorazes', 'Suzanne Collins', 'Rocco', 25.90, 'ELETRONICO'),
       ('Crepúsculo', 'Stephenie Meyer', 'Editora Nova Fronteira', 19.90, 'ELETRONICO'),
       ('O Senhor dos Anéis: A Sociedade do Anel', 'J.R.R. Tolkien', 'HarperCollins', 27.90, 'ELETRONICO'),
       ('O Senhor dos Anéis: As Duas Torres', 'J.R.R. Tolkien', 'HarperCollins', 27.90, 'ELETRONICO'),
       ('O Senhor dos Anéis: O Retorno do Rei', 'J.R.R. Tolkien', 'HarperCollins', 27.90, 'ELETRONICO'),
       ('Harry Potter e a Câmara Secreta', 'J.K. Rowling', 'Rocco', 24.90, 'ELETRONICO'),
       ('Harry Potter e o Prisioneiro de Azkaban', 'J.K. Rowling', 'Rocco', 24.90, 'ELETRONICO'),
       ('Harry Potter e o Cálice de Fogo', 'J.K. Rowling', 'Rocco', 24.90, 'ELETRONICO'),
       ('Harry Potter e a Ordem da Fênix', 'J.K. Rowling', 'Rocco', 24.90, 'ELETRONICO'),
       ('Harry Potter e o Enigma do Príncipe', 'J.K. Rowling', 'Rocco', 24.90, 'ELETRONICO'),
       ('Harry Potter e as Relíquias da Morte', 'J.K. Rowling', 'Rocco', 24.90, 'ELETRONICO'),
       ('Percy Jackson e o Ladrão de Raios', 'Rick Riordan', 'Intrínseca', 22.90, 'ELETRONICO');

INSERT INTO eletronico (id, tamanho)
VALUES (10, 5000),
       (11, 6000),
       (12, 4500),
       (13, 7000),
       (14, 5500),
       (15, 6500),
       (16, 4000),
       (17, 7500),
       (18, 5000),
       (19, 6000),
       (20, 4500),
       (21, 7000),
       (22, 5500),
       (23, 6500),
       (24, 4000),
       (25, 7500),
       (26, 5000),
       (27, 6000),
       (28, 4500);
