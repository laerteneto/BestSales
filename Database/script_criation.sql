---- Script para criação do banco!
CREATE DATABASE projetoIvo;
\c projetoivo;
---CLIENTES
CREATE TABLE clientes (
	id SERIAL NOT NULL,
	nome VARCHAR(50),
	cpf VARCHAR(17),
	telefone VARCHAR(16) ,
	endereco VARCHAR(200),
	data_cadastro DATE ,
	PRIMARY KEY (id) 
  )
;  
---COMPRAS
CREATE TABLE compras (
	id SERIAL NOT NULL ,
	clientes_id INT ,
	valor FLOAT NOT NULL,
	data_compra DATE NOT NULL ,
  PRIMARY KEY (id) ,
  CONSTRAINT fk_compras_clientes
    FOREIGN KEY (clientes_id)
    REFERENCES clientes(id)	
)
;
---PRODUTOS
CREATE TABLE produtos (
	id SERIAL NOT NULL ,
	nome VARCHAR(50) NOT NULL,
	preco_compra FLOAT NOT NULL,
	preco_venda FLOAT NOT NULL,
	descricao VARCHAR(255),
  PRIMARY KEY (id) 
)
;
---ESTOQUE
CREATE TABLE estoque (
	id SERIAL NOT NULL,
	produtos_id INT NOT NULL,
	quantidade INT,
	data_estoque DATE,
  PRIMARY KEY (id) ,
  CONSTRAINT fk_estoque_produtos
    FOREIGN KEY (produtos_id)
    REFERENCES produtos(id)	
) 
;
---RELATORIOS
CREATE TABLE relatorios (
	id SERIAL NOT NULL,
	data_relatorio DATE,
	venda FLOAT,
	lucro FLOAT,
  PRIMARY KEY (id) 
)
;
---fim do script
