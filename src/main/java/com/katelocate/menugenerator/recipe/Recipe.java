package com.katelocate.menugenerator.recipe;

public record Recipe(
        Integer id,
        String title,
        RecipeType recipeType,
        String body
){
    public Recipe {
//        if(!RecipeType)
    }
}
