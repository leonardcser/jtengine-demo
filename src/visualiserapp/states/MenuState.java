/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:53
 */

package visualiserapp.states;

import com.leo.jtengine.graphics.StringArrayGraphics;
import com.leo.jtengine.graphics.TextGraphics;
import com.leo.jtengine.io.AsciiFileReader;
import com.leo.jtengine.maths.DiscreteCoordinates;
import com.leo.jtengine.utils.Audio;
import com.leo.jtengine.window.Keyboard;
import com.leo.jtengine.window.render.Color;

import visualiserapp.AlgorithmVisualiser;
import visualiserapp.algorithms.sorting.SortingAlgorithm;

public class MenuState extends AlgorithmVisualiserState {

    private static final String[] MENU_TITLES_TEXT = new String[] { "[1] Sorting Algorithms", "[2] Maze generation",
            "[3] Pathfinding Algorithms" };
    private static final String[] MENU_OPTIONS_SORTING_TEXT = new String[] { "- Bubble Sort", "- Quick Sort" };
    private static final String[] MENU_OPTIONS_MAZE_TEXT = new String[] { "- Recursive Back Tracking" };

    private final StringArrayGraphics mainTitle;
    private final TextGraphics[] menuTitles = new TextGraphics[MENU_TITLES_TEXT.length];
    private final TextGraphics[] menuOptionsSorting = new TextGraphics[MENU_OPTIONS_SORTING_TEXT.length];
    private final TextGraphics[] menuOptionsMaze = new TextGraphics[MENU_OPTIONS_MAZE_TEXT.length];
    private final StringArrayGraphics sortingThumbnail;
    private final StringArrayGraphics mazeThumbnail;
    private final StringArrayGraphics divider;
    private final TextGraphics helpArrows;
    private final TextGraphics helpReset;
    private final TextGraphics helpEnter;
    private final TextGraphics helpEsc;

    public MenuState(AlgorithmVisualiser algorithmVisualiser) {
        super(algorithmVisualiser);
        // ASCII Title
        String[] titleArray = new AsciiFileReader("algorithmvisualiser/menu_title.txt").toArray();
        mainTitle = new StringArrayGraphics(getCanvas(),
                new DiscreteCoordinates((getCanvas().getWidth() / 2) - (titleArray[0].length() / 2), 2), titleArray,
                new Color(32));
        // Menu Titles
        for (int i = 0; i < menuTitles.length; ++i) {
            menuTitles[i] = new TextGraphics(getCanvas(), new DiscreteCoordinates(24 + (i * 40), 20),
                    MENU_TITLES_TEXT[i]);
        }
        // SORTING ALGORITHMS
        // ASCII art graph
        sortingThumbnail = new StringArrayGraphics(getCanvas(), new DiscreteCoordinates(28, 12),
                new AsciiFileReader("algorithmvisualiser/menu_sorting_thumbnail.txt").toArray());
        // menu options
        for (int i = 0; i < menuOptionsSorting.length; ++i) {
            menuOptionsSorting[i] = new TextGraphics(getCanvas(), new DiscreteCoordinates(25, 22 + i),
                    MENU_OPTIONS_SORTING_TEXT[i]);
        }
        // MAZE GENERATION
        // ASCII art maze
        mazeThumbnail = new StringArrayGraphics(getCanvas(), new DiscreteCoordinates(68, 12),
                new AsciiFileReader("algorithmvisualiser/menu_maze_thumbnail.txt").toArray());
        // menu options
        for (int i = 0; i < menuOptionsMaze.length; ++i) {
            menuOptionsMaze[i] = new TextGraphics(getCanvas(), new DiscreteCoordinates(65, 22 + i),
                    MENU_OPTIONS_MAZE_TEXT[i]);
        }

        divider = new StringArrayGraphics(getCanvas(), new DiscreteCoordinates(3, 38),
                new AsciiFileReader("algorithmvisualiser/menu_divider.txt").toArray());

        helpArrows = new TextGraphics(getCanvas(), new DiscreteCoordinates(3, 40), "Use mouse to navigate");
        helpEnter = new TextGraphics(getCanvas(), new DiscreteCoordinates(54, 40),
                "(ENTER) to select and play/pause animations");
        helpReset = new TextGraphics(getCanvas(), new DiscreteCoordinates(3, 42), "(SPACE) to reset animations");
        helpEsc = new TextGraphics(getCanvas(), new DiscreteCoordinates(54, 42),
                "(ESC) to go back to menu/quit application");
    }

    @Override
    public boolean keyDown(Keyboard key) {
        if (key.equals(Keyboard.ESC)) {
            // TODO: find better way of closing app
            getTerminal().bip(Audio.MENU_CLICK);
            super.getAlgorithmVisualiser().end();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseHover(DiscreteCoordinates hover) {
        return updateColor(menuOptionsSorting, hover) || updateColor(menuOptionsMaze, hover);
    }

    private boolean updateColor(TextGraphics[] options, DiscreteCoordinates hover) {
        boolean updated = false;
        for (int i = 0; i < options.length; ++i) {
            if (options[i].mouseHover(hover)) {
                updated = true;
                options[i].setColor(new Color(Color.BOLD));
            } else {
                options[i].setColor(null);
            }
        }
        return updated;
    }

    @Override
    public boolean mouseClick(DiscreteCoordinates click) {
        boolean updated = false;
        for (int i = 0; i < menuOptionsSorting.length; ++i) {
            if (menuOptionsSorting[i].mouseClick(click)) {
                updated = true;
                if (i == 0) {
                    getAlgorithmVisualiser()
                            .pushState(new SortingState(getAlgorithmVisualiser(), SortingAlgorithm.BUBBLE_SORT));
                } else if (i == 1) {
                    getAlgorithmVisualiser()
                            .pushState(new SortingState(getAlgorithmVisualiser(), SortingAlgorithm.QUICK_SORT));
                }
                menuOptionsSorting[i].setColor(null);
            }
        }
        for (int i = 0; i < menuOptionsMaze.length; ++i) {
            if (menuOptionsMaze[i].mouseClick(click)) {
                updated = true;
                if (i == 0) {
                    getAlgorithmVisualiser().pushState(new MazeState(getAlgorithmVisualiser()));
                }
                menuOptionsMaze[i].setColor(null);
            }
        }
        if (updated) {
            getTerminal().bip(Audio.MENU_CLICK);
        }
        return updated;
    }

    @Override
    public void update(float deltaTime) {
        // Update Graphics
        mainTitle.update(deltaTime);
        for (TextGraphics menuTitle : menuTitles) {
            menuTitle.update(deltaTime);
        }

        updateMenuOptions(deltaTime, menuOptionsSorting, 0);
        updateMenuOptions(deltaTime, menuOptionsMaze, 1);

        sortingThumbnail.update(deltaTime);
        mazeThumbnail.update(deltaTime);
        divider.update(deltaTime);
        helpArrows.update(deltaTime);
        helpEnter.update(deltaTime);
        helpReset.update(deltaTime);
        helpEsc.update(deltaTime);
    }

    private void updateMenuOptions(float deltaTime, TextGraphics[] options, int columnPosition) {
        for (int i = 0; i < options.length; ++i) {
            options[i].update(deltaTime);
        }
    }

    @Override
    public void end() {
        // Empty on purpose, do nothing
    }
}
