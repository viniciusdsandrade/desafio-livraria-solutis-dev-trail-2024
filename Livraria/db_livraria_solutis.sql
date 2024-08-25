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
       COALESCE(i.estoque, 0) AS estoque
FROM livro l
         LEFT JOIN impresso i ON l.id = i.id
         LEFT JOIN eletronico e ON l.id = e.id;

-- Consultas para contar o total de livros impressos, eletrônicos e vendas
-- Contar o total de livros impressos
SELECT COUNT(*) AS total_livro_impresso
FROM impresso;

-- Contar o total de livros eletrônicos
SELECT COUNT(*) AS total_livro_eletronico
FROM eletronico;

-- Contar o total de vendas
SELECT COUNT(*) AS total_venda
FROM venda;


-- Primeiro insira 21 livros na tabela 'livro' com tipo 'ELETRONICO'
INSERT INTO livro (titulo, autores, editora, preco, tipo)
VALUES ('Dom Casmurro', 'Machado de Assis', 'Editora A', 19.99, 'ELETRONICO'),
       ('Memórias Póstumas de Brás Cubas', 'Machado de Assis', 'Editora B', 15.99, 'ELETRONICO'),
       ('Vidas Secas', 'Graciliano Ramos', 'Editora C', 12.99, 'ELETRONICO'),
       ('O Cortiço', 'Aluísio Azevedo', 'Editora D', 17.99, 'ELETRONICO'),
       ('Iracema', 'José de Alencar', 'Editora E', 10.99, 'ELETRONICO'),
       ('A Hora da Estrela', 'Clarice Lispector', 'Editora F', 14.99, 'ELETRONICO'),
       ('Capitães da Areia', 'Jorge Amado', 'Editora G', 18.99, 'ELETRONICO'),
       ('Gabriela, Cravo e Canela', 'Jorge Amado', 'Editora H', 16.99, 'ELETRONICO'),
       ('Dona Flor e Seus Dois Maridos', 'Jorge Amado', 'Editora I', 19.99, 'ELETRONICO'),
       ('O Alienista', 'Machado de Assis', 'Editora J', 11.99, 'ELETRONICO'),
       ('Quincas Borba', 'Machado de Assis', 'Editora K', 13.99, 'ELETRONICO'),
       ('Esaú e Jacó', 'Machado de Assis', 'Editora L', 15.99, 'ELETRONICO'),
       ('Memorial de Aires', 'Machado de Assis', 'Editora M', 17.99, 'ELETRONICO'),
       ('Triste Fim de Policarpo Quaresma', 'Lima Barreto', 'Editora N', 12.99, 'ELETRONICO'),
       ('O Guarani', 'José de Alencar', 'Editora O', 10.99, 'ELETRONICO'),
       ('Ubirajara', 'José de Alencar', 'Editora P', 14.99, 'ELETRONICO'),
       ('Lucíola', 'José de Alencar', 'Editora Q', 16.99, 'ELETRONICO'),
       ('Senhora', 'José de Alencar', 'Editora R', 18.99, 'ELETRONICO'),
       ('O Primo Basílio', 'Eça de Queirós', 'Editora S', 11.99, 'ELETRONICO'),
       ('A Relíquia', 'Eça de Queirós', 'Editora T', 13.99, 'ELETRONICO');
--      ('Os Maias', 'Eça de Queirós', 'Editora U', 15.99, 'ELETRONICO');

-- Em seguida, insira os dados na tabela 'eletronico' usando o ID do livro recém-inserido
INSERT INTO eletronico (id, tamanho)
VALUES (LAST_INSERT_ID() - 20, 2048),
       (LAST_INSERT_ID() - 19, 1536),
       (LAST_INSERT_ID() - 18, 3072),
       (LAST_INSERT_ID() - 17, 1024),
       (LAST_INSERT_ID() - 16, 2560),
       (LAST_INSERT_ID() - 15, 1280),
       (LAST_INSERT_ID() - 14, 4096),
       (LAST_INSERT_ID() - 13, 1792),
       (LAST_INSERT_ID() - 12, 3584),
       (LAST_INSERT_ID() - 11, 896),
       (LAST_INSERT_ID() - 10, 2304),
       (LAST_INSERT_ID() - 9, 1152),
       (LAST_INSERT_ID() - 8, 3328),
       (LAST_INSERT_ID() - 7, 768),
       (LAST_INSERT_ID() - 6, 2176),
       (LAST_INSERT_ID() - 5, 1088),
       (LAST_INSERT_ID() - 4, 3168),
       (LAST_INSERT_ID() - 3, 640),
       (LAST_INSERT_ID() - 2, 1920),
       (LAST_INSERT_ID() - 1, 960);
--    (LAST_INSERT_ID(), 2880);

-- Inserir 10 livros impressos
INSERT INTO livro (titulo, autores, editora, preco, tipo)
VALUES ('Livro Impresso 1', 'Autor 1', 'Editora X', 30.00, 'IMPRESSO'),
       ('Livro Impresso 2', 'Autor 2', 'Editora Y', 25.50, 'IMPRESSO'),
       ('Livro Impresso 3', 'Autor 3', 'Editora Z', 40.00, 'IMPRESSO'),
       ('Livro Impresso 4', 'Autor 4', 'Editora A', 22.99, 'IMPRESSO'),
       ('Livro Impresso 5', 'Autor 5', 'Editora B', 35.00, 'IMPRESSO'),
       ('Livro Impresso 6', 'Autor 6', 'Editora C', 28.75, 'IMPRESSO'),
       ('Livro Impresso 7', 'Autor 7', 'Editora D', 32.50, 'IMPRESSO'),
       ('Livro Impresso 8', 'Autor 8', 'Editora E', 27.00, 'IMPRESSO'),
       ('Livro Impresso 9', 'Autor 9', 'Editora F', 38.00, 'IMPRESSO'),
       ('Livro Impresso 10', 'Autor 10', 'Editora G', 29.99, 'IMPRESSO');

-- Inserir dados na tabela 'impresso'
INSERT INTO impresso (id, frete, estoque)
SELECT id, 10.00, 50
FROM livro
WHERE tipo = 'IMPRESSO'
ORDER BY id DESC
LIMIT 10;