package hu.bp.minesweeper.graph;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hu.bp.minesweeper.core.Minesweeper;

public class MinesweeperButton extends TextButton {
	int minesweeperCellValue;

	public MinesweeperButton(int minesweeperCellValue, Skin skin, Runnable showCells, Runnable clickCounter) {
		super(" ", skin, "toggle");
		this.addListener(new MinesweeperButtonClickListener(this, minesweeperCellValue, showCells, clickCounter));
		this.minesweeperCellValue = minesweeperCellValue;
	}

	public void showCell() {
		setText(Minesweeper.cellToStringMapper(minesweeperCellValue));
		setDisabled(true);
		setProgrammaticChangeEvents(false);
		setChecked(true);
	}
}

class MinesweeperButtonClickListener extends ClickListener {
	private final MinesweeperButton button;
	private final int hiddenValue;
	private final Runnable showCells;
	private final Runnable clickCounter;

	public MinesweeperButtonClickListener(MinesweeperButton button, int hiddenValue, Runnable showCells, Runnable clickCounter) {
		this.button = button;
		this.hiddenValue = hiddenValue;
		this.showCells = showCells;
		this.clickCounter = clickCounter;
	}

	@Override
	public void clicked(InputEvent event, float x, float y){
		button.showCell();

		if (hiddenValue == Minesweeper.INT_BOMB) {
			showCells.run();
		}
		else {
			clickCounter.run();
		}
	}
}
