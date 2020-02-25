import javax.swing.JFrame;
import javax.swing.JPanel;

public class Minesweeper extends JPanel {

    private static final long serialVersionUID = 8501019418928408310L;

    private int width = 800;
    private int height = 800;

    private JFrame frame;

    private Grid grid;

    public Minesweeper() {
        frame = new JFrame("Minesweeper");
        frame.setSize(width, height);
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        grid = new Grid(10, 10);
        frame.add(grid.getPanel());
        
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Minesweeper();
    }

}