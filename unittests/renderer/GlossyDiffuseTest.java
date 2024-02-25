package renderer;

import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import static java.awt.Color.BLUE;
import static java.awt.Color.WHITE;

public class GlossyDiffuseTest {

    /**
     * Scene of the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder of the tests
     */
    private final Camera.Builder camera = Camera.getBuilder()
            .setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
            .setDirection(Point.ZERO, Vector.Y)
            .setVpSize(200, 200)
            .setRayTracer(new SimpleRayTracer(scene));

    @Test
    public void testGlossy(){
        scene.geometries.add(
                new Sphere(30d, new Point(0, 0, -11)) //
                        .setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setKr(1).setShininess(100)),
                new Sphere(15d, new Point(25,40, 150))
                        .setEmission(new Color(150, 0, 0))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(10)),//
                new Triangle(new Point(  0,  -40, 300),
                        new Point(  100, -40, -150),
                        new Point( -100, -40, -150))
                        .setEmission(new Color(0, 100, 0))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100))
        );
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
        scene.lights.add(
                new DirectionalLight(new Color(WHITE), new Vector(-1, -1, -4)));

        camera.setImageWriter(new ImageWriter("GlossyTest", 600, 600)).setTotalRays(40)
                .build()
                .renderImage()
                .writeToImage();
    }

    @Test
    public void testDiffuse(){
        scene.geometries.add(
                new Sphere(30d, new Point(0, 0, -11)) //
                        .setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setKr(1).setShininess(100).setGloss(10)),
                new Sphere(15d, new Point(25,40, 150))
                        .setEmission(new Color(150, 0, 0))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(10)),//
                new Triangle(new Point(  0,  85, 300),
                        new Point(  85, -85, 150),
                     new Point( -85, -85, 150))
                        .setEmission(new Color(0, 100, 0))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setKt(1))
        );
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
        scene.lights.add(
                new DirectionalLight(new Color(WHITE), new Vector(-1, -1, -4)));

        camera.setImageWriter(new ImageWriter("DiffuseTest", 600, 600)).setTotalRays(40)
                .build()
                .renderImage()
                .writeToImage();
    }
}
