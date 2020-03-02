import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;

public class Minesweeper extends JPanel {

    private static final long serialVersionUID = 8501019418928408310L;

    private int width = 750;
    private int height = 800;
    private int topPanelHeight = 50;
   
    private int gridWidth = 9;
    private int gridHeight = 9;
    private int numOfBombs = 10;

    List<String> difficulties = Arrays.asList("Beginner 9 9 10", "Intermediate 16 16 40", "Expert 16 30 99");

    private JFrame frame;

    private Grid grid;
    private int flags;

    private JPanel topPanel;
    private JLabel flagsLabel;

    private boolean playing;


    public Minesweeper() {
        frame = new JFrame("Minesweeper");
        frame.setSize(width, height);
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Difficulty");

        menuBar.add(menu);
        for(String difficulty: difficulties) {
            String[] arr = difficulty.split(" ");
            final int newGridWidth = Integer.parseInt(arr[1]);
            final int newGridHeight = Integer.parseInt(arr[2]);
            final int newNumOfBombs = Integer.parseInt(arr[3]);
            JMenuItem menuItem = new JMenuItem(arr[0]);
            menuItem.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    gridWidth = newGridWidth;
                    gridHeight = newGridHeight;
                    numOfBombs = newNumOfBombs;
                    reset();
                }
                
            });
            menu.add(menuItem);
        }

        topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.setPreferredSize(new Dimension(frame.getWidth(), topPanelHeight));
        frame.add(topPanel, BorderLayout.NORTH);

        flags = numOfBombs;
        flagsLabel = new JLabel(flags + " flags left");
        topPanel.add(flagsLabel);

        playing = true;

        JButton button = new JButton();
        ImageIcon icon = new ImageIcon("./sprites/face.jpg");
        icon.setImage(icon.getImage().getScaledInstance(topPanelHeight, topPanelHeight, Image.SCALE_SMOOTH));
        button.setIcon(icon);
        topPanel.add(button);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
            
        });

        topPanel.add(new JLabel("Timer"));

        frame.setJMenuBar(menuBar);

        grid = new Grid(gridWidth, gridHeight, frame.getWidth(), frame.getHeight() - topPanelHeight, numOfBombs, this);

        frame.setVisible(true);
    }

    public void reset() {
        frame.remove(grid.getPanel());
        grid = new Grid(gridWidth, gridHeight, frame.getWidth(), frame.getHeight() - topPanelHeight, numOfBombs, this);
        setFlags(-1);
        setFlags(numOfBombs);
        playing = true;
    }

    public void gameOver() {
        playing = false;
        for(Tile tile: grid.getTileList()) {
            if(tile.isFlagged() && tile.getState() != Tile.BOMB_STATE) {
                tile.setFlagged(false);
                tile.getButton().setIcon(tile.createIcon("./sprites/not_mine.jpg"));
            }
            if(!tile.isRevealed())
                if(tile.getState() == Tile.BOMB_STATE)
                    tile.reveal();
                else
                    tile.getButton().setDisabledIcon(tile.getButton().getIcon());
            tile.getButton().setEnabled(false);
        }

    }
    
    public boolean isPlaying() {
        return playing;
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