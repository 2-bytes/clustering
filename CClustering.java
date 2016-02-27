package cclusteringmodified;

import cclusteringmodified.ui.MainForm;
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

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/**
 *
 * @author Vitalii Umanets
 */
public class CClustering {

    private static final int MAX_ITERATIONS = 10000;
    private static ArrayList<Point> points;
    private static Cluster[] clusters;
    private static int numClusters;

    public static void main(String[] args) {
        try {
            double[][] data = ExcelDataLoader.loadXLSXFile("H:\\Book2.xlsx", 0, 500, true);
            DataProcessingUtil.printData(data, 4);
            Cluster[] clusterized= kMeans(
                    DataProcessingUtil.normalize(
                    DataProcessingUtil.selectRandomVariables(data, 2, true)), 6, new int[]{110, 110, 100, 100, 100, 100});
            MainForm form = new MainForm(clusterized);
            form.setVisible(true);
        } catch (IOException | InvalidFormatException ex) {
            Logger.getLogger(CClustering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Cluster[] kMeans(double[][] data, int k, int[] inert){
       numClusters=k;
        if(inert.length<k)
           throw new IllegalArgumentException("Inert has less than k elements");
        clusters = new Cluster[k];
        double[][] centroids = initCentroids(data, k);
        for(int i=0;i<k;i++)
            clusters[i] = new Cluster(centroids[i], inert[i]);
        points = new ArrayList<>();
        int i=1;
        for(double[] p: data){
            points.add(new Point(p, String.valueOf(i)));
            i++;
        }
        while(!areAssigned(points))
            assignPoints(clusters, points);
        double[][] newCentroids = calculateCentroids(clusters);
        for(int j=0;j<numClusters;j++)
            clusters[j].setCentroid(newCentroids[j]);
        return clusters;
    }

    public static Cluster[] singleIteration(){
        for(int i=0;i<numClusters;i++)
            clusters[i].clear();
        while(!areAssigned(points))
            assignPoints(clusters, points);
        double[][] newCentroids = calculateCentroids(clusters);
        for(int j=0;j<numClusters;j++)
            clusters[j].setCentroid(newCentroids[j]);
        return clusters;
    }

    /**
     * Function which ensures that all points are assigned to clusters
     * @param points
     * @return True if assigned
     */
    private static boolean areAssigned(ArrayList<Point> points){
        return !points.stream().anyMatch((p) -> (!p.assigned));
    }

    private static boolean hasChanged(double[] previous, double[] current){
        for(int i=0;i<previous.length;i++)
                if(previous[i]!=current[i])
                    return false;
        return true;
    }

    /**
     * Initialize centroids for k-means clustering.
     * @param data points.
     * @param k number of clusters.
     * @return Centers of clusters.
     */
    private static double[][] initCentroids(double[][] data, int k){
        Random r = new Random();
        //Initialize a stream of k distinct random values and iterate through them creating an array of clusters
        IntStream ints =  r.ints(0, data.length).distinct().limit(k);
        PrimitiveIterator<Integer, IntConsumer> iterator = ints.iterator();
        double[][] result = new double[k][data[0].length];
        for(int i=0;i<k;i++){
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
                        //clusters[distances.indexOf(a)].shiftCentroid(point.coordinates);
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
