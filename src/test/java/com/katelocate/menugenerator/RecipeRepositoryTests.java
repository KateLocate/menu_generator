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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static class RecipeRowMapper implements RowMapper<Recipe> {

        @Override
        public Recipe mapRow(ResultSet resultSet, int rowNum) throws SQLException {

            return new Recipe(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    RecipeType.valueOf(resultSet.getString("recipeType")),
                    resultSet.getString("body")
            );
        }
    }

    @Test
    void shouldCreateRecipe() {
        Recipe recipe = new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs");
        recipeRepository.create(recipe);

        assertThat(jdbcTemplate.queryForObject("SELECT * FROM Recipe", new RecipeRowMapper())).isEqualTo(recipe);
    }

    @Test
    void shouldGetAllRecipes() {
        List<Recipe> recipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "Burrito")
        );
        recipeRepository.saveAll(recipes);

        assertThat(jdbcTemplate.query("SELECT * FROM Recipe WHERE id IN (0, 1)", new RecipeRowMapper())).isEqualTo(recipes);
    }

    @Test
    void shouldFindRecipeById() {
        Recipe recipe = new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Boiled eggs");
        recipeRepository.create(recipe);

        assertThat(jdbcTemplate.queryForObject("SELECT * FROM Recipe WHERE id = 0", new RecipeRowMapper())).isEqualTo(recipe);
    }

    @Test
    void shouldUpdateRecipe() {
        Recipe updatedRecipe = new Recipe(0, "Scramble Eggs", RecipeType.BREAKFAST, "Scramble eggs");
        recipeRepository.create(new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"));
        recipeRepository.update(updatedRecipe, 0);

        assertThat(jdbcTemplate.queryForObject("SELECT * FROM Recipe WHERE id = 0", new RecipeRowMapper())).isEqualTo(updatedRecipe);

    }

    @Test
    void shouldDeleteRecipe() {
        List<Recipe> recipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "Burrito"),
                new Recipe(2, "Snack", RecipeType.SNACK, "Potato chips")
        );
        recipeRepository.saveAll(recipes);
        recipeRepository.delete(1);

        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Recipe WHERE id = 1", Integer.class)).isEqualTo(0);
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Recipe", Integer.class)).isEqualTo(2);
    }

    @Test
    void shouldDeleteAllRecipes() {
        List<Recipe> recipes = List.of(
                new Recipe(0, "Breakfast", RecipeType.BREAKFAST, "Scramble eggs"),
                new Recipe(1, "Dinner", RecipeType.DINNER, "Burrito")
        );
        recipeRepository.saveAll(recipes);
        recipeRepository.deleteAll();

        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Recipe", Integer.class)).isEqualTo(0);
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

        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Recipe", Integer.class)).isEqualTo(4);
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

        assertThat(jdbcTemplate.query("SELECT * FROM Recipe", new RecipeRowMapper())).isEqualTo(recipes);
    }
}
