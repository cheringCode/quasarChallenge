package com.meli.quasar.controllers;

import com.meli.quasar.dtos.RequestDTO;
import com.meli.quasar.dtos.ResponseDTO;
import com.meli.quasar.exceptions.QuasarException;
import com.meli.quasar.services.MessageService;
import com.meli.quasar.services.PositionService;
import com.meli.quasar.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topsecret_split")
public class TopSecretSplitController {

    @Autowired
    private RequestService requestService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private PositionService positionService;

    @PostMapping("/{satelliteName}")
    public ResponseEntity<String> topsecretSplit(@PathVariable String satelliteName, @RequestBody RequestDTO requestSplit) {
        requestSplit.setName(satelliteName);
        try {
            requestService.guardarRequest(requestSplit);
        } catch (QuasarException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> topsecretSplit() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setPosition(positionService.getPosicionDeTransmisionesSplit());
        responseDTO.setMessage(messageService.ensamblarMensajeDeTransmisionesSplit());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
