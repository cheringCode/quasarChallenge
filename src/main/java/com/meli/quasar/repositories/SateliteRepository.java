package com.meli.quasar.repositories;

import com.meli.quasar.model.Satelite;
import org.springframework.data.repository.CrudRepository;

public interface SateliteRepository extends CrudRepository<Satelite, Long> {
    Satelite findByNombre(String nombre);
    long deleteByNombre(String nombre);
}
