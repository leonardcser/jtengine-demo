/*
 *	Author:      Leonard Cseres
 *	Date:        29.12.20
 *	Time:        02:30
 */

package visualiserapp.algorithms.maze;

import com.leo.jtengine.maths.DiscreteCoordinates;
import com.leo.jtengine.window.render.Terminal;

import java.util.*;

public class RecursiveBackTracker extends MazeGenerator {
    private final Deque<MazeCell> activeCells = new ArrayDeque<>();
    private final Random random = new Random();

    public RecursiveBackTracker(MazeCell[][] maze) {
        super(maze);
    }

    @Override
    protected void generate(MazeCell[][] maze) {
        while (!isFinished() && !isExit()) {
            MazeCell newCell = getRandomNeighbor();
            if (newCell != null) {

                int newX = newCell.getCoordinates().x;
                int newY = newCell.getCoordinates().y;
                int lastX = activeCells.peekFirst().getCoordinates().x;
                int lastY = activeCells.peekFirst().getCoordinates().y;
                DiscreteCoordinates link = new DiscreteCoordinates(newX - lastX, newY - lastY);
                // link = (1, 0) -> moved down
                // link = (-1, 0) -> moved up
                // link = (0, 1) -> moved right
                // link = (0, -1) -> moved left
                if (link.equals(DiscreteCoordinates.ORIGIN.up())) {
                    activeCells.peekFirst().setHasRightWall();
                } else if (link.equals(DiscreteCoordinates.ORIGIN.right())) {
                    activeCells.peekFirst().setHasBottomWall();
                } else if (link.equals(DiscreteCoordinates.ORIGIN.down())) {
                    newCell.setHasRightWall();
                } else if (link.equals(DiscreteCoordinates.ORIGIN.left())) {
                    newCell.setHasBottomWall();
                }
                activeCells.push(newCell);
                newCell.setVisited();
            } else {
                activeCells.removeFirst();
            }
        }
    }

    @Override
    public boolean isFinished() {
        if (activeCells.isEmpty()) {
            return false;
        }
        for (MazeCell[] mazeCells : getMaze()) {
            for (MazeCell mazeCell : mazeCells) {
                if (!mazeCell.isVisited()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void run() {
        super.run();
        activeCells.push(getMazeCell(START_POS));
        getMazeCell(START_POS).setVisited();
        generate(getMaze());
        activeCells.clear();
        setStarted(false);
        setPaused(false);
        setExit(false);
    }

    private MazeCell getRandomNeighbor() {
        Terminal.sleep(getGenerateSleepTime());
        while (isPaused() && !isExit()) {
            Thread.onSpinWait();
        }
        List<DiscreteCoordinates> neighbors = new ArrayList<>(getNeighbors());
        if (!neighbors.isEmpty()) {
            int randomIndex = random.nextInt(neighbors.size());
            return getMazeCell(neighbors.get(randomIndex));
        }
        return null;
    }

    private List<DiscreteCoordinates> getNeighbors() {
        assert activeCells.peekFirst() != null;
        List<DiscreteCoordinates> possibleNeighbors =
                new ArrayList<>(activeCells.peekFirst().getCoordinates().getNeighbours());

        possibleNeighbors.removeIf(n -> isOutOfBound(n) || getMazeCell(n).isVisited());

        return possibleNeighbors;
    }

    private boolean isOutOfBound(DiscreteCoordinates coor) {
        return (coor.y < 0) || (coor.x < 0) || (coor.y > getMaze().length - 1) || (coor.x > getMaze()[0].length - 1);
    }

    @Override
    public MazeCell getHead() {
        return activeCells.peekFirst();
    }
}
