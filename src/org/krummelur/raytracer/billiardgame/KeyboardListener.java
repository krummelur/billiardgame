package org.krummelur.raytracer.billiardgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyboardListener implements KeyListener {
    ArrayList<KeySubscriber> subscribers = new ArrayList<>();

    public KeyboardListener(){
        super();
    }

    //boolean[] keys = new boolean[65536];
    public void subscribe(KeySubscriber ks) {
        this.subscribers.add(ks);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.println(e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyChar());
        for(KeySubscriber ks : subscribers) {
            for(char c : ks.getKeys()){
                if(c == e.getKeyChar())
                    ks.keyPressed(e.getKeyChar());
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for(KeySubscriber ks : subscribers) {
            for(char c : ks.getKeys()){
                if(c == e.getKeyChar())
                    ks.keyReleased(e.getKeyChar());
            }
        }
    }
}
