package com.katelocate.menugenerator;

import com.katelocate.menugenerator.recipe.Recipe;
import com.katelocate.menugenerator.recipe.RecipeRepository;
import com.katelocate.menugenerator.recipe.RecipeType;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.hamcrest.Matchers.equalTo;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeRepositoryTests {

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
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
//        ???? Test shouldGetRecipeById covers this functionality
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
    void shouldGetRecipeById() {
        recipeRepository.create(new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/recipes/0")
                .then()
                .statusCode(200)
                .body(equalTo("{\"id\":0,\"title\":\"Breakfast\",\"recipeType\":\"BREAKFAST\",\"body\":\"Scramble eggs\"}"));
    }

    @Test
    void shouldUpdateRecipe() {
        recipeRepository.create(new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"));
        recipeRepository.update(new Recipe(0, "Scramble Eggs", RecipeType.BREAKFAST, "Scramble eggs"), 0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/recipes/0")
                .then()
                .statusCode(200)
                .body(equalTo("{\"id\":0,\"title\":\"Scramble Eggs\",\"recipeType\":\"BREAKFAST\",\"body\":\"Scramble eggs\"}"));

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

    }
}
