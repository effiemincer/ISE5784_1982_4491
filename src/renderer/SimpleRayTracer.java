package renderer;

import geometries.Geometries;
import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.*;

/**
 * A simple ray tracer that calculates the color of a point in a scene based on ray intersections.
 */
public class SimpleRayTracer extends RayTracerBase {



    private static final Double3 INITIAL_K = Double3.ONE;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    public static final double MIN_CALC_COLOR_K = 0.001;

    /**
     * Constructs a SimpleRayTracer object with the given scene.
     *
     * @param scene The scene to be rendered.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces the given ray through the scene and returns the color of the closest intersection point.
     *
     * @param ray The ray to be traced.
     * @return The color of the closest intersection point, or the background color if no intersection is found.
     */
    @Override
    public Color traceRay(Ray ray) {
        GeoPoint closestPoint = findClosestIntersection(ray);
        return closestPoint == null ? scene.background : calcColor(closestPoint, ray);
    }

    /**
     * Finds the closest intersection point of the given ray with the geometries in the scene.
     *
     * @param ray The ray for which to find the closest intersection point.
     * @return The closest intersection point of the ray with the geometries in the scene, or {@code null} if no intersection is found.
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        return intersections == null ? null : ray.findClosestGeoPoint(intersections);
    }

    /**
     * Calculates the color of a given point in the scene.
     *
     * @param gp The point for which to calculate the color.
     * @return The color of the specified point in the scene.
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        return calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.ambientLight.getIntensity());
    }
    private Color calcColor(GeoPoint gp, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(gp, ray, k);
        return 1 == level ? color : color.add(calcGlobalEffects(gp, ray, level, k));
    }

    /**
     * Calculates the color of a given point in the scene, based on local effects only.
     *
     * @param gp  The point for which to calculate the color.
     * @param ray The ray that hit the point
     * @return The color of the specified point in the scene.
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
        Vector n = gp.geometry.getNormal(gp.point);
        Vector v = ray.getDirection();
        double nv = alignZero(n.dotProduct(v));
        if (isZero(nv)) return Color.BLACK;

        Material material = gp.geometry.getMaterial();
        Color color = gp.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) {    // sign(nl) == sign(nv)
                Double3 ktr = transparency(gp, lightSource, l,n);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color iL = lightSource.getIntensity(gp.point).scale(ktr);
                    color = color.add(iL.scale(calcDiffusive(material, nl).add(calcSpecular(material, n, l, nl, v))));
                }
            }
        }
        return color;
    }

    /**
     * Calculates the diffusive color of a point on a geometry.
     *
     * @param material The material of the geometry
     * @param nl       The dot product of n and l
     * @return The diffusive color
     */
    private Double3 calcDiffusive(Material material, double nl) {
        return material.kD.scale(Math.abs(nl));
    }

    /**
     * Calculates the specular color of a point on a geometry.
     *
     * @param material The material of the geometry
     * @param n        The normal vector
     * @param l        The light vector
     * @param nl       The dot product of n and l
     * @param v        The view vector
     * @return The specular color
     */
    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
        Vector r = l.subtract(n.scale(2 * nl));
        double vr = alignZero(v.dotProduct(r));
        return material.kS.scale(Math.pow(Math.max(0, -vr), material.nShininess));
    }


    private boolean unshaded(GeoPoint gp, LightSource light, Vector l, Vector n, double nl) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray ray = new Ray(gp.point, lightDirection, n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null) return true;


        double lightDistance = light.getDistance(gp.point);
        for (GeoPoint gpoint : intersections) {
            if (alignZero(gpoint.point.distance(gp.point) - lightDistance) <= 0)
                return false;
        }
        return true;
    }

    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Material material = gp.geometry.getMaterial();
        return calcGlobalEffect(constructReflectedRay(gp, ray), level, k, material.kR)
                .add(calcGlobalEffect(constructRefractedRay(gp, ray),  level, k, material.kT));
    }

    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        GeoPoint gp = findClosestIntersection(ray);
        return (gp == null ? scene.background : calcColor(gp, ray, level - 1, kkx)).scale(kx);
    }

    private Ray constructRefractedRay(GeoPoint gp, Ray ray) {
        Vector normal = gp.geometry.getNormal(gp.point);
//        Vector epsVector = normal.scale(Ray.DELTA);
//        Point point = gp.point.add(epsVector);
        return new Ray(gp.point, ray.getDirection(), normal);
    }

    private Ray constructReflectedRay(GeoPoint gp, Ray ray) {
        Vector normal = gp.geometry.getNormal(gp.point);
        //Vector direction = ray.getDirection();
        //if (isZero(alignZero(normal.dotProduct(ray.getDirection())))) return null;
        //Point point = gp.point.add(normal.scale(Ray.DELTA));
        return new Ray(gp.point, ray.getDirection().scale(-1), normal);
    }

    /**
     * Calculates the transparency of a point on a geometry.
     *
     * @param gp The point for which to calculate the transparency.
     * @param ls The light source
     * @param l  The light vector
     * @param n  The normal vector
     * @return The transparency of the point
     */
    private Double3 transparency(GeoPoint gp, LightSource ls, Vector l, Vector n) {
        Vector lightDirection = l.scale(-1); // from point to light source
//        Vector epsVector = n.scale(nl < 0 ? DELTA : -DELTA);
//        Point point = gp.point.add(epsVector);
        Ray ray = new Ray(gp.point, lightDirection, n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null) return Double3.ONE;

        Double3 ktr = Double3.ONE;
        double lightDistance = ls.getDistance(gp.point);
        for (GeoPoint gpoint : intersections)
            if (alignZero(gpoint.point.distance(gp.point) - lightDistance) <= 0)
                ktr = ktr.product(gpoint.geometry.getMaterial().kT);

        return ktr;
    }
}
