/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        16:01
 */

package visualiserapp.states;

import com.leo.jtengine.states.State;

import visualiserapp.AlgorithmVisualiser;

public abstract class AlgorithmVisualiserState extends State {

    private final AlgorithmVisualiser algorithmVisualiser;

    protected AlgorithmVisualiserState(AlgorithmVisualiser algorithmVisualiser) {
        super(algorithmVisualiser);
        this.algorithmVisualiser = algorithmVisualiser;
    }

    public AlgorithmVisualiser getAlgorithmVisualiser() {
        return algorithmVisualiser;
    }
}
