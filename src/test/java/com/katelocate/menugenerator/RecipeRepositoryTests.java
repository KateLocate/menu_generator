package com.katelocate.menugenerator;

import com.katelocate.menugenerator.recipe.Recipe;
import com.katelocate.menugenerator.recipe.RecipeRepository;
import com.katelocate.menugenerator.recipe.RecipeType;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeRepositoryTests {

    @LocalServerPort
    private Integer port;

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
        RestAssured.baseURI = "http://localhost:" + port;
        recipeRepository.deleteAll();
    }

    @Test
    void shouldCreateRecipe() {
        List<Recipe> recipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "Burrito"),
                new Recipe(2, "Snack", RecipeType.SNACK, "Potato chips")
        );
        recipeRepository.saveAll(recipes);
        int afterInsertionTableSize = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Recipe", Integer.class);
        assertThat(afterInsertionTableSize).isEqualTo(3);
    }

    @Test
    void shouldGetAllRecipes() {
        List<Recipe> recipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "Burrito")
        );
        recipeRepository.saveAll(recipes);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/recipes")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

    @Test
    void shouldFindRecipeById() {
        Recipe recipe = new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Boiled eggs");
        recipeRepository.create(recipe);

        assertThat(recipeRepository.findById(0).get()).isEqualTo(recipe);
    }

    @Test
    void shouldUpdateRecipe() {
        Recipe updatedRecipe = new Recipe(0, "Scramble Eggs", RecipeType.BREAKFAST, "Scramble eggs");
        recipeRepository.create(new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"));
        recipeRepository.update(updatedRecipe, 0);

        assertThat(recipeRepository.findById(0).get()).isEqualTo(updatedRecipe);

    }

    @Test
    void shouldDeleteRecipe() {
        recipeRepository.create(new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"));
        recipeRepository.delete(0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/recipes/0")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldDeleteAllRecipes() {
        List<Recipe> recipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "Burrito")
        );
        recipeRepository.saveAll(recipes);

        recipeRepository.deleteAll();
        int afterDeletionTableSize = recipeRepository.findAll().size();
        assertThat(afterDeletionTableSize).isEqualTo(0);
    }

    @Test
    void shouldCountAllRecipes() {
        List<Recipe> recipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "Burrito"),
                new Recipe(2, "Snack", RecipeType.SNACK, "Potato chips"),
                new Recipe(3, "Dessert", RecipeType.DESSERT, "Mochi")
        );
        recipeRepository.saveAll(recipes);

        assertThat(recipeRepository.count()).isEqualTo(4);
    }

    @Test
    void shouldSaveAll() {
        List<Recipe> recipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "Burrito"),
                new Recipe(2, "Snack", RecipeType.SNACK, "Potato chips"),
                new Recipe(3, "Dessert", RecipeType.DESSERT, "Mochi")
        );
        recipeRepository.saveAll(recipes);

        assertThat(recipeRepository.findAll()).isEqualTo(recipes);
    }
}
