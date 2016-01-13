/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclusteringmodified;

import cclusteringmodified.utils.ExcelDataLoader;
import cclusteringmodified.utils.DataProcessingUtil;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/**
 *
 * @author Vitalii Umanets
 */
public class CClustering {

    private static final int MAX_ITERATIONS = 10000;

    public static void main(String[] args) {
        try {
            double[][] data = ExcelDataLoader.loadXLSXFile("I:\\Book1.xlsx", false);
            DataProcessingUtil.printData(data, 4);
            //DataProcessingUtil.printData(
            //        DataProcessingUtil.correlationMatrix(
            //                DataProcessingUtil.normalize(data)), 2);
            Cluster[] clusterized= kMeans(DataProcessingUtil.normalize(data), 5, new int[]{6, 8, 3, 6, 7});
            BufferedImage image = new BufferedImage(201, 201, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            for(int i=1;i<=clusterized.length;i++){
                for(Point point:clusterized[i-1].getPoints()){
                    //assert(point[0]<1.0 && point[1]<1.0);
                    int shift = (255<<16)*(4 & i)+(255<<8)*(2 & i)+255*(1 & i);
                    image.setRGB((int)(200*point.coordinates[0]), 200-(int)(200*point.coordinates[1]), shift);
                }
            }
            File imag = new File("result.png");
            ImageIO.write(image, "png", imag);
        } catch (IOException | InvalidFormatException ex) {
            Logger.getLogger(CClustering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static Cluster[] kMeans(double[][] data, int k, int[] inert){
       if(inert.length<k)
           throw new IllegalArgumentException("Inert has less than k elements");
        Cluster[] clusters = new Cluster[k];
        double[][] centroids = initCentroids(data, k);
        for(int i=0;i<k;i++)
            clusters[i] = new Cluster(centroids[i], inert[i]);
        ArrayList<Point> points = new ArrayList<>();
        for(double[] p: data)
            points.add(new Point(p));
        assignPoints(clusters, points);
        double[][] newCentroids = calculateCentroids(clusters);
        for(int i=0;i<MAX_ITERATIONS;i++){
            for(int j=0;j<k;j++)
                clusters[j].setCentroid(newCentroids[j]);
            assignPoints(clusters, points);
            newCentroids = calculateCentroids(clusters);
        }
        return clusters;
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
        for(Point point:points){
            findNearest(clusters, point);
        }

    }

    private static double[][] calculateCentroids(Cluster[] clusters){
        int numClusters = clusters.length;
        int dimensions = clusters[0].getCentroid().length;
        double[][] result = new double[numClusters][dimensions];
        double[] sumVector = new double[dimensions];
        for(int i=0;i<numClusters;i++){
            for(Point p:clusters[i].getPoints())
                for(int j=0;j<dimensions;j++)
                    sumVector[j] += p.coordinates[j];
            for(int j=0;j<dimensions;j++)
                sumVector[j]/=clusters[i].getPoints().size();
            result[i]=sumVector;
        }
        return result;
    }

    private static void findNearest(Cluster[] clusters, Point point){
        double[] distances = new double[clusters.length];
        for(int i=0;i<clusters.length;i++){
            distances[i]=euclidDistance(clusters[i].getCentroid(), point.coordinates);
        }
        Arrays.sort(distances);

        for(int i=0;!point.assigned&&i<clusters.length;i++){
            point.distance = distances[i];
            clusters[i].addPoint(point);
        }
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
