/**
 * Author:     Leonard Cseres
 * Date:       Fri Jan 08 2021
 * Time:       15:36:10
 */

package snakegame.states;

import com.leo.jtengine.graphics.TextGraphics;
import com.leo.jtengine.maths.DiscreteCoordinates;
import com.leo.jtengine.maths.Orientation;
import com.leo.jtengine.window.render.Color;
import com.leo.jtengine.window.Keyboard;

import snakegame.SnakeGame;

import java.util.*;

public class PlayState extends SnakeGameState {
    private static final float DEATH_TIMEOUT = 1f;
    private static final float SNAKE_SPEED = 0.03f;
    private static final int FOOD_LENGTH = 2;
    private final Random random = new Random();
    private final DiscreteCoordinates spawnCoordinates;
    private final Deque<List<DiscreteCoordinates>> snake = new ArrayDeque<>();
    private final TextGraphics scoreText;
    private final TextGraphics highScoreText;
    private final Deque<Orientation> desiredOrientations = new ArrayDeque<>();
    private Orientation currentOrientation = null;
    private List<DiscreteCoordinates> foodPosition = new ArrayList<>();
    private float moveSpeed = 0;
    private float timeoutCount = 0;
    private boolean isDead = false;
    private boolean isFoodEaten = false;
    private int foodLengthCouter = 0;
    private int snakeLength = 0;
    private int highScore = 0;

    public PlayState(SnakeGame snakeGame) {
        super(snakeGame);
        int x = getCanvas().getWidth() / 2;
        int y = getCanvas().getHeight();
        spawnCoordinates = new DiscreteCoordinates(x % 2 == 0 ? x : x + 1, y % 2 == 0 ? y : y + 1);
        snake.push(getOccupationList(spawnCoordinates));
        scoreText = new TextGraphics(getCanvas(), new DiscreteCoordinates(1, getCanvas().getHeight() - 2), "Score: 0");
        highScoreText = new TextGraphics(getCanvas(),
                new DiscreteCoordinates(getCanvas().getWidth() - 16, getCanvas().getHeight() - 2), "");
        placeRandomFood();
    }

    private List<DiscreteCoordinates> getOccupationList(DiscreteCoordinates coord) {
        List<DiscreteCoordinates> toReturn = new ArrayList<>();
        toReturn.add(new DiscreteCoordinates(coord.x, coord.y));
        toReturn.add(new DiscreteCoordinates(coord.x, coord.y + 1));
        toReturn.add(new DiscreteCoordinates(coord.x + 1, coord.y));
        toReturn.add(new DiscreteCoordinates(coord.x + 1, coord.y + 1));
        return toReturn;
    }

    private void placeRandomFood() {
        List<DiscreteCoordinates> emptyCoords = getEmptyCoordinates();

        DiscreteCoordinates randCoord = emptyCoords.get(random.nextInt(emptyCoords.size()));
        foodPosition = getOccupationList(randCoord);
    }

    private List<DiscreteCoordinates> getEmptyCoordinates() {
        List<DiscreteCoordinates> emptyCoords = new ArrayList<>();
        for (int x = 2; x < getCanvas().getWidth() - 3; x += 2) {
            for (int y = 2; y < getCanvas().getHeight() * 2 - 9; y += 2) {
                DiscreteCoordinates coord = new DiscreteCoordinates(x, y);
                boolean isEmpty = true;
                for (List<DiscreteCoordinates> snakeCoord : snake) {
                    if (snakeCoord.contains(coord)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    emptyCoords.add(coord);
                }

            }
        }

        return emptyCoords;
    }

    @Override
    public boolean keyDown(Keyboard key) {
        switch (key) {
            case ESC:
                getSnakeGame().removeFirstState();
                break;
            case UP:
                desiredOrientations.push(Orientation.UP);
                break;
            case DOWN:
                desiredOrientations.push(Orientation.DOWN);
                break;
            case LEFT:
                desiredOrientations.push(Orientation.LEFT);
                break;
            case RIGHT:
                desiredOrientations.push(Orientation.RIGHT);
                break;
            case SPACE:
                currentOrientation = null;
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void update(float deltaTime) {
        // Switch orientation
        if (!isDead) {
            if (!desiredOrientations.isEmpty() && (snake.peek().get(0).x % 2 == 0)
                    && (snake.peek().get(0).y % 2 == 0)) {
                currentOrientation = desiredOrientations.poll();
            }

            moveSpeed += deltaTime;
            if (moveSpeed >= SNAKE_SPEED) {
                moveSnake();
                moveSpeed = 0;
            }

            if (Objects.equals(snake.peek(), foodPosition)) {
                isFoodEaten = true;
                placeRandomFood();
                ++snakeLength;
                scoreText.setText("Score: " + snakeLength);
            }

            // CHECK IF COLLISION WITH ITSELF
            List<DiscreteCoordinates> totalSnake = new ArrayList<>();
            for (List<DiscreteCoordinates> coords : snake) {
                totalSnake.addAll(coords);
            }
            if (countHeadMatches(totalSnake) > 8) {
                isDead = true;
            }
        } else {
            timeoutCount += deltaTime;
            if (timeoutCount >= DEATH_TIMEOUT) {
                timeoutCount = 0;
                isDead = false;
                reset();
            }
        }

        for (List<DiscreteCoordinates> coords : snake) {
            Color color = null;
            int priority = 2;
            boolean isHead = Objects.equals(coords, snake.peek());
            if (isDead) {
                color = new Color(88);
                priority = 5;
            } else if (isHead) {
                color = new Color(76);
                priority = 5;
            } else {
                color = new Color(64);
            }

            for (DiscreteCoordinates coord : coords) {
                getCanvas().putPixel(new DiscreteCoordinates(coord.x, coord.y), color, priority);
            }
        }

        for (DiscreteCoordinates foodCoord : foodPosition) {
            getCanvas().putPixel(new DiscreteCoordinates(foodCoord.x, foodCoord.y), new Color(203), 1);
        }

        // draw border
        for (int x = 0; x < getCanvas().getWidth(); ++x) {
            getCanvas().putPixel(new DiscreteCoordinates(x, 0), new Color(Color.WHITE), 10);
            getCanvas().putPixel(new DiscreteCoordinates(x, getCanvas().getHeight() * 2 - 7),
                    new Color(Color.WHITE), 10);
        }
        for (int y = 0; y < getCanvas().getHeight() * 2 - 7; ++y) {
            getCanvas().putPixel(new DiscreteCoordinates(0, y), new Color(Color.WHITE), 10);
            getCanvas().putPixel(new DiscreteCoordinates(getCanvas().getWidth() - 1, y),
                    new Color(Color.WHITE), 10);
        }



        int offset = 0;
        int count = 0;
        for (int y = 2; y < getCanvas().getHeight() * 2 - 9; y += 2) {
            ++count;
            if (count % 2 == 0) {
                offset = 2;
            } else {
                offset = 0;
            }
            for (int x = 2 + offset; x < getCanvas().getWidth() - 2 + offset; x += 4) {
                List<DiscreteCoordinates> greySquare = getOccupationList(new DiscreteCoordinates(x, y));
                for (DiscreteCoordinates pos : greySquare) {
                    getCanvas().putPixel(pos, new Color(236), 0);
                }
            }
        }
        
        scoreText.update(deltaTime);
        highScoreText.update(deltaTime);
    }

    private void moveSnake() {
        if (currentOrientation != null) {
            List<DiscreteCoordinates> newHeadCoords = snake.peek();
            boolean touchedWalls = false;
            for (DiscreteCoordinates headCoord : newHeadCoords) {
                if (headCoord.x < 2 || headCoord.x > getCanvas().getWidth() - 3 || headCoord.y < 2
                        || headCoord.y > getCanvas().getHeight() * 2 - 9) {
                    touchedWalls = true;
                    break;
                }
            }
            if (!touchedWalls) {
                snake.push(getOccupationList(
                        newHeadCoords.get(0).jump(currentOrientation.coord.x, currentOrientation.coord.y)));
                if (!isFoodEaten) {
                    snake.removeLast();
                } else {
                    ++foodLengthCouter;
                    if (foodLengthCouter >= FOOD_LENGTH) {
                        foodLengthCouter = 0;
                        isFoodEaten = false;
                    }
                }
            } else {
                isDead = true;
            }
        }
    }

    private int countHeadMatches(List<DiscreteCoordinates> coords) {
        int count = 0;
        for (DiscreteCoordinates coord : coords) {
            if (snake.peek().contains(coord)) {
                ++count;
            }
        }
        return count;
    }

    private void reset() {
        if (snakeLength > highScore) {
            highScore = snakeLength;
            highScoreText.setText("High Score: " + highScore);
        }
        snakeLength = 0;
        scoreText.setText("Score: 0");
        snake.clear();
        snake.push(getOccupationList(spawnCoordinates));
        desiredOrientations.clear();
        currentOrientation = null;
        placeRandomFood();
    }

    @Override
    public void end() {
        // TODO Auto-generated method stub
    }

}
