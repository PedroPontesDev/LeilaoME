CREATE TABLE tb_oferta (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    valor_oferta DOUBLE NOT NULL,
    status_oferta VARCHAR(15) NOT NULL,
 
    comprador_id BIGINT NOT NULL,
    vendedor_id BIGINT NOT NULL,
    leilao_id BIGINT NOT NULL,
    
    momento_oferta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_oferta_comprador FOREIGN KEY (comprador_id) 
        REFERENCES tb_usuario_comprador(id),
     
        
    CONSTRAINT fk_oferta_vendedor FOREIGN KEY (vendedor_id) 
        REFERENCES tb_usuario_vendedor(id),
      
        
    CONSTRAINT fk_oferta_leilao FOREIGN KEY (leilao_id) 
        REFERENCES tb_leilao(id)
      
);