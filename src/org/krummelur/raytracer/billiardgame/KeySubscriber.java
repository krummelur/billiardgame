package org.krummelur.raytracer.billiardgame;

public interface KeySubscriber {
    void keyPressed(char k);
    void keyReleased(char k);
    char[] getKeys();
}
