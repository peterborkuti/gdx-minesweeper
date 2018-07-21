package hu.bp.minesweeper;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import hu.bp.minesweeper.core.Minesweeper;
import hu.bp.minesweeper.core.MinesweeperData;
import hu.bp.minesweeper.graph.ButtonDrawer;

public class GdxMinesweeper extends ApplicationAdapter {
	private Stage stage;
	private Table table;

	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		table = new Table();
		table.setFillParent(true);

		ButtonDrawer bd = new ButtonDrawer();
		MinesweeperData mData = Minesweeper.createMinesweeperData(10, 10, 20);
		bd.addButtons(Minesweeper.getIntegerGrid(mData), table);

		stage.addActor(table);

		table.setDebug(true);
	}

	@Override
	public void resize (int width, int height) {
		// See below for what true means.
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	public void dispose() {
		stage.dispose();
	}
}
