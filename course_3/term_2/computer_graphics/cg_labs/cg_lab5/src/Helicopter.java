import com.sun.j3d.utils.universe.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JFrame;

public class Helicopter extends JFrame {
    private static Canvas3D canvas;
    private static SimpleUniverse universe;
    private static BranchGroup root;

    private static TransformGroup helicopter;

    public Helicopter() throws IOException {
        configureWindow();
        configureCanvas();
        configureUniverse();

        root = new BranchGroup();

        addImageBackground();

        addDirectionalLightToUniverse();
        addAmbientLightToUniverse();

        ChangeViewAngle();

        helicopter = getHelicopterGroup("helicopter", "model/helicopter.obj");
        root.addChild(helicopter);

        root.compile();
        universe.addBranchGraph(root);
    }

    private void configureWindow() {
        setTitle("cg_lab5");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void configureCanvas() {
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.setDoubleBufferEnable(true);
        getContentPane().add(canvas, BorderLayout.CENTER);
    }

    private void configureUniverse() {
        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
    }

    private void addImageBackground() {
        TextureLoader t = new TextureLoader("img/landing.jpg", canvas);
        Background background = new Background(t.getImage());
        background.setImageScaleMode(Background.SCALE_FIT_ALL);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        background.setApplicationBounds(bounds);
        root.addChild(background);
    }

    private void addDirectionalLightToUniverse() {
        BoundingSphere bounds = new BoundingSphere (new Point3d (0.0, 0.0, 0.0), 1000000.0);

        DirectionalLight light = new DirectionalLight(new Color3f(1, 1, 1), new Vector3f(-1, -1, -1));
        light.setInfluencingBounds(bounds);

        root.addChild(light);
    }

    private void addAmbientLightToUniverse() {
        AmbientLight light = new AmbientLight(new Color3f(1, 1, 1));
        light.setInfluencingBounds(new BoundingSphere());
        root.addChild(light);
    }

    private void ChangeViewAngle() {
        ViewingPlatform vp = universe.getViewingPlatform();
        TransformGroup vpGroup = vp.getMultiTransformGroup().getTransformGroup(0);
        Transform3D vpTranslation = new Transform3D();
        vpTranslation.setTranslation(new Vector3f(0, 0, 6));
        vpGroup.setTransform(vpTranslation);
    }

    private TransformGroup getHelicopterGroup(String name, String path) throws IOException {
        Scene scene = getSceneFromFile(path);
        Hashtable helicopterObjects = scene.getNamedObjects();

        Transform3D tfHelicopter = new Transform3D();
        TransformGroup tgHelicopter = new TransformGroup(tfHelicopter);

        TransformGroup sceneGroup = new TransformGroup();

        Shape3D propellers = (Shape3D) helicopterObjects.get("propellers");
        Shape3D helicopter = (Shape3D) helicopterObjects.get("helicopter");

        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);

        TransformGroup transformGroup = new TransformGroup();
        transformGroup.addChild(helicopter.cloneTree());

        TransformGroup propellersGroup = new TransformGroup();
        propellersGroup.addChild(propellers.cloneTree());

        int timeStart = 500;
        int timeRotationHour = 5;

        Transform3D propellerRotationAxis = new Transform3D();
        propellerRotationAxis.rotZ(0);
        Transform3D changeAxis = new Transform3D();
        changeAxis.setTranslation(new Vector3d(-0.000000008f, -2.0f, 0.3005f));

        propellerRotationAxis.mul(changeAxis);

        Alpha propellerRotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, timeStart, 0,
                timeRotationHour, 0, 0, timeRotationHour, 0, 0);
        RotationInterpolator propellerRotation = new RotationInterpolator(propellerRotationAlpha, propellersGroup,
                propellerRotationAxis, 0.0f, 1.0f);

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);
        propellerRotation.setSchedulingBounds(bounds);

        propellersGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        propellersGroup.addChild(propellerRotation);

        sceneGroup.addChild(transformGroup);
        sceneGroup.addChild(propellersGroup);
        tgHelicopter.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        tgHelicopter.addChild(sceneGroup);

        tgHelicopter.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        Transform3D transform3D = new Transform3D();
        transform3D.setScale(new Vector3d(0.9, 0.9, 0.9));
        tgHelicopter.setTransform(transform3D);

        printModelElementsList(helicopterObjects);

        return tgHelicopter;
    }

    private Scene getSceneFromFile(String path) throws IOException {
        ObjectFile file = new ObjectFile(ObjectFile.RESIZE);

        return file.load(new FileReader(path));
    }

    private void printModelElementsList(Map<String, Shape3D> map) {
        for (String name : map.keySet()) {
            System.out.println("Name: " + name);
        }
    }

    public static void main(String[] args) {
        try {
            Helicopter window = new Helicopter();
            HelicopterAnimation helicopterMovement = new HelicopterAnimation(helicopter);
            canvas.addKeyListener(helicopterMovement);
            window.setVisible(true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}