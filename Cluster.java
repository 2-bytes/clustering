package cclusteringmodified;

import java.util.ArrayList;
import java.util.Optional;

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
    }

    public void clear(){
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
 *
 * @param point Point that will be assigned to cluster
 */
    public void addPoint(Point point){
        if(this.size<=points.size())
            addToFull(point);
        else{
            points.add(point);
            cost += point.distance;
            point.assigned = true;
        }

    }

    private void addToFull(Point point){
        Point furthest = points.stream().max(
                (Point a, Point b)->Double.compare(a.distance, b.distance)).get();
        if(point.distance<furthest.distance){
            furthest.assigned = false;
            cost -= furthest.distance;
            points.remove(furthest);
            addPoint(point);
        }
    }

}
