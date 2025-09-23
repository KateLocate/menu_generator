package com.katelocate.menugenerator;

import com.katelocate.menugenerator.recipe.Recipe;
import com.katelocate.menugenerator.recipe.RecipeRepository;
import com.katelocate.menugenerator.recipe.RecipeType;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeRepositoryTests {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    static JdbcTemplate jdbcTemplate;

    static void jdbcTemplateSetup() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(postgres.getJdbcUrl());
        dataSource.setUsername(postgres.getUsername());
        dataSource.setPassword(postgres.getPassword());
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @BeforeAll
    static void beforeAll() {
        postgres.start();
        jdbcTemplateSetup();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
    }

    public static class RecipeRowMapper implements RowMapper<Recipe> {

        @Override
        public Recipe mapRow(ResultSet resultSet, int rowNum) throws SQLException {

            return new Recipe(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    RecipeType.valueOf(resultSet.getString("recipeType")),
                    resultSet.getString("ingredients"),
                    resultSet.getString("instructions")
            );
        }
    }

    @Test
    void shouldCreateRecipe() {
        Recipe recipe = new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "[\"2 eggs\", \"salt\", \"peppa\"]", "[\"Scramble eggs\"]");

        recipeRepository.create(recipe);
        String sql = String.format("SELECT * FROM Recipe WHERE id = %d", recipe.id());

        assertThat(jdbcTemplate.queryForObject(sql, new RecipeRowMapper())).isEqualTo(recipe);
    }

    @Test
    void shouldGetAllRecipes() {
        List<Recipe> testRecipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "[\"2 eggs\", \"salt\", \"peppa\"]", "[\"Scramble eggs\"]"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "[\"1 tortilla\", \"tomatoes\", \"ground beef\", \"rice\", \"corn\"]", "[\"Burrito\"]"),
                new Recipe(2, "Snack", RecipeType.SNACK, "[\"3 potatoes\", \"salt\", \"peppa\", \"1 tbsp of cooking oil\"]", "[\"Potato chips\"]"),
                new Recipe(3, "Dessert", RecipeType.DESSERT, "[\"glutinous rice flour\", \"sugar\", \"water\"]", "[\"Mochi\"]")
        );
        recipeRepository.saveAll(testRecipes);

        assertThat(jdbcTemplate.query("SELECT * FROM Recipe", new RecipeRowMapper())).isEqualTo(testRecipes);
    }

    @Test
    void shouldFindRecipeById() {
        Recipe recipe =  new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "[\"2 eggs\", \"salt\", \"peppa\"]", "[\"Scramble eggs\"]");
        recipeRepository.create(recipe);
        String sql = String.format("SELECT * FROM Recipe WHERE id = %d", recipe.id());

        assertThat(jdbcTemplate.queryForObject(sql, new RecipeRowMapper())).isEqualTo(recipe);
    }

    @Test
    void shouldUpdateRecipe() {
        Recipe recipe =  new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "[\"2 eggs\", \"salt\", \"peppa\"]", "[\"Scramble eggs\"]");
        recipeRepository.create(recipe);
        Recipe updatedRecipe = new Recipe(recipe.id(), "Scramble Eggs", recipe.recipeType(), recipe.ingredients(), recipe.instructions());
        recipeRepository.update(updatedRecipe, recipe.id());
        String sql = String.format("SELECT * FROM Recipe WHERE id = %d", recipe.id());

        assertThat(jdbcTemplate.queryForObject(sql, new RecipeRowMapper())).isEqualTo(updatedRecipe);

    }

    @Test
    void shouldDeleteRecipe() {
        List<Recipe> testRecipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "[\"2 eggs\", \"salt\", \"peppa\"]", "[\"Scramble eggs\"]"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "[\"1 tortilla\", \"tomatoes\", \"ground beef\", \"rice\", \"corn\"]", "[\"Burrito\"]"),
                new Recipe(2, "Snack", RecipeType.SNACK, "[\"3 potatoes\", \"salt\", \"peppa\", \"1 tbsp of cooking oil\"]", "[\"Potato chips\"]"),
                new Recipe(3, "Dessert", RecipeType.DESSERT, "[\"glutinous rice flour\", \"sugar\", \"water\"]", "[\"Mochi\"]")
                );
        recipeRepository.saveAll(testRecipes);
        Integer targetId = 2;
        recipeRepository.delete(targetId);
        String sqlCheckIfOnlyTargetAffected = "SELECT COUNT(*) FROM Recipe";
        String sqlCheckIfStillExists = String.format("SELECT COUNT(*) FROM Recipe WHERE id = %d", targetId);

        assertThat(jdbcTemplate.queryForObject(sqlCheckIfStillExists, Integer.class)).isEqualTo(0);
        assertThat(jdbcTemplate.queryForObject(sqlCheckIfOnlyTargetAffected, Integer.class)).isEqualTo(testRecipes.size() - 1);
    }

    @Test
    void shouldDeleteAllRecipes() {
        List<Recipe> testRecipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "[\"2 eggs\", \"salt\", \"peppa\"]", "[\"Scramble eggs\"]"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "[\"1 tortilla\", \"tomatoes\", \"ground beef\", \"rice\", \"corn\"]", "[\"Burrito\"]"),
                new Recipe(2, "Snack", RecipeType.SNACK, "[\"3 potatoes\", \"salt\", \"peppa\", \"1 tbsp of cooking oil\"]", "[\"Potato chips\"]"),
                new Recipe(3, "Dessert", RecipeType.DESSERT, "[\"glutinous rice flour\", \"sugar\", \"water\"]", "[\"Mochi\"]")
                );
        recipeRepository.saveAll(testRecipes);
        recipeRepository.deleteAll();

        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Recipe", Integer.class)).isEqualTo(0);
    }

    @Test
    void shouldCountAllRecipes() {
        List<Recipe> testRecipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "[\"2 eggs\", \"salt\", \"peppa\"]", "[\"Scramble eggs\"]"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "[\"1 tortilla\", \"tomatoes\", \"ground beef\", \"rice\", \"corn\"]", "[\"Burrito\"]"),
                new Recipe(2, "Snack", RecipeType.SNACK, "[\"3 potatoes\", \"salt\", \"peppa\", \"1 tbsp of cooking oil\"]", "[\"Potato chips\"]"),
                new Recipe(3, "Dessert", RecipeType.DESSERT, "[\"glutinous rice flour\", \"sugar\", \"water\"]", "[\"Mochi\"]")
                );
        recipeRepository.saveAll(testRecipes);

        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Recipe", Integer.class)).isEqualTo(testRecipes.size());
    }

    @Test
    void shouldSaveAll() {
        List<Recipe> testRecipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "[\"2 eggs\", \"salt\", \"peppa\"]", "[\"Scramble eggs\"]"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "[\"1 tortilla\", \"tomatoes\", \"ground beef\", \"rice\", \"corn\"]", "[\"Burrito\"]"),
                new Recipe(2, "Snack", RecipeType.SNACK, "[\"3 potatoes\", \"salt\", \"peppa\", \"1 tbsp of cooking oil\"]", "[\"Potato chips\"]"),
                new Recipe(3, "Dessert", RecipeType.DESSERT, "[\"glutinous rice flour\", \"sugar\", \"water\"]", "[\"Mochi\"]")
                );
        recipeRepository.saveAll(testRecipes);

        assertThat(jdbcTemplate.query("SELECT * FROM Recipe", new RecipeRowMapper())).isEqualTo(testRecipes);
    }

    @Test
    void shouldFindByType() {
        List<Recipe> testRecipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "[\"2 eggs\", \"salt\", \"peppa\"]", "[\"Scramble eggs\"]"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "[\"1 tortilla\", \"tomatoes\", \"ground beef\", \"rice\", \"corn\"]", "[\"Burrito\"]"),
                new Recipe(2, "Snack", RecipeType.SNACK, "[\"3 potatoes\", \"salt\", \"peppa\", \"1 tbsp of cooking oil\"]", "[\"Potato chips\"]"),
                new Recipe(3, "Dessert", RecipeType.DESSERT, "[\"glutinous rice flour\", \"sugar\", \"water\"]", "[\"Mochi\"]"),
                new Recipe(4, "Dessert", RecipeType.DESSERT, "[\"glutinous rice flour\", \"sugar\", \"water\"]", "[\"Mochi\"]"),
                new Recipe(5, "Dessert", RecipeType.DESSERT, "[\"glutinous rice flour\", \"sugar\", \"water\"]", "[\"Mochi\"]"),
                new Recipe(6, "Dessert", RecipeType.DESSERT, "[\"glutinous rice flour\", \"sugar\", \"water\"]", "[\"Mochi\"]"),
                new Recipe(7, "Dessert", RecipeType.DESSERT, "[\"glutinous rice flour\", \"sugar\", \"water\"]", "[\"Mochi\"]"),
                new Recipe(8, "Dessert", RecipeType.DESSERT, "[\"glutinous rice flour\", \"sugar\", \"water\"]", "[\"Mochi\"]")
                );
        recipeRepository.saveAll(testRecipes);

        RecipeType[] recipeTypes = RecipeType.values();
        for (RecipeType type: recipeTypes) {
            List<Recipe> recipesOfType = recipeRepository.findByType(type);

            if (recipesOfType.size() > 1) {
                Set<RecipeType> typeVarieties = new HashSet<>();

                for (Recipe recipe: recipesOfType) {
                    typeVarieties.add(recipe.recipeType());
                }
                assertThat(typeVarieties.size()).isEqualTo(1);
            }
        }
    }

}
