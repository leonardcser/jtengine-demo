/**
 * Author:     Leonard Cseres
 * Date:       Fri Jan 08 2021
 * Time:       15:26:03
 */

package snakegame;

import com.leo.jtengine.Application;
import com.leo.jtengine.window.render.Terminal;

import snakegame.states.MenuState;
import snakegame.states.PlayState;

public class SnakeGame extends Application {

    public SnakeGame(Terminal terminal) {
        super(terminal);
        // pushState(new PlayState(this));
        pushState(new MenuState(this));
    }

    @Override
    public void end() {
        // Empty on purpose, do nothing
        super.end();
    }

}