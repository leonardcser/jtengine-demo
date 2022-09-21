/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        11:37
 */

package visualiserapp.algorithms.sorting;

import com.leo.jtengine.window.render.Color;
import com.leo.jtengine.window.render.Terminal;

import java.util.Arrays;

public abstract class Sort implements Runnable {
    private int sortSleepTime = 21;
    private final Integer[] arr;
    private final Color[] states;
    private final int start;
    private final int end;
    private boolean finished = true;
    private boolean paused = false;
    private int swaps = 0;
    private boolean exit = false;


    protected Sort(Integer[] arr) {
        this.arr = arr;
        states = new Color[arr.length];
        start = 0;
        end = arr.length - 1;
    }

    public boolean decreaseSpeed() {
        if (sortSleepTime * 2 < 200) {
            sortSleepTime *= 2;
            return true;
        }
        return false;
    }

    public boolean increaseSpeed() {
        if (sortSleepTime / 2 > 0) {
            sortSleepTime /= 2;
            return true;
        }
        return false;
    }

    public boolean isExit() {
        return exit;
    }

    protected void setExit(boolean exit) {
        this.exit = exit;
    }

    public boolean isFinished() {
        return finished;
    }

    protected void setFinished(boolean finished) {
        this.finished = finished;
    }

    protected void modifyStatesAt(int index, Color color) {
        states[index] = color;
    }

    protected Integer[] getArr() {
        return arr;
    }

    public Color[] getStates() {
        return states;
    }

    protected int getStart() {
        return start;
    }

    protected int getEnd() {
        return end;
    }

    protected void swap(Integer[] arr, int initIndex, int destIndex) {
        while (paused && !exit) {
            Thread.onSpinWait();
        }

        if (!exit) {
            Terminal.sleep(sortSleepTime);
            int tmp = arr[initIndex];
            arr[initIndex] = arr[destIndex];
            arr[destIndex] = tmp;
            ++swaps;
        }
    }

    protected abstract void sort(Integer[] arr, int start, int end);

    protected void reset() {
        swaps = 0;
        clearStates();
        setFinished(true);
        setPaused(false);
        setExit(false);
    }

    protected void clearStates() {
        Arrays.fill(states, null);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void stop() {
        exit = true;
    }

    public void togglePause() {
        paused = !paused;
    }

    @Override
    public void run() {
        finished = false;
    }

    public int getSwaps() {
        return swaps;
    }
}
