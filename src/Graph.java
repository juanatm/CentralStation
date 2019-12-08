import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.Annotation;
import org.jfree.chart.annotations.CategoryAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
public class Graph {

public static double[] getArray() throws IOException {
    File f=new File("C:\\Users\\juanb\\Documents\\temperature.txt");
    FileReader fr=new FileReader(f);
    FileReader fr1=new FileReader(f);
    BufferedReader br = new BufferedReader(fr);
    BufferedReader br1 = new BufferedReader(fr1);
    String line;
    int size=0;

    while((line=br.readLine())!=null){
        size++;
    }

    double[] data=new double[size+10];

    for(int i=0;i<size;i++){
        line=br1.readLine();
        double number=Double.parseDouble(line);
        data[i]=number;
    }
    return data;
}
    //read txt
    public static class Ventana1 extends JFrame {
    JPanel panel;
        public Ventana1() throws IOException {

            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);
            line();

        }
        private void line() throws IOException {
            panel = new JPanel();
            double data[]=Graph.getArray();
            int size=data.length-10;
            getContentPane().add(panel);
            // Fuente de Datos
            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
            int x=1;
            for (int i = 1; i < size; i++) {
                if(i%10==0){
                    line_chart_dataset.addValue(data[i], "Temperature","Day "+x);
                    x++;
                }else {

                    line_chart_dataset.addValue(data[i], "Temperature", "e"+i );
                }
            }

            // Creando el Grafico
            JFreeChart chart1 = ChartFactory.createLineChart("Raw temperature data over the examined period",
                    "Time", "Temperature", line_chart_dataset, PlotOrientation.VERTICAL,
                    true, true, false);



            CategoryPlot p = chart1.getCategoryPlot();

            for(int i=1;i<size;i=i+10) {
                final CategoryMarker start = new CategoryMarker("e" + i);
                start.setPaint(Color.red);
                start.setLabel("Day");


                start.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
                start.setLabelTextAnchor(TextAnchor.TOP_LEFT);
                chart1.getCategoryPlot().addDomainMarker(start);
            }




            // Mostrar Grafico
            ChartPanel chartPanel1 = new ChartPanel(chart1);
            panel.add(chartPanel1);





        }
    }

        public static class Ventana extends JFrame {
            JPanel panel;

            public Ventana() throws IOException {
                setTitle("Temperature per day");
                setSize(800, 600);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
                media();
            }

            private void media() throws IOException {
                double[] data = Graph.getArray();
                int size = data.length - 10;
                panel = new JPanel();
                getContentPane().add(panel);
                // Fuente de Datos
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                int days;
                if (size % 10 == 0) {
                    days = size / 10;
                } else {
                    days = (size / 10) + 1;
                }

                int c = 0;
                int cont;
                for (int i = 0; i < days; i++) {
                    double datas = 0;
                    cont =0;
                    for (int j = 0; j < 10; j++) {

                        if (data[j + c] != 0) {
                            cont++;
                            datas = datas + data[j + c];

                        }
                    }
                    datas = datas / cont;

                    c = c + 10;
                    if (datas > 40) {
                        System.err.println("Warning: RISK of high temperature in day " + (i + 1));
                    }
                    dataset.setValue(datas, "Temperature", "Day " + (i + 1));
                }
                // Creando el Grafico
                JFreeChart chart = ChartFactory.createBarChart3D
                        ("Average Temperature/ Day", "Days", "Average Temperature",
                                dataset, PlotOrientation.VERTICAL, true, true, false);
                chart.setBackgroundPaint(Color.white);
                chart.getTitle().setPaint(Color.black);
                CategoryPlot p = chart.getCategoryPlot();
                p.setRangeGridlinePaint(Color.red);
                final Marker start = new ValueMarker(40);
                start.setPaint(Color.red);
                start.setLabel("Risk temperature");
                start.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
                start.setLabelTextAnchor(TextAnchor.TOP_LEFT);
                p.addRangeMarker(start);

                // Mostrar Grafico
                ChartPanel chartPanel = new ChartPanel(chart);
                panel.add(chartPanel);



            }
        }

        public static void main(String args[]) throws IOException {
            new Ventana().setVisible(true);
            new Ventana1().setVisible(true);

        }


    }