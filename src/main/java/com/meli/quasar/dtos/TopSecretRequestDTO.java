package com.meli.quasar.dtos;

import java.util.List;

public class TopSecretRequestDTO {
    private List<RequestDTO> satellites;

    public List<RequestDTO> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<RequestDTO> satellites) {
        this.satellites = satellites;
    }
}
