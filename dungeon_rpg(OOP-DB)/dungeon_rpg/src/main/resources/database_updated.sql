-- Script para criar e testar o banco de dados dungeon_rpg
-- Atualizado para incluir apenas elementos FOGO, AGUA, ELETRICO, LUZ, SOMBRA, NEUTRO

-- Criar banco de dados se não existir
CREATE DATABASE IF NOT EXISTS dungeon_rpg;
USE dungeon_rpg;

-- Remover tabelas na ordem correta para evitar erros de chave estrangeira
DROP TABLE IF EXISTS combate_log;
DROP TABLE IF EXISTS personagem_habilidade;
DROP TABLE IF EXISTS personagem;
DROP TABLE IF EXISTS habilidade;

-- Criar tabela de personagens com todas as colunas necessárias
CREATE TABLE personagem (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    nivel INT NOT NULL DEFAULT 1,
    xp INT NOT NULL DEFAULT 0,
    hp INT NOT NULL DEFAULT 100,
    ataque INT NOT NULL DEFAULT 10,
    defesa INT NOT NULL DEFAULT 5,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Criar tabela de habilidades
CREATE TABLE habilidade (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    dano_base INT NOT NULL DEFAULT 10,
    bonus_ataque INT NOT NULL DEFAULT 0,
    elemento VARCHAR(20) NOT NULL DEFAULT 'NEUTRO'
);

-- Criar tabela para relacionar personagens com habilidades
CREATE TABLE personagem_habilidade (
    id INT AUTO_INCREMENT PRIMARY KEY,
    personagem_id INT,
    habilidade_id INT,
    FOREIGN KEY (personagem_id) REFERENCES personagem(id) ON DELETE CASCADE,
    FOREIGN KEY (habilidade_id) REFERENCES habilidade(id) ON DELETE CASCADE
);

-- Inserir habilidades atualizadas
INSERT INTO habilidade (nome, descricao, dano_base, bonus_ataque, elemento) VALUES
('Golpe Básico', 'Um ataque simples mas eficaz', 10, 0, 'NEUTRO'),

-- Habilidades de Fogo
('Bola de Fogo', 'Projétil flamejante', 12, 8, 'FOGO'),
('Fúria Flamejante', 'Ataques ígneos devastadores', 18, 15, 'FOGO'),

-- Habilidades de Água
('Torrente Aquática', 'Fluxo d\'água poderoso', 14, 6, 'AGUA'),
('Maré Devastadora', 'Ondas destruidoras', 16, 10, 'AGUA'),

-- Habilidades Elétricas
('Raio Elétrico', 'Descarga elétrica poderosa', 16, 8, 'ELETRICO'),
('Tempestade', 'Múltiplos raios devastadores', 20, 12, 'ELETRICO'),

-- Habilidades de Luz
('Raio Divino', 'Luz purificadora', 15, 10, 'LUZ'),
('Lança de Luz', 'Perfura as trevas', 17, 12, 'LUZ'),

-- Habilidades de Sombra
('Golpe Sombrio', 'Ataque das trevas', 17, 12, 'SOMBRA'),
('Lâmina das Trevas', 'Corte sombrio letal', 19, 14, 'SOMBRA'),

-- Habilidades Neutras
('Força Brutal', 'Poder físico puro', 14, 15, 'NEUTRO'),
('Investida Feroz', 'Ataque devastador', 16, 10, 'NEUTRO');

-- Verificar se as tabelas foram criadas
SHOW TABLES;

-- Verificar estrutura das tabelas
DESCRIBE personagem;
DESCRIBE habilidade;
DESCRIBE personagem_habilidade;

-- Inserir um personagem de teste
INSERT INTO personagem (nome, nivel, xp, hp, ataque, defesa)
VALUES ('Teste', 1, 0, 100, 10, 5);

-- Verificar se foi inserido
SELECT * FROM personagem;
