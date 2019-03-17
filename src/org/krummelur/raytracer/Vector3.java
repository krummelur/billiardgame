package org.krummelur.raytracer;

public class Vector3 {
    double x, y, z;

    public static Vector3 ZERO() {
        return new Vector3();
    }

    private Vector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() {
        return x;
    }
    public double y() {
        return y;
    }
    public double z() {
        return z;
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3 add(double d) {
        return new Vector3(this.x + d, this.y + d, this.z + d);
    }

    public Vector3 subtract(Vector3 other) {
        return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3 subtract(double d) {
        return new Vector3(this.x - d, this.y - d, this.z - d);
    }

    public double distance(Vector3 other) {
        return Math.sqrt(distanceSquared(other));
    }

    public double magnitudeSquared() {
        return this.distanceSquared(Vector3.ZERO());
    }

    public double distanceSquared(Vector3 other) {
        Vector3 delta = subtract(other);
        return (delta.y * delta.y) + (delta.z * delta.z) + (delta.x * delta.x);
     }

    public double dot(Vector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    boolean equals(Vector3 other) {
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    public Vector3 divide(double d) {
        return new Vector3(this.x / d, this.y / d, this.z / d);
    }

    public Vector3 divide(Vector3 other) {
        return new Vector3(this.x / other.x, this.y / other.y, this.z / other.z);
    }

    public Vector3 normalize() {
        return divide(distance(Vector3.ZERO()));
    }

    @Override
    public String toString() {
        return "{ "+ x + ", " +  y + ", "  + z + "}";
    }

    public Vector3 multiply(Vector3 other) {
        return new Vector3(this.x * other.x, this.y * other.y, this.z * other.z);
    }

    Vector3 cross(Vector3 other) {
        return new Vector3(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x);
    }

    public Vector3 multiply(double d) {
        return new Vector3(this.x * d, this.y * d, this.z * d);
    }

    public Vector3 clampNegative() {
        return new Vector3(Math.max(this.x, 0), Math.max(this.y, 0), Math.max(this.z, 0));
    }

    public Vector3 clampMaximum(double max) {
        return new Vector3(Math.min(this.x, max), Math.min(this.y, max), Math.min(this.z, max));
    }

    public Vector3 reflect(Vector3 n) {
        return this.subtract(n.multiply(this.dot(n)).multiply(2));

    }

    public void replace(Vector3 newVector) {
        this.x = newVector.x;
        this.y = newVector.y;
        this.z = newVector.z;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof  Vector3) {
            return ((Vector3)other).x == this.x &&
                    ((Vector3)other).y == this.y &&
                    ((Vector3)other).z == this.z;
        }
        return false;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Vector3 clampMinimum(double min) {
        return new Vector3(Math.max(this.x, min), Math.max(this.y, min), Math.max(this.z, min));
    }
}