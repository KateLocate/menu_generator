package com.katelocate.menugenerator.recipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;


@Repository
public class RecipeRepository {

    private static final Logger log = LoggerFactory.getLogger(RecipeRepository.class);
    private final JdbcClient jdbcClient;
    private final RecipeRowMapper recipeRowMapper = new RecipeRowMapper();

    public RecipeRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Recipe> findAll() {
        return jdbcClient.sql("SELECT * FROM Recipe")
                .query(recipeRowMapper)
                .list();
    }

    public Optional<Recipe> findById(Integer id) {
        return jdbcClient.sql("SELECT id, title, recipe_type, ingredients, instructions FROM Recipe WHERE id = :id")
                .param("id", id)
                .query(recipeRowMapper)
                .optional();
    }

    public void create(Recipe recipe) {
        var updated = jdbcClient.sql("INSERT INTO Recipe (id, title, recipe_type, ingredients, instructions) VALUES (?,?,?,?,?)")
                .params(List.of(recipe.id, recipe.title, recipe.recipeType.name(), recipe.ingredients, recipe.instructions))
                .update();

        Assert.state(updated == 1, "Failed to create recipe " + recipe.title);
    }

    public void update(Recipe recipe, Integer id) {
        var updated = jdbcClient.sql("UPDATE Recipe SET title = ?, recipe_type = ?, ingredients = ?, instructions = ? WHERE id = ?")
                .params(List.of(recipe.title, recipe.recipeType.name(), recipe.ingredients, recipe.instructions, id))
                .update();

        Assert.state(updated == 1, "Failed to update recipe " + recipe.title);
    }

    public void delete(Integer id) {
        jdbcClient.sql("DELETE FROM Recipe WHERE id = :id")
                .param("id", id)
                .update();
    }

    public void deleteAll() {
        jdbcClient.sql("DELETE FROM Recipe").update();
    }

    public int count() {
        return jdbcClient.sql("SELECT * FROM Recipe").query().listOfRows().size();
    }

    public void saveAll(List<Recipe> recipes) {
        recipes.stream().forEach(this::create);
    }

    public List<Recipe> findByType(RecipeType recipeType) {
        return jdbcClient.sql("SELECT * FROM Recipe WHERE recipe_type = :recipeType")
                .param("recipeType", recipeType.name())
                .query(recipeRowMapper)
                .list();
    }
}
