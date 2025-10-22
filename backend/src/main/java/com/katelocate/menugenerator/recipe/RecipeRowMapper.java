package com.katelocate.menugenerator.recipe;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeRowMapper implements RowMapper<Recipe> {

    @Override
    public Recipe mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("id");
        String title = rs.getString("title");
        RecipeType recipeType = RecipeType.valueOf(rs.getString("recipe_type"));

        String[] instructions = new String[0];
        java.sql.Array sqlInstructions = rs.getArray("instructions");
        if (sqlInstructions != null) {
            instructions = (String[]) sqlInstructions.getArray();
        }

        String[] ingredients = new String[0];
        java.sql.Array sqlIngredients = rs.getArray("ingredients");
        if (sqlIngredients != null) {
            ingredients = (String[]) sqlIngredients.getArray();
        }

        return new Recipe(id, title, recipeType, instructions, ingredients);
    }
}
