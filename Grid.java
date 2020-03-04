import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class Grid {

    private Tile[][] tiles;
    private int width;
    private int height;

    private JPanel panel;

    private Minesweeper game;

    private int numOfBombs;
    private boolean generatedBombs;

    public Grid(int width, int height, int pixelWidth, int pixelHeight, int numOfBombs, Minesweeper game) {
        this.width = width;
        this.height = height;
        tiles = new Tile[height][width];
        panel = new JPanel(new GridLayout(height, width));
        panel.setPreferredSize(new Dimension(Tile.WIDTH * height, Tile.WIDTH * width));
        JFrame frame = game.getFrame();
        frame.add(panel);
        this.game = game;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                tiles[i][j] = new Tile(i, j, this);
                panel.add(tiles[i][j].getButton());
            }
        }
        this.numOfBombs = numOfBombs;
    }

    public Tile get(Location loc) {
        return get(loc.getX(), loc.getY());
    }

    public Tile get(int x, int y) {
        return isValid(x, y)? tiles[x][y]: null;
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
                if((i != 0 || j != 0) && isValid(x + i, y + j))
                    list.add(get(x + i, y + j));

        return list;
    }

    public boolean isValid(Location location) {
        return isValid(location.getX(), location.getY());
    }

    public boolean isValid(int x, int y) {
        return x >= 0 && x < height && y >= 0 && y < width;
    }

    public void generateBombs(Location location) {
        generatedBombs = true;
        int bombCount = 0;
        while(bombCount < numOfBombs) {
            int bombX = (int) (Math.random() * height);
            int bombY = (int) (Math.random() * width);

            if(get(bombX, bombY).getState() == Tile.UNSET_STATE && 
            (Math.abs(bombX - location.getX()) > 1 || 
            Math.abs(bombY - location.getY()) > 1)) {
                get(bombX, bombY).setState(Tile.BOMB_STATE);
                bombCount++;
            }
        }
        Util.Filter<Tile> bombFilter = new Util.Filter<Tile>() {

            @Override
            public boolean shouldKeep(Tile obj) {
                return obj.getState() == Tile.BOMB_STATE;
            }
            
        };
        for(int x = 0; x < height; x++)
            for(int y = 0; y < width; y++) {
                Tile tile = get(x, y);
                if(tile.getState() == Tile.UNSET_STATE) {
                    tile.setState(bombFilter.filter(getNeighbors(x, y)).size());
                }
                if(tile.isFlagged()) 
                    tile.getButton().setDisabledIcon(tile.createIcon("./" + game.getIconFolder() + "/flagged.png"));
            }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public List<Tile> getTileList() {
        List<Tile> list = new ArrayList<>();
        for(Tile[] arr: tiles) for(Tile t: arr) list.add(t);
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

    public boolean didGenerateBombs() {
        return generatedBombs;
    }

    public Minesweeper getGame() {
        return game;
    }
}