package view;

import objectdata.Scene;
import raster.Raster;
import rasterize.Liner;
import rasterops.Renderer3D;
import transforms.Camera;
import transforms.Mat4PerspRH;
import transforms.Vec2D;
import transforms.Vec3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Canvas extends JFrame {
    int width, height;
    Raster raster;
    Panel panel;
    Liner liner;
    Scene scene;
    Renderer3D renderer3D;
    Camera camera;

    private int x,y;

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

        raster = new Raster(width, height);
        renderer3D = new Renderer3D();
        panel = new Panel(raster);
        scene = new Scene();
        add(panel, BorderLayout.CENTER);
        pack();
        setVisible(true);
        draw();
    }

    private void initListeners(){
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double dx = e.getX() - x;
                double dy = e.getY() - y;
                camera.addAzimuth(dx / 100).addZenith(dy / 100);
                x = (int)dx;
                y = (int)dy;
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
            return (view.getZ() > 0 ? angle : 2 * Math.PI - angle );
        })).orElse(0.0);
    }

    public void draw (){
        raster.clear();
        Vec3D observerPosition = new Vec3D(2, 3, 2);

        camera = new Camera().withPosition(observerPosition).withAzimuth(azimuthToOrigin(observerPosition)).withZenith(zenithToOrigin(observerPosition));

        renderer3D.renderScene(raster, scene, camera.getViewMatrix(), new Mat4PerspRH(Math.PI / 2, (double) raster.getHeight() / raster.getWidth(), 0.01, 100), liner, 0xff0000);
        panel.repaint();
    }

    public void start(){
        raster.clear();
    }
}