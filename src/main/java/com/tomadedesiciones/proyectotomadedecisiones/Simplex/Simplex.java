package com.tomadedesiciones.proyectotomadedecisiones.Simplex;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Simplex {
    // Arreglo que guardara la tabla del simplex
    private Double[][] tabla;
    //Parametros de entrada: las restricciones, la funcion objetivo y el tipo de problema
    private Double[][] restricciones;
    private Double[] funcionObjetivo;
    private String tipoProblema;
    //Parametros de salida: la solucion optima y el valor optimo
    private Double[] solucion;
    private Double valorOptimo;
    private int numVariables;
    private int numRestricciones;

    //Constructor
    public Simplex(Double[][] restricciones, Double[] funcionObjetivo, String tipoProblema) {
        this.restricciones = restricciones;
        this.funcionObjetivo = funcionObjetivo;
        this.tipoProblema = tipoProblema;

        // Inicializar solucion como un arreglo de ceros
        this.solucion = new Double[funcionObjetivo.length];
        Arrays.fill(this.solucion, 0.0);
    }


    //Construir la tabla inicial
    public void inicializarTabla() {
        numRestricciones = restricciones.length;
        numVariables = funcionObjetivo.length;

        // Crear la tabla con las dimensiones adecuadas
        tabla = new Double[numRestricciones + 1][numVariables + numRestricciones + 1];
        // Inicializar todos los elementos en 0
        for (int i = 0; i < tabla.length; i++) {
            for (int j = 0; j < tabla[i].length; j++) {
                tabla[i][j] = 0.0;
            }
        }

        // Llenar la parte de coeficientes de restricciones y RHS
        for (int i = 0; i < numRestricciones; i++) {
            for (int j = 0; j < numVariables; j++) {
                tabla[i][j] = restricciones[i][j];
            }
            tabla[i][numVariables + i] = 1.0; // Variables de holgura

            tabla[i][numVariables + numRestricciones] = restricciones[i][numVariables]; // RHS
        }

        // Llenar la fila de la función objetivo
        for (int j = 0; j < numVariables; j++) {
            tabla[numRestricciones][j] = -funcionObjetivo[j];
        }
        System.out.println("Tabla inicial: ");
        imprimirTabla();
    }

    public void imprimirTabla(){
        //Imprimir la tabla
        for (int i = 0; i < tabla.length; i++) {
            for (int j = 0; j < tabla[0].length; j++) {
                System.out.print(String.format("%.2f ",tabla[i][j]) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Añadir un método para obtener el resultado como un Map
    public Map<String, Object> obtenerResultado() {
        // Crear un mapa para almacenar la respuesta
        Map<String, Object> respuesta = new HashMap<>();

        // Crear un arreglo para almacenar los valores de las variables
        double[] valoresVariables = new double[numVariables];

        // Calcular los valores de las variables a partir de la tabla
        for (int j = 0; j < numVariables; j++) {
            boolean esVariableBasica = false;
            int filaVariableBasica = -1;

            // Buscar una columna con un 1 en una fila
            for (int i = 0; i < numRestricciones; i++) {
                if (tabla[i][j] == 1.0) {
                    if (filaVariableBasica == -1) {
                        filaVariableBasica = i;
                    } else {
                        // Si ya encontramos otra fila con 1, esta variable no es básica
                        esVariableBasica = false;
                        break;
                    }
                } else {
                    esVariableBasica = true;
                }
            }
            // Si la variable es básica, su valor es el valor en el Lado Derecho de la fila correspondiente
            if (esVariableBasica && filaVariableBasica != -1) {
                valoresVariables[j] = tabla[filaVariableBasica][tabla[0].length - 1];
            } else {
                valoresVariables[j] = 0.0; // La variable no es básica, su valor es 0
            }
        }

        // El valor óptimo de la función objetivo es el valor en la última fila y última columna de la tabla
        valorOptimo = tabla[numRestricciones][tabla[0].length - 1];

        // Almacenar la solución en el mapa
        respuesta.put("valoresVariables", Arrays.toString(valoresVariables));


        return respuesta;
    }

    // Método para obtener resultados como un Map


    // Métodos get para solución y valor óptimo
    public Double[] getSolucion() {
        return solucion;
    }

    public Double getValorOptimo() {
        return valorOptimo;
    }

    public void resolver(){
        //Aqui hara cada iteracion hasta que se cumpla la condicion de paro que es que no haya negativos en la fila de la funcion objetivo
        inicializarTabla();
        while (hayNegativos()) {
            //Obtener la columna pivote
            int columnaPivote = obtenerColumnaPivote();
            //Obtener la fila pivote
            int filaPivote = obtenerFilaPivote(columnaPivote);
            /*
            System.out.println("Columna pivote: " + columnaPivote);
            System.out.println("Fila pivote: " + filaPivote);
            System.out.println(tabla[filaPivote][columnaPivote]);
            */
            //Hacer 1 al pivote
            hacerUno(columnaPivote,filaPivote);
            //Hacer 0 a los demas valores de la columna pivote
            hacerCeros(filaPivote, columnaPivote);
            //imprimirTabla();
            imprimirTabla();
        }
        //Obtener la solucion
        obtenerSolucion();
    }
    public boolean hayNegativos(){
        //Aqui se recorre la fila de la funcion objetivo para ver si hay negativos
        for (int i = 0; i < tabla.length + 1; i++) {
            if (this.tabla[this.tabla.length-1][i] < 0){
                return true;
            }
        }
        return false;
    }

    public int obtenerColumnaPivote(){
        //Aqui se recorre la fila de la funcion objetivo para ver si hay negativos en la columna
        for (int i = 0; i < tabla[tabla.length-1].length; i++) {
            if (tabla[tabla.length-1][i] < 0){
                return i;
            }
        }
        return -1;
    }

    public int obtenerFilaPivote(int columnaPivote){
        //Aqui se recorre la columna pivote para ver cual es el menor positivo
        double[] valores = new double[tabla.length-1];
        for (int i = 0; i < tabla.length-1; i++) {
            valores[i] = tabla[i][tabla[0].length-1]/tabla[i][columnaPivote];
        }
        double menor = Double.POSITIVE_INFINITY;
        int filaPivote = -1;

        for (int i = 0; i < valores.length; i++) {
            if (valores[i] < menor && valores[i] > 0){
                menor = valores[i];
                filaPivote = i;
            }
        }
        return filaPivote;
    }

    public void hacerUno(int columnaPivote, int filaPivote){
        //Aqui se divide toda la fila pivote entre el valor del pivote
        double pivote = tabla[filaPivote][columnaPivote];
        for (int i = 0; i < tabla[0].length; i++) {
            tabla[filaPivote][i] = tabla[filaPivote][i]/pivote;
        }
    }

    private void hacerCeros(int filaPivote, int columnaPivote) {
        //Aqui se hace 0 a los demas valores de la columna pivote
        for (int i = 0; i < tabla.length; i++) {
            if (i != filaPivote){
                double valor = tabla[i][columnaPivote];
                for (int j = 0; j < tabla[0].length; j++) {
                    tabla[i][j] = tabla[i][j] - (valor*tabla[filaPivote][j]);
                }
            }
        }
    }

    public void obtenerSolucion() {
        // Crear un arreglo para almacenar los valores de las variables
        double[] valoresVariables = new double[numVariables];

        // Calcular los valores de las variables a partir de la tabla
        for (int j = 0; j < numVariables; j++) {
            boolean esVariableBasica = false;
            int filaVariableBasica = -1;

            // Buscar una columna con un 1 en una fila
            for (int i = 0; i < numRestricciones; i++) {
                if (tabla[i][j] == 1.0) {
                    if (filaVariableBasica == -1) {
                        filaVariableBasica = i;
                    } else {
                        // Si ya encontramos otra fila con 1, esta variable no es básica
                        esVariableBasica = false;
                        break;
                    }
                } else {
                    esVariableBasica = true;
                }
            }
            // Si la variable es básica, su valor es el valor en el Lado Derecho de la fila correspondiente
            if (esVariableBasica && filaVariableBasica != -1) {
                valoresVariables[j] = tabla[filaVariableBasica][tabla[0].length - 1];
            } else {
                valoresVariables[j] = 0.0; // La variable no es básica, su valor es 0
            }
        }

        // El valor óptimo de la función objetivo es el valor en la última fila y última columna de la tabla
        valorOptimo = tabla[numRestricciones][tabla[0].length - 1];

        // Imprimir la solución
        System.out.println("Solución óptima para " + tipoProblema + ":");
        System.out.println("Valores de las variables: " + Arrays.toString(valoresVariables));
        System.out.println("Valor óptimo de Z: " + valorOptimo);
    }


}
