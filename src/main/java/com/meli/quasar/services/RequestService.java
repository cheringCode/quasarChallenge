package com.meli.quasar.services;

import com.meli.quasar.dtos.RequestDTO;
import com.meli.quasar.exceptions.QuasarException;
import com.meli.quasar.model.PositionRequest;
import com.meli.quasar.model.Satelite;
import com.meli.quasar.model.request.Request;
import com.meli.quasar.repositories.RequestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RequestService {

    @Autowired
    private SatelitesService satelitesService;
    @Autowired
    private RequestRepository requestRepository;
    private ModelMapper modelMapper = new ModelMapper();

    public List<PositionRequest> listarSatelitesYDistancias() {
        Iterable<Request> requestIterable = requestRepository.findAll();
        return StreamSupport.stream(requestIterable.spliterator(), false)
                .map(req -> new PositionRequest(req.getName(), req.getDistance()))
                .collect(Collectors.toList());
    }

    public List<List<String>> listarTodosLosMensajesDeTransmisiones() {
        Iterable<Request> requestIterable = requestRepository.findAll();
        return StreamSupport.stream(requestIterable.spliterator(), false)
                .map(Request::getMessage)
                .collect(Collectors.toList());
    }

    @Transactional
    public void guardarRequest(RequestDTO dto) {
        if (dto != null) {
            validarSateliteExistente(dto.getName());
            Request requestFromDB = requestRepository.findByName(dto.getName());
            if (requestFromDB != null) {
                requestFromDB.setDistance(dto.getDistance());
                requestFromDB.setMessage(dto.getMessage());
            } else {
                requestFromDB = modelMapper.map(dto, Request.class);
            }
            requestRepository.save(requestFromDB);
        }
    }

    private void validarSateliteExistente(String nombreSatelite) {
        Optional<Satelite> sateliteEncontrado = satelitesService.getSatelitesFromDB().stream()
                .filter(sat -> sat.getNombre().equalsIgnoreCase(nombreSatelite))
                .findAny();
        if (!sateliteEncontrado.isPresent()) {
            throw new QuasarException("El satelite " + nombreSatelite + " no existe");
        }
    }
}
