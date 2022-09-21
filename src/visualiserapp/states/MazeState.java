/*
 *	Author:      Leonard Cseres
 *	Date:        29.12.20
 *	Time:        02:33
 */

package visualiserapp.states;

import com.leo.jtengine.maths.DiscreteCoordinates;
import com.leo.jtengine.utils.Audio;
import com.leo.jtengine.window.render.Color;
import com.leo.jtengine.window.Keyboard;

import visualiserapp.AlgorithmVisualiser;
import visualiserapp.algorithms.maze.MazeCell;
import visualiserapp.algorithms.maze.MazeGenerator;
import visualiserapp.algorithms.maze.RecursiveBackTracker;

public class MazeState extends AlgorithmVisualiserState {
    private final MazeCell[][] maze;
    private final MazeGenerator mazeAlgorithm;

    public MazeState(AlgorithmVisualiser algorithmVisualiser) {
        super(algorithmVisualiser);
        maze = new MazeCell[getCanvas().getWidth() / 3][(int) (2 * getCanvas().getHeight() / 3)];
        generateEmptyMaze();
        mazeAlgorithm = new RecursiveBackTracker(maze);
    }

    private void generateEmptyMaze() {
        for (int i = 0; i < maze.length; ++i) {
            for (int j = 0; j < maze[i].length; ++j) {
                maze[i][j] = new MazeCell(new DiscreteCoordinates(j, i));
            }
        }
    }

    @Override
    public boolean keyDown(Keyboard key) {
        switch (key) {
            case ESC:
                getTerminal().bip(Audio.MENU_CLICK);
                if (mazeAlgorithm.isStarted()) {
                    mazeAlgorithm.stop();
                }
                getAlgorithmVisualiser().removeFirstState();
                break;
            case SPACE:
                getTerminal().bip(Audio.MENU_CLICK);
                if (mazeAlgorithm.isStarted()) {
                    mazeAlgorithm.stop();
                }
                while (mazeAlgorithm.isExit()) {
                    Thread.onSpinWait();
                }
                generateEmptyMaze();
                break;
            case ENTER:
                getTerminal().bip(Audio.MENU_CLICK);
                if (!mazeAlgorithm.isStarted()) {
                    new Thread(mazeAlgorithm).start();
                } else {
                    mazeAlgorithm.togglePause();
                }
                break;
            case LEFT:
                if (mazeAlgorithm.decreaseSpeed()) {
                    getTerminal().bip(Audio.MENU_CLICK);
                }
                break;
            case RIGHT:
                if (mazeAlgorithm.increaseSpeed()) {
                    getTerminal().bip(Audio.MENU_CLICK);
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < maze.length; ++i) {
            for (int j = 0; j < maze[i].length; ++j) {
                updateHead(i, j);
                updateRightWalls(i, j);
                updateBottomWalls(i, j);
                updateCorners(i, j);
            }

        }
    }

    private void updateHead(int i, int j) {
        Color color = null;
        if (maze[i][j].equals(mazeAlgorithm.getHead())) {
            color = new Color(203);
        } else if (maze[i][j].isVisited()) {
            color = new Color(Color.WHITE);
        }

        getCanvas().putPixel(new DiscreteCoordinates(i * 3, j * 3), color, 2);
        getCanvas().putPixel(new DiscreteCoordinates(i * 3, j * 3 + 1), color, 2);
        getCanvas().putPixel(new DiscreteCoordinates(i * 3 + 1, j * 3), color, 2);
        getCanvas().putPixel(new DiscreteCoordinates(i * 3 + 1, j * 3 + 1), color, 2);
    }

    private void updateRightWalls(int i, int j) {
        Color color = getWallColor(maze[i][j].hasRightWall());
        getCanvas().putPixel(new DiscreteCoordinates(i * 3 + 2, j * 3), color, 1);
        getCanvas().putPixel(new DiscreteCoordinates(i * 3 + 2, j * 3 + 1), color, 1);
    }

    private void updateBottomWalls(int i, int j) {
        Color color = getWallColor(maze[i][j].hasBottomWall());
        getCanvas().putPixel(new DiscreteCoordinates(i * 3, j * 3 + 2), color, 1);
        getCanvas().putPixel(new DiscreteCoordinates(i * 3 + 1, j * 3 + 2), color, 1);
    }

    private void updateCorners(int i, int j) {
        getCanvas().putPixel(new DiscreteCoordinates(i * 3 + 2, j * 3 + 2), new Color(233), 1);
    }

    private Color getWallColor(boolean hasWall) {
        Color color = new Color(Color.WHITE);
        if (hasWall) {
            color = new Color(233);
        }
        return color;
    }

    @Override
    public void end() {
        // Empty on purpose, do nothing
    }
}
