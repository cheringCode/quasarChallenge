package com.meli.quasar.repositories;

import com.meli.quasar.model.request.Request;
import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Long> {
    Request findByName(String name);
}
