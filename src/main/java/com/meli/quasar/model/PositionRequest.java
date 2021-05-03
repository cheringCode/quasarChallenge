package com.meli.quasar.model;

import java.util.Objects;

public class PositionRequest {
    private String nombreSatelite;
    private Float distance;

    public PositionRequest(String nombreSatelite, Float distance) {
        this.nombreSatelite = nombreSatelite;
        this.distance = distance;
    }

    public String getNombreSatelite() {
        return nombreSatelite;
    }

    public void setNombreSatelite(String nombreSatelite) {
        this.nombreSatelite = nombreSatelite;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionRequest that = (PositionRequest) o;
        return nombreSatelite.equals(that.nombreSatelite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreSatelite);
    }
}
