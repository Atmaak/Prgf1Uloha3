package objectdata;

import java.util.ArrayList;

public class Scene {
    private final ArrayList<Object3D> objects;
    public Scene(){
        objects = new ArrayList<>();
    }
    public void add(Object3D object3D){
        objects.add(object3D);
    }
    public ArrayList<Object3D> getObjects(){
        return objects;
    }
    public void remove(Object3D object3D){
        objects.remove(object3D);
    }
}
