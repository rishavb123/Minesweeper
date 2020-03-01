import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;

public class Minesweeper extends JPanel {

    private static final long serialVersionUID = 8501019418928408310L;

    private int width = 800;
    private int height = 800;

    private JFrame frame;

    private Grid grid;
    private int flags;
    private JLabel flagsLabel;

    public Minesweeper() {
        frame = new JFrame("Minesweeper");
        frame.setSize(width, height);
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        grid = new Grid(10, 10, this);
        flags = 10;

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Hello world");
        menuBar.add(menu);
        JMenuItem menuItem = new JMenuItem("A text-only menu item");
        menu.add(menuItem);

        flagsLabel = new JLabel(flags + " flags left");
        frame.add(flagsLabel, BorderLayout.NORTH);
        
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
        flagsLabel.setText(flags + " flags left");
    }

    public void useFlag() {
        setFlags(flags - 1);
    }

    public void addFlag() {
        setFlags(flags + 1);
    }

}