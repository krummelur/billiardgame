package org.krummelur.raytracer;

public class PointLight extends Light{

    public PointLight(Vector3 location, double strength, Vector3 color){
        super(location, strength, color);
    }

    public PointLight(Vector3 location, double strength){
        super(location, strength);
    }

}
