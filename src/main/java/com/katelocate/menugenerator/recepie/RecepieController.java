package com.katelocate.menugenerator.recepie;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recepies")
public class RecepieController {

    private final RecepieRepository recepieRepository;

    public RecepieController(final RecepieRepository recepieRepository) {
        this.recepieRepository = recepieRepository;
    }

    @GetMapping("")
    List<Recepie> findAll() {
        return recepieRepository.findAll();
    }

    @GetMapping("/{id}")
    Recepie findById(@PathVariable Integer id) {
        return recepieRepository.findById(id);
    }

}
