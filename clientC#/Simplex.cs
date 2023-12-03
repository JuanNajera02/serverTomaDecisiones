using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
//consumir endpoint
using System.Net.Http;
using System.Net.Http.Headers;
using Newtonsoft.Json;

namespace Proyecto_Figueroa
{
    public partial class Simplex : Form
    {
        public Simplex()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {// Obtener los datos de la solicitud

            if (!validarCampos())
            {
                MessageBox.Show("Ingrese todos los valores");
                return;
            }

            List<List<double>> restricciones = new List<List<double>>();
            List<double> funcionObjetivo = new List<double>();

            // Funcion Objetivo
            funcionObjetivo.Add(Double.Parse(textBox1.Text));
            funcionObjetivo.Add(Double.Parse(textBox2.Text));
            funcionObjetivo.Add(Double.Parse(textBox3.Text));

            // Restricciones (Horas en cada maquina)
            List<double> restriccionSierra = new List<double>();
            restriccionSierra.Add(Double.Parse(textBox4.Text));
            restriccionSierra.Add(Double.Parse(textBox7.Text));
            restriccionSierra.Add(Double.Parse(textBox10.Text));
            restriccionSierra.Add(Double.Parse(textBox13.Text)); // Disponible Sierra
            restricciones.Add(restriccionSierra);

            List<double> restriccionRouter = new List<double>();
            restriccionRouter.Add(Double.Parse(textBox5.Text));
            restriccionRouter.Add(Double.Parse(textBox8.Text));
            restriccionRouter.Add(Double.Parse(textBox11.Text));
            restriccionRouter.Add(Double.Parse(textBox14.Text)); // Disponible Router
            restricciones.Add(restriccionRouter);

            List<double> restriccionLijadora = new List<double>();
            restriccionLijadora.Add(Double.Parse(textBox6.Text));
            restriccionLijadora.Add(Double.Parse(textBox9.Text));
            restriccionLijadora.Add(Double.Parse(textBox12.Text));
            restriccionLijadora.Add(Double.Parse(textBox15.Text)); // Disponible Lijadora
            restricciones.Add(restriccionLijadora);

            string tipoOptimizacion = "Max"; // O establece esto de acuerdo a tu lógica

            // Formar el JSON
            string url = "http://localhost:8080/Simplex/generarSimplex";
            string json = "{";
            json += "\"restricciones\":[";

            foreach (var restriccion in restricciones)
            {
                json += "[" + string.Join(",", restriccion) + "],";
            }

            json = json.TrimEnd(',') + "],";
            json += "\"funcionObjetivo\":[" + string.Join(",", funcionObjetivo) + "],";
            json += "\"tipoOptimizacion\":\"" + tipoOptimizacion + "\"";
            json += "}";

            MessageBox.Show(json);

            // Consumir el endpoint
            HttpClient client = new HttpClient();
            client.BaseAddress = new Uri(url);
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

            // Enviar el JSON
            var content = new StringContent(json, Encoding.UTF8, "application/json");
            HttpResponseMessage response = client.PostAsync(url, content).Result;

            // Recibir el JSON
            var result = response.Content.ReadAsStringAsync().Result;
            //MessageBox.Show(result);

            //Label 15 19 y 20 para las variables y 21 para el valor de z
            /*
             *       
   
                    "valorOptimoZ", simplex.getValorOptimo(),
                    "valoresVariables", simplex.obtenerResultado().values()
            );
            */

            //Procesar el JSON


            var dict = Newtonsoft.Json.JsonConvert.DeserializeObject<Dictionary<string, object>>(result);
            var valorOptimoZ = dict["valorOptimoZ"];
            var valoresVariables = dict["valoresVariables"];

            // Mostrar el resultado
            label21.Text = valorOptimoZ.ToString();

            //separar los valores de las variables y mostrarlos en los labels a 2 decimale , separarlo por comas, dejar solo numeros y quitar los corchetes
            string[] valoresVariablesArray = valoresVariables.ToString().Split(',');
            //quitar corchetes
            valoresVariablesArray[0] = valoresVariablesArray[0].Substring(1);
            valoresVariablesArray[valoresVariablesArray.Length - 1] = valoresVariablesArray[valoresVariablesArray.Length - 1].Substring(0, valoresVariablesArray[valoresVariablesArray.Length - 1].Length - 1);

            //mostrar en labels
            label15.Text = valoresVariablesArray[0];
            label19.Text = valoresVariablesArray[1];
            label20.Text = valoresVariablesArray[2];



            String mensaje = "El valor optimo de Z es: " + valorOptimoZ.ToString() + "\n";
            mensaje += "Los valores de las variables son: " + valoresVariables.ToString();

            MessageBox.Show(mensaje);


        }

        private void button3_Click(object sender, EventArgs e)
        {



        }

        private void label14_Click(object sender, EventArgs e)
        {

        }

        private Boolean validarCampos()
        {
            if (textBox1.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X1");
                return false;
            }
            if (textBox2.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X2");
                return false;
            }
            if (textBox3.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X3");
                return false;
            }
            if (textBox4.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X4");
                return false;
            }
            if (textBox5.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X5");
                return false;
            }
            if (textBox6.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X6");
                return false;
            }
            if (textBox7.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X7");
                return false;
            }
            if (textBox8.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X8");
                return false;
            }
            if (textBox9.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X9");
                return false;
            }
            if (textBox10.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X10");
                return false;
            }
            if (textBox11.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X11");
                return false;
            }
            if (textBox12.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X12");
                return false;
            }
            if (textBox13.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X13");
                return false;
            }
            if (textBox14.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X14");
                return false;
            }
            if (textBox15.Text == "")
            {
                MessageBox.Show("Ingrese el valor de X15");
                return false;
            }
            return true;
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }
    }
}
