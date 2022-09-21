/*
 *	Author:      Leonard Cseres
 *	Date:        29.12.20
 *	Time:        13:27
 */


package visualiserapp.algorithms.maze;

import com.leo.jtengine.maths.DiscreteCoordinates;

public abstract class MazeGenerator implements Runnable {
    protected static final DiscreteCoordinates START_POS = new DiscreteCoordinates(0, 0);
    private final MazeCell[][] maze;
    private int generateSleepTime = 45;
    private boolean exit = false;
    private boolean paused = false;
    private boolean started = false;

    protected MazeGenerator(MazeCell[][] maze) {
        this.maze = maze;
    }

    public boolean decreaseSpeed() {
        if (generateSleepTime * 2 < 400) {
            generateSleepTime *= 2;
            return true;
        }
        return false;
    }

    public boolean increaseSpeed() {
        if (generateSleepTime / 2 > 0) {
            generateSleepTime /= 2;
            return true;
        }
        return false;
    }

    protected int getGenerateSleepTime() {
        return generateSleepTime;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    protected MazeCell[][] getMaze() {
        return maze;
    }

    protected MazeCell getMazeCell(DiscreteCoordinates coor) {
        return maze[coor.y][coor.x];
    }

    public abstract boolean isFinished();

    @Override
    public void run() {
        started = true;
    }

    protected abstract void generate(MazeCell[][] maze);

    public void togglePause() {
        paused = !paused;
    }

    public void stop() {
        exit = true;
    }

    public abstract MazeCell getHead();
}
