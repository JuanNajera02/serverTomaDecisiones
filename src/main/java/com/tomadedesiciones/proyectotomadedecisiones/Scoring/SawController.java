package com.tomadedesiciones.proyectotomadedecisiones.Scoring;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/Saw")
public class SawController {


    //ejemplo de input   http://localhost:8080/Saw/generarTablaSaw

//    {
//        "matriz": [
//    [0.0, 0.0, 0.0, 0.0, 0.0],
//    [4.0, 2.0, 4.0, 2.0, 2.0],
//    [2.0, 3.0, 3.0, 2.0, 1.0],
//    [2.0, 3.0, 3.0, 1.0, 2.0],
//    [4.0, 1.0, 3.0, 1.0, 1.0]
//  ],
//        "maximizarMinimizar": [true, true, true, true, true]
//    }

    @PostMapping("/generarTablaSaw")
    public ResponseEntity<Map<String, Object>> generarTablaSaw(@RequestBody Map<String, Object> request) {
        // Extraer la matriz y maximizarMinimizar del cuerpo de la solicitud
        List<List<Double>> matrizList = (List<List<Double>>) request.get("matriz").stream()
                .map(row -> row.stream().map(element -> Double.parseDouble(element.toString())).collect(Collectors.toList()))
                .collect(Collectors.toList());

        List<Boolean> maximizarMinimizarList = (List<Boolean>) request.get("maximizarMinimizar").stream()
                .map(element -> Boolean.parseBoolean(element.toString()))
                .collect(Collectors.toList());

        // Convertir la lista de listas a un array bidimensional
        double[][] matriz = new double[matrizList.size()][];
        for (int i = 0; i < matrizList.size(); i++) {
            matriz[i] = matrizList.get(i).stream().mapToDouble(Double::doubleValue).toArray();
        }

        // Convertir la lista de Boolean a un array de boolean
        boolean[] maximizarMinimizar = new boolean[maximizarMinimizarList.size()];
        for (int i = 0; i < maximizarMinimizarList.size(); i++) {
            maximizarMinimizar[i] = maximizarMinimizarList.get(i);
        }

        // Verifica que la matriz y el arreglo booleano tengan las dimensiones correctas
        if (matriz.length != 5 || matriz[0].length != 5 || maximizarMinimizar.length != 5) {
            // Manejar error de dimensiones incorrectas
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Dimensiones incorrectas de matriz o arreglo booleano");
            return ResponseEntity.badRequest().body(response);
        }

        // Crear instancia de SawAlgoritmo y establecer los datos
        SawAlgoritmo saw = new SawAlgoritmo();
        saw.setMatriz(matriz);
        saw.setMaximizarMinimizar(maximizarMinimizar);
        saw.ejecutarMetodoSAW();

        // Obtener resultados
        double[] objetivos = saw.getObjetivo();
        double[][] matrizDivisiones = saw.getNewM();

        // Crear la respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("objetivos", objetivos);
        response.put("matrizDivisiones", matrizDivisiones);

        return ResponseEntity.ok(response);
    }
}
