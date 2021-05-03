package com.meli.quasar.controllers;

import com.meli.quasar.dtos.RequestDTO;
import com.meli.quasar.dtos.ResponseDTO;
import com.meli.quasar.dtos.TopSecretRequestDTO;
import com.meli.quasar.model.PositionRequest;
import com.meli.quasar.services.MessageService;
import com.meli.quasar.services.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TopSecretController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private PositionService positionService;

    @PostMapping(value = "/topsecret", produces = "application/json; charset=UTF-8")
    public ResponseEntity<ResponseDTO> topSecret(@RequestBody TopSecretRequestDTO requestDTOList) {
        ResponseDTO response = new ResponseDTO();
        response.setMessage(messageService.getMessage(requestDTOList.getSatellites().stream().map(RequestDTO::getMessage).collect(Collectors.toList())));
        List<PositionRequest> locationsInput = new ArrayList<>();
        requestDTOList.getSatellites().forEach(sateliteInfo -> locationsInput.add(new PositionRequest(sateliteInfo.getName(), sateliteInfo.getDistance())));
        response.setPosition(positionService.getPosition(locationsInput));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
