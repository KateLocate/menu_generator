package com.katelocate.menugenerator.recepie;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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
        Optional<Recepie> recepie = recepieRepository.findById(id);
        if (recepie.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return recepie.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@RequestBody Recepie recepie) {
        recepieRepository.create(recepie);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@RequestBody Recepie recepie, @PathVariable Integer id) {
        recepieRepository.update(recepie, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        recepieRepository.delete(id);
    }
}
