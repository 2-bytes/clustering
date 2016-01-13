/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclusteringmodified;

/**
 *
 * @author Vitalii Umanets
 */
public class Point {
    public double[] coordinates;
    public double distance;
    public boolean assigned;

    Point(double[] coord){
        coordinates = new double[coord.length];
        System.arraycopy(coord, 0, coordinates, 0, coord.length);
        distance = 0;
        assigned = false;
    }
}
