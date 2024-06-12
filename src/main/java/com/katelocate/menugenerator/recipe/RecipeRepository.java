package com.katelocate.menugenerator.recipe;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RecipeRepository {

    private List<Recipe> recipes = new ArrayList<>();

    List<Recipe> findAll() {
        return recipes;
    }

    Optional<Recipe> findById(Integer id){
        return recipes.stream().filter(recipe -> recipe.id() == id).findFirst();
    }

    void create(Recipe recipe) {
        recipes.add(recipe);
    }

    void update(Recipe recipe, Integer id) {
        Optional<Recipe> existingRecipe = findById(id);
        if (existingRecipe.isPresent()) {
            recipes.set(recipes.indexOf(existingRecipe.get()), recipe);
        }
    }

    void delete(Integer id) {
        recipes.removeIf(recipe -> recipe.id().equals(id));
    }

    @PostConstruct
    private void init() {
        recipes.add(new Recipe(0, "Boiled Eggs", RecipeType.SNACK,
                "Put an egg in a pot with cold water so that it covers an egg, wait until the water boils. " +
                "Simmer for 8 minutes. Cool it down with running water in a sink."));
        recipes.add(new Recipe(1, "Boiled", RecipeType.BREAKFAST,
                "it covers an egg, wait until the water boils. Simmer for 8 minutes. Cool it down with running" +
                        " water in a sink."));
    }
}
