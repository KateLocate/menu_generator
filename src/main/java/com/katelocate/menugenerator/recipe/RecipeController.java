package com.katelocate.menugenerator.recipe;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(final RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("")
    List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @GetMapping("/{id}")
    Recipe findById(@PathVariable Integer id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isEmpty()) {
            throw new RecipeNotFoundException();
        }
        return recipe.get();
    }

    @GetMapping("/day")
    ArrayList<Recipe> getDayMenu() {
        RecipeType[] types = {
                RecipeType.BREAKFAST,
                RecipeType.SNACK,
                RecipeType.DINNER,
        };
        Random random = new Random();
        ArrayList<Recipe> dayMenu = new ArrayList<>(3);
        for (RecipeType type : types) {
            List<Recipe> choices = recipeRepository.findByType(type);
            int randomIndex = random.nextInt(choices.size());
            dayMenu.add(choices.get(randomIndex));
        }
        return dayMenu;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Recipe recipe) {
        recipeRepository.create(recipe);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody Recipe recipe, @PathVariable Integer id) {
        recipeRepository.update(recipe, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        recipeRepository.delete(id);
    }
}
