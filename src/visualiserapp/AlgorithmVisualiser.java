/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:07
 */

package visualiserapp;

import com.leo.jtengine.Application;
import com.leo.jtengine.window.render.Terminal;

import visualiserapp.states.MenuState;

public class AlgorithmVisualiser extends Application {

    public AlgorithmVisualiser(Terminal terminal) {
        super(terminal);
        pushState(new MenuState(this));
    }

    @Override
    public void end() {
        // Empty on purpose, do nothing
        super.end();
    }

}