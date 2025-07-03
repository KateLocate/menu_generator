package com.katelocate.menugenerator;

import com.katelocate.menugenerator.recipe.Recipe;
import com.katelocate.menugenerator.recipe.RecipeType;
import com.katelocate.menugenerator.recipe.RecipeRepository;

import static com.katelocate.menugenerator.recipe.Constants.dayRecipeTypes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;

import com.fasterxml.jackson.databind.ObjectMapper;


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
            new Recipe(3, "Dessert", RecipeType.DESSERT, "Mochi"),
            new Recipe(4, "Breakfast number two", RecipeType.BREAKFAST, "Peanut butter toast")
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
                    .statusCode(HttpStatus.OK.value())
                    .body(".", hasSize(testRecipes.size()));
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
                    .body(jsonRecipe)
                    .when()
                        .post("")
                    .then()
                        .statusCode(HttpStatus.CREATED.value());
        } catch (IOException e) {
            logger.severe("Failed to read test JSON.");
            assert false;
        };

    }

    @Test
    void shouldUpdateRecipe() {
        recipeRepository.saveAll(testRecipes);

        try {
            String jsonRecipe = new String(
                    Files.readAllBytes(
                            Paths.get("src/test/java/com/katelocate/menugenerator/data/test-put-request.json")
                    )
            );

            ObjectMapper jsonMapper = new ObjectMapper();
            Recipe targetRecipe = jsonMapper.readValue(jsonRecipe, Recipe.class);

            given()
                    .contentType(ContentType.JSON)
                    .body(jsonRecipe)
                    .when()
                        .put("/" + targetRecipe.id())
                    .then()
                        .statusCode(HttpStatus.NO_CONTENT.value());

            Recipe updatedRecipe = given()
                    .contentType(ContentType.JSON)
                    .get("/" + targetRecipe.id())
                    .getBody()
                    .as(Recipe.class);

            assertThat(targetRecipe).isEqualTo(updatedRecipe);
        } catch (IOException e) {
            logger.severe("Failed to read test JSON.");
            assert false;
        };
    }

    @Test
    void shouldDeleteRecipe(){
        recipeRepository.saveAll(testRecipes);
        Recipe targetRecipe = testRecipes.get(1);

        given()
                .when()
                    .delete("/" + targetRecipe.id())
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .when()
                    .get("/" + targetRecipe.id())
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
    }


    List<Recipe> getTestDayMenuFromTestObjects(){
        List<Recipe> dayRecipes = new ArrayList<>();

        for (RecipeType type: dayRecipeTypes) {
            for (Recipe recipe: testRecipes){
                if (recipe.recipeType() == type){
                    dayRecipes.add(recipe);
                    break;
                }
            }
        }
        return dayRecipes;
    }

    List<Recipe> getTestDayMenuFromAPI() {
        return given()
                .contentType(ContentType.JSON)
                .get("/menu/" + 1 + "/types")
                .jsonPath()
                // "[0]" - gets the first day content from response JSON
                // "Recipe.class" - converts each recipe into Java object
                .getList("[0]", Recipe.class);
    }


    @Test
    void shouldGetDayMenuRecipes(){
        List<Recipe> targetDayRecipes = getTestDayMenuFromTestObjects();
        recipeRepository.saveAll(targetDayRecipes);

        List<Recipe> dayRecipes = getTestDayMenuFromAPI();
        assertThat(dayRecipes).isEqualTo(targetDayRecipes);
    }

    @Test
    void shouldGetRandomMenus(){
        List<Recipe> suitableRecipes = new ArrayList<>();
        for (Recipe recipe: testRecipes) {
            if (dayRecipeTypes.contains(recipe.recipeType())){
                suitableRecipes.add(recipe);
            }
        }

        if (suitableRecipes.size() <= dayRecipeTypes.size()) {
            logger.severe("Not enough recipes for two different day menus, provide more.");
            assert(false);
        }
        recipeRepository.saveAll(testRecipes);

        List<Recipe> firstDayRecipes = getTestDayMenuFromAPI();

        while (true) {
            List<Recipe> secondDayRecipes = getTestDayMenuFromAPI();
            if (firstDayRecipes != secondDayRecipes){
                logger.info(String.valueOf(firstDayRecipes) + secondDayRecipes);
                break;
            }
        }
    }

    @Test
    void shouldGetMenuForPeriod() {
        recipeRepository.saveAll(testRecipes);

        // Get right number of Recipe lists
        List<Integer> periods = List.of(1, 7, 31);
        for (Integer period: periods) {
            given()
                    .contentType(ContentType.JSON)
                    .when()
                        .get("/menu/" + period + "/types")
                    .then()
                        .statusCode(HttpStatus.OK.value())
                        .body(".", hasSize(period));
        }
        // Catch the UnexpectedTypeException
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/menu/" + 1 + "/types?bsdnjv=1wsas")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

}
