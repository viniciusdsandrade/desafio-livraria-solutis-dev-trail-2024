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

-- Criar uma função para calcular o novo estoque com base na operação (adição ou subtração)
DELIMITER //
CREATE FUNCTION calcular_novo_estoque(id_livro BIGINT, quantidade INT, tipo_operacao VARCHAR(3))
    RETURNS INT
BEGIN
    DECLARE estoque_atual INT;
    DECLARE novo_estoque INT;

    -- Obtem o estoque atual do livro
    SELECT i.estoque INTO estoque_atual FROM impresso i WHERE i.id = id_livro;

    -- Calcula o novo estoque com base no tipo de operação
    IF tipo_operacao = 'ADD' THEN
        SET novo_estoque = estoque_atual + quantidade;
    ELSEIF tipo_operacao = 'SUB' THEN
        SET novo_estoque = estoque_atual - quantidade;
    ELSE
        -- Tipo de operação inválido, retorna o estoque atual
        SET novo_estoque = estoque_atual;
    END IF;

    RETURN novo_estoque;
END //
DELIMITER ;

-- Criar um trigger para atualizar o estoque na tabela 'impresso' após a inserção em 'venda_livro'
DELIMITER //
CREATE TRIGGER tr_atualizar_estoque_impresso_apos
    AFTER INSERT
    ON venda_livro
    FOR EACH ROW
BEGIN
    -- Atualiza o estoque do livro na tabela 'impresso'
    UPDATE impresso
    SET estoque = calcular_novo_estoque(NEW.livro_id, 1, 'SUB')
    WHERE id = NEW.livro_id;
END //
DELIMITER ;
