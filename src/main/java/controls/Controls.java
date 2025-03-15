package controls;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Controls {
    private JToolBar controls;
    private JButton reset;
    private JButton cube;
    private JButton pyramid;
    private JButton bezier;
    private JButton ferguson;
    private JButton coons;
    private JButton fillable;

    public Controls() {
        controls = new JToolBar();

        reset = new JButton("Reset");
        cube = new JButton("Cube");
        pyramid = new JButton("Pyramid");
        bezier = new JButton("Bezier");
        ferguson = new JButton("Ferguson");
        coons = new JButton("Coons");
        fillable = new JButton("Fillable");

        controls.add(reset);
        controls.add(cube);
        controls.add(pyramid);
        controls.add(bezier);
        controls.add(ferguson);
        controls.add(coons);
        controls.add(fillable);

        reset.setFocusable(false);
        cube.setFocusable(false);
        pyramid.setFocusable(false);
        bezier.setFocusable(false);
        ferguson.setFocusable(false);
        coons.setFocusable(false);
        fillable.setFocusable(false);
    }

    public JToolBar getControls() {
        return controls;
    }

    public JButton getReset() {
        return reset;
    }

    public JButton getCube() {
        return cube;
    }

    public JButton getPyramid() {
        return pyramid;
    }

//    public JButton getBezier() {
//        return bezier;
//    }

//    public JButton getFerguson() {
//        return ferguson;
//    }

//    public JButton getCoons(){
//        return coons;
//    }

    public JButton getFillable() { return fillable; }
}
