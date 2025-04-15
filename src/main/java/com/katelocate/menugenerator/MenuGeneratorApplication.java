package com.katelocate.menugenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;
import java.util.Set;


@SpringBootApplication
public class MenuGeneratorApplication {

    private static final Logger logger = LoggerFactory.getLogger(MenuGeneratorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MenuGeneratorApplication.class, args);

        int[] arr = {11, 12, 13, 14, 18, 49, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        Random random = new Random();
        int randomIndex = random.nextInt(arr.length);

        logger.info(String.format("Random int index: %d value: %d", randomIndex, arr[randomIndex]));

        Set<Integer> dayRecipeIndexes = new java.util.HashSet<>(Set.of(random.nextInt(arr.length)));
        int recipesNeeded = 3;
        int counter = 0;
        logger.info("Random day recipe: " + dayRecipeIndexes);


        while (counter < recipesNeeded) {
            randomIndex = random.nextInt(arr.length);
            if (!dayRecipeIndexes.contains(randomIndex)) {
                dayRecipeIndexes.add(randomIndex);
                counter++;
            }
        }

        logger.info("Random day recipes: " + dayRecipeIndexes);

    }
}
