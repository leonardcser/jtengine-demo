/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        12:00
 */


package visualiserapp.algorithms.sorting;

import com.leo.jtengine.window.render.Color;

public class BubbleSort extends Sort {

    public BubbleSort(Integer[] arr) {
        super(arr);
    }

    @Override
    public String toString() {
        return "Bubble Sort";
    }

    @Override
    protected void sort(Integer[] arr, int start, int end) {
        boolean exit;
        do {
            exit = true;
            for (int i = 0; i < arr.length - 1 && !isExit(); ++i) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i + 1, i);
                    clearStates();
                    modifyStatesAt(i + 1, new Color(203));
                    exit = false;
                }
            }
        } while (!exit && !isExit());
    }

    @Override
    public void run() {
        super.run();
        sort(getArr(), getStart(), getEnd());
        reset();
    }


}