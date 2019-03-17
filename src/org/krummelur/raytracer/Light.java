package org.krummelur.raytracer;

public abstract class Light extends Object3d implements BehaviourObject {
    double strength;
    Vector3 color;

    public Light(Vector3 location, double strength, Vector3 color) {
        this.strength = strength;
        this.color = color;
        this.location = location;
    }

    public Light(Vector3 location, double strength) {
        this(location, strength, new Vector3(1,1,1));
    }

    @Override
    public void update(double deltatime) {}
}
