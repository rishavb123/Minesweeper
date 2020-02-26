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

    public Grid(int width, int height, JFrame frame) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
        panel = new JPanel(new GridLayout(width, height));
        frame.add(panel);
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                tiles[i][j] = new Tile(frame.getWidth() / width, frame.getHeight() / height);
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

    public List<Tile> getNeighbors(Location loc) {

        List<Tile> list = new ArrayList<Tile>();

        for(int i = -1; i < 2; i++)
            for(int j = -1; j < 2; j++)
                if(i != 0 || j != 0)
                    list.add(get(loc.getX() + i, loc.getY() + j));

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

}