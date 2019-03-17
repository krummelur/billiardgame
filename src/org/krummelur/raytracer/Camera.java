package org.krummelur.raytracer;

import org.krummelur.raytracer.billiardgame.GameManager;
import org.krummelur.raytracer.billiardgame.KeySubscriber;

import java.util.HashMap;
import java.util.Map;

public class Camera implements BehaviourObject {
    Map<Integer, Boolean> inputs = new HashMap<Integer, Boolean>(){{
        put((int)'w', false);
        put((int)'a', false);
        put((int)'s', false);
        put((int)'d', false);
    }};
    Vector3 location;
    OrthogonalSet lookPlane;
    double fov;
    boolean orthographic = true;
    double orthogonalSize = 60;
    Vector3 center = Vector3.ZERO();

    public Camera(Vector3 location, Vector3 direction) {
        this.location = location;
        lookPlane = new OrthogonalSet(direction);
        this.location = this.center.subtract(this.direction().multiply(10));
        setUpKeyEvents();
    }

    Vector3 direction() {
        return this.lookPlane.forward;
    }

    public void setUpKeyEvents() {
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
               return new char[]{'w','a','s','d'};
            }
        });
    }


    public void rotate(Vector3 rotation) {
        this.lookPlane.rotate(rotation);
        //this.lookPlane.forward = this.lookPlane.forward.normalize();
        //if the camera is orthographic, move it 'infinitely' far away from the center, in the opposite of it's direction. Set to 1000 since we will lose precision as the camera moves away
        /*
        this.lookPlane.forward = new Vector3(-0.9446110269413361, 0.0, 0.32819202522128077).normalize();
        this.lookPlane.up= new Vector3( 0.32819202560887367, -4.0191931366759337E-17, 0.9446110280569164);
        this.lookPlane.right= new Vector3( -1.2246467991473532E-16, -1.0, 0.0);
        this.location = new Vector3( 94.46110269413361, 0.0, -32.81920252212808);
        */
        if (this.orthographic) {
        this.location = this.center.subtract(this.direction().multiply(100));
        }
        //System.out.println("LOOKPLANE " + this.lookPlane);
        //System.out.println("CENTER: " + this.center);
        //System.out.println("LOCATION: " + this.location);
    }

    @Override
    public void update(double deltaTime) {
        handleInput(deltaTime);
    }

    void handleInput(double deltaTime) {
        if(this.inputs.get((int)'w')) {
            this.rotate(new Vector3(0,2,0));
            System.out.print("'a' is pressed, rotation: \n" + direction());
        }
        if(this.inputs.get((int)'a')) {
            this.rotate(new Vector3(2,0,0));
            System.out.print("'a' is pressed, rotation: \n" + direction());
        }
        if(this.inputs.get((int)'s')) {
            this.rotate(new Vector3(0,-2,0));
            System.out.print("'a' is pressed, rotation: \n" + direction());
        }
        if(this.inputs.get((int)'d')) {
            System.out.print("'d' is pressed");
            this.rotate(new Vector3(-2,0,0));
        }
    }
}
