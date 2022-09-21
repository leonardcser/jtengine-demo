/*
*	Author:      Leonard Cseres
*	Date:        29.12.20
*	Time:        02:31
*/

package visualiserapp.algorithms.maze;

import com.leo.jtengine.maths.DiscreteCoordinates;

public class MazeCell {
    private final DiscreteCoordinates coordinates;
    private boolean isVisited = false;
    private boolean hasRightWall = true;
    private boolean hasBottomWall = true;

    public MazeCell(DiscreteCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public boolean hasRightWall() {
        return hasRightWall;
    }

    public void setHasRightWall() {
        this.hasRightWall = false;
    }

    public boolean hasBottomWall() {
        return hasBottomWall;
    }

    public void setHasBottomWall() {
        this.hasBottomWall = false;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited() {
        isVisited = true;
    }

    public DiscreteCoordinates getCoordinates() {
        return coordinates;
    }

}
