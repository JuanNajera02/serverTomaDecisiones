using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Proyecto_Figueroa
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Simplex form = new Simplex();
            form.Show();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            Scoring scoring = new Scoring();
            scoring.Show();

        }

        private void button2_Click(object sender, EventArgs e)
        {
            MineriaDatos mineria = new MineriaDatos();
            mineria.Show();
        }
    }
}
