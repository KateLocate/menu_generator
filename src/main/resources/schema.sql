CREATE TABLE IF NOT EXISTS Recipe (
   id INT NOT NULL,
   title VARCHAR(250) NOT NULL,
   recipeType VARCHAR(50) NOT NULL,
   body VARCHAR(max),
   PRIMARY KEY (id)
);
