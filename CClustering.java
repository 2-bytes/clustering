/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclusteringmodified;

import cclusteringmodified.utils.ExcelDataLoader;
import cclusteringmodified.utils.DataProcessingUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    public static void main(String[] args) {
        try {
            double[][] data = ExcelDataLoader.loadXLSXFile("I:\\Book1.xlsx", false);
            DataProcessingUtil.printData(data, 4);
            ArrayList<double[]>[] clusterized= kMeans(DataProcessingUtil.normalize(data), 4);
            BufferedImage image = new BufferedImage(101, 101, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            for(int i=0;i<clusterized.length;i++){
                for(double[] point:clusterized[i]){
                    assert(point[0]<1.0 && point[1]<1.0);
                    int shift = (255<<16)*(4 & ~i)+(255<<8)*(2 & ~i)+255*(1 & ~i);
                    image.setRGB((int)(100*point[0]), 100-(int)(100*point[1]), shift);
                }
            }
            File imag = new File("result.png");
            ImageIO.write(image, "png", imag);
        } catch (IOException ex) {
            Logger.getLogger(CClustering.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(CClustering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static ArrayList<double[]>[] kMeans(double[][] data, int k){
        double[][] centroids = initCentroids(data, k);
        double[][] newCentroids = new double[k][data[0].length];
        ArrayList<double[]>[] assigned = assignPoints(centroids, data);
        newCentroids = calculateCentroids(assigned);
        while(!hasChanged(centroids, newCentroids))
        {
            centroids = newCentroids;
            assigned = assignPoints(centroids, data);
            newCentroids = calculateCentroids(assigned);
        }

        return assigned;
    }

    private static boolean hasChanged(double[][] previous, double[][] current){
        for(int i=0;i<previous.length;i++){
            for(int j=0;j<previous[0].length;j++)
                if(previous[i][j]!=current[i][j])
                    return false;
        }
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
     * @param centroids  centers of clusters.
     * @param points points to assign.
     * @return An array with length centroids.length of ArrayLists which have indexes of assigned points.
     */
    private static ArrayList<double[]>[] assignPoints(double[][] centroids, double[][] points){
        ArrayList<double[]>[] assigned = new ArrayList[centroids.length];
        for(int i=0;i<centroids.length;i++)
            assigned[i] = new ArrayList<>();
        for(int i=0;i<points.length;i++){
            assigned[findNearest(centroids, points[i])].add(points[i]);
        }
        return assigned;
    }

    private static double[][] calculateCentroids(ArrayList<double[]>[] assignedPoints){
        int clusters = assignedPoints.length;
        int dimensions = assignedPoints[0].get(0).length;
        double[][] result = new double[clusters][dimensions];
        double[] sumVector;
        for(int i=0;i<clusters;i++){
            sumVector = assignedPoints[i].parallelStream()
                    .reduce((a, b)->{
                        double[] res = new double[a.length];
                        for(int p=0;p<a.length;p++)
                            res[p] = a[p]+b[p];
                        return res;
                        }).get();
            for (int j=0;j<dimensions;j++){
                sumVector[j]/= assignedPoints[i].size();
            }
            result[i] = sumVector;
        }
        return result;
    }

    private static int findNearest(double[][] clusters, double[] point){
        double nearest = euclidDistance(point, clusters[0]); //distance
        int nearestIndex = 0; //index
        double distance = 0;
        for(int i=0;i<clusters.length;i++){
            distance = euclidDistance(clusters[i], point);
            nearestIndex = nearest < distance ? nearestIndex:i;
            nearest = nearest < distance ? nearest:distance;
        }
        return nearestIndex;
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
