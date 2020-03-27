import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;

public class Minesweeper extends JPanel {

    private static final long serialVersionUID = 8501019418928408310L;

    private int topPanelHeight = 50;

    private int gridWidth = 9;
    private int gridHeight = 9;
    private int numOfBombs = 10;

    List<String> difficulties = Arrays.asList("Beginner 9 9 10", "Intermediate 16 16 40", "Expert 30 16 99");
    List<String> iconFolders = Arrays.asList("Default", "Mario Kart", "Magic The Gathering");

    private JFrame frame;

    private Grid grid;
    private int flags;

    private JPanel topPanel;
    private JLabel flagsLabel;
    private JLabel timeLabel;
    private JButton smileButton;

    private boolean playing;
    private int time;

    private Timer timer;

    private int numSelected;

    private String iconFolder = "Default";

    public Minesweeper() {
        frame = new JFrame("Minesweeper");
        frame.setSize(Tile.WIDTH * gridWidth, Tile.WIDTH * gridHeight + topPanelHeight);
        frame.add(this);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Game");
        JMenu icons = new JMenu("Icon");
        JMenu controls = new JMenu("Controls");

        menuBar.add(menu);
        menuBar.add(icons);
        menuBar.add(controls);

        for (String difficulty : difficulties) {
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
                    frame.setSize(Tile.WIDTH * gridWidth, Tile.WIDTH * gridHeight + topPanelHeight);
                    reset();
                }

            });
            menu.add(menuItem);
        }
        JTextField field = new JTextField();
        field.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String[] arr = e.getActionCommand().split(" ");
                    int nGridWidth = Integer.parseInt(arr[0]);
                    int nGridHeight = Integer.parseInt(arr[1]);
                    int nNumOfBombs = Integer.parseInt(arr[2]);
                    if(nGridWidth * nGridHeight - 9 < nNumOfBombs || nGridWidth < 0 || nGridHeight < 0 || nNumOfBombs < 0) throw new Exception();
                    gridWidth = nGridWidth;
                    gridHeight = nGridHeight;
                    numOfBombs = nNumOfBombs;
                    frame.setSize(Tile.WIDTH * gridWidth, Tile.WIDTH * gridHeight + topPanelHeight);
                    reset();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(frame, "Make sure to format your input as follows:\n{grid width} {grid height} {num of bombs}\nand also ensure that there are enough squares for the bombs to fit even after the first 9 squares are expanded.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
                field.setText("");
            }
            
        });
        menu.add(field);

        for(String iconFolder: iconFolders) {
            JMenuItem menuItem = new JMenuItem(iconFolder);
            menuItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    setIconFolder(iconFolder);
                }
                
            });
            icons.add(menuItem);
        }

        JTextArea textArea = new JTextArea("   - Left-click an empty square to reveal it.\n   - Right-click an empty square to flag it.\n   - Press the middle top button to restart the game.   ");
        textArea.setEditable(false);
        controls.add(textArea);

        topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.setPreferredSize(new Dimension(frame.getWidth(), topPanelHeight));
        frame.add(topPanel, BorderLayout.NORTH);

        flags = numOfBombs;
        flagsLabel = new JLabel(flags + " flags left", SwingConstants.CENTER);
        topPanel.add(flagsLabel);

        playing = true;

        timer = new Timer();

        smileButton = new JButton();
        smileButton.setPreferredSize(new Dimension(topPanelHeight, topPanelHeight));
        
        setSmileIcon("./" + iconFolder + "/face.png");

        JPanel tempPanel = new JPanel();
        tempPanel.setBackground(Color.BLACK);
        tempPanel.add(smileButton);
        topPanel.add(tempPanel);
        smileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }

        });

        timeLabel = new JLabel("000", SwingConstants.CENTER);

        Font font = Util.createFont("./fonts/digital.ttf").deriveFont(25f);
        timeLabel.setFont(font);
        timeLabel.setForeground(Color.RED);

        flagsLabel.setFont(font.deriveFont(15f));
        flagsLabel.setForeground(Color.RED);
        
        topPanel.setOpaque(true);
        topPanel.setBackground(Color.BLACK);
        topPanel.add(timeLabel);

        frame.setJMenuBar(menuBar);

        grid = new Grid(gridWidth, gridHeight, frame.getWidth(), frame.getHeight() - topPanelHeight, numOfBombs, this);

        frame.setVisible(true);
    }   

    public void setSmileIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        icon.setImage(icon.getImage().getScaledInstance(topPanelHeight, topPanelHeight, Image.SCALE_SMOOTH));
        smileButton.setIcon(icon);
    }

    public int getNumSelected() {
        return numSelected;
    }

    public void setIconFolder(String iconFolder) {
        this.iconFolder = iconFolder;
        reset();
    }

    public String getIconFolder() {
        return iconFolder;
    }

    public void addSelected() {
        this.numSelected++;
    }

    public void removeSelected() {
        this.numSelected--;
    }

    public void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask(){
        
            @Override
            public void run() {
                time++;
                if(time > 999) time = 999;
                timeLabel.setText(String.format("%03d", time));
            }
        }, 0, 1000);
    }

    public void reset() {
        setSmileIcon("./" + iconFolder + "/face.png");
        frame.remove(grid.getPanel());
        grid = new Grid(gridWidth, gridHeight, frame.getWidth(), frame.getHeight() - topPanelHeight, numOfBombs, this);
        setFlags(-1);
        setFlags(numOfBombs);
        playing = true;
        time = 0;
        timeLabel.setText("000");
        timer.cancel();
        numSelected = 0;
    }

    public void gameOver() {
        if(playing) {
            playing = false;
            timer.cancel();
            setSmileIcon("./" + iconFolder + "/dead.png");
            for(Tile tile: grid.getTileList()) {
                if(tile.isFlagged() && tile.getState() != Tile.BOMB_STATE) {
                    tile.setFlagged(false);
                    tile.getButton().setIcon(tile.createIcon("./" + iconFolder + "/not_mine.png"));
                }
                if(!tile.isRevealed())
                if(tile.getState() == Tile.BOMB_STATE)
                tile.reveal();
                else
                tile.getButton().setDisabledIcon(tile.getButton().getIcon());
                tile.getButton().setEnabled(false);
            }
            JOptionPane.showMessageDialog(frame, "You hit a bomb. Better luck next time!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void wonGame() {
        if(playing) {
            playing = false;
            timer.cancel();
            setSmileIcon("./" + iconFolder + "/win.png");
            for(Tile tile: grid.getTileList()) {
                if(!tile.isFlagged() && tile.getState() == Tile.BOMB_STATE) {
                    tile.setFlagged(true);
                }
                tile.getButton().setEnabled(false);
            }
            JOptionPane.showMessageDialog(frame, "Great job you managed to uncover all non-bomb tiles!", "You Won!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public boolean isPlaying() {
        return playing;
    }

    public JFrame getFrame() {
        return frame;
    }

    public int getNumOfBombs() {
        return numOfBombs;
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