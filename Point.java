package cclusteringmodified;

/**
 *
 * @author Vitalii Umanets
 */
public class Point {
    public double[] coordinates;
    public double distance;
    public boolean assigned;
    public final String tag;
    Point(double[] coord, String tag){
        this.tag = tag;
        coordinates = new double[coord.length];
        System.arraycopy(coord, 0, coordinates, 0, coord.length);
        distance = 0;
        assigned = false;
    }
}
