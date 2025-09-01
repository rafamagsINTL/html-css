-- Script para verificar e consultar dados do banco dungeon_rpg

USE dungeon_rpg;

-- 1. Verificar todas as tabelas existentes
SHOW TABLES;

-- 2. Ver estrutura da tabela personagem
DESCRIBE personagem;

-- 3. Ver estrutura da tabela habilidade (se existir)
SHOW TABLES LIKE 'habilidade';

-- 4. Consultar todos os personagens salvos
SELECT * FROM personagem ORDER BY data_criacao DESC;

-- 5. Consultar personagens por nível
SELECT nome, nivel, xp, hp, ataque, defesa, data_criacao
FROM personagem
WHERE nivel >= 2
ORDER BY nivel DESC;

-- 6. Contar quantos personagens foram salvos
SELECT COUNT(*) as total_personagens FROM personagem;

-- 7. Ver o personagem mais forte (maior nível)
SELECT nome, nivel, ataque, defesa, data_criacao
FROM personagem
ORDER BY nivel DESC, ataque DESC
LIMIT 1;

-- 8. Estatísticas gerais
SELECT
    COUNT(*) as total_personagens,
    ROUND(AVG(nivel), 2) as nivel_medio,
    MAX(nivel) as nivel_maximo,
    ROUND(AVG(xp), 2) as xp_medio,
    MAX(ataque) as maior_ataque
FROM personagem;

-- 9. Personagens criados hoje
SELECT nome, nivel, hp, ataque, defesa, data_criacao
FROM personagem
WHERE DATE(data_criacao) = CURDATE()
ORDER BY data_criacao DESC;

-- 10. Personagens criados nas últimas 24 horas
SELECT nome, nivel, hp, ataque, defesa,
       TIMESTAMPDIFF(HOUR, data_criacao, NOW()) as horas_atras
FROM personagem
WHERE data_criacao >= DATE_SUB(NOW(), INTERVAL 24 HOUR)
ORDER BY data_criacao DESC;

-- 11. Top 5 personagens mais fortes
SELECT nome, nivel, ataque, defesa, (ataque + defesa) as poder_total, data_criacao
FROM personagem
ORDER BY poder_total DESC, nivel DESC
LIMIT 5;

-- 12. Verificar se tabela habilidade existe e suas colunas
SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'dungeon_rpg' AND TABLE_NAME = 'habilidade'
ORDER BY ORDINAL_POSITION;

-- 13. Se tabela habilidade existir, mostrar habilidades (sem ordenar por elemento)
-- Descomente a linha abaixo se a tabela existir:
-- SELECT * FROM habilidade ORDER BY nome;

-- 14. Limpar dados de teste (descomente se necessário)
-- DELETE FROM personagem WHERE nome = 'Teste';

-- 15. Backup básico dos personagens (opcional)
-- SELECT nome, nivel, xp, hp, ataque, defesa, data_criacao
-- FROM personagem
-- INTO OUTFILE '/tmp/personagens_backup.csv'
-- FIELDS TERMINATED BY ',' ENCLOSED BY '"'
-- LINES TERMINATED BY '\n';
