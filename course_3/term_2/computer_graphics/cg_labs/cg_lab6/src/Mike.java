import javax.vecmath.*;

import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.behaviors.vp.*;

import javax.swing.JFrame;
import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;

import java.util.Hashtable;
import java.util.Enumeration;

public class Mike extends JFrame
{
    public Canvas3D myCanvas3D;

    public Mike() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myCanvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

        SimpleUniverse simpUniv = new SimpleUniverse(myCanvas3D);

        simpUniv.getViewingPlatform().setNominalViewingTransform();

        createSceneGraph(simpUniv);

        addLight(simpUniv);

        OrbitBehavior ob = new OrbitBehavior(myCanvas3D);
        ob.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE));
        simpUniv.getViewingPlatform().setViewPlatformBehavior(ob);

        setTitle("cg-lab6");
        setSize(800, 600);
        getContentPane().add("Center", myCanvas3D);
        setVisible(true);
    }
    public static void main(String[] args)
    {

        Mike mike = new Mike();
    }

    public void createSceneGraph(SimpleUniverse su)
    {

        ObjectFile f = new ObjectFile(ObjectFile.RESIZE);
        Scene mikeScene = null;
        try
        {
            mikeScene = f.load("models/mike.obj");
        }
        catch (Exception e)
        {
            System.out.println("File loading failed:" + e);
        }

        Transform3D scaling = new Transform3D();
        scaling.setScale(1.0/5);
        Transform3D tfMike = new Transform3D();
        tfMike.rotX(Math.PI/5);
        tfMike.mul(scaling);
        TransformGroup tgMike = new TransformGroup(tfMike);

        TransformGroup sceneGroup = new TransformGroup();
        Hashtable mikeNamedObjects = mikeScene.getNamedObjects();
        Enumeration enumer = mikeNamedObjects.keys();
        String name;
        while (enumer.hasMoreElements())
        {
            name = (String) enumer.nextElement();
            System.out.println("Name: "+name);
        }

        Shape3D leftLeg = (Shape3D) mikeNamedObjects.get("left_leg");
        Shape3D rightLeg = (Shape3D) mikeNamedObjects.get("right_leg");
        Shape3D leftHand = (Shape3D) mikeNamedObjects.get("left_hand");
        Shape3D rightHand = (Shape3D) mikeNamedObjects.get("right_hand");
        Shape3D body = (Shape3D) mikeNamedObjects.get("monstr");

        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);

        TransformGroup transformGroup = new TransformGroup();
        transformGroup.addChild(body.cloneTree());


        TransformGroup leftLegGr = new TransformGroup();
        TransformGroup rightLegGr = new TransformGroup();
        TransformGroup leftHandGr = new TransformGroup();
        TransformGroup rightHandGr = new TransformGroup();
        leftLegGr.addChild(leftLeg.cloneTree());
        rightLegGr.addChild(rightLeg.cloneTree());
        leftHandGr.addChild(leftHand.cloneTree());
        rightHandGr.addChild(rightHand.cloneTree());


        BoundingSphere bounds = new BoundingSphere(new Point3d(120.0,250.0,100.0),Double.MAX_VALUE);
        BranchGroup theScene = new BranchGroup();
        Transform3D tCrawl = new Transform3D();
        Transform3D tCrawl1 = new Transform3D();
        tCrawl.rotY(-Math.PI/2);
        long crawlTime = 10000;
        Alpha crawlAlpha = new Alpha(1,
                Alpha.INCREASING_ENABLE,
                0,
                0, crawlTime,0,0,0,0,0);
        float crawlDistance = 8.0f;
        PositionInterpolator posICrawl = new PositionInterpolator(crawlAlpha,
                sceneGroup,tCrawl, -8.0f, crawlDistance);


        BoundingSphere bs = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);
        posICrawl.setSchedulingBounds(bs);
        sceneGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        sceneGroup.addChild(posICrawl);


        int timeStart = 500;
        int timeRotationHour = 500;

        Transform3D leftLegRotationAxis = new Transform3D();
        leftLegRotationAxis.rotZ(Math.PI / 2);
        Alpha leftLegRotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, timeStart, 0,
                timeRotationHour, 0, 0, timeRotationHour, 0, 0);
        RotationInterpolator leftLegRotation = new RotationInterpolator(leftLegRotationAlpha, leftLegGr,
                leftLegRotationAxis, (float) Math.PI / 4, 0.0f);
        leftLegRotation.setSchedulingBounds(bounds);


        Transform3D rightHandRotationAxis = new Transform3D();
        rightHandRotationAxis.rotZ(Math.PI / 2);
        RotationInterpolator rightHandRotation = new RotationInterpolator(leftLegRotationAlpha, rightHandGr,
                rightHandRotationAxis, (float) Math.PI / 4, 0.0f);
        rightHandRotation.setSchedulingBounds(bounds);


        Transform3D rightLegRotationAxis = new Transform3D();
        rightLegRotationAxis.rotZ(Math.PI / 2);
        Alpha rightLegRotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0,
                timeRotationHour, 0, 0, timeRotationHour, 0, 0);
        RotationInterpolator rightLegRotation = new RotationInterpolator(rightLegRotationAlpha, rightLegGr,
                rightLegRotationAxis, (float) Math.PI / 4, 0.0f);
        rightLegRotation.setSchedulingBounds(bounds);

        Transform3D leftHandRotationAxis = new Transform3D();
        leftHandRotationAxis.rotZ(Math.PI / 2);
        RotationInterpolator leftHandRotation = new RotationInterpolator(rightLegRotationAlpha, leftHandGr,
                leftHandRotationAxis, (float) Math.PI / 4, 0.0f);
        leftHandRotation.setSchedulingBounds(bounds);


        leftLegGr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        rightLegGr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        leftHandGr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        rightHandGr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        leftLegGr.addChild(leftLegRotation);
        rightLegGr.addChild(rightLegRotation);
        leftHandGr.addChild(leftHandRotation);
        rightHandGr.addChild(rightHandRotation);

        sceneGroup.addChild(transformGroup);
        sceneGroup.addChild(leftLegGr);
        sceneGroup.addChild(rightLegGr);
        sceneGroup.addChild(leftHandGr);
        sceneGroup.addChild(rightHandGr);
        tgMike.addChild(sceneGroup);
        theScene.addChild(tgMike);

        TextureLoader myLoader = new  TextureLoader("img/bg4.jpeg",this);
        ImageComponent2D myImage = myLoader.getImage( );

        Background bg = new Background();
        bg.setImage(myImage);

        bg.setApplicationBounds(bounds);
        theScene.addChild(bg);
        theScene.compile();

        su.addBranchGraph(theScene);
    }


    public void addLight(SimpleUniverse su)
    {
        BranchGroup bgLight = new BranchGroup();

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
        Color3f lightColour1 = new Color3f(1.5f,2.0f,0.5f);
        Vector3f lightDir1 = new Vector3f(0.0f,0.0f,-0.5f);
        DirectionalLight light1 = new DirectionalLight(lightColour1, lightDir1);
        light1.setInfluencingBounds(bounds);

        bgLight.addChild(light1);
        su.addBranchGraph(bgLight);
    }
}
