package org.krummelur.raytracer.billiardgame;

import org.krummelur.raytracer.*;

import java.util.ArrayList;
import java.util.Random;

//Manages game objects and other game logic, keeps the main update loop going
public class GameManager {
    int totalFrames = 0;
    long startTime = 0;
    int minimalDeltaTMilli = 10;
    public static KeyboardListener keyboardListener = new KeyboardListener();
    boolean testmode = true;
    ArrayList<BehaviourObject> updatable = new ArrayList<>();
    RenderManager rm;
    PhysicsManager pm;
    public long elapsedTime = 0l;
    public GameManager() {
        //Spinning camera
        Camera camera = new Camera(new Vector3(1,0,0), new Vector3(-1,0,0).normalize()); /*{
            @Override
            public void update(double deltaTime) {
                //Vector3 rotationDelta = new Vector3(0.1, 0.0,0);
                //rotate(rotationDelta.multiply(deltaTime));
            }
        };
        */
        pm = new PhysicsManager();
        rm = new RenderManager(camera);
        updatable.add(camera);
        setUpWorld();
        rm.getWindow().getJFrame().addKeyListener(keyboardListener);
        loop(60);
    }
    public void addObject(Object3d object) {
        if(object instanceof BehaviourObject)
            this.updatable.add((BehaviourObject) object);
        if(object instanceof Shape3d)
            this.rm.addObject3d((Shape3d) object);
        if(object instanceof Light)
            this.rm.addLight((Light) object);
    }

    public void addObject(GameObject object) {
        if(object.gRepresentation != null)
            this.addObject(object.gRepresentation);
        if(object instanceof BilliardBall)
            this.pm.addBall((BilliardBall) object);
        this.updatable.add(object);
    }

    void loop(long deltaTime){

        long previousTime;
        long previousTimeNano;
        long frameCounter = 0;
        while(true){
            previousTime = System.currentTimeMillis();
            previousTimeNano = System.nanoTime();
            elapsedTime+=deltaTime;
            final long dt = deltaTime;
            pm.update();
            updatable.forEach(u-> u.update(dt));
            if(rm.isShuttingDown())
                return;
            rm.draw();
            totalFrames++;
            if(frameCounter % 10 == 0) {
                startTime = startTime == 0 ? System.currentTimeMillis() : startTime;
                //System.out.println("uncapped framerate: " + (1d / (double)(System.currentTimeMillis() - previousTime))*1000d);
                System.out.println("frame Time: " + ((double)(System.nanoTime() - previousTimeNano))/1000000d + " ms");
                System.out.println("AVG framerate: " + (1000d/((System.currentTimeMillis()-startTime)/(double)totalFrames)));
            }

            java.util.concurrent.locks.LockSupport.parkNanos((minimalDeltaTMilli-deltaTime)*1000000);
            deltaTime = System.currentTimeMillis() - previousTime;

            frameCounter++;
        }
    }


    void setUpWorld() {
        BilliardBall b1 = new BilliardBall(new Vector3(0,-8,0), Color.WHITE);
        double r = b1.radius;
        BilliardBall b2 = new BilliardBall(new Vector3(0,0,0), Color.WHITE);
        b2 = new BilliardBall(new Vector3(0,0,0), new Vector3(1,0.6,1));
        BilliardBall b3 = new BilliardBall(new Vector3(0,r*1.76,r), Color.GREEN);
        BilliardBall b4 = new BilliardBall(new Vector3(0,r*1.76,-r), Color.BLUE);
        BilliardBall b5 = new BilliardBall(new Vector3(0,r*1.76*2,r*2), Color.YELLOW);
        BilliardBall b6 = new BilliardBall(new Vector3(0,r*1.76*2,-r*2), Color.RED);
        BilliardBall b7 = new BilliardBall(new Vector3(0,r*1.76*2,0), Color.WHITE);
        BilliardBall b8 = new BilliardBall(new Vector3(0,r*1.76*3,r), Color.RED);
        BilliardBall b9 = new BilliardBall(new Vector3(0,r*1.76*3,-r), Color.GREEN);
        BilliardBall b10 = new BilliardBall(new Vector3(0,r*1.76*3,3*r), Color.YELLOW);
        BilliardBall b11 = new BilliardBall(new Vector3(0,r*1.76*3,-3*r), Color.BLUE);
        addObject(b1);

        addObject(b2);

        addObject(b3);
        addObject(b4);
        addObject(b5);
        addObject(b6);
        addObject(b7);
        addObject(b8);
        addObject(b9);
        addObject(b10);
        addObject(b11);

        if(testmode)
        new Thread() {
            @Override
            public void run(){
            try {
                Random rand = new Random();
                Thread.sleep(2000);
                b1.applyForce(new Vector3(0, rand.nextDouble()*1.5, rand.nextDouble()*1.5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            run();
        }

        }.start();

        Sphere spinningSphere2 = new Sphere(new Vector3(0,0,1), 0.5, new Vector3(1,0,1)) {
            @Override
            public void update(double deltaTime) {
                this.location = new Vector3(Math.sin(elapsedTime/1200.0+Math.PI), 0, Math.cos(elapsedTime/1200.0+Math.PI));
            }
        };
        Sphere spinningSphere = new Sphere(new Vector3(0,0,-1), 0.5, new Vector3(1,0,1)) {
            @Override
            public void update(double deltaTime) {
                this.location = new Vector3(Math.sin(elapsedTime/1200.0), 0, Math.cos(elapsedTime/1200.0));
            }
        };
        //addObject(spinningSphere);
        //addObject(spinningSphere2);
        PointLight light1 = new PointLight(new Vector3(0, 10,0),20, Color.RED.RGB()){
            double rot = 1450.0;
            @Override
            public void update(double deltaTime) {
                this.location = new Vector3(-Math.sin(elapsedTime/rot)*50, 00, Math.cos(elapsedTime/rot)*50);
            }


        };
        PointLight light2 = new PointLight(new Vector3(50,50 ,0),35, new Vector3(1,1,1));
        PointLight light3 = new PointLight(new Vector3(0, 10,0),30, Color.BLUE.RGB()){
            @Override
            public void update(double deltaTime) {
                double rot = 2000;
                double offset = -Math.PI/2;
                this.location = new Vector3(-Math.sin(offset+(elapsedTime/rot))*50, Math.cos(offset+(elapsedTime/rot))*50, 0);
            }
        };
        PointLight light4 = new PointLight(new Vector3(0, 50,0),14, new Vector3(1,0.25,1)){
            @Override
            public void update(double deltaTime) {
                double rot = 2000;
                double offset = -Math.PI/2;
                this.location = new Vector3(3,-Math.sin(offset+(elapsedTime/rot))*60, Math.cos(offset+(elapsedTime/rot))*60);
            }
        };
        addObject(light1);
        addObject(light2);
        addObject(light3);
        addObject(light4);
    }
}
