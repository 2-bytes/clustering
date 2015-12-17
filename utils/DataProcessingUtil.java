/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclusteringmodified.utils;

import java.util.ArrayList;
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
            for(int j=0;j<data.length;j++)
            {
                min=Math.min(data[j][i], min);
                max=Math.max(data[j][i], max);
            }
            for(int j=0;j<data.length;j++)
            {
                data[j][i]=(data[j][i]-min)/(max-min);
            }
        }
        return data;
    }
    public static void printData(double[][] data, int precission)
    {
        StringBuilder spaces = new StringBuilder();
            int maxsize=0;
            int cellsize=0;
            for(int i=0;i<data.length;i++)
                for(int j=0;j<data[0].length;j++){
                    cellsize=String.valueOf(Math.round(data[i][j])).length();
                    maxsize=maxsize<cellsize?cellsize:maxsize;
            }

            for(int i=0;i<maxsize;i++)
            spaces.append(' ');

            for(int i=0;i<data.length;i++){
                for(int j=0;j<data[0].length;j++){
                    cellsize=String.valueOf(Math.round(data[i][j])).length();
                    spaces.delete(maxsize, spaces.length());
                    spaces.insert(maxsize, data[i][j]<0?"":" ");
                    spaces.append("%.").append(precission).append("f ");
                    System.out.format(spaces.substring(cellsize-1), data[i][j]);
                }
                System.out.println();
            }
            System.out.println();
    }
    public static double[][] selectRandomVariables(double[][] data, int k)
    {
        //ArrayList<double[]> selected = new ArrayList<>(k);
        Random r = new Random();
        double[][] newdata = new double[data.length][k];//transpose(data);
        IntStream ints =  r.ints(0, data[0].length).distinct().limit(k);
        PrimitiveIterator<Integer, IntConsumer> iterator = ints.iterator();
        for(int i=0;i<k;i++){
            int column = iterator.next();
            for(int j=0;j<data.length;j++){
                newdata[j][i] = data[j][column];
            }
        }
        return newdata;
        //forEach((a)->selected.add(newdata[a]));
        //Double[][] boxedResult = (Double[][])selected.toArray();
        //double[][] unboxedResult = new double[k][data.length];
        //for(int i=0;i<selected.size();i++)
        //    unboxedResult[i]=selected.get(i);
        //return transpose(unboxedResult);
    }
    public static double[][] transpose(double[][] data)
    {
        double[][] transposed = new double[data[0].length][data.length];
        for(int i=0;i<data[0].length;++i)
            for(int j=0;j<data.length;++j)
                transposed[i][j]=data[j][i];
        return transposed;
    }
}
