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

    @GetMapping("/menu/{days}/types")
    ArrayList<ArrayList<Recipe>> getMenuForPeriod (
            @PathVariable Integer days, @RequestParam Map<String, String> types
    ) {
        List<RecipeType> recipeTypes = new ArrayList<>();
        try {
            for (String type: types.keySet()) {
                if (dayRecipeTypes.contains(RecipeType.valueOf(type.toUpperCase())) & types.get(type).equals("true")) {
                    recipeTypes.add(RecipeType.valueOf(type.toUpperCase()));
                }
            }
        } catch (java.lang.IllegalArgumentException e) {
            throw new UnexpectedTypeException();
        }

        // If no valid types provided, dayRecipeTypes serves as a source
        if (recipeTypes.isEmpty()) {
            recipeTypes = dayRecipeTypes;
        }

        // Initialize each menu "row"
        ArrayList<ArrayList<Recipe>> menu = new ArrayList<>(days);
        for (int i = 0; i < days; ++i) {
            menu.add(new ArrayList<>());
        }

        Random random = new Random();
        // Fill menu via "columns"
        for (RecipeType type : recipeTypes) {
            List<Recipe> choices = recipeRepository.findByType(type);
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
