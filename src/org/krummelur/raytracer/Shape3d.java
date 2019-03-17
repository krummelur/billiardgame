package org.krummelur.raytracer;

public abstract class Shape3d extends Object3d{
    Vector3 color;
    abstract double hit(Ray ray);
    abstract Vector3 surfaceNormal(Vector3 point);
    public double radius;

    /**
     * https://gamedev.stackexchange.com/questions/18436/most-efficient-aabb-vs-ray-collision-algorithms
     */

    boolean hitBool(Ray ray) {
        double hit = hit(ray);
        return hit < Integer.MAX_VALUE ? hit > 0 : false;
    }

    double hitFast(Ray ray) {
        return hit(ray);
    }

    double hitSquare(Ray ray) {
        return hit(ray);
    }

    abstract Vector3[] AABB();

    boolean collidesAABB(Ray ray) {


        // r.dir is unit direction vector of ray
        Vector3 dirfrac = new Vector3(1,1,1).divide(ray.direction);
        // lb is the corner of AABB with minimal coordinates - left bottom, rt is maximal corner
        // r.org is origin of ray
        Vector3[] AABB = this.AABB();
        double t1 = (AABB[0].x - ray.origin.x)*dirfrac.x;
        double t2 = (AABB[1].x - ray.origin.x)*dirfrac.x;
        double t3 = (AABB[0].y - ray.origin.x)*dirfrac.y;
        double t4 = (AABB[1].y - ray.origin.x)*dirfrac.y;
        double t5 = (AABB[0].z - ray.origin.x)*dirfrac.z;
        double t6 = (AABB[1].z - ray.origin.x)*dirfrac.z;

        double tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
        double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

// if tmax < 0, ray (line) is intersecting AABB, but the whole AABB is behind us
        if (tmax < 0)
        {
            //t = tmax;
            return false;
        }

// if tmin > tmax, ray doesn't intersect AABB
        if (tmin > tmax)
        {
            //t = tmax;
            return false;
        }

        //t = tmin;
        return true;
    }

}
