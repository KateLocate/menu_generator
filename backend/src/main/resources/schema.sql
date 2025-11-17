DROP TABLE IF EXISTS Recipe;
CREATE TABLE IF NOT EXISTS Recipe (
   id INT NOT NULL,
   title VARCHAR(250) NOT NULL,
   recipe_type VARCHAR(50) NOT NULL,
   ingredients TEXT[],
   instructions TEXT[],
   PRIMARY KEY (id)
);
