/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        17:15
 */

package visualiserapp.states;

import com.leo.jtengine.graphics.TextGraphics;
import com.leo.jtengine.maths.DiscreteCoordinates;
import com.leo.jtengine.utils.Audio;
import com.leo.jtengine.window.render.Cell;
import com.leo.jtengine.window.Keyboard;

import visualiserapp.AlgorithmVisualiser;
import visualiserapp.algorithms.sorting.BubbleSort;
import visualiserapp.algorithms.sorting.QuickSort;
import visualiserapp.algorithms.sorting.Sort;
import visualiserapp.algorithms.sorting.SortingAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SortingState extends AlgorithmVisualiserState {
    private final TextGraphics title;
    private final Random rand = new Random();
    private final Integer[] barLengths;
    private final Sort sortingAlgorithm;

    public SortingState(AlgorithmVisualiser algorithmVisualiser, SortingAlgorithm algorithm) {
        super(algorithmVisualiser);
        barLengths = new Integer[getCanvas().getWidth()];

        if (algorithm.equals(SortingAlgorithm.BUBBLE_SORT)) {
            sortingAlgorithm = new BubbleSort(barLengths);
        } else {
            sortingAlgorithm = new QuickSort(barLengths);
        }
        generateBars(true);
        title = new TextGraphics(getCanvas(), new DiscreteCoordinates(2, 0), sortingAlgorithm + " (shuffled)");
    }

    private void generateBars(boolean randomised) {
        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < getCanvas().getWidth(); ++i) {
            arr.add((i / 4) + 1);
        }
        Collections.shuffle(arr);
        for (int i = 0; i < getCanvas().getWidth(); ++i) {
            if (randomised) {
                barLengths[i] = rand.nextInt(getCanvas().getHeight()) + 1;
            } else {
                barLengths[i] = arr.get(i);
            }
        }
    }

    @Override
    public boolean keyDown(Keyboard key) {
        switch (key) {
            case ESC:
                getTerminal().bip(Audio.MENU_CLICK);

                if (!sortingAlgorithm.isFinished()) {
                    sortingAlgorithm.stop();
                }
                getAlgorithmVisualiser().removeFirstState();
                break;
            case SPACE:
                getTerminal().bip(Audio.MENU_CLICK);

                if (!sortingAlgorithm.isFinished()) {
                    sortingAlgorithm.stop();
                }
                while (!sortingAlgorithm.isFinished()) {
                    Thread.onSpinWait();
                }
                title.setText(sortingAlgorithm + " (shuffled)");
                generateBars(true);
                break;
            case ENTER:
                getTerminal().bip(Audio.MENU_CLICK);

                if (sortingAlgorithm.isFinished()) {
                    new Thread(sortingAlgorithm).start();
                } else {
                    sortingAlgorithm.togglePause();
                }
                break;
            case LEFT:
                if (sortingAlgorithm.decreaseSpeed()) {
                    getTerminal().bip(Audio.MENU_CLICK);
                }

                break;
            case RIGHT:
                if (sortingAlgorithm.increaseSpeed()) {
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
        // Update arr
        for (int i = 0; i < barLengths.length; ++i) {
            for (int j = 1; j <= barLengths[i]; ++j) {
                getCanvas().putCell(new Cell(new DiscreteCoordinates(i, getCanvas().getHeight() - j), '█',
                                                       sortingAlgorithm.getStates()[i], 1));
            }
        }
        if (!sortingAlgorithm.isFinished()) {
            title.setText(sortingAlgorithm + " (swaps: " + sortingAlgorithm.getSwaps() + ")");
        }
        title.update(deltaTime);

    }

    @Override
    public void end() {
        // Empty on purpose, do nothing
    }
}
