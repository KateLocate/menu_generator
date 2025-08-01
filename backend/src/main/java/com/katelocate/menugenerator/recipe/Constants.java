package com.katelocate.menugenerator.recipe;

import java.util.List;

public final class Constants {

    private Constants() {
        //  restrict instantiation
    }
    public static final List<RecipeType> dayRecipeTypes = List.of(new RecipeType[]{
            RecipeType.BREAKFAST,
            RecipeType.SNACK,
            RecipeType.DINNER,
            RecipeType.DESSERT,
    });
}
