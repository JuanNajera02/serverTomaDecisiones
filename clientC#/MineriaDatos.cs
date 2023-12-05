using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

using System.Net.Http;
using System.Net.Http.Headers;
using Newtonsoft.Json;


namespace Proyecto_Figueroa
{
    public partial class MineriaDatos : Form
    {
        public MineriaDatos()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void label4_Click(object sender, EventArgs e)
        {

        }

        private void button2_Click(object sender, EventArgs e)
        {
            Double claseBoletos = Double.Parse(comboBox1.Text);
            String sexo = comboBox2.Text;
            Double edad = Double.Parse(textBox1.Text);
            Double HermanosConyuge = Double.Parse(textBox2.Text);
            Double padresHijos = Double.Parse(textBox3.Text);

            //
            //ejemplo de input  http://localhost:8080/Mineria/generarMineriaDeDatos

            //  {
            //      "pclass": 3.0,
            //          "sexo": "male",
            //          "edad": 30,
            //          "sibSp": 1.0,
            //          "parch": 2.0
            //  }


            //Crear json
            var json = JsonConvert.SerializeObject(new
            {
                pclass = claseBoletos,
                sexo = sexo,
                edad = edad,
                sibSp = HermanosConyuge,
                parch = padresHijos
            });

            MessageBox.Show(json);

            //Consumir la api
            HttpClient client = new HttpClient();
            client.BaseAddress = new Uri("http://localhost:8080/Mineria/generarMineriaDeDatos");
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            var response = client.PostAsync(client.BaseAddress, new StringContent(json, Encoding.UTF8, "application/json")).Result;
            var result = response.Content.ReadAsStringAsync().Result;

            //Procesar el json
            var dict = Newtonsoft.Json.JsonConvert.DeserializeObject<Dictionary<string, object>>(result);

            MessageBox.Show(result.ToString());
            //imprimir probabilidadSobrevivir x 100 solo 2 decimales en el label 9

            label9.Text = (Double.Parse(dict["probabilidadSobrevivir"].ToString()) * 100).ToString("0.00") + "%";
            label11.Text = dict["resultadosExploracion"].ToString();

        }
    }
}
