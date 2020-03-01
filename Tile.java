import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.JToggleButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tile {

    public final int UNSET_STATE = 908327987;
    public final int BOMB_STATE = 902923334;
    public final int EMPTY_STATE = 0;

    public boolean flagged;

    private JToggleButton button;
    private int state;

    private Location location;

    private int width;
    private int height;

    private Grid grid;
    private Minesweeper game;

    public Tile(int x, int y, int width, int height, Grid grid) {

        game = grid.getGame();

        button = new JToggleButton();
        button.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) {
                    if(!button.isSelected() && (game.getFlags() > 0 || isFlagged()))
                        setFlagged(!isFlagged());
                }
                else {
                    if(button.isEnabled()) {
                        button.setEnabled(false);
                    }
                }
            }

        });

        this.location = new Location(x, y);

        this.width = width;
        this.height = height;

        this.grid = grid;

        button.setIcon(createIcon("./sprites/block.png"));
        setState(UNSET_STATE);
        
    }

    public ImageIcon createIcon(String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        icon.setImage(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        return icon;
    }

    public void setState(int state) {
        this.state = state;
        switch(state) {
            case BOMB_STATE:
                button.setDisabledIcon(createIcon("./sprites/bomb.png"));
                break;
            case UNSET_STATE:
            case EMPTY_STATE:
                button.setDisabledIcon(createIcon("./sprites/empty.png"));
                break;
            default:
                button.setDisabledIcon(createIcon("./sprites/" + state + ".png"));
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

    public void setFlagged(boolean flagged) {
        if(this.flagged == flagged) return;
        if(flagged) {
            button.setDisabledIcon(createIcon("./sprites/flagged.png"));
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