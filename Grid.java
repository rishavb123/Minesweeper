import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class Grid {

    private Tile[][] tiles;
    private int width;
    private int height;

    private JPanel panel;

    private Minesweeper game;

    public Grid(int width, int height, Minesweeper game) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
        panel = new JPanel(new GridLayout(width, height));
        JFrame frame = game.getFrame();
        frame.add(panel);
        this.game = game;
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                tiles[i][j] = new Tile(i, j, frame.getWidth() / width, frame.getHeight() / height, this);
                panel.add(tiles[i][j].getButton());
            }
        }
    }

    public Tile get(Location loc) {
        return get(loc.getX(), loc.getY());
    }

    public Tile get(int x, int y) {
        return tiles[x][y];
    }

    public List<Tile> getNeighbors(Tile tile) {
        return getNeighbors(tile.getLocation());
    }

    public List<Tile> getNeighbors(Location loc) {
        return getNeighbors(loc.getX(), loc.getY());
    }

    public List<Tile> getNeighbors(int x, int y) {
        List<Tile> list = new ArrayList<Tile>();

        for(int i = -1; i < 2; i++)
            for(int j = -1; j < 2; j++)
                if(i != 0 || j != 0)
                    list.add(get(x + i, y + j));

        return list;
    }

    public JPanel getPanel() {
        return panel;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Minesweeper getGame() {
        return game;
    }
}