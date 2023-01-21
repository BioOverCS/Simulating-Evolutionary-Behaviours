/**
 * A standard vector. Used mainly as positional vectors and velocity vectors.
 * Having vector objects allows for isolated arithmetic and clean code.
 */
public class Vector {
    private double x, y;

    /** The cosine of some angle theta, measured in degrees. */
    private static double cos(double theta) {
        return Math.cos(Math.toRadians(theta));
    }

    /** The side of some angle theta, measured in degrees. */
    private static double sin(double theta) {
        return Math.sin(Math.toRadians(theta));
    }

    /**
     * Math is generally easier with a vector's components, thinking about and
     * designing with a vector is much easier in polar coordinates, that is why this
     * constructor takes in polar coordinates despite the class using components.
     * @param mag   The magnitude of this vector
     * @param angle The angle of this vector from the positive x-axis.
     */
    public Vector(double mag, double angle) {
        this.x = mag * cos(angle);
        this.y = mag * sin(angle);
    }

    // Getters.
    public double x() { return x; }
    public double y() { return y; }
    public double magnitude() { return Math.hypot(x, y); }
    public double direction() { return Math.toDegrees(Math.atan2(y, x)); }

    /** Returns a random vector scaled by some factor. */
    public static Vector random(double scale) {
        return new Vector(scale * Math.random(), Tools.random(0, 360));
    }

    /**
     * Computes the distance between the tips of two vectors.
     * For whatever variable names such as v, w and z are common in linear algebra,
     * so having them as variable names here makes sense.
     */
    public static double distance(Vector v, Vector w) {
        return Math.hypot(v.x - w.x, v.y - w.y);
    }

    /** Returns the angle between this vector and another. */
    public double angleBetween(Vector v) {
        return Math.toDegrees(Math.atan2(v.y - this.y, v.x - this.x));
    }

    /** Adds another vector onto this vector. */
    public void plusEquals(Vector v) {
        this.x += v.x;
        this.y += v.y;
    }

    /** Computes the vector sum between two vectors. */
    public static Vector add(Vector v, Vector w) {
        return new Vector(w.x + v.x, w.y + v.y);
    }

    /** Shifts this vector by some angle, measured in degrees. */
    public Vector shift(double angle) {
        return new Vector(magnitude(), direction() + angle);
    }

    @Override
    public String toString() {
        return String.format("[%.1f, %.1f°]", magnitude(), direction());
    }

}