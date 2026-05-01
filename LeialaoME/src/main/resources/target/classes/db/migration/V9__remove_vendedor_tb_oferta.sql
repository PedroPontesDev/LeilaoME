-- 1. Remover a constrainter (FK)
ALTER TABLE tb_oferta
DROP FOREIGN KEY fk_oferta_vendedor;

-- 2. Remover a coluna vendoder da tabela de ofertas
ALTER TABLE tb_oferta
DROP COLUMN vendedor_id;