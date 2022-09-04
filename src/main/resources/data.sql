INSERT INTO categorias(categoria_type, descricao) VALUES ('DESPESA', 'Alimentação');
INSERT INTO categorias(categoria_type, descricao) VALUES ('DESPESA', 'Saúde');
INSERT INTO categorias(categoria_type, descricao) VALUES ('DESPESA', 'Moradia');
INSERT INTO categorias(categoria_type, descricao) VALUES ('DESPESA', 'Transporte');
INSERT INTO categorias(categoria_type, descricao) VALUES ('DESPESA', 'Educação');
INSERT INTO categorias(categoria_type, descricao) VALUES ('DESPESA', 'Lazer');
INSERT INTO categorias(categoria_type, descricao) VALUES ('DESPESA', 'Imprevistos');
INSERT INTO categorias(categoria_type, descricao) VALUES ('DESPESA', 'Outras');

INSERT INTO receitas (descricao, valor, data ) VALUES ('SALÁRIO', 10000.00, '2021-02-05');
INSERT INTO receitas (descricao, valor, data ) VALUES ('PREMIO', 165, '2021-01-31');
INSERT INTO receitas (descricao, valor, data ) VALUES ('ALUGUEL', 2500, '2021-02-10');
INSERT INTO receitas (descricao, valor, data ) VALUES ('OUTROS', 500, '2021-02-15');

INSERT INTO despesas(descricao, valor, data, categoria_descricao, categoria_categoria_type) VALUES ('ALUGUEL COTIA', 2000.00, '2021-02-15', 'Moradia', 'DESPESA' );
INSERT INTO despesas(descricao, valor, data, categoria_descricao, categoria_categoria_type) VALUES ('POSTO COMBUSTIVEL', 180, '2021-02-03', 'Transporte', 'DESPESA' );
INSERT INTO despesas(descricao, valor, data, categoria_descricao, categoria_categoria_type) VALUES ('ALURA', 1400.00, '2021-02-19', 'Educação', 'DESPESA' );
INSERT INTO despesas(descricao, valor, data, categoria_descricao, categoria_categoria_type) VALUES ('HAPPY HOUR', 120.00, '2021-02-19', 'Lazer', 'DESPESA' );