package com.katelocate.menugenerator.recepie;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RecepieRepository {

    private List<Recepie> recepies = new ArrayList<>();

    List<Recepie> findAll() {
        return recepies;
    }

    Recepie findById(Integer id){
        return recepies.stream().filter(recepie -> recepie.id() == id).findFirst().get();
    }

    @PostConstruct
    private void init() {
        recepies.add(new Recepie(0, "Boiled Eggs", "Put an egg in a pot with cold water so that " +
                "it covers an egg, wait until the water boils. " +
                "Simmer for 8 minutes. Cool it down with running water in a sink."));
        recepies.add(new Recepie(1, "Boiled", "it covers an egg, wait until the water boils. " +
                "Simmer for 8 minutes. Cool it down with running water in a sink."));
    }
}
