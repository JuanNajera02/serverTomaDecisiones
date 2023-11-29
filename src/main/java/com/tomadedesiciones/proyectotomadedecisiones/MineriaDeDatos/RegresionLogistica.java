package com.tomadedesiciones.proyectotomadedecisiones.MineriaDeDatos;

public class RegresionLogistica {

    private double[][] caracteristicas;
    private double[] etiquetas;
    private double[] pesos;

    public RegresionLogistica(double[][] caracteristicas, double[] etiquetas) {
        this.caracteristicas = caracteristicas;
        this.etiquetas = etiquetas;
        this.pesos = new double[caracteristicas[0].length];
    }

    public void entrenar(int maxIteraciones, double tasaAprendizaje) {
        for (int iteracion = 0; iteracion < maxIteraciones; iteracion++) {
            double[] gradientes = calcularGradientes();

            // Actualizar los pesos utilizando el descenso de gradiente
            for (int i = 0; i < pesos.length; i++) {
                pesos[i] -= tasaAprendizaje * gradientes[i];
            }
        }
    }

    public double[] getPesos() {
        return pesos.clone();  // Devuelve una copia para evitar modificaciones no deseadas
    }

    private double[] calcularGradientes() {
        int m = caracteristicas.length; // Número de ejemplos de entrenamiento
        int n = caracteristicas[0].length; // Número de características

        double[] gradientes = new double[n];

        // Calcular el gradiente para cada peso
        for (int j = 0; j < n; j++) {
            double suma = 0.0;

            // Calcular la suma para el gradiente
            for (int i = 0; i < m; i++) {
                double h = sigmoide(productoPunto(caracteristicas[i], pesos));
                suma += (h - etiquetas[i]) * caracteristicas[i][j];
            }

            // Calcular el gradiente promedio
            gradientes[j] = suma / m;
        }

        return gradientes;
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
}

