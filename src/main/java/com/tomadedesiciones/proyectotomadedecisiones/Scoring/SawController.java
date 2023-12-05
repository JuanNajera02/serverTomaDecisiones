package com.tomadedesiciones.proyectotomadedecisiones.Scoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Saw")
public class SawController {
    private static final Logger logger = LoggerFactory.getLogger(SawController.class);


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
        try {
            // Extraer la matriz y maximizarMinimizar del cuerpo de la solicitud
            List<List<Double>> matrizList = (List<List<Double>>) request.get("matriz");
            List<Boolean> maximizarMinimizarList = (List<Boolean>) request.get("maximizarMinimizar");

            // Convertir la lista de listas a un array bidimensional
            double[][] matriz = new double[matrizList.size()][matrizList.get(0).size()];
            for (int i = 0; i < matrizList.size(); i++) {
                for (int j = 0; j < matrizList.get(i).size(); j++) {
                    matriz[i][j] = ((Number) matrizList.get(i).get(j)).doubleValue();
                }
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
        } catch (Exception e) {
            logger.error("Error durante el procesamiento del endpoint 'generarTablaSaw'", e);
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Error en el servidor");
            return ResponseEntity.status(500).body(response);
        }
    }
}
