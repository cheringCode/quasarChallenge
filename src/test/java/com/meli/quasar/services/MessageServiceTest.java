package com.meli.quasar.services;

import com.meli.quasar.exceptions.QuasarException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class MessageServiceTest {
	@Mock
	private RequestService requestService;
	@InjectMocks
	private MessageService messageService;

	@Test
	void mensajeValidoTest() {
		assertEquals("este es un mensaje", messageService.getMessage(mockMessagesValido()));
	}

	@Test
	void mensajeInvalidoTest() {
		try {
			messageService.getMessage(mockMessagesCantidadPalabrasInvalida());
			fail();
		} catch (QuasarException e) {
			assertEquals(messageService.CANTIDAD_DE_PALABRAS_NO_COINCIDEN, e.getMessage());
		}
	}

	@Test
	void mensajeConPalabraSinDeterminarTest() {
		String msg = messageService.getMessage(mockMessagesPalabraSinDeterminar());
		assertEquals("esta no se puede determinar", msg);
	}

	@Test
	void mensajeConVariasPalabrasSinDeterminarTest() {
		String msg = messageService.getMessage(mockMessagesVariasPalabraSinDeterminar());
		assertEquals("esta no se determinar", msg);
	}

	@Test
	void mensajeLlegoConBasuraTest() {
		String msg = messageService.getMessage(mockMessagesLlegaronConBasura());
		assertNotEquals("este mensaje llego con basura", msg);
	}

	@Test
	void mensajeDesdeLaBDTest() {
		when(requestService.listarTodosLosMensajesDeTransmisiones()).thenReturn(mockMessagesValido());
		String message = messageService.ensamblarMensajeDeTransmisionesSplit();
		assertEquals("este es un mensaje", message);
	}

	private List<List<String>> mockMessagesValido() {
		List<List<String>> messages = new ArrayList<>();

		List<String> input1 = Arrays.asList("este", "", "", "mensaje");
		List<String> input2 = Arrays.asList("este", "es", "", "");
		List<String> input3 = Arrays.asList("", "", "un", "");

		messages.add(input1);
		messages.add(input2);
		messages.add(input3);

		return messages;
	}

	private List<List<String>> mockMessagesCantidadPalabrasInvalida() {
		List<List<String>> messages = new ArrayList<>();

		List<String> input1 = Arrays.asList("este", "", "");
		List<String> input2 = Arrays.asList("este", "es", "", "invalido");
		List<String> input3 = Arrays.asList("", "", "un", "");

		messages.add(input1);
		messages.add(input2);
		messages.add(input3);

		return messages;
	}

	private List<List<String>> mockMessagesPalabraSinDeterminar() {
		List<List<String>> messages = new ArrayList<>();

		List<String> input1 = Arrays.asList("esta", "", "no", "se", "puede", "determinar");
		List<String> input2 = Arrays.asList("esta", "", "no", "se", "puede", "determinar");
		List<String> input3 = Arrays.asList("esta", "", "no", "se", "puede", "determinar");

		messages.add(input1);
		messages.add(input2);
		messages.add(input3);

		return messages;
	}

	private List<List<String>> mockMessagesVariasPalabraSinDeterminar() {
		List<List<String>> messages = new ArrayList<>();

		List<String> input1 = Arrays.asList("esta", "", "no", "se", "", "determinar");
		List<String> input2 = Arrays.asList("esta", "", "no", "se", "", "determinar");
		List<String> input3 = Arrays.asList("esta", "", "no", "se", "", "determinar");

		messages.add(input1);
		messages.add(input2);
		messages.add(input3);

		return messages;
	}

	private List<List<String>> mockMessagesLlegaronConBasura() {
		List<List<String>> messages = new ArrayList<>();

		List<String> input1 = Arrays.asList("este", "", "", "sin", "");
		List<String> input2 = Arrays.asList("", "mensaje", "", "%f!/()", "");
		List<String> input3 = Arrays.asList("#$!", "", "", "con", "basura");

		messages.add(input1);
		messages.add(input2);
		messages.add(input3);

		return messages;
	}
}