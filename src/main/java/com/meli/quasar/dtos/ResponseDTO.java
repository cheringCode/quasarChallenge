package com.meli.quasar.dtos;

import com.meli.quasar.model.response.Position;

public class ResponseDTO {
    private Position position;
    private String message;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
