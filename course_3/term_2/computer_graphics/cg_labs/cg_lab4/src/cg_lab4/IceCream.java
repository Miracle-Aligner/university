package cg_lab4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.*;
import javax.swing.Timer;
import javax.vecmath.*;

// Vector3f - float, Vector3d - double
public class IceCream implements ActionListener {
    private float upperEyeLimit = 11.0f; // 5.0
    private float lowerEyeLimit = 11.0f; // 1.0
    private float farthestEyeLimit = 7.0f; // 6.0
    private float nearestEyeLimit = 4.0f; // 3.0

    private TransformGroup treeTransformGroup;
    private TransformGroup viewingTransformGroup;
    private Transform3D treeTransform3D = new Transform3D();
    private Transform3D viewingTransform = new Transform3D();
    private float angle = 0;
    private float eyeHeight;
    private float eyeDistance;
    private boolean descend = true;
    private boolean approaching = true;

    public static void main(String[] args) {
        new IceCream();
    }

    private IceCream() {
        Timer timer = new Timer(50, this);
        SimpleUniverse universe = new SimpleUniverse();

        viewingTransformGroup = universe.getViewingPlatform().getViewPlatformTransform();
        universe.addBranchGraph(createSceneGraph());

        eyeHeight = upperEyeLimit;
        eyeDistance = farthestEyeLimit;
        timer.start();
    }

    private BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();

        treeTransformGroup = new TransformGroup();
        treeTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        buildIceCream();
        objRoot.addChild(treeTransformGroup);

        Background background = new Background(new Color3f(0.9f, 0.9f, 0.9f)); // white color
        BoundingSphere sphere = new BoundingSphere(new Point3d(0,0,0), 100000);
        background.setApplicationBounds(sphere);
        objRoot.addChild(background);

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),100.0);
        Color3f light1Color = new Color3f(1.0f, 0.5f, 0.4f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);

        Color3f ambientColor = new Color3f(1.0f, 1.0f, 1.0f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);
        return objRoot;
    }

    private void buildIceCream() {

        Cone body = new Cone(-1, -4, Utils.getBodyAppearence());
        Transform3D bodyT = new Transform3D();
        bodyT.setTranslation(new Vector3f());
        bodyT.rotX(Math.PI / 2);
        TransformGroup bodyTG = new TransformGroup();
        bodyTG.setTransform(bodyT);
        bodyTG.addChild(body);



        Sphere ball = new Sphere(1.1f, Utils.getBallAppearence(Color.WHITE));
        Transform3D ballT = new Transform3D();
        ballT.setTranslation(new Vector3f(0, 4, 0));
        TransformGroup ballTG = new TransformGroup();
        ballTG.setTransform(ballT);
        ballTG.addChild(ball);
        bodyTG.addChild(ballTG);



        Sphere ball3 = new Sphere(1.1f, Utils.getBallAppearence(Color.GREEN));
        Transform3D ballT3 = new Transform3D();
        ballT3.setTranslation(new Vector3f(0, 3, 0));
        TransformGroup ballTG3 = new TransformGroup();
        ballTG3.setTransform(ballT3);
        ballTG3.addChild(ball3);
        bodyTG.addChild(ballTG3);

        Sphere ball2 = new Sphere(1.1f, Utils.getBallAppearence(Color.PINK));
        Transform3D ballT2 = new Transform3D();
        ballT2.setTranslation(new Vector3f(0, 2, 0));
        TransformGroup ballTG2 = new TransformGroup();
        ballTG2.setTransform(ballT2);
        ballTG2.addChild(ball2);
        bodyTG.addChild(ballTG2);


        Sphere pistacho1 = new Sphere(0.2f , Utils.getPistachoAppearence());
        Transform3D pistacho1T = new Transform3D();
        pistacho1T.setTranslation(new Vector3f(-0.7f, 0.6f , 0));
        TransformGroup pistacho1TG = new TransformGroup();
        pistacho1TG.setTransform(pistacho1T);
        pistacho1TG.addChild(pistacho1);

        Sphere pistacho2 = new Sphere(0.2f , Utils.getPistachoAppearence());
        Transform3D pistacho2T = new Transform3D();
        pistacho2T.setTranslation(new Vector3f(0.7f, 0.6f , 0));
        TransformGroup pistacho2TG = new TransformGroup();
        pistacho2TG.setTransform(pistacho2T);
        pistacho2TG.addChild(pistacho2);

        Sphere pistacho3 = new Sphere(0.2f , Utils.getPistachoAppearence());
        Transform3D pistacho3T = new Transform3D();
        pistacho3T.setTranslation(new Vector3f(0, 0.6f , -0.7f));
        TransformGroup pistacho3TG = new TransformGroup();
        pistacho3TG.setTransform(pistacho3T);
        pistacho3TG.addChild(pistacho3);


        ballTG.addChild(pistacho1TG);
        ballTG.addChild(pistacho2TG);
        ballTG.addChild(pistacho3TG);


        treeTransformGroup.addChild(bodyTG);

    }

    // ActionListener interface
    @Override
    public void actionPerformed(ActionEvent e) {
        float delta = 0.03f;

        // rotation of the castle
        treeTransform3D.rotZ(angle);
        treeTransformGroup.setTransform(treeTransform3D);
        angle += delta;

        // change of the camera position up and down within defined limits
        if (eyeHeight > upperEyeLimit){
            descend = true;
        }else if(eyeHeight < lowerEyeLimit){
            descend = false;
        }
        if (descend){
            eyeHeight -= delta;
        }else{
            eyeHeight += delta;
        }

        // change camera distance to the scene
        if (eyeDistance > farthestEyeLimit){
            approaching = true;
        }else if(eyeDistance < nearestEyeLimit){
            approaching = false;
        }
        if (approaching){
            eyeDistance -= delta;
        }else{
            eyeDistance += delta;
        }

        Point3d eye = new Point3d(eyeDistance, eyeDistance, eyeHeight); // spectator's eye
        Point3d center = new Point3d(.0f, .0f ,0.1f); // sight target
        Vector3d up = new Vector3d(.0f, .0f, 1.0f);; // the camera frustum
        viewingTransform.lookAt(eye, center, up);
        viewingTransform.invert();
        viewingTransformGroup.setTransform(viewingTransform);
    }
}