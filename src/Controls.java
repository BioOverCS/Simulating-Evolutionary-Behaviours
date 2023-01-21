import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class features all the controls for the simulation. Has some buttons and
 * some sliders.
 */
public class Controls extends JPanel implements ActionListener, ChangeListener, ItemListener {
    private static final int WIDTH = 600, HEIGHT = 55, SEP = 10;
    Main frame;
    // These variables explained in the constructor below.
    JButton startStop = new JButton("Start"), reset = new JButton("Reset"), info = new JButton("Info");
    JCheckBox togMutations;
    JSlider foodSlider, spread;

    /** Constructor that gets everything going. */
    public Controls(Main frame) {
        this.frame = frame;
        setBackground(frame.getBackground());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);

        // Start and stop button, start and stops the simulation.
        startStop.setSize(230, 40);
        startStop.setLocation(SEP, SEP);
        startStop.addActionListener(this);
        add(startStop);

        // Reset button, resets the simulation.
        reset.setSize(100, startStop.getHeight());
        reset.setLocation(beside(startStop), SEP);
        reset.addActionListener(this);
        add(reset);

        // Info button, shows an info message.
        info.setSize(reset.getSize());
        info.setLocation(beside(reset), SEP);
        info.addActionListener(this);
        add(info);

        JSeparator line = new JSeparator(SwingConstants.VERTICAL); // A vertical line.
        line.setSize(1, startStop.getHeight());
        line.setLocation(beside(info), SEP);
        add(line);

        // Check box that toggles mutations.
        togMutations = new JCheckBox("Mutations");
        togMutations.setSize(100, reset.getHeight());
        togMutations.setHorizontalAlignment(SwingConstants.CENTER);
        togMutations.setVerticalAlignment(SwingConstants.CENTER);
        togMutations.setLocation(line.getX() + 1, SEP);
        togMutations.setMnemonic(KeyEvent.VK_M);
        togMutations.addItemListener(this);
        add(togMutations);

        // Label for the food count slider.
        JLabel foodCountLabel = new JLabel("Food Count");
        foodCountLabel.setSize(200, 20);
        foodCountLabel.setHorizontalAlignment(JLabel.CENTER);
        foodCountLabel.setLocation(beside(togMutations), 35);
        add(foodCountLabel);

        // Slider that controls how much food there is.
        foodSlider = new JSlider(JSlider.HORIZONTAL, 10, 100, 30);
        foodSlider.addChangeListener(this);
        foodSlider.setMajorTickSpacing(10);
        foodSlider.setMinorTickSpacing(2);
        foodSlider.setPaintTicks(true);
        foodSlider.setToolTipText("How much food spawns per round.");
        foodSlider.setSize(200, 40);
        foodSlider.setLocation(beside(togMutations), 0);
        add(foodSlider);

        // A label for the food density slider.
        JLabel foodDensityLabel = new JLabel("Food Density");
        foodDensityLabel.setSize(foodCountLabel.getSize());
        foodDensityLabel.setHorizontalAlignment(JLabel.CENTER);
        foodDensityLabel.setLocation(beside(foodCountLabel), foodCountLabel.getY());
        add(foodDensityLabel);

        /* A slider that controls the density of food spawning. JSlider doesn't take in
         * double so I'll just divide by 100 later. */
        spread = new JSlider(JSlider.HORIZONTAL, 40, 80, 60);
        spread.addChangeListener(this);
        spread.setMajorTickSpacing(10);
        spread.setMinorTickSpacing(2);
        spread.setPaintTicks(true);
        spread.setToolTipText("How spread apart the food is.");
        spread.setSize(foodSlider.getSize());
        spread.setLocation(beside(foodSlider), foodSlider.getY());
        add(spread);

        setFocusable(true);
        requestFocus();

    }

    /** This method gets the ideal X position to place a component, besides another one. */
    public int beside(Component comp) {
        return comp.getX() + comp.getWidth() + SEP;
    }

    /** Deals with action events. */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startStop) {
            if (!frame.plane.isRunning()) {
                frame.plane.startSimulation();
            } else {
                frame.plane.stopSimulation();
            }
        } else if (e.getSource() == reset) {
            frame.charts.clear();
            frame.plane.stopSimulation();
            frame.remove(frame.plane);
            frame.plane = new Plane(frame);
            frame.add(frame.plane);
            frame.pack();
        } else if (e.getSource() == info) {
            frame.infoMessage("Info");
        }
    }

    /** Deals with item events. */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == togMutations) {
            frame.plane.mutations = togMutations.isSelected();
        }
    }

    /** Deals with change events. */
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == foodSlider) {
            frame.plane.food_count = foodSlider.getValue();
        } else if (e.getSource() == spread) {
            frame.plane.food_density = spread.getValue() / 100.0;
        }
    }

}