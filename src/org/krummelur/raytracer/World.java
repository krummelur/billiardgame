package org.krummelur.raytracer;


import java.util.ArrayList;

public class World {
    ArrayList<Light> lights = new ArrayList<>();
    ArrayList<Shape3d> objects = new ArrayList<>();

    ArrayList<Shape3d> objects() {
        return objects;
    }

    ArrayList<Light> lights() {
        return lights;
    }

    void addObject3d(Shape3d o) {
        objects.add(o);
    }
    void addLight(Light l) {
        lights.add(l);
    }
}
