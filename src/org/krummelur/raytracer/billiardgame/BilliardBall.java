package org.krummelur.raytracer.billiardgame;

import org.krummelur.raytracer.Color;
import org.krummelur.raytracer.Sphere;
import org.krummelur.raytracer.Vector3;

import java.util.HashMap;
import java.util.Map;


public class BilliardBall extends GameObject  {

    Vector3 momentum = Vector3.ZERO();
    double radius = 4;
    Map<Integer, Boolean> inputs = new HashMap<Integer, Boolean>(){{
        put((int)'p', false);
        put((int)'m', false);
    }};

    public BilliardBall(Vector3 location, Color color) {
        this(location, color.RGB());
    }

    void setRadius(double rad) {
        this.radius = rad;
        gRepresentation.radius = rad;
    }

    public BilliardBall(Vector3 location, Vector3 color) {
        this.location = location;
        this.gRepresentation = new Sphere( this.location,radius, color);
        GameManager.keyboardListener.subscribe(new KeySubscriber() {
            @Override
            public void keyPressed(char key) {
                inputs.put((int)key, true);
            }
            @Override
            public void keyReleased(char key) {
                inputs.put((int)key, false);
            }

            @Override
            public char[] getKeys() {
                return new char[]{'p','m'};
            }
        });
    }

    public boolean isMoving() {
        return !this.momentum.equals(Vector3.ZERO());
    }

    public double getRadius () {
        return this.radius;
    }
    void applyForce(Vector3 force) {
        this.momentum = momentum.add(force);
    }

    void handleInput() {
        if(this.inputs.get((int)'p'))
            setRadius(radius * 1.02);

        if(this.inputs.get((int)'m'))
            setRadius(radius* 0.98);
    }

    //update will calculate the speed and direction based on the force applied, also calculating movement speed based on friction
    @Override
    public void update(double deltaTime) {
        handleInput();
        double scaleFactor = deltaTime / 10d;
        if(this.momentum.magnitudeSquared() > 0){
            this.location.replace(this.location.add(this.momentum.multiply(scaleFactor)));
            //Apply friction force
            this.applyForce(this.momentum.multiply(-1d).multiply(scaleFactor/150));
        }
    }
}
