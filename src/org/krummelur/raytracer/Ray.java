package org.krummelur.raytracer;

public class Ray{

	Vector3 origin;
	Vector3 direction;

	public Ray(Vector3 origin, Vector3 direction) {
		this.origin = origin;
		this.direction = direction;
	}
	
	//returns the point length away from origin in ray direction 
	public Vector3 travel(double length) {
		return new Vector3(origin.x + direction.x*length, origin.y + direction.y*length, origin.z + direction.z*length);
	}
}