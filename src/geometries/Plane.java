package geometries;

import jdk.jshell.spi.ExecutionControl;
import primitives.*;

import java.util.List;

/**
 * Represents a plane in a three-dimensional (3D) space, defined by a point on the plane (q) and a normal vector.
 * The plane implements the Geometry interface.
 */
public class Plane implements Geometry {
    private final Point q; // A point on the plane
    private final Vector normal; // The normal vector to the plane

    /**
     * Constructs a Plane object defined by three points (a, b, c) on the plane.
     * Calculates the normal to the plane using the given points.
     *
     * @param a A point on the plane.
     * @param b Another point on the plane.
     * @param c Yet another point on the plane.
     */
    Plane(Point a, Point b, Point c) {
        //throws exception if the points are not duplicates
        if (a.equals(b) || a.equals(c) || b.equals(c))
            throw new IllegalArgumentException("Two of the points of a plane cannot be the same");

        //throws exception if points are on the same line by calculating the area of the triangle they make and seeing if it's zero
        //create vectors, find cross product, divide by 2
        Vector ab = b.subtract(a);
        Vector ac = c.subtract(a);
        Vector cross = ab.crossProduct(ac);
        if ((cross.length() * 0.5) == 0) {
            throw new IllegalArgumentException("The points cannot all be on the same line");
        }

        // Calculating the normal to the plane
        // AB x AC = normal
        this.normal = ab.crossProduct(ac).normalize(); // Calculates and normalizes the normal vector
        this.q = a; // Initializes the point 'q' on the plane
    }

    /**
     * Constructs a Plane object with a specified point and normal vector.
     *
     * @param p      A point on the plane.
     * @param normal The normal vector to the plane.
     */
    Plane(Point p, Vector normal) {
        this.q = p; // Initializes the point 'q' on the plane
        this.normal = normal.normalize(); // Normalizes the provided normal vector
    }

    /**
     * Retrieves the normal vector to the plane.
     *
     * @return The normal vector to the plane.
     */
    public Vector getNormal() {
        return this.normal;
    }

    @Override
    public Vector getNormal(Point p) {
        // Additional implementation might be needed for accurate normal at a specific point
        return this.normal;
    }

    public List<Point> findIntersections(Ray ray) {

        return null;
    }
}
