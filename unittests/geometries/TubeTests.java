package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

class TubeTests {

    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        double radius = 1;
        Tube tube = new Tube(radius, axis);

        //TC01: testing the calculation for getNormal
        assertEquals(new Vector(0, 1, 0).normalize(), tube.getNormal(new Point(4, 1, 0)), "ERROR: calculation of normal is incorrect");

        // =============== Boundary Values Tests ==================

        //TC02: when the connection between the point on the body and the ray’s head creates 90 degrees with the ray
        assertThrows(IllegalArgumentException.class, () -> tube.getNormal(new Point(0, 1, 0)), "ERROR: extreme case where P- P0 is orthogonal to the direction of the axis");
    }
}