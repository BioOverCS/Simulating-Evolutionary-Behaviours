import java.awt.*;

public class Creature {
    private Vector pos; // Position
    private Vector velocity;
    private double speed;
    private double sight;
    private int energy;
    private Food target;
    private int meals = 0;
    private Color colour;

    /** Constructor that initializes speed, sight and colour. */
    public Creature(double speed, double sight) {
        this.speed = speed;
        this.sight = sight;
        int min = 30, max = 135; // Approximately the smallest and largest values a Creature could get.
        double normalized = (energyConsumption() - min) / (max - min) * (Tools.gradient.length - 1);
        if (normalized < 0) {
            normalized = 0;
        } else if (normalized > Tools.gradient.length - 1) {
            normalized = Tools.gradient.length - 1;
        }
        // Selects a color from the gradient.
        this.colour = Tools.gradient[Tools.round(normalized)];
    }

    /** Alternate constructor when not cloning or mutating. */
    public Creature() {
        this(3, 10);
    }

    // Getters.
    public Vector position() { return pos; }
    public int energy() { return energy; }
    public Food target() { return target; }
    public double sight() { return sight; }
    public double speed() { return speed; }
    public int meals() { return meals; }

    /** Sets a target. */
    public void setTarget(Food food) {
        target = food;
        if (food != null)
        changeDirection(pos.angleBetween(food.position()));
    }

    // Changes this creature's direction.
    public void changeDirection(double angle) {
        velocity = new Vector(speed, angle);
    }

    /** Moves this creature. */
    public void move() {
        if (target == null) {
            // Randomly shifts the velocity.
            velocity = velocity.shift(Tools.random(2.5));
        }
        pos.plusEquals(velocity);
        energy -= Tools.round(energyConsumption());
    }

    /**
     * How much energy is consumed per step. I could have chosen any function of
     * speed and sight for this, I think this one makes sense in theory.
     */
    public double energyConsumption() {
        return speed * speed + 2 * sight;
    }

    /** Can this creature see a food? */
    public boolean canSee(Food food) {
        return Vector.distance(pos, food.position()) < sight;
    }

    /** Is this creature close to its target? */
    public boolean isCloseToTarget() {
        return Vector.distance(pos, target.position()) < 10;
    }

    /** The necessary changes for when a creature eats a food. */
    public void eat() {
        target = null;
        meals++;
    }

    /** Resets a creature to the edge of the plane. */
    public void reset(double angle) {
        meals = 0;
        energy = 2500; // I think this value makes sense based on experimentation.
        pos = new Vector(Plane.RADIUS, angle);
        changeDirection(angle + 180);
    }

    /**
     * Produces a nearly identical copy of this Creature, but with slightly
     * different speed and sight values, which are randomly altered.
     */
    public Creature mutate() {
        double new_speed = this.speed + Tools.random(0.5);
        double new_sense = this.sight + Tools.random(1.5);
        return new Creature(
            new_speed > 0 ? new_speed : speed,
            new_sense > 0 ? new_sense : sight);
    }

    /** Produces a replica of this creature. */
    public Creature clone() {
        return new Creature(this.speed, this.sight);
    }

    /** Draws the creature. */
    public void draw(Graphics g) {
        g.setColor(colour);
        int size = 6;
        /* I like rectangles for this. In the past I have used images of cookie monster,
         * but I think this is better because that then I would have to use images
         * of cookies for food, and that is sending a bad message. */
        g.fillRect(Plane.displaceScale(pos.x() - size/2), Plane.displaceScale(pos.y() - size/2), size, size);
    }

    @Override
    public String toString() {
        return String.format("%.1f, %.0f, %s", speed, sight, pos.toString());
    }

}