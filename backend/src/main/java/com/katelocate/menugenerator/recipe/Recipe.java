package com.katelocate.menugenerator.recipe;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record Recipe(
        Integer id,
        @NotEmpty
        String title,
        RecipeType recipeType,
        List<String> ingredients,
        List<String> instructions
){};
