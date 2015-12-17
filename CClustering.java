/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        } catch (IOException ex) {
            Logger.getLogger(CClustering.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(CClustering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static double[][] kMeans(double[][] data, int k)
    {
        double[][] centroids = initCentroids(data, k);
        return null;
    }
    private static double[][] initCentroids(double[][] data, int k)
    {
        //ArrayList<double[]> centroids = new ArrayList(k);
        Random r = new Random();
        IntStream ints =  r.ints(0, data.length).distinct().limit(k);
        PrimitiveIterator<Integer, IntConsumer> iterator = ints.iterator();
        //ints.forEach((a)->centroids.add(data[a]));
        double[][] result = new double[k][data[0].length];
        for(int i=0;i<k;i++){
            int row = iterator.next();
            result[i] = data[row];
        }
        //for(int i=0;i<centroids.size();i++)
        //    result[i] = centroids.get(i);
        return result;
    }
    private static double euclidDistance(double[] x, double[] y)
    {
        if(x.length!=y.length)
            throw new ArrayIndexOutOfBoundsException("x and y must have the same length");
        double sum=0;
        for (int i=0;i<x.length;i++)
            sum += Math.pow(x[i] - y[i], 2);
        return Math.sqrt(sum);
    }

}
