package com.tomadedesiciones.proyectotomadedecisiones.Scoring;

public class SawAlgoritmo {
    private double[][] matriz;
    private boolean[] maximizarMinimizar;
    private double[] objetivo;
    private double[][] newM;

    public SawAlgoritmo() {
        matriz = new double[5][5];
        maximizarMinimizar = new boolean[5];
        objetivo = new double[5];
        newM = new double[5][5];
    }

    public void setMatriz(double[][] matriz) {
        this.matriz = matriz;
    }

    public double[][] getNewM() {
        return newM;
    }

    public void setMaximizarMinimizar(boolean[] maximizarMinimizar) {
        this.maximizarMinimizar = maximizarMinimizar;
    }

    public double[] getObjetivo() {
        return objetivo;
    }


    // Ejecuta el método SAW
    public void ejecutarMetodoSAW() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                newM[i][j] = 0;
            }
        }

        // Obtiene el objetivo
        for (int i = 0; i < 5; i++) {
            double var = 0;
            if (!maximizarMinimizar[i]) {
                var = Double.MAX_VALUE;
            } else {
                var = -Double.MAX_VALUE;
            }
            for (int j = 1; j < 5; j++) {
                if ((matriz[j][i] > var && maximizarMinimizar[i]) || (matriz[j][i] < var && !maximizarMinimizar[i])) {
                    var = matriz[j][i];
                }
            }
            objetivo[i] = var;
        }


        // Imprime la matriz con divisiones
        for (int j = 1; j < 5; j++) {
            for (int i = 0; i < 5; i++) {
                newM[j][i] = objetivo[i] / matriz[j][i];
            }
        }
    }
}

