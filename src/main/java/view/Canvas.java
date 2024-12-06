package view;

import objectdata.Cube;
import objectdata.Pyramid;
import objectdata.Scene;
import raster.Raster;
import rasterize.Liner;
import rasterops.Renderer3D;
import transforms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Canvas extends JFrame {
    int width, height;
    Raster raster;
    Panel panel;

    Mat4 projection;

    Scene scene;
    Renderer3D renderer3D;
    Camera camera;
    private Mat4 model;
    private int x = 400, y = 400, newX = 400, newY = 400;


    public Canvas(int width, int height){
        this.width = width;
        this.height = height;
        init();
        initListeners();
    }

    private void init(){
        setLayout(new BorderLayout());
        setTitle("PGRF");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        model = new Mat4Identity();

        raster = new Raster(width, height);

        projection = new Mat4PerspRH(Math.PI / 2, (double) raster.getHeight() / raster.getWidth(), 0.01, 100);
        Vec3D observerPosition = new Vec3D(4, 6, 2);

        camera = new Camera(observerPosition, azimuthToOrigin(observerPosition), zenithToOrigin(observerPosition), 1, true);

        renderer3D = new Renderer3D();
        panel = new Panel(raster);
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        scene = new Scene();
        scene.add(new Cube());
        add(panel, BorderLayout.CENTER);
        pack();
        setVisible(true);
        draw();
    }

    private void initListeners(){
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_W){ // up
                    camera = camera.forward(0.5);
                }
                if(e.getKeyCode() == KeyEvent.VK_A){
                    camera = camera.left(0.5);
                }
                if(e.getKeyCode() == KeyEvent.VK_S){
                    camera = camera.backward(0.5);
                }
                if(e.getKeyCode() == KeyEvent.VK_D){
                    camera = camera.right(0.5);
                }
                draw();
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                draw();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
             public void mouseDragged(MouseEvent e) {
                 newX = x;
                 newY = y;
                 x = e.getX();
                 y = e.getY();

                 camera = camera.addAzimuth(-(x - newX) * Math.PI / 360);
                 camera = camera.addZenith(-(y - newY) * Math.PI / 360); // TODO: nefunguje tak jak ma, jebne to nahoru hned po kliknuti

                 draw();
             }
        });


    }

    private double azimuthToOrigin(Vec3D observerPosition){
        Vec3D viewVector = observerPosition.opposite();
        return viewVector.ignoreZ().normalized().map(viewNormalized -> {
            double angle = Math.acos(viewNormalized.dot(new Vec2D(1,0)));
            return (viewNormalized.getY() > 0 ? angle : 2 * Math.PI - angle );
        }).orElse(0.0);
    }

    private double zenithToOrigin(Vec3D observerPosition){
        Vec3D viewVector = observerPosition.opposite();
        return viewVector.normalized().flatMap(view -> view.withZ(0).normalized().map(projetion -> {
            double angle = Math.acos(view.dot(projetion));
            System.out.println(angle);
            return (view.getZ() > 0 ? angle : 2 * Math.PI - angle );
        })).orElse(0.0);
    }

    public void draw() {
        raster.clear();
        scene.add(new Pyramid());
        renderer3D.renderScene(raster, scene, camera.getViewMatrix(), projection, this.model);
        panel.repaint();
    }

    public void start(){
        raster.clear();
    }
}