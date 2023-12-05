using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Proyecto_Figueroa
{
    public partial class Scoring : Form
    {
        public Scoring()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private async void button2_Click(object sender, EventArgs e)
        {
            //ORDEN DE LOS TEXTBOX (color, diseño, dureza, precio, tamaño)
            //Mesas textbox 1 10 15 20 25
            //Sillas textbox 2 9 14 19 24
            //Base textbox 3 8 13 18 23
            //Closet de ruedas textbox 4 7 12 17 22
            //Buro textbox 5 6 11 16 21

            // Obtener los datos de la solicitud en una lista de listas de dobles
            List<List<double>> criterios = new List<List<double>>();
            List<double> criterioColor = new List<double>();
            List<double> criterioDiseño = new List<double>();
            List<double> criterioDureza = new List<double>();
            List<double> criterioPrecio = new List<double>();
            List<double> criterioTamaño = new List<double>();

            // Llenar criterios
            criterioColor.Add(Double.Parse(textBox1.Text));
            criterioColor.Add(Double.Parse(textBox2.Text));
            criterioColor.Add(Double.Parse(textBox3.Text));
            criterioColor.Add(Double.Parse(textBox4.Text));
            criterioColor.Add(Double.Parse(textBox5.Text));
            criterios.Add(criterioColor);

            criterioDiseño.Add(Double.Parse(textBox10.Text));
            criterioDiseño.Add(Double.Parse(textBox9.Text));
            criterioDiseño.Add(Double.Parse(textBox8.Text));
            criterioDiseño.Add(Double.Parse(textBox7.Text));
            criterioDiseño.Add(Double.Parse(textBox6.Text));
            criterios.Add(criterioDiseño);

            criterioDureza.Add(Double.Parse(textBox15.Text));
            criterioDureza.Add(Double.Parse(textBox14.Text));
            criterioDureza.Add(Double.Parse(textBox13.Text));
            criterioDureza.Add(Double.Parse(textBox12.Text));
            criterioDureza.Add(Double.Parse(textBox11.Text));
            criterios.Add(criterioDureza);

            criterioPrecio.Add(Double.Parse(textBox20.Text));
            criterioPrecio.Add(Double.Parse(textBox19.Text));
            criterioPrecio.Add(Double.Parse(textBox18.Text));
            criterioPrecio.Add(Double.Parse(textBox17.Text));
            criterioPrecio.Add(Double.Parse(textBox16.Text));
            criterios.Add(criterioPrecio);

            criterioTamaño.Add(Double.Parse(textBox25.Text));
            criterioTamaño.Add(Double.Parse(textBox24.Text));
            criterioTamaño.Add(Double.Parse(textBox23.Text));
            criterioTamaño.Add(Double.Parse(textBox22.Text));
            criterioTamaño.Add(Double.Parse(textBox21.Text));
            criterios.Add(criterioTamaño);

            // Lista de minimizar o maximizar según los checkbox seleccionados
            List<bool> minimizar = new List<bool>();
            minimizar.Add(checkBox1.Checked);
            minimizar.Add(checkBox2.Checked);
            minimizar.Add(checkBox3.Checked);
            minimizar.Add(checkBox4.Checked);
            minimizar.Add(checkBox5.Checked);

            //Crear una lista de Strings con true o false para saber si se minimiza o maximiza
            List<String> minimizarString = new List<String>();
            foreach (var item in minimizar)
            {
                if (item == true)
                {
                    minimizarString.Add("true");
                }
                else
                {
                    minimizarString.Add("false");
                }
            }

            // Formar el JSON
            string url = "http://localhost:8080/Saw/generarTablaSaw";
            string json = "{";
            json += "\"matriz\":[";
            foreach (var criterio in criterios)
            {
                json += "[" + string.Join(",", criterio) + "],";
            }

            json = json.TrimEnd(',') + "],";
            json += "\"maximizarMinimizar\":[" + string.Join(",", minimizarString) + "]";
            json += "}";
            MessageBox.Show(json);

            // Consumir el endpoint
            HttpClient client = new HttpClient();
            client.BaseAddress = new Uri(url);

            var content = new StringContent(json, Encoding.UTF8, "application/json");

            try
            {
                // Enviar el JSON de manera asincrónica y esperar la respuesta
                var response = await client.PostAsync(url, content);

                // Verificar si la solicitud fue exitosa
                response.EnsureSuccessStatusCode();

                // Obtener el JSON de respuesta de manera asincrónica
                var responseString = await response.Content.ReadAsStringAsync();

                // Mensaje de respuesta
                MessageBox.Show(responseString);

                // Procesar el JSON
                var dict = Newtonsoft.Json.JsonConvert.DeserializeObject<Dictionary<string, object>>(responseString);

                //cargar "objetivos" en obj.text y "matrizDivisiones" en matriz.text
                var objetivos = dict["objetivos"];
                var matrizDivisiones = dict["matrizDivisiones"];

                //MessageBox.Show(objetivos.ToString());
                //MessageBox.Show(matrizDivisiones.ToString());

                // Mostrar el resultado
                obj.Text = "Objetivos: " + objetivos.ToString();
                richTextBox1.Text = "Matriz Divisiones: \n" + matrizDivisiones.ToString();
            }
            catch (HttpRequestException ex)
            {
                // Manejar cualquier error de solicitud HTTP
                MessageBox.Show($"Error de solicitud HTTP: {ex.Message}");
            }
        }

        private void checkBox7_CheckedChanged(object sender, EventArgs e)
        {

        }

        private void vScrollBar1_Scroll(object sender, ScrollEventArgs e)
        {
            

        }
    }
}
