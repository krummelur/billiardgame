package org.krummelur.raytracer;


public class OrthogonalSet {

    private boolean flipUp = false;
    private boolean flipRight = false;

    final double Deg_To_Rad = Math.PI/180;
    final double Rad_To_Deg = 180/Math.PI;
    Vector3 forward, up, right;
    public OrthogonalSet(Vector3 forward) {
        this.forward = forward;
        recalculate();
    }

    void rotate(Vector3 degrees) {
        //yaw, pitch, roll
        degrees  = degrees.multiply(Deg_To_Rad);
        //degrees.x = Math.PI/2;

        //rotate z axis
        double x = this.forward.x *Math.cos(degrees.x) - this.forward.y*Math.sin(degrees.x);
        double y = this.forward.x *Math.sin(degrees.x) + this.forward.y*Math.cos(degrees.x);

        //rotate y axis
        x = x*Math.cos(degrees.y) + this.forward.z*Math.sin(degrees.y);
        double z = -x*Math.sin(degrees.y) + this.forward.z*Math.cos(degrees.y);
        Vector3 before = new Vector3(this.forward.x, this.forward.y, this.forward.z);
        this.forward.x = x;
        this.forward.y = y;
        this.forward.z = z;
        this.forward = this.forward.normalize();
        recalculate();
    }

    private void recalculate() {
        //Make up a right vector that is on the xy plane (roll not necessary)
        Vector3 normalizedforwardXY = new Vector3(forward.x, forward.y, 0).normalize();
        double thetaForward = Math.asin(normalizedforwardXY.y);
        thetaForward = Math.atan2(normalizedforwardXY.x, normalizedforwardXY.y);

        right = new Vector3(Math.sin(thetaForward-(Math.PI / 2)),Math.cos(thetaForward-(Math.PI / 2)), 0).normalize().multiply(flipRight ? -1 : 1);
        this.up = forward.cross(right).normalize();
        //System.out.println(this);
    }



    @Override
    public String toString() {
        return "{\n  forward: " + forward + "\n  up: " + up + "\n  " + "right: " + right + "\n}";
    }
}
