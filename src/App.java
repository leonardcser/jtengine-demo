
import com.leo.jtengine.Loop;
import com.leo.jtengine.window.Window;
import com.leo.jtengine.window.render.Terminal;

import snakegame.SnakeGame;
import visualiserapp.AlgorithmVisualiser;

public class App {
    public static void main(String[] args) {
        // Window window = new Window();
        Window window = new Window(159, 45);
        // Window window = new Window(40, 24);
        // window.setFullScreen();
        Terminal terminal = new Terminal(window);
        Loop app = new Loop(new AlgorithmVisualiser(terminal));
        // Loop app = new Loop(new SnakeGame(terminal));
        app.setShowFpsTitle(true);
        app.start();
    }
}