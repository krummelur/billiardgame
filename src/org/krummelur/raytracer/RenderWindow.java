package org.krummelur.raytracer;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.*;

public class RenderWindow extends JFrame {

        BufferedImage img;


        public JFrame getJFrame(){
            return this;
        }

        void setImage(BufferedImage img) {
            this.img = img;
        }

        public RenderWindow(BufferedImage img) {
            super("WINDOW");
            this.img = img;
            this.setVisible(true);

            this.start();
            this.add(new JLabel(new ImageIcon(this.getImage())));

            this.pack();
//      frame.setSize(WIDTH, HEIGHT);
            // Better to DISPOSE than EXIT
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    //System.out.println("jdialog window closed event received");
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    System.out.println("jdialog window closing event received");
                    System.exit(0);

                }
            });

        }


        public Image getImage() {
            return img;
        }

        public void start(){
                BufferStrategy bs=this.getBufferStrategy();
                if(bs==null){
                    this.createBufferStrategy(4);
                    return;
                }

                int border = 12;
                try{
                    Graphics g = bs.getDrawGraphics();
                    g.drawImage(img, this.getWidth()/2-img.getWidth()/2, this.getHeight()/2-img.getHeight()/2+border, img.getWidth(), img.getHeight(), null);
                    g.dispose();
                }
                catch (IllegalStateException e) {
                    System.exit(0);
                }
                bs.show();
        }
    }
