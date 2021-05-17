package com.meli.quasar.services;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.meli.quasar.model.PositionRequest;
import com.meli.quasar.model.Satelite;
import com.meli.quasar.model.TriangulacionRequest;
import com.meli.quasar.model.response.Position;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PositionService {

    @Autowired
    private SatelitesService satelitesService;
    @Autowired
    private RequestService requestService;
    private ModelMapper modelMapper = new ModelMapper();

    /**
     * Calcula la posicion a partir de las transmisiones guardadas de forma separada en la BD
     * @return
     */
    public Position getPosicionDeTransmisionesSplit() {
        return getPosition(requestService.listarSatelitesYDistancias());
    }

    private void validarLocationsInput(List<PositionRequest> locationsInput) {
        if (locationsInput.size() < 3) {
            throw new IllegalArgumentException("Se necesitan al menos 3 satelites para obtener la posicion.");
        }
    }

    /**
     * Devuelve la posicion de la fuente recibida por los satelites
     * @param locationsInput
     * @return the position
     */
    public Position getPosition(List<PositionRequest> locationsInput) {
        if (!locationsInput.isEmpty()) {
            validarLocationsInput(locationsInput);
            List<Satelite> satelites = satelitesService.getSatelitesFromDB();
            List<TriangulacionRequest> triangulacionList = new ArrayList<>();
            locationsInput.forEach(input -> {
                TriangulacionRequest triangulacionRequest = new TriangulacionRequest();
                Optional<PositionRequest> sateliteRequest = Optional.of(locationsInput.stream()
                        .filter(req -> req.getNombreSatelite()
                                .equalsIgnoreCase(input.getNombreSatelite())).findFirst())
                        .orElse(Optional.empty());
                Optional<Satelite> satFound = satelites.stream().filter(sat -> sat.getNombre().equalsIgnoreCase(input.getNombreSatelite())).findAny();
                if (sateliteRequest.isPresent() && satFound.isPresent()) {
                    Satelite sateliteEncontrado = modelMapper.map(satFound.get(), Satelite.class);
                    triangulacionRequest.setPosition(new Position(sateliteEncontrado.getPosX(), sateliteEncontrado.getPosY()));
                    triangulacionRequest.setDistance(sateliteRequest.get().getDistance());
                    triangulacionList.add(triangulacionRequest);
                }
            });
            return obtenerPosicionPorTresSatelitesMasCercanos(triangulacionList);
        }
        return null;
    }

    /**
     * Utiliza los 3 satelites mas cercanos para calcular la posicion de origen de la transmision
     * @param triangulacionList
     * @return la posicion de origen
     */
    private Position obtenerPosicionPorTresSatelitesMasCercanos(List<TriangulacionRequest> triangulacionList) {
        List<TriangulacionRequest> listaOrdenada = triangulacionList.stream().sorted((o1, o2) -> {
            if (o1.getDistance() < o2.getDistance()) {
                return -1;
            } else if (o1.getDistance().equals(o2.getDistance())) {
                return 0;
            } else {
                return 1;
            }
        }).collect(Collectors.toList());

        // Utilizo los 3 satelites mas cercanos
        double[][] positions = new double[3][2];
        double[] distances = new double[3];
        for (int i = 0; i < 3; i++) {
            positions[i] = new double[]{listaOrdenada.get(i).getPosition().getX(), listaOrdenada.get(i).getPosition().getY()};
            distances[i] = listaOrdenada.get(i).getDistance();
        }

        return obtenerLocalizacion(positions , distances);
    }

    /**
     * Efectua el calculo de coordenadas utilizando la solucion de lemmingapex.trilateration
     * @param positions array de posiciones
     * @param distances array de distancias
     * @return la posicion con sus coordenadas (x, y)
     */
    private Position obtenerLocalizacion(double[][] positions, double[] distances) {
        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
        Optimum optimum = solver.solve();
        double[] coordenadasCalculadas = optimum.getPoint().toArray();
        return new Position(valorRedondeado(coordenadasCalculadas[0]),
                valorRedondeado(coordenadasCalculadas[1]));
    }

    private float valorRedondeado(double value) {
        double redondeado = Math.round(value * (10)) / (double) (10);
        return (float) redondeado;
    }
}
