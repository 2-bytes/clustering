/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclusteringmodified;

import java.util.ArrayList;

/**
 *
 * @author Vitalii Umanets
 */
public class Cluster {
    private double cost;
    private final int size;
    private double[] centroid;
    private ArrayList<Point> points;

    Cluster(double[] centroid, int size){
        points = new ArrayList<>();
        this.centroid = new double[centroid.length];
        System.arraycopy(centroid, 0, this.centroid, 0, centroid.length);
        this.size = size;
        cost = 0;
    }

    Cluster(double[] centroid){
        points = new ArrayList<>();
        this.centroid = new double[centroid.length];
        System.arraycopy(centroid, 0, this.centroid, 0, centroid.length);
        this.size = 0;
        cost = 0;
    }

    public void setCentroid(double[] centr){
        System.arraycopy(centr, 0, this.centroid, 0, centr.length);
        cost = 0;
        for(Point point:points)
            point.assigned = false;
        points.removeAll(points);
    }

    public double[] getCentroid(){
        return centroid;
    }

    public ArrayList<Point> getPoints(){
        return points;
    }

    public int getSize(){
        return size;
    }

    public double getCost(){
        return cost;
    }
/**
 * Tries to assign a point to cluster.
 * If there is no more place in this cluster function tries to remove the furthest point and add given instead;
 *
 * @param point Point that will be assigned to cluster
 */
    public void addPoint(Point point){
        if(this.size<=points.size())
            addToFull(point);
        else{
            //Ensures that the furthest element is always the last
            if(points.size()>0&&point.distance>points.get(points.size()-1).distance)
                points.add(point);
            else
                points.add(0, point);
            cost += point.distance;
            point.assigned = true;
        }

    }

    private void addToFull(Point point){
    if(point.distance<points.get(points.size()-1).distance){
            points.get(size-1).assigned = false;
            cost -= points.get(size-1).distance;
            points.remove(size-1);
            addPoint(point);
        }
    }

}
