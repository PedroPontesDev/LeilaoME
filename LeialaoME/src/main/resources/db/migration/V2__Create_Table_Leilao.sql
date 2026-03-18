CREATE TABLE tb_leilao (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  vendedor_id BIGINT NULL,
  comprador_id BIGINT NULL,
  ainda_ativo BOOLEAN DEFAULT FALSE,
  descricao TEXT,
  inicio DATETIME(6) DEFAULT NULL,
  lance_inicial DOUBLE DEFAULT NULL,
  termino DATETIME(6) DEFAULT NULL,
  urlfoto_produto VARCHAR(255) DEFAULT NULL,
  incremento_lance DOUBLE DEFAULT NULL,

  CONSTRAINT fk_usuario_vendedor
    FOREIGN KEY (vendedor_id) REFERENCES tb_usuario_vendedor(id),

  CONSTRAINT fk_usuario_comprador
    FOREIGN KEY (comprador_id) REFERENCES tb_usuario_comprador(id)
);