package org.krummelur.raytracer.billiardgame;

import org.krummelur.raytracer.Vector3;

import java.util.ArrayList;

//Takes care of checcking the balls collision, applying force to them.
// the balls use this to calculate their trajectory, the balls also take care of adding friction on their own.
public class PhysicsManager {
    ArrayList<BilliardBall> balls = new ArrayList<>();

    double fieldRadius = 30;
    double[] playingField = {
            -fieldRadius, fieldRadius, -fieldRadius, fieldRadius
    };
    public void addBall(BilliardBall ball) {
        this.balls.add(ball);
    }

    void update() {
        //in order to accidentally recheck a ball that is not moving at the start of the update function, but have gained momentum duiring the method,
        //first make a list of moving balls
        ArrayList<BilliardBall> movingBalls = new ArrayList<>();
        for(BilliardBall b : balls) {
            //also make sure that no moving balls are outside the playing field
            if(b.isMoving()){
                movingBalls.add(b);
                double wallMul = 0.8;
                if(b.location.y()-b.radius < playingField[0]){
                    b.momentum.setY(b.momentum.y()*-wallMul);
                    b.location.setY(playingField[0]+b.radius);
                }
                if(b.location.y()+b.radius > playingField[1]){
                    b.momentum.setY(b.momentum.y()*-wallMul);
                    b.location.setY(playingField[1]-b.radius);
                }
                if(b.location.z()-b.radius < playingField[2]){
                    b.momentum.setZ(b.momentum.z()*-wallMul);
                    b.location.setZ(playingField[2]+b.radius);
                }
                if(b.location.z()+b.radius > playingField[3]){
                    b.momentum.setZ(b.momentum.z()*-wallMul);
                    b.location.setZ(playingField[3]-b.radius);
                }
            }
        }
        /*
        Calculation ripped directly from:
            https://en.wikipedia.org/wiki/Elastic_collision#Two-dimensional
        */
        for (BilliardBall mb : movingBalls) {
            for(BilliardBall b : balls) {
                if(mb != b) {
                    if(b.location.distanceSquared(mb.location) < b.getRadius()*b.getRadius()*4) {
                        //If we dont first move the balls out of each others space, they may start "dancing" with each other (attaching to each other)
                        double encroachment = b.getRadius()*2 - b.location.distance(mb.location);
                        mb.location.replace(mb.location.add((mb.location.subtract(b.location)).normalize().multiply(encroachment+0.1)));//mb.location.subtract(mb.location.subtract(b.location)).normalize().multiply(encroachment)));
                        Vector3 mbMomentumPrime = mb.momentum.subtract((mb.location.subtract(b.location)).multiply( mb.momentum.subtract(b.momentum).dot(mb.location.subtract(b.location)) / (4*b.radius*b.radius)));
                        Vector3 mMomentumPrime =   b.momentum.subtract((b.location.subtract(mb.location)).multiply( b.momentum.subtract(mb.momentum).dot(b.location.subtract(mb.location)) / (4*b.radius*b.radius)));
                        mb.momentum = mbMomentumPrime;
                        b.momentum = mMomentumPrime;

                        //Play a sound cue for the collision
                        if(mb.momentum.subtract(b.momentum).magnitudeSquared() > 0.02)
                        AudioManager.getInstance().loadIfNeededAndPlay("billiardCollision.wav", (float)Math.min(Math.pow(mb.momentum.subtract(b.momentum).magnitudeSquared(),2) *10, 1));
                    }
                }
            }
        }
    }
}
