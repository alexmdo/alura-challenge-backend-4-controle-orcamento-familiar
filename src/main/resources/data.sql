INSERT INTO categories(category_type, description) VALUES ('EXPENSE', 'Alimentação');
INSERT INTO categories(category_type, description) VALUES ('EXPENSE', 'Saúde');
INSERT INTO categories(category_type, description) VALUES ('EXPENSE', 'Moradia');
INSERT INTO categories(category_type, description) VALUES ('EXPENSE', 'Transporte');
INSERT INTO categories(category_type, description) VALUES ('EXPENSE', 'Educação');
INSERT INTO categories(category_type, description) VALUES ('EXPENSE', 'Lazer');
INSERT INTO categories(category_type, description) VALUES ('EXPENSE', 'Imprevistos');
INSERT INTO categories(category_type, description) VALUES ('EXPENSE', 'Outras');

INSERT INTO incomes(description, amount, date ) VALUES ('SALÁRIO', 10000.00, '2021-02-05');
INSERT INTO incomes(description, amount, date ) VALUES ('PREMIO', 165, '2021-01-31');
INSERT INTO incomes(description, amount, date ) VALUES ('ALUGUEL', 2500, '2021-02-10');
INSERT INTO incomes(description, amount, date ) VALUES ('OUTROS', 500, '2021-02-15');

INSERT INTO expenses(description, amount, date, category_description, category_category_type) VALUES ('ALUGUEL COTIA', 2000.00, '2021-02-15', 'Moradia', 'EXPENSE' );
INSERT INTO expenses(description, amount, date, category_description, category_category_type) VALUES ('POSTO COMBUSTIVEL', 180, '2021-02-03', 'Transporte', 'EXPENSE' );
INSERT INTO expenses(description, amount, date, category_description, category_category_type) VALUES ('ALURA', 1400.00, '2021-02-19', 'Educação', 'EXPENSE' );
INSERT INTO expenses(description, amount, date, category_description, category_category_type) VALUES ('HAPPY HOUR', 120.00, '2021-02-19', 'Lazer', 'EXPENSE' );

INSERT INTO users(email, name, password) VALUES ('alexmdo@gmail.com', 'Alexandre Oliveira', '$2a$10$4xZW8N6a9VFwacpiL6l1teMC8rBAOSv45J7w/8QvXJZzyf0grA4qG');