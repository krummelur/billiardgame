package org.krummelur.raytracer.billiardgame;


import org.krummelur.raytracer.BehaviourObject;
import org.krummelur.raytracer.Object3d;
import org.krummelur.raytracer.Shape3d;

public abstract class GameObject extends Object3d implements BehaviourObject{
    Shape3d gRepresentation;

}
