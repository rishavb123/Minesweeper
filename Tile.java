import javax.swing.JToggleButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tile {

    public final int UNSET_STATE = 908327987;
    public final int BOMB_STATE = 902923334;
    public final int EMPTY_STATE = 234822490;
    public final int NUMBER_STATE = 238423322;

    private JToggleButton button;
    private int state = UNSET_STATE;

    public Tile() {
        button = new JToggleButton();
        button.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Clicked me!");
            }

        });
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public JToggleButton getButton() {
        return button;
    }

}