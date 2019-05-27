package cg_lab4;

import javax.media.j3d.*; // for transform
import javax.vecmath.Color3f;
import java.awt.Color;

public class Utils {
    public static Appearance getBodyAppearence() {
        Appearance ap = new Appearance();

        Color3f emissive = new Color3f(new Color(0,0, 0));
        Color3f ambient = new Color3f(Color.ORANGE);
        Color3f diffuse = new Color3f(Color.ORANGE);
        Color3f specular = new Color3f(new Color(0,0, 0));
        // ambient, emissive, diffuse, specular, 1.0f
        ap.setMaterial(new Material(ambient, emissive, diffuse, specular, 1.0f));

        return ap;
    }

    public static Appearance getBallAppearence(Color color) {
        Appearance ap = new Appearance();

        Color3f emissive = new Color3f(new Color(0,0, 0));
        Color3f ambient = new Color3f(color);
        Color3f diffuse = new Color3f(color);
        Color3f specular = new Color3f(color);
        // ambient, emissive, diffuse, specular, 1.0f
        ap.setMaterial(new Material(ambient, emissive, diffuse, specular, 1.0f));

        return ap;
    }

    public static Appearance getPistachoAppearence() {
        Appearance ap = new Appearance();

        Color3f emissive = new Color3f(new Color(0,0, 0));
        Color3f ambient = new Color3f(Color.YELLOW);
        Color3f diffuse = new Color3f(Color.YELLOW);
        Color3f specular = new Color3f(Color.YELLOW);
        // ambient, emissive, diffuse, specular, 1.0f
        ap.setMaterial(new Material(ambient, emissive, diffuse, specular, 1.0f));

        return ap;
    }
}
