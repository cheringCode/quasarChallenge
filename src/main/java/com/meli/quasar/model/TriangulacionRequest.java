package com.meli.quasar.model;

import com.meli.quasar.model.response.Position;

public class TriangulacionRequest {
    private Position position;
    private Float distance;

    public TriangulacionRequest() {
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }
}
