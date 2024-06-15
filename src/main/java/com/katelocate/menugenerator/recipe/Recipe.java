package com.katelocate.menugenerator.recipe;

import jakarta.validation.constraints.NotEmpty;

public record Recipe(
        Integer id,
        @NotEmpty
        String title,
        RecipeType recipeType,
        String body
){
    public Recipe {
//        boolean isRightType = false;
//        for (RecipeType type : RecipeType.values()) {
//            if (type == recipeType) {
//                isRightType = true;
//                break;
//            }
//        }
//        if (!isRightType) {
//            throw new IllegalArgumentException("Choose existing recipe type.");
//        }
        
    }
}
