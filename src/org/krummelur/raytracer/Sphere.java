package org.krummelur.raytracer;

public class Sphere extends Shape3d implements BehaviourObject {
    private final double epsilon = 0.000001;
    double rayToSphereSquared;
    double minDistanceRaySphereSquared;
    double travelDistanceClosestPoint;

    public Sphere(Vector3 location, double radius, Vector3 color) {
        this.radius = radius;
        this.location = location;
        this.color = color;
    }

    public Sphere(Vector3 location, double radius) {
        this(location, radius, new Vector3(1, 1, 1));
    }

    //Ray must be normalized
    @Override
    double hit(Ray normalizedRay) {

        //some edge cases caused mindistanceRaySohereSquared to become extremely small negative values when passing right through sphere, causing ray miss to be reported
        //Get distance from ray origin to sphere origin

        travelDistanceClosestPoint = normalizedRay.direction.dot(this.location.subtract(normalizedRay.origin));
        //travelDistanceClosestPoint = hitFast(normalizedRay);
        if (travelDistanceClosestPoint < 0) {
            return -1;
        }
        rayToSphereSquared = normalizedRay.origin.distanceSquared(this.location);
        minDistanceRaySphereSquared = rayToSphereSquared - (travelDistanceClosestPoint * travelDistanceClosestPoint);

        //if minDistanceRaySquared is < 0, that means that the closest distance is farther than the radius, meaning the ray missed
        	/*
			double thatNumber = this.radius * this.radius - minDistanceRaySphereSquared;
			double sqr = Double.longBitsToDouble(((Double.doubleToLongBits((this.radius * this.radius - minDistanceRaySphereSquared))-(1l<<52) )>>1 ) + ( 1l<<61 ) );

			sqr = (sqr+thatNumber/sqr)/2;
			sqr = (sqr+thatNumber/sqr)/2;
			*/
        return (minDistanceRaySphereSquared < -epsilon || this.radius * this.radius < minDistanceRaySphereSquared) ? -1 : travelDistanceClosestPoint - Math.sqrt(this.radius * this.radius - minDistanceRaySphereSquared);
        //return travelDistanceClosestPoint - halfDistanceInsideSphere;
        //Vector3 intersectionPoint = normalizedRay.travel(rayInstersectionDistance );
    }

    @Override
    double hitSquare(Ray normalizedRay) {

        //some edge cases caused mindistanceRaySohereSquared to become extremely small negative values when passing right through sphere, causing ray miss to be reported
        //Get distance from ray origin to sphere origin

        travelDistanceClosestPoint = normalizedRay.direction.dot(this.location.subtract(normalizedRay.origin));
        //travelDistanceClosestPoint = hitFast(normalizedRay);
        if (travelDistanceClosestPoint < 0) {
            return -1;
        }
        rayToSphereSquared = normalizedRay.origin.distanceSquared(this.location);
        minDistanceRaySphereSquared = rayToSphereSquared - (travelDistanceClosestPoint * travelDistanceClosestPoint);

        //if minDistanceRaySquared is < 0, that means that the closest distance is farther than the radius, meaning the ray missed
        	/*
			double thatNumber = this.radius * this.radius - minDistanceRaySphereSquared;
			double sqr = Double.longBitsToDouble(((Double.doubleToLongBits((this.radius * this.radius - minDistanceRaySphereSquared))-(1l<<52) )>>1 ) + ( 1l<<61 ) );

			sqr = (sqr+thatNumber/sqr)/2;
			sqr = (sqr+thatNumber/sqr)/2;
			*/
        return (minDistanceRaySphereSquared < -epsilon || this.radius * this.radius < minDistanceRaySphereSquared) ? -1 : travelDistanceClosestPoint - Math.sqrt(this.radius * this.radius - minDistanceRaySphereSquared);
        //return travelDistanceClosestPoint - halfDistanceInsideSphere;
        //Vector3 intersectionPoint = normalizedRay.travel(rayInstersectionDistance );
    }

    double hitFast(Ray normalizedRay) {
        //return normalizedRay.direction.dot(this.location.subtract(normalizedRay.origin));

        double closestDist =
                        (normalizedRay.direction.x *(this.location.x - normalizedRay.origin.x) +
                        normalizedRay.direction.y * (this.location.y - normalizedRay.origin.y) +
                        normalizedRay.direction.z * (this.location.z - normalizedRay.origin.z)) /
                        (normalizedRay.direction.x * normalizedRay.direction.x +
                                normalizedRay.direction.y * normalizedRay.direction.y +
                                normalizedRay.direction.z * normalizedRay.direction.z);


        double otherNum =               Math.pow((normalizedRay.direction.x * closestDist - this.location.x), 2) +
                Math.pow((normalizedRay.direction.y * closestDist - this.location.y), 2) +
                Math.pow((normalizedRay.direction.z * closestDist - this.location.z), 2);

        return closestDist < 0 ? -1 :
                Math.pow((normalizedRay.direction.x * closestDist - this.location.x), 2) +
                Math.pow((normalizedRay.direction.y * closestDist - this.location.y), 2) +
                Math.pow((normalizedRay.direction.z * closestDist - this.location.z), 2);
    }

    boolean hitBool(Ray normalizedRay) {
        return normalizedRay.direction.dot(this.location.subtract(normalizedRay.origin)) > 0;
        /*
        double relX = this.location.x - normalizedRay.origin.x;
        double relY = this.location.y - normalizedRay.origin.y;
        double relZ = this.location.z - normalizedRay.origin.z;
        double closestDist =
                (normalizedRay.direction.x * relX +
                        normalizedRay.direction.y * relY +
                        normalizedRay.direction.z * relZ) /
                        (normalizedRay.direction.x * normalizedRay.direction.x +
                                normalizedRay.direction.y * normalizedRay.direction.y +
                                normalizedRay.direction.z * normalizedRay.direction.z);
        //closestDist= closestDist;
        Vector3 closestPoint = new Vector3(
                normalizedRay.direction.x * closestDist,
                normalizedRay.direction.y * closestDist,
                normalizedRay.direction.z * closestDist);
        return ((relX - closestPoint.x) * (relX - closestPoint.x) +
                (relY - closestPoint.y) * (relY - closestPoint.y) +
                (relZ - closestPoint.z) * (relZ - closestPoint.z))
                < this.radius * this.radius;
        */
    }

    @Override
    public Vector3[] AABB() {
        return new Vector3[]{this.location.subtract(radius), this.location.add(radius)};
    }

    @Override
    Vector3 surfaceNormal(Vector3 point) {
        return point.subtract(this.location).divide(this.radius);
    }

    @Override
    public void update(double deltaTime) {

    }
}