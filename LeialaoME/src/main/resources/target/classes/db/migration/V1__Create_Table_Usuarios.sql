CREATE TABLE tb_usuario(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_usuario VARCHAR(31),

    username VARCHAR(30) NOT NULL,
    password VARCHAR(100) NOT NULL,

    biografia TEXT,
    url_foto_perfil VARCHAR(100)
);

CREATE TABLE tb_usuario_vendedor(
	
	id BIGINT PRIMARY KEY,
	cnpj_vendedor VARCHAR (17) UNIQUE,
	
	CONSTRAINT fk_vendedor_usuario
	FOREIGN KEY (id) REFERENCES tb_usuario(id)

);

CREATE TABLE tb_usuario_comprador(
	
	id BIGINT PRIMARY KEY,
	cpf_comprador VARCHAR (14) UNIQUE,
	
	CONSTRAINT fk_vendedor_comprador
	FOREIGN KEY (id) REFERENCES tb_usuario(id)

);