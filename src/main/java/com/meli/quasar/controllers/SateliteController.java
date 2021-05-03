package com.meli.quasar.controllers;

import com.meli.quasar.dtos.SateliteDTO;
import com.meli.quasar.services.SatelitesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SateliteController {

    @Autowired
    private SatelitesService satelitesService;

    @GetMapping(value = "/satelites", produces = "application/json; charset=UTF-8")
    public ResponseEntity<List<SateliteDTO>> getSatelites() {
        return new ResponseEntity<>(satelitesService.getSatelitesDTODisponibles(), HttpStatus.OK);
    }

    @PostMapping(value = "/satelite", produces = "application/json; charset=UTF-8")
    public ResponseEntity<Void> crearSatelite(@RequestBody SateliteDTO sateliteDTO) {
        satelitesService.guardarSatelite(sateliteDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/satelite", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> borrarSatelite(@RequestBody SateliteDTO sateliteDTO) {
        if (satelitesService.delete(sateliteDTO) > 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("El satelite " + sateliteDTO.getNombre() + " no se encontro", HttpStatus.NOT_FOUND);
        }
    }
}
