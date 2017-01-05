package clusteringassignment4;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.gui.explorer.ClustererPanel;
import weka.gui.visualize.VisualizePanel;
import weka.gui.visualize.PlotData2D;

/*We have referred to the following websites:
 *http://weka.wikispaces.com/Visualizing+cluster+assignments
 * http://weka.wikispaces.com/Use+Weka+in+your+Java+code
 */

/**
* Class to 
*/
public class ClusteringAssignment4 
{   
    public static void main(String[] args) throws Exception 
    {
        //Variables to store file names
        String MaximumTempPerYear = "MaxTempPerYear.csv";
        String MinimumTempPerYear = "MinimumTempPerYear.csv";
        String MaximumRainPerYear = "MaxRainPerYear.csv";
        String MaximumWindPerYear = "MaxWindPerYear.csv";
        
        //Variables to store Arrf file names
        String MaximumTempPerYearArffFile = "MaxTempPerYear.arff";
        String MinimumTempPerYearArffFile = "MinimumTempPerYear.arff";
        String MaximumRainPerYearArffFile = "MaxRainPerYear.arff";
        String MaximumWindPerYearArffFile = "MaxWindPerYear.arff";
        
        //Just replace the values with the data for which you need the
        //Machine learning output or predictions for
        String DataToAnalyse = MaximumWindPerYear;
        String ArffFileOfDataToanalyse = MaximumWindPerYearArffFile;
        
        int NumberOfCluster = 4;
        
        try
        {
            //Load CSV
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File(".\\data\\"+DataToAnalyse));
            Instances data = loader.getDataSet();

            //Convert the CSV to ARFF format
            ArffSaver saver = new ArffSaver();
            saver.setInstances(data);
            saver.setFile(new File(".\\data\\"+ArffFileOfDataToanalyse));
            saver.writeBatch();

            //Reading the Arff file     
            BufferedReader reader = new BufferedReader(
                    new FileReader(".\\data\\"+ArffFileOfDataToanalyse));

            Instances structure = new Instances(reader);

            //Using the Simple K-means algorithm
            SimpleKMeans simpleKMeans = new SimpleKMeans();
            simpleKMeans.setNumClusters(NumberOfCluster);
            simpleKMeans.buildClusterer(structure);

            //Evaluating the cluster
            ClusterEvaluation eval = new ClusterEvaluation();
            eval.setClusterer(simpleKMeans);
            eval.evaluateClusterer(structure);
            System.out.println(eval.clusterResultsToString());  

            //Plotting the data on 2-D plot
            PlotData2D plotData = 
                    ClustererPanel.setUpVisualizableInstances(structure, eval);

            String cname = simpleKMeans.getClass().getName();
            if (cname.startsWith("weka.clusterers."))
              cname += cname.substring("weka.clusterers.".length());

            //Visualizing data
            VisualizePanel vp = new VisualizePanel();
            vp.setName(cname + " (" + structure.relationName() + ")");
            plotData.setPlotName(cname + " (" + structure.relationName() + ")");
            vp.addPlot(plotData);
            
            //Using Java Swing to show the plots in a frame
            String plotName = vp.getName();
            final javax.swing.JFrame jf = 
              new javax.swing.JFrame("Weka Clusterer Visualize: " + plotName);
            jf.setSize(600,500);
            jf.getContentPane().setLayout(new BorderLayout());
            jf.getContentPane().add(vp, BorderLayout.CENTER);
            jf.addWindowListener(new java.awt.event.WindowAdapter() 
            {
              @Override
              public void windowClosing(java.awt.event.WindowEvent e) 
              {
                jf.dispose();
              }
            });
            jf.setVisible(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
