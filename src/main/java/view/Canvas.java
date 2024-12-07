package view;

import controls.Controls;
import objectdata.*;
import raster.Raster;
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
    private int x, y, newX, newY;
    private Controls controls;

    public Canvas(int width, int height){
        this.width = width;
        this.height = height;
        init();
        initListeners();
        initButtons();
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
        add(panel, BorderLayout.CENTER);
        controls = new Controls();
        panel.add(controls.getControls(), BorderLayout.PAGE_START);
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
                if(e.getKeyCode() == KeyEvent.VK_SPACE){
                    camera = camera.up(0.5);
                }
                if(e.getKeyCode() == KeyEvent.VK_SHIFT){
                    camera = camera.down(0.5);
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
                camera = camera.addZenith(-(y - newY) * Math.PI / 360); // TODO: nefunguje tak jak ma, hodí to nahoru hned po kliknuti

                draw();
             }
        });

        panel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                Mat4 scale;
                if (e.getWheelRotation() < 0) {
                    scale = new Mat4Scale(1.05, 1.05, 1.05);
                } else {
                    scale = new Mat4Scale(0.95, 0.95, 0.95);
                }
                model = model.mul(scale);
                draw();
            }
        });
    }

    private void initButtons(){
        controls.getReset().addActionListener(e -> reset());
        controls.getCube().addActionListener(e -> addObject(new Cube()));
        controls.getPyramid().addActionListener(e -> addObject(new Pyramid()));
        controls.getGrid().addActionListener(e -> addObject(new Grid()));
        controls.getBezier().addActionListener(e -> addObject(new BicubicGrid(Cubic.BEZIER, 0x458800)));
        controls.getFerguson().addActionListener(e -> addObject(new BicubicGrid(Cubic.FERGUSON, 0x450088)));
        controls.getCoons().addActionListener(e -> addObject(new BicubicGrid(Cubic.COONS, 0x004588)));
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
            return (view.getZ() > 0 ? angle : 2 * Math.PI - angle );
        })).orElse(0.0);
    }

    public void draw() {
        raster.clear();
        renderer3D.renderScene(raster, scene, camera.getViewMatrix(), projection, this.model);
        panel.repaint();
    }

    public void start(){
        raster.clear();
    }

    public void reset(){
        scene.clear();
        draw();
    }

    public void addObject(Object3D object){
        scene.add(object);
        draw();
    }
}