package org.krummelur.raytracer.billiardgame;

import org.krummelur.raytracer.OCLrenderer;
import org.krummelur.raytracer.RenderManager;

public class Game {

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.library.path"));
        //System.exit(0);
        new Thread(() -> new GameManager()).start();

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });//.start();
    }

}
