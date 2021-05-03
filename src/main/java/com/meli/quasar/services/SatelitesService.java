package com.meli.quasar.services;

import com.meli.quasar.dtos.SateliteDTO;
import com.meli.quasar.model.Satelite;
import com.meli.quasar.repositories.SateliteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SatelitesService {

    @Autowired
    private SateliteRepository sateliteRepository;
    private ModelMapper modelMapper = new ModelMapper();

    public void guardarSatelite(SateliteDTO dto) {
        if (dto != null) {
            Satelite sateliteFromDB = sateliteRepository.findByNombre(dto.getNombre());
            if (sateliteFromDB != null) {
                sateliteFromDB.setPosX(dto.getPosX());
                sateliteFromDB.setPosY(dto.getPosY());
            } else {
                sateliteFromDB = modelMapper.map(dto, Satelite.class);
            }
            sateliteRepository.save(sateliteFromDB);
        }
    }

    public List<SateliteDTO> getSatelitesDTODisponibles() {
        return getSatelitesFromDB().stream()
                .map(sat -> modelMapper.map(sat, SateliteDTO.class))
                .collect(Collectors.toList());
    }

    public List<Satelite> getSatelitesFromDB() {
        Iterable<Satelite> satelites = sateliteRepository.findAll();
        return StreamSupport.stream(satelites.spliterator(), false)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public long delete(SateliteDTO dto) {
        return sateliteRepository.deleteByNombre(dto.getNombre());
    }
}
