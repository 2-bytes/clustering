package cclusteringmodified;

import cclusteringmodified.utils.ExcelDataLoader;
import cclusteringmodified.utils.DataProcessingUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import flanagan.analysis.PCA;
import cclusteringmodified.ui.MainForm;
import java.util.Arrays;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/**
 *
 * @author Vitalii Umanets
 */
public class CClustering {

    //private static final int MAX_ITERATIONS = 10000;
    private static ArrayList<Point> points;
    private static Cluster[] clusters;
    private static int numClusters;

    public static void main(String[] args) {
        try {
            double[][] data = DataProcessingUtil.normalize(
                    DataProcessingUtil.selectSpecificVariables(
                            ExcelDataLoader.loadXLSXFile("I:\\seeds_dataset.xlsx", 0, 140, false), new int[]{0, 1, 2, 3, 4, 5, 6}));
            DataProcessingUtil.printData(data, 3);
            PCA pca = new PCA();
            pca.enterScoresAsRowPerItem(data);
            double[] eigenvalues = pca.orderedEigenValues();
            System.out.println(Arrays.toString(eigenvalues));
            //int numClusters =0;
            for(int i=0;i<eigenvalues.length;i++)
            {
                numClusters+=(eigenvalues[i]>1)?1:0;
            }
            Cluster[] clusterized= kMeans(
                    data, 20, 1);
            MainForm form = new MainForm(clusterized);
            form.setVisible(true);
        } catch (IOException | InvalidFormatException ex) {
            Logger.getLogger(CClustering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Cluster[] kMeans(double[][] data, int inert, int forgotten){
        //numClusters=k;
        clusters = new Cluster[numClusters];
        double[][] centroids = initCentroids(data);
        for(int i=0;i<numClusters;i++)
            clusters[i] = new Cluster(centroids[i], inert, forgotten);
        points = new ArrayList<>();
        for(int i=0;i<data.length;i++)
            points.add(new Point(data[i], String.valueOf(i+1)));
        while(!areAssigned(points))
            assignPoints(clusters, points);
        //double[][] newCentroids = calculateCentroids(clusters);
        //for(int j=0;j<numClusters;j++)
        //    clusters[j].setCentroid(newCentroids[j]);
        return clusters;
    }

    private static void mergeClusters(){
        //TODO
    }

    public static Cluster[] singleIteration(){
        for(int i=0;i<numClusters;i++)
            clusters[i].clear();
        while(!areAssigned(points))
            assignPoints(clusters, points);
        //double[][] newCentroids = calculateCentroids(clusters);
        //for(int j=0;j<numClusters;j++)
        //    clusters[j].setCentroid(newCentroids[j]);

       //DEBUG INFO OUTPUT
        for (Cluster cluster : clusters) {
            System.out.println(Arrays.toString(cluster.getCentroid())+" "+cluster.getPoints().size()+
                    " "+cluster.getTrace());
        }
        System.out.println();

        return clusters;
    }
    private static void mergeNearest(){
        
    }
    /**
     * Function which ensures that all points are assigned to clusters
     * @param points
     * @return True if assigned
     */
    private static boolean areAssigned(ArrayList<Point> points){
        return !points.stream().anyMatch((p) -> (!p.assigned));
    }

    /**
     * Initialize centroids for k-means clustering.
     * @param data points.
     * @param k number of clusters.
     * @return Centers of clusters.
     */
    private static double[][] initCentroids(double[][] data){
        Random r = new Random();
        //Initialize a stream of k distinct random values and iterate through them creating an array of clusters
        IntStream ints =  r.ints(0, data.length).distinct().limit(numClusters);
        PrimitiveIterator<Integer, IntConsumer> iterator = ints.iterator();
        double[][] result = new double[numClusters][data[0].length];
        for(int i=0;i<numClusters;i++){
            int row = iterator.next();
            result[i] = data[row];
        }
        return result;
    }

    /**
     * Assign each pint to the nearest cluster.
     * @param clusters  clusters to assign points to.
     * @param points points to assign.
     */
    private static void assignPoints(Cluster[] clusters, ArrayList<Point> points){
        points.stream().forEach((point) -> {
            findNearest(clusters, point);
        });

    }

    private static double[][] calculateCentroids(Cluster[] clusters){
        int dimensions = clusters[0].getCentroid().length;
        double[][] result = new double[numClusters][dimensions];
        double[] sumVector = new double[dimensions];
        for(int i=0;i<numClusters;i++){
            for(Point p:clusters[i].getPoints())
                for(int j=0;j<dimensions;j++)
                    sumVector[j] += p.coordinates[j];
            for(int j=0;j<dimensions;j++)
                sumVector[j]/=clusters[i].getPoints().size();
            System.arraycopy(sumVector, 0, result[i], 0, dimensions);
        }
        return result;
    }

    private static void findNearest(Cluster[] clusters, Point point){
        if(point.assigned)
            return;
        ArrayList<Double> distances = new ArrayList<>(numClusters);
        for (Cluster cluster : clusters) {
            distances.add(euclidDistance(cluster.getCentroid(), point.coordinates));
        }
        distances.stream().sorted((Double a, Double b) -> Double.compare(a, b)).
                forEachOrdered((a)->{
                    if(!point.assigned){
                        point.distance = a;
                        clusters[distances.indexOf(a)].addPoint(point);
                    }
                });
    }

    private static double euclidDistance(double[] x, double[] y){
        if(x.length!=y.length)
            throw new ArrayIndexOutOfBoundsException("x and y must have the same length");
        double sum=0;
        for (int i=0;i<x.length;i++)
            sum += Math.pow(x[i] - y[i], 2);
        return Math.sqrt(sum);
    }

}
