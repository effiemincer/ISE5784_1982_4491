package geometries;
import primitives.*;

/**
 * Represents a Triangle, a type of Polygon, defined by three points.
 */
public class Triangle extends Polygon {
    private final Point a,b,c;

    /**
     * Constructs a Triangle object with three specified points.
     *
     * @param a The first point of the triangle.
     * @param b The second point of the triangle.
     * @param c The third point of the triangle.
     */
    public Triangle(Point a, Point b, Point c) {
        super(a, b, c);
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getArea(){
        //create vectors, find cross product, divide by 2
        Vector ab = b.subtract(a);
        Vector ac = c.subtract(a);

        Vector cross = ab.crossProduct(ac);

        return cross.length() * 0.5;
    }
}


