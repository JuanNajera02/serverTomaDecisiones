package com.tomadedesiciones.proyectotomadedecisiones.Simplex;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Simplex")
public class SimplexController {


    //ejemplo de input   http://localhost:8080/Simplex/generarSimplex

//    {
//        "restricciones": [
//    [2.0, 1.0, 1.0, 8.0],
//    [1.0, 3.0, 2.0, 6.0],
//    [3.0, 2.0, 4.0, 10.0]
//  ],
//        "funcionObjetivo": [5.0, 4.0, 3.0],
//        "tipoOptimizacion": "Max"
//    }



    @PostMapping("/generarSimplex")
    public ResponseEntity<Map<String, Object>> generarSimplex(@RequestBody Map<String, Object> request) {
        try {
            // Obtener los datos de la solicitud
            List<List<Double>> restricciones = (List<List<Double>>) request.get("restricciones");
            List<Double> funcionObjetivo = (List<Double>) request.get("funcionObjetivo");
            String tipoOptimizacion = (String) request.get("tipoOptimizacion");

            // Convertir a un formato que tu lógica pueda manejar
            Double[][] restriccionesArray = restricciones.stream()
                    .map(row -> row.toArray(new Double[0]))
                    .toArray(Double[][]::new);

            Double[] funcionObjetivoArray = funcionObjetivo.toArray(new Double[0]);


            Simplex simplex = new Simplex(restriccionesArray, funcionObjetivoArray, tipoOptimizacion);
            simplex.resolver();


            // Aquí podrías devolver los resultados como desees
            Map<String, Object> response = Map.of(
                    "valorOptimoZ", simplex.getValorOptimo(),
                    "valoresVariables", simplex.obtenerResultado().values()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error en el servidor"));
        }
    }
}
