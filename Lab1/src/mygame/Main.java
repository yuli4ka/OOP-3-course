package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;


public class Main extends SimpleApplication {
  
  public static void main(String args[]) {
    Main app = new Main();
    app.start();
  }
  
  
  private BulletAppState bulletAppState;
  
  Material cylinder_mat;
  Material plane_mat;
  
  private RigidBodyControl    cylinder_phy;
  private static final Cylinder cylinder;
  private RigidBodyControl    plane_phy;
  private static final Box    plane;
  
  static {
      cylinder = new Cylinder(50, 50, 1.f, 5.f, true, false); 
      
      // задаємо площину
      plane = new Box(10f, 0.1f, 6f);
     plane.scaleTextureCoordinates(new Vector2f(3, 4));
  }


    @Override
  public void simpleInitApp() {
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    
    //напрямлене світло
    DirectionalLight sun = new DirectionalLight();
    sun.setColor(ColorRGBA.White);
    sun.setDirection(new Vector3f(0,-10,-10).normalizeLocal());
    rootNode.addLight(sun);

    //робимо камеру рухливою
    flyCam.setEnabled(true);   
    
    //задаємо позицію камери
    cam.setLocation(new Vector3f(20,10,15));
    
    //при нажатті лівої кнопки миші - ініціалізація циліндра
    inputManager.addMapping("go",new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addListener(actionListener, "go");
    
    //ініціалізація матеріалів і похилої площини
    initMaterials();
    initPlane();
  }
  
  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean keyPressed, float tpf) {
      if (name.equals("go") && !keyPressed) {
       initCylinder();
      }
    }
  };
  
  public void initMaterials() {
    
  //задаємо текстуру для циліндра, робимо його поверхню світловідбиваючою/дзеркальною
    cylinder_mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    cylinder_mat.setBoolean("UseMaterialColors",true);
    cylinder_mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Cylinder/images.jpg"));
    cylinder_mat.setColor("Diffuse",ColorRGBA.White);
    cylinder_mat.setColor("Specular",ColorRGBA.White);
    cylinder_mat.setFloat("Shininess", 50f);  

    //задаємо текстуру і інше для пох. площини
   plane_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
   TextureKey key = new TextureKey("Textures/Plane/yoba.png");
   key.setGenerateMips(true);
   Texture tex = assetManager.loadTexture(key);
   tex.setWrap(WrapMode.Repeat);
   plane_mat.setTexture("ColorMap", tex);
  }
  
  public void initPlane() {
      //створюємо пох. площину
    Geometry plane_geo = new Geometry("Plane", plane);
    plane_geo.setMaterial(plane_mat);
    //задаємо положення
    plane_geo.setLocalTranslation(5, 0, 0);
    this.rootNode.attachChild(plane_geo);
    
    plane_geo.rotate(0, 0, -FastMath.PI/2+FastMath.PI/40); //
    //створення фізики для площини
    plane_phy = new RigidBodyControl(0.0f); //маса
    plane_geo.addControl(plane_phy);
    bulletAppState.getPhysicsSpace().add(plane_phy);
    plane_phy.setFriction(1.f);
    plane_phy.setRestitution(0.4f);
  }
  
  //ініціалізація циліндра
  public void initCylinder() {
    Geometry cyl_geo = new Geometry("cyl", cylinder);
    cyl_geo.setMaterial(cylinder_mat);
    rootNode.attachChild(cyl_geo);
    //початкова позицію
    cyl_geo.setLocalTranslation(new Vector3f(5.2f, 15.f, 0.f)); //
    //фізика для циліндра
    cylinder_phy = new RigidBodyControl(10f); //маса
    cyl_geo.addControl(cylinder_phy);
    bulletAppState.getPhysicsSpace().add(cylinder_phy);
    cylinder_phy.setFriction(1.f);
    cylinder_phy.setRestitution(0.5f);
  }    
}
