package cclusteringmodified;

import java.util.ArrayList;

/**
 *
 * @author Vitalii Umanets
 */
public class Cluster {
    private double cost;
    private int mass;
    private int counter;
    private int forgotten;
    private double trace;
    private final double[] centroid;
    private final double[] cummul; //sum of coordinates
    private final ArrayList<Point> points;

    Cluster(double[] centroid, int mass, int forgotten){
        points = new ArrayList<>();
        this.centroid = new double[centroid.length];
        this.cummul = new double[centroid.length];
        System.arraycopy(centroid, 0, this.centroid, 0, centroid.length);
        System.arraycopy(centroid, 0, this.cummul, 0, centroid.length);//Ensuring that center will not jump to the nearest point right away.
        counter = 1;
        this.mass = mass;
        this.forgotten = forgotten;
        cost = 0;
        trace = 0;
    }

    public void setMass(int mass){
        this.mass = mass;
    }

    public int getMass(){
        return mass;
    }

    public double getTrace(){
        return trace;
    }

    public void setCentroid(double[] centr){
        System.arraycopy(centr, 0, this.centroid, 0, centr.length);
    }

    public void setForgottenAmount(int forgotten){
        this.forgotten = forgotten;
    }

    public int getForgottenAmount(){
        return forgotten;
    }
    public void clear(){
        cost = 0;
        trace = 0;
        points.stream().forEach((point) -> {
            point.assigned = false;
        });
        System.arraycopy(centroid, 0, this.cummul, 0, centroid.length);
        counter=1;
        points.removeAll(points);
    }

    public double[] getCentroid(){
        return centroid;
    }

    public ArrayList<Point> getPoints(){
        return points;
    }

    public int getSize(){
        return mass;
    }

    public double getCost(){
        return cost;
    }
/**
 * Assigns a point to cluster.
 *
 * @param point Point that will be assigned to cluster
 */
    public void addPoint(Point point){
        if(mass > counter){
            points.add(point);
            counter++;
            cost += point.distance;
            point.assigned = true;
            for(int i=0;i < cummul.length;i++)
                this.cummul[i] += point.coordinates[i];
        }
        else{
            points.add(point);
            cost += point.distance;
            point.assigned = true;
            for(int i=0;i<cummul.length;i++)
                this.cummul[i] += point.coordinates[i] - forgotten*this.cummul[i] / mass;
            counter -= forgotten;
        }
        shiftCentroid();

    }
    public void shiftCentroid(){
        for(int i=0;i<centroid.length;i++){
            double temp = cummul[i]/counter;
            trace += Math.pow(temp - centroid[i], 2);
            centroid[i] = temp;
        }
        trace = Math.sqrt(trace);
    }
}
