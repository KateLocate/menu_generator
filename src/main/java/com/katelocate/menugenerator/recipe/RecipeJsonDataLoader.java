package com.katelocate.menugenerator.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class RecipeJsonDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RecipeJsonDataLoader.class);
    private final RecipeRepository recipeRepository;
    private final ObjectMapper objectMapper;

    public RecipeJsonDataLoader(RecipeRepository recipeRepository, ObjectMapper objectMapper) {
        this.recipeRepository = recipeRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws  Exception {
        if(recipeRepository.count() == 0) {
            try(InputStream inputStream = TypeReference.class.getResourceAsStream("/data/recipes.json")) {
                Recipes allRecipes = objectMapper.readValue(inputStream, Recipes.class);
                logger.info("Reading {} recipes from JSON data and saving it to ", allRecipes.recipes().size());
                recipeRepository.saveAll(allRecipes.recipes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        } else {
            logger.info("The collection already contains data, JSON data loading will not be performed.");
        }
    }
}
