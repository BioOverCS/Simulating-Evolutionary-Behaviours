import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * The main class of this project. This frame uses three panels to display the
 * three different aspects of this project: the plane, the controls and the
 * charts. The challenge is that these three panels cannot be in the same class
 * because of various limitations, however these three panels are all part of
 * one unit, and must "speak" with each other. That is why this class is passed
 * into the three panels, and they can "speak" with each other, with this class
 * acting as an intermediary.
 */
public class Main extends JFrame {

    Controls controls = new Controls(this);
    Charts charts = new Charts();
    Plane plane = new Plane(this);

    public Main() {
        super("Simulating Evolutionary Behaviours");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("Images/icon.png").getImage());
        add(controls, BorderLayout.NORTH);
        add(charts, BorderLayout.WEST);
        add(plane, BorderLayout.EAST);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        // Shows the info message upon starting the program.
        infoMessage("Let's Get Started");
    }

    /** Displays important information about this project through a JOptionPane. */
    public void infoMessage(String title) {
        JOptionPane.showMessageDialog(
            this,
            Tools.getText().replaceAll("\n", ""),
            title,
            JOptionPane.PLAIN_MESSAGE
        );
    }
    
    public static void main(String[] m) {
        new Main();
    }

}