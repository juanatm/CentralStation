import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;
import javax.swing.*;
import java.awt.*;
import java.io.*;
public class Graph {

public static double[] getArray() throws IOException { //Method that read the temperature.txt file and insert the data into an array.
    File f=new File("C:\\Users\\juanb\\Documents\\temperature.txt");
    FileReader fr=new FileReader(f);
    FileReader fr1=new FileReader(f);
    BufferedReader br = new BufferedReader(fr);
    BufferedReader br1 = new BufferedReader(fr1);
    String line;
    int size=0;

    while((line=br.readLine())!=null){ //first get the number of values
        size++;
    }

    double[] data=new double[size+24];

    for(int i=0;i<size;i++){ //Then create the array and insert it.
        line=br1.readLine();
        double number=Double.parseDouble(line);
        data[i]=number;
    }
    return data;
}
    //read txt
    public static class Ventana1 extends JFrame {
    JPanel panel;
        public Ventana1() throws IOException { //Create the window

            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);
            line();

        }
        private void line() throws IOException { //Create the raw temperature graph
            panel = new JPanel();
            double data[]=Graph.getArray();
            int size=data.length-24;
            getContentPane().add(panel);
            // Fuente de Datos
            DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
            int x=1;
            for (int i = 1; i < size; i++) { //insert the values
                if(i%24==0){
                    line_chart_dataset.addValue(data[i], "Temperature","Day "+x);
                    x++; //number of days.
                }else {

                    line_chart_dataset.addValue(data[i], "Temperature", "e"+i );
                }
            }

            // Creando el Grafico
            JFreeChart chart1 = ChartFactory.createLineChart("Raw temperature data over the examined period",
                    "Time", "Temperature", line_chart_dataset, PlotOrientation.VERTICAL,
                    true, true, false); //Create the graph



            CategoryPlot p = chart1.getCategoryPlot();

           CategoryMarker start = new CategoryMarker("e" + 1);
            start.setPaint(Color.black);
            start.setLabel("   Day "+1); //Insert a black vertical name with the day 1 number in the graph
            start.setLabelAnchor(RectangleAnchor.LEFT);

            start.setDrawAsLine(true);
        start.setLabelTextAnchor(TextAnchor.TOP_LEFT);

            chart1.getCategoryPlot().addDomainMarker(start);
            int day=2;

            for(int i=24;i<size;i=i+24) {
               start = new CategoryMarker("e" + (i+   1));
                //Insert a black vertical name with the day number in the graph
                start.setPaint(Color.black);
                start.setLabel("   Day "+day);
                day++;



               start.setLabelAnchor(RectangleAnchor.LEFT);

               start.setDrawAsLine(true);

                start.setLabelTextAnchor(TextAnchor.TOP_LEFT);
                chart1.getCategoryPlot().addDomainMarker(start);
            }




            // Show Chart
            ChartPanel chartPanel1 = new ChartPanel(chart1);
            panel.add(chartPanel1);





        }
    }

        public static class Ventana extends JFrame {
            JPanel panel;

            public Ventana() throws IOException { //Create the window
                setTitle("Temperature per day");
                setSize(800, 600);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
                media();
            }

            private void media() throws IOException {
                double[] data = Graph.getArray();
                int size = data.length - 24;
                panel = new JPanel();
                getContentPane().add(panel);
                // Fuente de Datos
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                int days; //We need to know the number of days
                if (size % 24 == 0) { //If the division is exact, the number of days is just size/24
                    days = size / 24;
                } else { //If not we have to +1.
                    days = (size / 24) + 1;
                }

                int c = 0;
                int cont;
                for (int i = 0; i < days; i++) { //We iterate in the days
                    double datas = 0;
                    cont =0;
                    for (int j = 0; j < 24; j++) { //And we put the values for the chart

                        if (data[j + c] != 0) { //If is the last day, and it's not exact, we have to skip the empty days.
                            cont++;
                            datas = datas + data[j + c];

                        }
                    }
                    datas = datas / cont;

                    c = c + 24  ;
                    if (datas > 40) { //The warning if the temperature is >40
                        System.err.println("Warning: RISK of high temperature in day " + (i + 1));
                    }
                    dataset.setValue(datas, "Temperature", "Day " + (i + 1)); //We insert the day data value
                }
                // Creating the chart
                JFreeChart chart = ChartFactory.createBarChart3D
                        ("Average Temperature/ Day", "Days", "Average Temperature",
                                dataset, PlotOrientation.VERTICAL, true, true, false);
                chart.setBackgroundPaint(Color.white);
                chart.getTitle().setPaint(Color.black);
                CategoryPlot p = chart.getCategoryPlot();
                p.setRangeGridlinePaint(Color.red);
                final Marker start = new ValueMarker(40); //The Risk temperature line
                start.setPaint(Color.red);
                start.setLabel("Risk temperature");
                start.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
                start.setLabelTextAnchor(TextAnchor.TOP_LEFT);
                p.addRangeMarker(start); //We plot the risk temperature line

                // Show the chart
                ChartPanel chartPanel = new ChartPanel(chart);
                panel.add(chartPanel);



            }
        }

        public static void main(String args[]) throws IOException {
    //Make visible the 2 windows.
            new Ventana().setVisible(true);
            new Ventana1().setVisible(true);

        }


    }