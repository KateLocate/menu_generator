package com.katelocate.menugenerator.recipe;

import jakarta.validation.constraints.NotEmpty;

public record Recipe(
        Integer id,
        @NotEmpty
        String title,
        @NotEmpty
        RecipeType recipeType,
        String body
){};
