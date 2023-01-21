import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Where the actual simulation happens. Creatures will live on this plane and
 * duke it out for food and survival. This class is heavily controlled through
 * the controls class, which is why many variables have been left with the
 * default package level protection. This class updates the charts after every
 * round. All the calculations done with the simulation are done with standard
 * cartesian coordinates, that is to say a coordinate system with an origin at
 * the very centre of the plane. That being said these calculations are still
 * done with an identical scale to the screen. When drawing all the items of a
 * simulation round, the coordinates are transformed to match the screen's
 * standard coordinates.
 */
public class Plane extends JPanel implements ActionListener {
    public static final int RADIUS = 220, SIZE = 500, CENTRE = SIZE / 2;
    ArrayList<Creature> creatures = new ArrayList<Creature>();
    ArrayList<Food> food = new ArrayList<Food>();
    int initial_pop = 5; // The initial amount of creatures to spawn.
    Main frame;
    Timer timer = new Timer(5, this);
    int food_count; // How much food spawns per round.
    boolean mutations; // If creatures should mutate or not.
    double food_density = 0.6;
    int rounds = 0; // A counter.

    public Plane(Main frame) {
        this.frame = frame;
        food_count = frame.controls.foodSlider.getValue();
        mutations = frame.controls.togMutations.isSelected();
        for (int i = 0; i < initial_pop; i++) {
            creatures.add(new Creature());
        }
        setPreferredSize(new Dimension(SIZE, SIZE));
        orientCreatures();
        repaint();
        frame.charts.update(0, 0, creatures);
    }

    /** Places all the creatures at edge of the plane. */
    public void orientCreatures() {
        double angle_diff = 360.0 / creatures.size();
        double angle_to_place = 0;
        Collections.shuffle(creatures);
        for (Creature c : creatures) {
            c.reset(angle_to_place);
            angle_to_place += angle_diff;
        }
    }

    /** Transforms a coordinate to the coordinate system used by the screen. */
    public static int displaceScale(double position) {
        return Tools.round(CENTRE + position);
    }

    /** Starts the simulation. */
    public void startSimulation() {
        for (int i = 0; i < food_count; i++) {
            food.add(new Food(Vector.random(food_density * RADIUS)));
        }
        timer.start();
        frame.controls.startStop.setText("Stop");
    }

    /** Stops the simulation. */
    public void stopSimulation() {
        timer.stop();
        food.clear();
        orientCreatures();
        repaint();
        frame.controls.startStop.setText("Start");
    }

    /** Is the simulation running? */
    public boolean isRunning() {
        return timer.isRunning();
    }

    /** What happens every timer the timer is triggered. */
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    /** The dynamics of each simulation round. */
    public void move() {
        int done = 0; // How many creatures have finished and do need to move any more.
        for (Creature c : creatures) {
            if (c.energy() <= 0 || c.meals() == 2 || c.position().magnitude() > RADIUS * 1.05) {
                done++;
            } else {
                if (c.target() != null) {
                    if (food.contains(c.target())) { // Checks if target hasn't been eaten by anyone else yet.
                        if (c.isCloseToTarget()) { // Eating.
                            food.remove(c.target());
                            c.eat();
                        }
                    } else {
                        c.setTarget(null);
                    }
                }
                if (c.target() == null) {
                    // Selecting the food closest to the creature for a target.
                    ArrayList<Food> visible = new ArrayList<Food>();
                    ArrayList<Double> lengths = new ArrayList<Double>();
                    for (Food f : food) {
                        double distance = Vector.distance(c.position(), f.position());
                        if (distance < c.sight()) {
                            visible.add(f);
                            lengths.add(distance);
                        }
                    }
                    if (lengths.size() > 0) {
                        c.setTarget(visible.get(lengths.indexOf(Collections.min(lengths))));
                    }
                }
                c.move();
            }
        }
        if (done == creatures.size()) {
            reset();
        }
    }

    /** Resets everything, updates the graphs and gets ready for the next round. */
    public void reset() {
        rounds++;
        timer.stop();
        food.clear();
        ArrayList<Creature> to_add = new ArrayList<Creature>();
        ArrayList<Creature> to_remove = new ArrayList<Creature>();
        for (Creature c : creatures) {
            if (c.meals() == 2) {
                to_add.add(mutations ? c.mutate() : c.clone());
            } else if (c.meals() == 0) {
                to_remove.add(c);
            }
        }
        creatures.removeAll(to_remove);
        creatures.addAll(to_add);
        frame.charts.update(rounds, to_add.size() - to_remove.size(), creatures);
        if (creatures.size() == 0) {
            stopSimulation();
            JOptionPane.showMessageDialog(frame,
            "The civilization has gone extinct. Consider resetting the environment.",
            "Creatures could not survive.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        orientCreatures();
        // Spawns more food.
        for (int i = 0; i < food_count; i++) {
            food.add(new Food(Vector.random(food_density * RADIUS)));
        }
        timer.start();
    }

    /** Draws all the elements of the simulation. */
    public void paint(Graphics g) {
        // The radius scaled up so it visually encompasses everything.
        int adjusted_radius = Tools.round(RADIUS * 1.1);
        g.setColor(frame.getBackground());
        g.fillRect(0, 0, SIZE, SIZE);
        g.setColor(Color.darkGray);
        g.fillOval(
                SIZE / 2 - adjusted_radius,
                SIZE / 2 - adjusted_radius,
                adjusted_radius * 2,
                adjusted_radius * 2);
        // That gradient on the right side of the screen.
        for (int i = 0; i < Tools.gradient.length; i++) {
            g.setColor(Tools.gradient[i]);
            g.fillRect(
                SIZE - 5,
                i * SIZE / Tools.gradient.length,
                5, SIZE / Tools.gradient.length);
        }
        for (Creature c : creatures) {
            c.draw(g);
        }
        for (Food banana : food) {
            banana.draw(g);
        }
    }

}