CREATE TABLE tb_permissoes_usuarios (
    usuario_id BIGINT NOT NULL,
    permissao_id BIGINT NOT NULL,

    PRIMARY KEY (usuario_id, permissao_id),

    FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id),
    FOREIGN KEY (permissao_id) REFERENCES tb_permissao(id)
);