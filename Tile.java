import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.JToggleButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tile {

    public static final int UNSET_STATE = 908327987;
    public static final int BOMB_STATE = 902923334;
    public static final int EMPTY_STATE = 0;
    public static final int WIDTH = 40;

    public boolean flagged;

    private JToggleButton button;
    private int state;

    private Location location;

    private Grid grid;
    private Minesweeper game;

    private boolean revealed;

    public Tile(int x, int y, Grid grid) {

        game = grid.getGame();

        button = new JToggleButton();
        button.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mousePressed(MouseEvent e) {
                if(game.isPlaying() && e.getButton() == MouseEvent.BUTTON1)
                    game.setSmileIcon("./" + game.getIconFolder() + "/clicking.png");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(!game.isPlaying()) return;
                game.setSmileIcon("./" + game.getIconFolder() + "/face.png");
                if(e.getButton() == MouseEvent.BUTTON3) {
                    if(game.isPlaying() && !button.isSelected() && (game.getFlags() > 0 || isFlagged()))
                        setFlagged(!isFlagged());
                }
                else {
                    if(button.isEnabled()) {
                        if(!grid.didGenerateBombs()) {
                            grid.generateBombs(location);
                            game.startTimer();
                        }
                        if(state == BOMB_STATE) {
                            button.setDisabledIcon(createIcon("./" + game.getIconFolder() + "/red_mine.png"));
                            game.gameOver();
                            return;
                        }
                        reveal();
                    }
                }
            }

        });

        this.location = new Location(x, y);

        this.grid = grid;

        button.setIcon(createIcon("./" + game.getIconFolder() + "/block.png"));
        setState(UNSET_STATE);
        
    }

    public ImageIcon createIcon(String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        icon.setImage(icon.getImage().getScaledInstance(Tile.WIDTH, Tile.WIDTH, Image.SCALE_SMOOTH));
        return icon;
    }

    public void reveal() {
        if(!revealed)
            game.addSelected();
        revealed = true;
        button.setSelected(true);
        button.setEnabled(false);
        if(state == Tile.EMPTY_STATE) {
            for(Tile tile: new Util.Filter<Tile>() {
                
                @Override
                public boolean shouldKeep(Tile obj) {
                    return !obj.isRevealed() && !obj.isFlagged();
                }
                
            }.filter(grid.getNeighbors(this))) {
                tile.reveal();
            }
        }
        if(game.getNumSelected() == grid.getWidth() * grid.getHeight() - game.getNumOfBombs())
            game.wonGame();
    }

    public void setState(int state) {
        this.state = state;
        switch(state) {
            case BOMB_STATE:
                button.setDisabledIcon(createIcon("./" + game.getIconFolder() + "/mine.png"));
                break;
            case UNSET_STATE:
            case EMPTY_STATE:
                button.setDisabledIcon(createIcon("./" + game.getIconFolder() + "/empty.png"));
                break;
            default:
                button.setDisabledIcon(createIcon("./" + game.getIconFolder() + "/" + state + ".png"));
                break;
        }
    }

    public int getState() {
        return state;
    }

    public JToggleButton getButton() {
        return button;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isRevealed() {
        return revealed;
    }
    
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public void setFlagged(boolean flagged) {
        if(this.flagged == flagged) return;
        if(flagged) {
            button.setDisabledIcon(createIcon("./" + game.getIconFolder() + "/flagged.png"));
            button.setEnabled(false);
            game.useFlag();
        }
        else {
            setState(state);
            button.setEnabled(true);
            game.addFlag();
        }
        this.flagged = flagged;
    }

}