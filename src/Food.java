import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics;

/** A food object. Has a position, that's about it.*/
public class Food {
    public Vector pos; // Position
    final Image image;
    private static final int image_size = 20;

    public Food(Vector position) {
        this.pos = position;
        // Randomly selects a fruit image to use, 
        String[] fruits = {"banana", "apple", "watermelon"};
        image = new ImageIcon(
            "Images/" + fruits[Tools.random_int(0, fruits.length - 1)] + ".png")
            .getImage()
            .getScaledInstance(image_size, image_size, Image.SCALE_FAST);
    }

    /** Returns the position vector of the food. */
    public Vector position() { return pos; }

    /** Draws the food. */
    public void draw(Graphics g) {
        g.drawImage(
            image,
            Plane.displaceScale(pos.x() - image_size / 2),
            Plane.displaceScale(pos.y() - image_size / 2),
            image_size,
            image_size,
            null);
    }

    @Override
    public String toString() {
        return pos.toString();
    }

}

// Yup, that's about it.