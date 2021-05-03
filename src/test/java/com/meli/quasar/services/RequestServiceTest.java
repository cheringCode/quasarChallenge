package com.meli.quasar.services;

import com.meli.quasar.dtos.RequestDTO;
import com.meli.quasar.exceptions.QuasarException;
import com.meli.quasar.model.Satelite;
import com.meli.quasar.repositories.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class RequestServiceTest {
    @Mock
    private SatelitesService satelitesService;
    @Mock
    private RequestRepository requestRepository;
    @InjectMocks
    private RequestService requestService;

    @Test
    void guardarRequestNullTest() {
        requestService.guardarRequest(null);
        verify(requestRepository, never()).findByName(anyString());
        verify(requestRepository, never()).save(any());
    }

    @Test
    void guardarRequestNoHaySatelitesGuardadosEnLaBDTest() {
        RequestDTO dto = new RequestDTO();
        dto.setName("SateliteTest");
        when(satelitesService.getSatelitesFromDB()).thenReturn(new ArrayList<>());
        try {
            requestService.guardarRequest(dto);
            fail();
        } catch (QuasarException e) {
            assertEquals("El satelite SateliteTest no existe", e.getMessage());
        }
    }

    @Test
    void guardarRequestSateliteNoExisteTest() {
        RequestDTO dto = new RequestDTO();
        dto.setName("SateliteTest");
        Satelite satelite1 = new Satelite();
        satelite1.setNombre("Satelite1");
        List<Satelite> satelites = new ArrayList<>();
        satelites.add(satelite1);
        when(satelitesService.getSatelitesFromDB()).thenReturn(satelites);
        try {
            requestService.guardarRequest(dto);
            fail();
        } catch (QuasarException e) {
            assertEquals("El satelite SateliteTest no existe", e.getMessage());
        }
    }

    @Test
    void guardarRequestValidoTest() {
        RequestDTO dto = new RequestDTO();
        dto.setName("SateliteTest");
        Satelite satelite1 = new Satelite();
        satelite1.setNombre("SateliteTest");
        List<Satelite> satelites = new ArrayList<>();
        satelites.add(satelite1);
        when(satelitesService.getSatelitesFromDB()).thenReturn(satelites);
        try {
            requestService.guardarRequest(dto);
            verify(requestRepository, atMostOnce()).save(any());
        } catch (QuasarException e) {
            fail();
        }
    }
}