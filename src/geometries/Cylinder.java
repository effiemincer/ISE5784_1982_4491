package geometries;

import primitives.*;

/**
 * Represents a cylinder in a three-dimensional (3D) space, inheriting from the Tube class.
 * The cylinder is defined by its radius, axis, and height.
 */
public class Cylinder extends Tube {
    private final double height; // The height of the cylinder

    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point p) {
        return null; // Currently returns null as the normal calculation is not implemented for a cylinder
    }
}
