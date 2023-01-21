import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;

/** A class full of some useful methods. */
public class Tools {

    /** Correctly type casts a double to an integer. */
    public static int integer(double n) {
        return (int) Math.round(n);
    }

    /**
     * Rounds a number. Yes it is the same as integer, I just like the different
     * names for different use cases.
     */
    public static int round(double n) {
        return integer(n);
    }

    /** Returns a random number between two end points. */
    public static double random(double low, double high) {
        return Math.random() * (high - low) + low;
    }

    /**
     * Returns a random number within a range centred at 0, -5 to 5 for example.
     * @param range Distance from 0 to the interval endpoint.
     * @return A double
     */
    public static double random(double range) {
        return Math.random() * 2 * range - range;
    }

    public static int random_int(int low, int high) {
        return (int) (Math.random() * (high - low + 1) + low);
    }

    /**
     * Colour gradient. No I didn't actually write all this, I used a website, cited
     * in my references.
     */
    public static Color[] gradient = new Color[] {
        new Color(250, 250, 110),
        new Color(196, 236, 116),
        new Color(146, 220, 126),
        new Color(100, 201, 135),
        new Color(57, 180, 142),
        new Color(8, 159, 143),
        new Color(0, 137, 138),
        new Color(8, 115, 127),
        new Color(33, 93, 110),
        new Color(42, 72, 88),
    };

    /**
     * Conveniently, JOptionPane reads in text as HTML. This method gets the text
     * from the introText.html file. That file has no comments (JOptionPane will not
     * ignore those) so I will write them here. That file explains the mechanics of
     * the simulation round, it is important for the user to understand what is
     * going on before they play around.
     */
    public static String getText() {
        try {
            return Files.readString(Path.of("data/introText.html"));
        } catch (Exception ioe) {
            throw new RuntimeException("Failed to read text.", ioe);
        }
    }

}