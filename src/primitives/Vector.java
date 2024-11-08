package primitives;

/**
 * Represents a vector in a three-dimensional (3D) space.
 * Extends the Point class.
 */
public class Vector extends Point {

    final int ZERO = 0;
    final int VECTOR_INVERTER_SCALE = -1;
    public static final Vector Y = new Vector(0, 1, 0);

    /**
     * Constructs a Vector object with three individual coordinates.
     *
     * @param d1 The x-coordinate.
     * @param d2 The y-coordinate.
     * @param d3 The z-coordinate.
     * @throws IllegalArgumentException if the vector is initialized as a zero vector.
     */
    public Vector(double d1, double d2, double d3) {
        super(d1, d2, d3);
        checkForZeroVector(d1, d2, d3);
    }

    /**
     * Constructs a Vector object using Double3 coordinates.
     *
     * @param point The three-dimensional coordinates of the vector.
     */
    public Vector(Double3 point) {
        super(point);
        if (point.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector Zero is not allowed");
    }

    /**
     * Checks if the vector is a zero vector.
     *
     * @param d1 The x-coordinate.
     * @param d2 The y-coordinate.
     * @param d3 The z-coordinate.
     * @throws IllegalArgumentException if the vector is initialized as a zero vector.
     */
    private void checkForZeroVector(double d1, double d2, double d3) {
        if (new Double3(d1, d2, d3).equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector Zero is not allowed");
    }

    /**
     * Adds another vector to this vector.
     *
     * @param other The other vector to add.
     * @return The resulting vector after addition.
     * @throws IllegalArgumentException if attempting to add the vector to itself.
     */
    public Vector add(Vector other) {
        if (this.xyz.add(other.xyz).equals(Double3.ZERO))
            throw new IllegalArgumentException("Cannot add a vector + -itself!");

        return new Vector(this.xyz.add(other.xyz));
    }

    /**
     * Scales the vector by a given value.
     *
     * @param scalingValue The value to scale the vector by.
     * @return The resulting scaled vector.
     */
    public Vector scale(double scalingValue) {
        if (scalingValue == ZERO)
            throw new IllegalArgumentException("Vectors can't be scaled by zero");

        return new Vector(this.xyz.scale(scalingValue));
    }

    public Vector subtract(Vector other) {
        return new Vector(this.xyz.add(other.xyz.scale(this.VECTOR_INVERTER_SCALE)));
    }

    /**
     * Calculates the dot product of this vector and another vector.
     *
     * @param other The other vector for the dot product calculation.
     * @return The dot product value.
     */
    public double dotProduct(Vector other) {
        double xProduct = this.xyz.d1 * other.xyz.d1;
        double yProduct = this.xyz.d2 * other.xyz.d2;
        double zProduct = this.xyz.d3 * other.xyz.d3;

        return xProduct + yProduct + zProduct;
    }

    /**
     * Calculates the cross product of this vector and another vector.
     *
     * @param other The other vector for the cross product calculation.
     * @return The resulting vector from the cross product.
     */
    public Vector crossProduct(Vector other) {
        double x = this.xyz.d2 * other.xyz.d3 - this.xyz.d3 * other.xyz.d2;
        double y = this.xyz.d3 * other.xyz.d1 - this.xyz.d1 * other.xyz.d3;
        double z = this.xyz.d1 * other.xyz.d2 - this.xyz.d2 * other.xyz.d1;
        return new Vector(x, y, z);
    }

    /**
     * Calculates the squared length of the vector.
     *
     * @return The squared length of the vector.
     */
    public double lengthSquared() {
        return this.dotProduct(this);
    }

    /**
     * Calculates the length of the vector.
     *
     * @return The length of the vector.
     */
    public double length() {
        return Math.sqrt(this.dotProduct(this));
    }

    /**
     * Normalizes the vector (converts it to a unit vector).
     *
     * @return The normalized vector.
     * @throws IllegalArgumentException if the length of the vector is zero.
     */
    public Vector normalize() {
        double length = this.length();

        double xNormalized = this.xyz.d1 / length;
        double yNormalized = this.xyz.d2 / length;
        double zNormalized = this.xyz.d3 / length;

        return new Vector(xNormalized, yNormalized, zNormalized);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other)
                && this.xyz.equals(other.xyz);
    }

    /**
     * Returns a string representation of this vector's coordinates.
     *
     * @return A string representation of the vector.
     */
    public String toString() {
        return (xyz.toString());
    }

    /**
     * finds the orthogonal vector
     *
     * @return the orthogonal vector
     */
    public Vector findOrthogonal() {
        // Extracting components from the vector
        double a, b, c;
        a = getXyz().d1;
        b = getXyz().d2;
        c = getXyz().d3;

        // Checking if c is not zero
        if (!Util.isZero(c)) {
            // Returning a vector orthogonal to (1,1,c)
            return new Vector(1d, 1d, -(a + b) / c).normalize();
        }
        // Checking if b is not zero
        else if (!Util.isZero(b)) {
            // Returning a vector orthogonal to (1,b,1)
            return new Vector(1d, -(a + c) / b, 1d).normalize();
        }
        else {
            // Returning a vector orthogonal to (a,1,1)
            return new Vector(-(b + c) / a, 1d, 1d).normalize();
        }
    }

}
