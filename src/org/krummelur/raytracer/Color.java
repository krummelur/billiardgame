package org.krummelur.raytracer;

public enum Color {
    BLACK(0),
    RED(1),
    GREEN(2),
    BLUE(3),
    YELLOW(4),
    WHITE(5);

    private int code;

    Color(int code) {
        this.code = code;
    }


    public Vector3 RGB() {
        switch(this.code) {
            case  0: return new Vector3(0.1,0.1,0.1);
            case  1: return new Vector3(1,0.125,0.125);
            case  2: return new Vector3(0.125,1,0.125);
            case  3: return new Vector3(0.125,0.125,1);
            case  4: return new Vector3(1,1,0.125);
            case  5: return new Vector3(0.9,0.9,0.9);
        }
        return null;
    }
}