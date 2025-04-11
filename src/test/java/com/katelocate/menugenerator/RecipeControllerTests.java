package com.katelocate.menugenerator;

import com.katelocate.menugenerator.recipe.Recipe;
import com.katelocate.menugenerator.recipe.RecipeController;
import com.katelocate.menugenerator.recipe.RecipeType;
import com.katelocate.menugenerator.recipe.RecipeRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeControllerTests {

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
        RestAssured.baseURI = "http://localhost:" + port + "/api/recipes";
        recipeRepository.deleteAll();
    }

    List<Recipe> testRecipes = List.of(
            new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"),
            new Recipe(1, "Dinner", RecipeType.DINNER, "Burrito"),
            new Recipe(2, "Snack", RecipeType.SNACK, "Potato chips"),
            new Recipe(3, "Dessert", RecipeType.DESSERT, "Mochi")
    );

    static final Logger logger = Logger.getLogger(RecipeControllerTests.class.getName());
    @Test
    void shouldFindAll() {
        recipeRepository.saveAll(testRecipes);

        given()
                .contentType(ContentType.JSON)
                .when()
                    .get("")
                .then()
                    .statusCode(200)
                    .body(".", hasSize(4));
    }

    @Test
    void shouldFindById() {
        recipeRepository.saveAll(testRecipes);
        Recipe targetRecipe = testRecipes.get(1);
        Recipe recipe = given()
                .contentType(ContentType.JSON)
                .get("/" + targetRecipe.id())
                .getBody()
                .as(Recipe.class);

        assertThat(targetRecipe).isEqualTo(recipe);
    }

    @Test
    void shouldCreateRecipe() {
        try {
            String jsonRecipe = new String(
                    Files.readAllBytes(
                            Paths.get("src/test/java/com/katelocate/menugenerator/data/test-post-request.json")
                    )
            );
            given()
                    .contentType(ContentType.JSON)
                    .body(jsonRecipe.toString())
                    .when()
                        .post("")
                    .then()
                        .statusCode(201);
        } catch (IOException e) {
            logger.severe("Failed to read test JSON.");
            assert false;
        };


    }
}
