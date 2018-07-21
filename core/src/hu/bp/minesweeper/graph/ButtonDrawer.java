package hu.bp.minesweeper.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;

class ButtonsShower implements Runnable {
	private final List<MinesweeperButton> buttons;

	public ButtonsShower(List<MinesweeperButton> buttons) {
		this.buttons = buttons;
	}

	/**
	 * Shows all the button's hidden value.
	 * Call this when game ends
	 */
	@Override
	public void run() {
		buttons.forEach(MinesweeperButton::showCell);
	}

}

public class ButtonDrawer {
	private Skin skin;
	private List<MinesweeperButton> buttons = new ArrayList<>();
	private final int numberOfBombs;
	private final Counter counter;
	private final ButtonsShower buttonsShower;

	public ButtonDrawer(int numberOfBombs, int numberOfCells) {
		skin = new Skin(Gdx.files.internal("skin/number-cruncher/skin/number-cruncher-ui.json"));
		this.numberOfBombs = numberOfBombs;
		this.buttonsShower = new ButtonsShower(buttons);
		this.counter = new Counter(numberOfCells - numberOfBombs, buttonsShower);
	}

	public void addButtons(Integer[][] grid, Table table) {
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				MinesweeperButton button = new MinesweeperButton(grid[r][c], skin, buttonsShower, counter);
				buttons.add(button);
				table.add(button);
			}
			table.row();
		}
	}
}
