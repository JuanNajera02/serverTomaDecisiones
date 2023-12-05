package com.tomadedesiciones.proyectotomadedecisiones.MineriaDeDatos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/Mineria")
public class MineriaDeDatosController {


    //ejemplo de input  http://localhost:8080/Mineria/generarMineriaDeDatos

  //  {
  //      "pclass": 3.0,
  //          "sexo": "male",
  //          "edad": 30,
  //          "sibSp": 1.0,
  //          "parch": 2.0
  //  }



    @PostMapping("/generarMineriaDeDatos")
        public ResponseEntity<Map<String, Object>> generarMineriaDeDatos(@RequestBody Map<String, Object> request) {
            try {
                // Recuperar los parámetros desde el cuerpo de la solicitud
                double pclass = ((Number) request.get("pclass")).doubleValue();
                String sexo = (String) request.get("sexo");
                double edad = ((Number) request.get("edad")).doubleValue();
                double sibSp = ((Number) request.get("sibSp")).doubleValue();
                double parch = ((Number) request.get("parch")).doubleValue();

                // Especificar el archivo CSV para la exploración de datos
                String archivoCSV = "src/main/java/com/tomadedesiciones/proyectotomadedecisiones/MineriaDeDatos/Titanic.csv";

                // Crear una instancia de MineriaDatosTitanic para cargar los datos
                MineriaDatosTitanic mineriaDatosTitanic = new MineriaDatosTitanic();
                double[] resultadosExploracion = mineriaDatosTitanic.explorarDatos(archivoCSV);

                // Especificar otros parámetros para el entrenamiento del modelo
                int maxIteraciones = 100000;
                double tasaAprendizaje = 0.70;

                // Entrenar el modelo
                mineriaDatosTitanic.entrenar(archivoCSV, maxIteraciones, tasaAprendizaje);

                // Realizar predicciones de ejemplo
                double probabilidadSobrevivir = mineriaDatosTitanic.predecir(pclass, sexo, edad, sibSp, parch);

                // Crear la respuesta
                Map<String, Object> response = Map.of(
                        "resultadosExploracion", resultadosExploracion,
                        "probabilidadSobrevivir", probabilidadSobrevivir
                );

                return ResponseEntity.ok(response);
            } catch (Exception e) {
                e.printStackTrace();
                // Manejar cualquier excepción y devolver un error si es necesario
                return ResponseEntity.status(500).body(Map.of("error", "Error en el servidor"));
            }
        }
    }

