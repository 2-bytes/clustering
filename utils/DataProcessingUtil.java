package cclusteringmodified.utils;

import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 *
 * @author Vitalii Umanets
 */
public class DataProcessingUtil {
    /**
     * Normalizes every column of the given array using formula xn=(x-min)/(max-min)
     * Attention: A column of zeros will result in NaN column as an output.
     * @param data Rectangular array which columns will be normalized.
     * @return Normalized array of the same size as input.
     */
    public static double[][] normalize (double[][] data){
        for(int i=0;i<data[0].length;i++)
        {
            double min = data[0][i];
            double max = min;
            for (double[] data1 : data) {
                min = Math.min(data1[i], min);
                max = Math.max(data1[i], max);
            }
            for (double[] data1 : data) {
                data1[i] = (data1[i] - min) / (max-min);
            }
        }
        return data;
    }
    /**
     * Displays given array with specified precision
     * @param data
     * @param precision
     */
    public static void printData(double[][] data, int precision){
        StringBuilder spaces = new StringBuilder();
        int maxsize=0;
        int cellsize;
        for (double[] data1 : data) {
            for (int j = 0; j<data[0].length; j++) {
                cellsize = String.valueOf(Math.round(data1[j])).length();
                maxsize=maxsize<cellsize?cellsize:maxsize;
            }
        }

            for(int i=0;i<maxsize;i++)
            spaces.append(' ');

        for (double[] data1 : data) {
            for (int j = 0; j<data[0].length; j++) {
                cellsize = String.valueOf(Math.round(data1[j])).length();
                spaces.delete(maxsize, spaces.length());
                spaces.insert(maxsize, data1[j] < 0 ? "" : " ");
                spaces.append("%.").append(precision).append("f ");
                System.out.format(spaces.substring(cellsize-1), data1[j]);
            }
            System.out.println();
        }
            System.out.println();
    }
    /**
     * Selects random columns from given array
     * @param data points
     * @param k number of variables to select
     * @return Array, with same number of rows as data and k columns selected from data
     */
    public static double[][] selectRandomVariables(double[][] data, int k, boolean omitFirstColumn){
        if (data[0].length<k)
            throw new ArrayIndexOutOfBoundsException("Array has less than k columns");
        if(data[0].length == k)
            return data;
        int shift = omitFirstColumn? 1: 0;
        Random r = new Random();
        double[][] newdata = new double[data.length][k];//transpose(data);
        IntStream ints =  r.ints(shift, data[0].length).distinct().limit(k);
        PrimitiveIterator<Integer, IntConsumer> iterator = ints.iterator();
        for(int i=0;i<k;i++){
            int column = iterator.next();
            for(int j=0;j<data.length;j++)
                newdata[j][i] = data[j][column];
        }
        return newdata;
    }

    public static double[][] transpose(double[][] data){
        double[][] transposed = new double[data[0].length][data.length];
        for(int i=0;i<data[0].length;++i)
            for(int j=0;j<data.length;++j)
                transposed[i][j]=data[j][i];
        return transposed;
    }

    public static double average(double[] data){
        double sum = 0;
        for(int i=0;i<data.length;i++)
            sum+=data[i];
        return sum/data.length;
    }
    public static double correlation(double[] x, double[] y){
        if(x.length!=y.length)
            throw new IllegalArgumentException("Arrays must have same size.");
        double nom =0;
        double deltaX, deltaY, sumSqX, sumSqY;
        sumSqX = 0;
        sumSqY = 0;
        double avgX=average(x);
        double avgY=average(y);

        for(int i=0;i<x.length;i++){
            deltaX = x[i]-avgX;
            deltaY = y[i]-avgY;
            nom += deltaX*deltaY;
            sumSqX += Math.pow(deltaX, 2);
            sumSqY += Math.pow(deltaY, 2);
        }
        return nom/Math.sqrt(sumSqX*sumSqY);
    }

    public static double[][] correlationMatrix(double[][] data){
        double[][] result = new double[data.length][data.length];
        /*for(int i=0;i<data.length;i++)
            for(int j=0;j<data.length;j++)
                result[i][j] = correlation(data[i], data[j]);*/
        result = multiplyMatrices(data, transpose(data));
        for (double[] result1 : result) {
            for (int j = 0; j<result.length; j++) {
                result1[j] /= result.length-1;
            }
        }
        return result;
    }
    public static double[][] multiplyMatrices(double[][] a, double[][] b){
        if(a[0].length!=b.length)
            throw new IllegalArgumentException("Icompartible matrices");
        double[][] result = new double[a.length][b[0].length];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < b[0].length; j++)
                for(int k =0;k<b.length;k++)
                    result[i][j]+= a[i][k]*b[k][j];
        return result;
    }
}
