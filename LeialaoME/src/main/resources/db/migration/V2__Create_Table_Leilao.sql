CREATE TABLE `tb_leilao` (
  `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   vendedor_id BIGINT, -- Criar sempre o id da coluna da referencia das relações
  `ainda_ativo` bit(1) DEFAULT NULL,
  `descricao` tinytext,
  `inicio` datetime(6) DEFAULT NULL,
  `lance_inicial` double DEFAULT NULL,
  `termino` datetime(6) DEFAULT NULL,
  `urlfoto_produto` varchar(255) DEFAULT NULL,
  `valor_de_incremento` double DEFAULT NULL,
  
  CONSTRAINT fk_usuario_vendedor
  FOREIGN KEY (vendedor_id) REFERENCES tb_usuario_vendedor(id)
 
);