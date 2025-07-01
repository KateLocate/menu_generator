package com.katelocate.menugenerator.recipe;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.katelocate.menugenerator.recipe.Constants.dayRecipeTypes;


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

    @GetMapping("/day/{types}")
    ArrayList<Recipe> getDayMenu(@PathVariable Map<String, String> recipeTypes) {
        recipeTypes.values().removeIf(s -> s.equals("false"));

        Random random = new Random();
        ArrayList<Recipe> dayMenu = new ArrayList<>(3);
        for (String type: recipeTypes.keySet()) {
            List<Recipe> choices = recipeRepository.findByType(RecipeType.valueOf(type.toUpperCase()));
            int randomIndex = random.nextInt(choices.size());
            dayMenu.add(choices.get(randomIndex));
        }
        return dayMenu;
    }

    @GetMapping("/period/{days}/types")
    ArrayList<ArrayList<Recipe>> getMenuForPeriod(@PathVariable Integer days, @RequestParam Map<String, String> types) {
        ArrayList<ArrayList<Recipe>> menu = new ArrayList<>();
        for (int i = 1; i <= days; ++i) {
            menu.add(getDayMenu(types));
        }
        return menu;
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
