package com.meli.quasar.services;

import com.meli.quasar.exceptions.QuasarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {

    @Autowired
    private RequestService requestService;

    public static final String CANTIDAD_DE_PALABRAS_NO_COINCIDEN = "La cantidad de palabras no coinciden entre los mensajes";
    public static final String NO_HAY_INFORMACION = "No hay suficiente informacion";
    private Map<Integer, String> messageMap;

    public MessageService() {
        this.messageMap = new HashMap<>();
    }

    /**
     * Arma el mensaje a partir de las transmisiones enviadas a cada satelite
     * @param messages
     * @return el mensaje
     */
    public String getMessage(List<List<String>> messages) {
        validarMensajes(messages);
        messages.forEach(message -> {
            for (int i = 0; i < message.size(); i++) {
                if (!(message.get(i).isEmpty())) {
                    messageMap.put(i, message.get(i));
                }
            }
        });
        return String.join(" ", messageMap.values());
    }

    /**
     * Valida que los mensajes tengan la misma cantidad de palabras
     * @param messages
     */
    private void validarMensajes(List<List<String>> messages) {
        if (messages.isEmpty()) {
            throw new QuasarException(NO_HAY_INFORMACION);
        }
        // valido la cantidad de palabras del primer mensaje vs el resto de los mensajes
        int longitud = messages.get(0).size();
        messages.forEach(msg -> {
            if (msg.size() != longitud) {
                throw new QuasarException(CANTIDAD_DE_PALABRAS_NO_COINCIDEN);
            }
        });
    }

    /**
     * Este metodo lee de la BD los mensajes enviados por partes en las transmisiones previas y los ensambla para
     * formar el mensaje final
     * @return el mensaje de las transmisiones ensamblado
     */
    public String ensamblarMensajeDeTransmisionesSplit() {
        List<List<String>> listaMensajes = requestService.listarTodosLosMensajesDeTransmisiones();
        return getMessage(listaMensajes);
    }
}
