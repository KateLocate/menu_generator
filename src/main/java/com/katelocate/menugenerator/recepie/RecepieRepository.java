package com.katelocate.menugenerator.recepie;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RecepieRepository {

    private List<Recepie> recepies = new ArrayList<>();

    List<Recepie> findAll() {
        return recepies;
    }

    Optional<Recepie> findById(Integer id){
        return recepies.stream().filter(recepie -> recepie.id() == id).findFirst();
    }

    void create(Recepie recepie) {
        recepies.add(recepie);
    }

    void update(Recepie recepie, Integer id) {
        Optional<Recepie> existingRecepie = findById(id);
        if (existingRecepie.isPresent()) {
            recepies.set(recepies.indexOf(existingRecepie.get()), recepie);
        }
    }

    void delete(Integer id) {
        recepies.removeIf(recepie -> recepie.id().equals(id));
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
