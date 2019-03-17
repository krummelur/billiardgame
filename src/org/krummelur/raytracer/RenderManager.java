package org.krummelur.raytracer;

import javax.swing.*;
import java.util.ArrayList;

public class RenderManager {
    int elapsedTime = 0;
    OCLrenderer renderer = null;
    RenderWindow window = null;
    public RenderManager(Camera camera) {
        this.start(camera);
    }

    public RenderWindow getWindow(){
        return window;
    }
    //ArrayList<Object>

    void start(Camera camera) {
        //ArrayList<BehaviourObject> updatable = new ArrayList<>();
        renderer = new OCLrenderer(new World(), camera);
        window = new RenderWindow(renderer.getImage());
    }

    public void addObject3d(Shape3d o) {
        renderer.world.addObject3d(o);
    }
    public void addLight(Light l) {renderer.world.addLight(l);}

    public void setCamera(Camera camera){

    }

    public boolean isShuttingDown() {
        return renderer.isShuttingDown();
    }


    public int draw() {
        return renderer.render(window);
    }

    void update(double deltaTime, ArrayList<BehaviourObject> updatable) {
        updatable.forEach(u-> u.update(deltaTime));
    }
}
