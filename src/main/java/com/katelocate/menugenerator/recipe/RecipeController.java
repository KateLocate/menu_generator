package com.katelocate.menugenerator.recipe;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @GetMapping("/menu/{days}/types")
    ArrayList<ArrayList<Recipe>> getMenuForPeriod(
            @PathVariable Integer days, @RequestParam Map<String, String> types
    ) {
        HashSet<String> typesVarieties = new HashSet<>(types.values());
        if (typesVarieties.contains("true")) {
            types.values().removeIf(s -> s.equals("false"));
        }

        ArrayList<ArrayList<Recipe>> menu = new ArrayList<>(days);
        for (int i = 0; i < days; ++i) {
            menu.add(new ArrayList<>());
        }

        Random random = new Random();
        for (String type: types.keySet()) {
            List<Recipe> choices = recipeRepository.findByType(RecipeType.valueOf(type.toUpperCase()));
            for (int i = 0; i < days; ++i) {
                int randomIndex = random.nextInt(choices.size());
                menu.get(i).add(choices.get(randomIndex));
            }
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
