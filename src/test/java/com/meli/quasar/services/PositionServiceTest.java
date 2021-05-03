package com.meli.quasar.services;

import com.meli.quasar.model.response.Position;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class PositionServiceTest {

    @Autowired
    private PositionService positionService;

    private final Position positionKenobi = new Position(-500, -200);
    private final Position positionSkywalker = new Position(100, -100);
    private final Position positionSato = new Position(500, 100);

    @Test
    void localizacionTest() {
        probarPosicion(300f, 400f, 800f, -216.5f, -303.0f);
    }

    private void probarPosicion(float distancia1, float distancia2, float distancia3, float expectedX, float expectedY) {
        double[][] positions = new double[3][2];
        double[] distances = new double[3];

        positions[0][0] = positionKenobi.getX();
        positions[0][1] = positionKenobi.getY();

        positions[1][0] = positionSkywalker.getX();
        positions[1][1] = positionSkywalker.getY();

        positions[2][0] = positionSato.getX();
        positions[2][1] = positionSato.getY();

        distances[0] = distancia1;
        distances[1] = distancia2;
        distances[2] = distancia3;


        Position respuesta = positionService.obtenerLocalizacion(positions, distances);

        assertEquals(expectedX, respuesta.getX());
        assertEquals(expectedY, respuesta.getY());
    }

}