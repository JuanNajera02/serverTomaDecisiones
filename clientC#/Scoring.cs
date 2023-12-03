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

        private void button2_Click(object sender, EventArgs e)
        {
            //ORDEN DE LOS TEXTBOX (color,diseño,dureza,precio,tamaño)
            //Mesas textbox 1 10 15 20 25
            //Sillas textbox 2 9 14 19 24
            //Base textbox 3 8 13 18 23
            //cLOSET de ruedas textbox 4 7 12 17 22
            //Buro textbox 5 6 11 16 21

            //Obtener los datos de la solicitud en un Double List de criterios x alternativas
            List<List<double>> criterios = new List<List<double>>();
            List<double> criterioColor = new List<double>();
            List<double> criterioDiseño = new List<double>();
            List<double> criterioDureza = new List<double>();
            List<double> criterioPrecio = new List<double>();
            List<double> criterioTamaño = new List<double>();

            //Criterio Color
            criterioColor.Add(Double.Parse(textBox1.Text));
            criterioColor.Add(Double.Parse(textBox2.Text));
            criterioColor.Add(Double.Parse(textBox3.Text));
            criterioColor.Add(Double.Parse(textBox4.Text));
            criterioColor.Add(Double.Parse(textBox5.Text));

            //Criterio Diseño
            criterioDiseño.Add(Double.Parse(textBox10.Text));
            criterioDiseño.Add(Double.Parse(textBox9.Text));
            criterioDiseño.Add(Double.Parse(textBox8.Text));
            criterioDiseño.Add(Double.Parse(textBox7.Text));
            criterioDiseño.Add(Double.Parse(textBox6.Text));

            //Criterio Dureza
            criterioDureza.Add(Double.Parse(textBox15.Text));
            criterioDureza.Add(Double.Parse(textBox14.Text));
            criterioDureza.Add(Double.Parse(textBox13.Text));
            criterioDureza.Add(Double.Parse(textBox12.Text));
            criterioDureza.Add(Double.Parse(textBox11.Text));

            //Criterio Precio
            criterioPrecio.Add(Double.Parse(textBox20.Text));
            criterioPrecio.Add(Double.Parse(textBox19.Text));
            criterioPrecio.Add(Double.Parse(textBox18.Text));
            criterioPrecio.Add(Double.Parse(textBox17.Text));
            criterioPrecio.Add(Double.Parse(textBox16.Text));

            //Criterio Tamaño
            criterioTamaño.Add(Double.Parse(textBox25.Text));
            criterioTamaño.Add(Double.Parse(textBox24.Text));
            criterioTamaño.Add(Double.Parse(textBox23.Text));
            criterioTamaño.Add(Double.Parse(textBox22.Text));
            criterioTamaño.Add(Double.Parse(textBox21.Text));

            criterios.Add(criterioColor);
            criterios.Add(criterioDiseño);
            criterios.Add(criterioDureza);
            criterios.Add(criterioPrecio);
            criterios.Add(criterioTamaño);

            //Lista de minimizar o maximizar segun los checkbox seleccionados
            List<bool> minimizar = new List<bool>();
            minimizar.Add(checkBox1.Checked);
            minimizar.Add(checkBox2.Checked);
            minimizar.Add(checkBox3.Checked);
            minimizar.Add(checkBox4.Checked);
            minimizar.Add(checkBox5.Checked);

            /*
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
             */

            // Formar el JSON
            string url = "http://localhost:8080/Saw/generarTablaSaw";
            string json = "{";
            json += "\"matriz\":[";
            foreach (var criterio in criterios)
            {
                json += "[" + string.Join(",", criterio) + "],";
            }

            json = json.TrimEnd(',') + "],";
            json += "\"maximizarMinimizar\":[" + string.Join(",", minimizar) + "]";
            json += "}";
            MessageBox.Show(json);

            // Consumir el endpoint
            HttpClient client = new HttpClient();
            client.BaseAddress = new Uri(url);

            var content = new StringContent(json, Encoding.UTF8, "application/json");

            // Enviar el JSON
            var response = client.PostAsync(url, content).Result;

            // Obtener el JSON de respuesta
            var responseString = response.Content.ReadAsStringAsync().Result;

            // Mensaje de respuesta
            MessageBox.Show(responseString);

            // Procesar el JSON
            var dict = Newtonsoft.Json.JsonConvert.DeserializeObject<Dictionary<string, object>>(responseString);






        }
    }
}
