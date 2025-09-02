INSERT INTO tb_permissao (id, usuario_role)
VALUES (1, 'ROLE_COMPRADOR') -- 0 corresponde ao ROLE_COMPRADOR
ON DUPLICATE KEY UPDATE usuario_role = 0;
