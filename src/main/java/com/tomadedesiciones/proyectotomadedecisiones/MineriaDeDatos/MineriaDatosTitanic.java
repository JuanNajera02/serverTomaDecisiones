package com.tomadedesiciones.proyectotomadedecisiones.MineriaDeDatos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MineriaDatosTitanic {

    private double[] pesos;

    public MineriaDatosTitanic() {
        // Inicializar los pesos (incluido el sesgo)
        // Inicializar los pesos (incluido el sesgo) de manera aleatoria
        pesos = new double[6];

        pesos[0] = .10; // Clase boleto
        pesos[1] = .10; // Genero
        pesos[2] = .15; // Edad
        pesos[3] = .10; // hermanos o conyugues
        pesos[4] = .10; // padres o hijos
        pesos[5] = .5; // sesgo
    }
    public void entrenar(String archivo, int maxIteraciones, double tasaAprendizaje) {
        try {
            // Cargar datos desde el archivo CSV
            List<String[]> datos = cargarDatosDesdeCSV(archivo);

            // Crear el conjunto de datos para entrenar la regresión logística
            double[][] caracteristicas = new double[datos.size()][5];
            double[] etiquetas = new double[datos.size()];

            // Calcular la media y la desviación estándar de la edad
            double[] edades = datos.stream()
                    .mapToDouble(fila -> !fila[6].trim().isEmpty() ? Double.parseDouble(fila[6]) : 0.0)
                    .toArray();
            double mediaEdad = calcularMedia(edades);
            double desviacionEstandarEdad = calcularDesviacionEstandar(edades);

            //Si la edad esta vacia ponle 26
            for (int i = 0; i < datos.size(); i++) {
                String[] fila = datos.get(i);
                if (fila[6].trim().isEmpty()) {
                    fila[6] = "26";
                }
            }

            // Asignar valores a características y etiquetas
            for (int i = 0; i < datos.size(); i++) {
                String[] fila = datos.get(i);

                caracteristicas[i][0] = Double.parseDouble(fila[2]); // Pclass
                caracteristicas[i][1] = codificarSexo(fila[5]); // Sex
                caracteristicas[i][2] = (Double.parseDouble(fila[6]) - mediaEdad) / desviacionEstandarEdad; // Age (normalizado)
                caracteristicas[i][3] = Double.parseDouble(fila[7]); // SibSp
                caracteristicas[i][4] = Double.parseDouble(fila[8]); // Parch
                etiquetas[i] = Double.parseDouble(fila[1]); // Survived
            }

            // Resto del código...
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public double predecir(double pclass, String sexo, double edad, double sibSp, double parch) {
        // Codificar los datos de entrada
        double[] entrada = {pclass, codificarSexo(sexo), edad, sibSp, parch, .05}; // Agregar un sesgo (bias)

        // Calcular la probabilidad utilizando la función sigmoide
        double probabilidad = sigmoide(productoPunto(entrada, pesos));

        // Devolver la probabilidad
        return probabilidad;
    }

    public double[] getPesos() {
        return pesos.clone();  // Devuelve una copia para evitar modificaciones no deseadas
    }

    private double sigmoide(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private double productoPunto(double[] a, double[] b) {
        double resultado = 0.0;
        for (int i = 0; i < a.length; i++) {
            resultado += a[i] * b[i];
        }
        return resultado;
    }

    private double codificarSexo(String sexo) {
        return sexo.equalsIgnoreCase("male") ? 1.0 : 0.0;
    }

    private List<String[]> cargarDatosDesdeCSV(String archivo) throws IOException {
        List<String[]> datos = new ArrayList<>();
        String line;
        String splitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            //Leer el archivo línea por línea
            //Quitar el encabezado
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fila = line.split(splitBy);
                datos.add(fila);
            }
        }

        return datos;
    }


    public double[] explorarDatos(String archivo) {
        double[] resultado = null;

        try {
            // Cargar datos desde el archivo CSV
            List<String[]> datos = cargarDatosDesdeCSV(archivo);

            // Calcular información básica y estadísticas descriptivas
            int totalRegistros = datos.size();

            // Calcular estadísticas descriptivas para la edad
            double[] edades = datos.stream()
                    .mapToDouble(fila -> !fila[6].trim().isEmpty() ? Double.parseDouble(fila[6]) : 0.0)
                    .toArray();
            double mediaEdad = calcularMedia(edades);
            double desviacionEstandarEdad = calcularDesviacionEstandar(edades);

            // Detectar y almacenar anomalías en la edad
            List<Double> edadesAnomalas = detectarAnomalias(edades);

            // Almacenar la información en el arreglo resultado
            resultado = new double[]{totalRegistros, mediaEdad, desviacionEstandarEdad};

            // Agregar las edades anómalas al resultado
            double[] edadesAnomalasArray = edadesAnomalas.stream().mapToDouble(Double::doubleValue).toArray();
            resultado = concatArrays(resultado, edadesAnomalasArray);

        } catch (IOException e) {
            e.printStackTrace();
            // Puedes manejar la excepción de alguna manera, según tus necesidades
        }

        return resultado;
    }

    // Método para concatenar dos arreglos de double
    private double[] concatArrays(double[] arr1, double[] arr2) {
        double[] result = new double[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, result, 0, arr1.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
    }

    private double calcularMedia(double[] datos) {
        return java.util.stream.DoubleStream.of(datos).average().orElse(Double.NaN);
    }

    private double calcularDesviacionEstandar(double[] datos) {
        double media = calcularMedia(datos);
        double varianza = java.util.stream.DoubleStream.of(datos)
                .map(dato -> Math.pow(dato - media, 2))
                .average()
                .orElse(Double.NaN);
        return Math.sqrt(varianza);
    }

    private List<Double> detectarAnomalias(double[] datos) {
        double media = calcularMedia(datos);
        double desviacionEstandar = calcularDesviacionEstandar(datos);

        // Definir un umbral para las anomalías (por ejemplo, 2 desviaciones estándar)
        double umbralAnomalias = 2.0;

        // Identificar valores que están más allá del umbral
        List<Double> anomalias = new ArrayList<>();
        for (double dato : datos) {
            if (Math.abs(dato - media) > umbralAnomalias * desviacionEstandar) {
                anomalias.add(dato);
            }
        }

        return anomalias;
    }
}

