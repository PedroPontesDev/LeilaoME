CREATE TABLE `tb_leilao` (
  `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   vendedor_id BIGINT NULL, -- Criar sempre o id da coluna da referencia das relações
   comprador_id BIGINT NULL,
  `ainda_ativo` BOOLEAN DEFAULT FALSE,
  `descricao` TEXT,
  `inicio` datetime(6) DEFAULT NULL,
  `lance_inicial` DOUBLE DEFAULT NULL,
  `termino` datetime(6) DEFAULT NULL,
  `urlfoto_produto` varchar(255) DEFAULT NULL,
  `valor_de_incremento` double DEFAULT NULL,
  
  CONSTRAINT fk_usuario_vendedor
  FOREIGN KEY (vendedor_id) REFERENCES tb_usuario_vendedor(id),
  
   
  CONSTRAINT fk_usuario_comprador
  FOREIGN KEY (comprador_id) REFERENCES tb_usuario_comprador(id)
  
);