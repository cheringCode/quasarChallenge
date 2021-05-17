package com.meli.quasar.services;

import com.meli.quasar.model.PositionRequest;
import com.meli.quasar.model.Satelite;
import com.meli.quasar.model.response.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class PositionServiceTest {
	@Mock
	private SatelitesService satelitesService;
	@Mock
	private RequestService requestService;
	@InjectMocks
	private PositionService positionService;

	@BeforeEach
	public void setUp() {
		when(satelitesService.getSatelitesFromDB()).thenReturn(mockSateliteList());
	}

	@Test
	void localizacionDesdeSplitTest() {
		when(requestService.listarSatelitesYDistancias()).thenReturn(mockPositionRequestList(300f, 400f, 800f));
		Position respuesta = positionService.getPosicionDeTransmisionesSplit();

		assertEquals(-216.5f, respuesta.getX());
		assertEquals(-303.0f, respuesta.getY());
	}

	@Test
	void localizacion1Test() {
		probarPosicion(300f, 400f, 800f, -216.5f, -303.0f);
	}

	@Test
	void localizacionCoincideConKenobiTest() {
		probarPosicion(0f, 400f, 800f, -500f, -200f);
	}

	@Test
	void localizacionCoincideConSkywalkerTest() {
		probarPosicion(100f, 0f, 800f, 100f, -100f);
	}

	@Test
	void localizacionCoincideConSatoTest() {
		probarPosicion(100f, 450f, 0f, 500f, 100f);
	}

	private void probarPosicion(float distancia1, float distancia2, float distancia3, float expectedX, float expectedY) {
		List<PositionRequest> requests = mockPositionRequestList(distancia1, distancia2, distancia3);

		Position respuesta = positionService.getPosition(requests);

		assertEquals(expectedX, respuesta.getX());
		assertEquals(expectedY, respuesta.getY());
	}

	private List<PositionRequest> mockPositionRequestList(float distancia1, float distancia2, float distancia3) {
		PositionRequest positionRequest1 = new PositionRequest("kenobi", distancia1);
		PositionRequest positionRequest2 = new PositionRequest("skywalker", distancia2);
		PositionRequest positionRequest3 = new PositionRequest("sato", distancia3);

		List<PositionRequest> requests = new ArrayList<>();
		requests.add(positionRequest1);
		requests.add(positionRequest2);
		requests.add(positionRequest3);

		return requests;
	}

	private List<Satelite> mockSateliteList() {
		Satelite kenobi = new Satelite();
		kenobi.setNombre("kenobi");
		kenobi.setPosX(-500f);
		kenobi.setPosY(-200f);

		Satelite skywalker = new Satelite();
		skywalker.setNombre("skywalker");
		skywalker.setPosX(100f);
		skywalker.setPosY(-100f);

		Satelite sato = new Satelite();
		sato.setNombre("sato");
		sato.setPosX(500f);
		sato.setPosY(100f);

		List<Satelite> satelites = new ArrayList<>();
		satelites.add(kenobi);
		satelites.add(skywalker);
		satelites.add(sato);

		return satelites;
	}

}
