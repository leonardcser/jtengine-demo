/**
 * Author:     Leonard Cseres
 * Date:       Fri Jan 08 2021
 * Time:       15:32:04
 */

package snakegame.states;

import com.leo.jtengine.graphics.StringArrayGraphics;
import com.leo.jtengine.graphics.TextGraphics;
import com.leo.jtengine.graphics.shapes.Polygon;
import com.leo.jtengine.graphics.shapes.Shape;
import com.leo.jtengine.io.AsciiFileReader;
import com.leo.jtengine.maths.DiscreteCoordinates;
import com.leo.jtengine.utils.Audio;
import com.leo.jtengine.window.Keyboard;
import com.leo.jtengine.window.render.Color;
import com.leo.jtengine.window.render.Xterm;

import snakegame.SnakeGame;

public class MenuState extends SnakeGameState {

    private StringArrayGraphics mainTitle;
    private TextGraphics[] options = new TextGraphics[2];
    private int r = 140;
    private boolean transitonSwitch = true;
    private final Shape shape;

    public MenuState(SnakeGame snakegame) {
        super(snakegame);
        // ASCII Title
        String[] titleArray = new AsciiFileReader("snakegame/menu_title.txt").toArray();
        mainTitle = new StringArrayGraphics(getCanvas(), new DiscreteCoordinates(0, 7), titleArray, new Color(64));
        mainTitle.setXCentered();
        options[0] = new TextGraphics(getCanvas(), new DiscreteCoordinates(0, 17), "PLAY");
        options[1] = new TextGraphics(getCanvas(), new DiscreteCoordinates(0, 19), "EXIT");

        for (TextGraphics option : options) {
            option.setXCentered();
        }

        DiscreteCoordinates p1 = new DiscreteCoordinates(10, 10);
        DiscreteCoordinates p2 = new DiscreteCoordinates(20, 10);
        DiscreteCoordinates p3 = new DiscreteCoordinates(20, 15);
        DiscreteCoordinates p4 = new DiscreteCoordinates(10, 15);
        DiscreteCoordinates p5 = new DiscreteCoordinates(0, 0);
        shape = new Polygon(getCanvas(), new DiscreteCoordinates[]{p1, p2, p3, p4}, new Color(Color.YELLOW), new Color(Color.YELLOW), 1, 50);
    }

    @Override
    public boolean mouseHover(DiscreteCoordinates hover) {
        boolean found = false;
        for (TextGraphics option : options) {
            if (option.mouseHover(hover)) {
                option.setColor(new Color(Color.UNDERLINE + Color.BOLD));
                found = true;
            } else {
                option.setColor(null);
            }
        }

        return found;
    }

    @Override
    public boolean mouseClick(DiscreteCoordinates click) {
        boolean found = false;
        for (int i = 0; i < options.length; i++) {
            if (options[i].mouseClick(click)) {
                if (i == 0) {
                    getSnakeGame().pushState(new PlayState(getSnakeGame()));
                } else if (i == 1) {
                    super.getSnakeGame().end();
                }
                options[i].setColor(null);
                found = true;
                getTerminal().bip(Audio.MENU_CLICK);
            }
        }
        return found;
    }

    @Override
    public boolean keyDown(Keyboard key) {
        if (key.equals(Keyboard.ESC)) {
            getTerminal().bip(Audio.MENU_CLICK);
            super.getSnakeGame().end();
            return true;
        }
        if (key.equals(Keyboard.R)) {
            shape.rotate(20f);
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        if (r >= 140) {
            transitonSwitch = true;
        }
        if (transitonSwitch && r > 40) {
            --r;
        } else {
            ++r;
            transitonSwitch = false;
        }
        mainTitle.setColor(new Color(Xterm.get24BitFgColor(r, 144, 67)));
        mainTitle.update(deltaTime);
        for (TextGraphics option : options) {
            option.update(deltaTime);
        }

        
        // shape.update(deltaTime);
    }

    @Override
    public void end() {
        // TODO Auto-generated method stub
    }

}
