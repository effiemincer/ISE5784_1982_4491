package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {

    private final List<Intersectable> list = new LinkedList<>();

    public Geometries(Intersectable... geometries) {

    }

    public void add(Intersectable... geometries) {

    }

    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
