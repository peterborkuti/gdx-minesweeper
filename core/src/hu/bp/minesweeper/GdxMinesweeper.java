package hu.bp.minesweeper;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import hu.bp.minesweeper.core.Minesweeper;
import hu.bp.minesweeper.core.MinesweeperData;
import hu.bp.minesweeper.graph.ButtonDrawer;

public class GdxMinesweeper extends ApplicationAdapter {
	private Stage stage;
	private Table table;

	@Override
	public void create () {
		stage = new Stage(new FitViewport(1, 1));
		Gdx.input.setInputProcessor(stage);

		table = new Table();
		table.setFillParent(true);

		MinesweeperData mData = Minesweeper.createMinesweeperData(4, 4, 3);

		ButtonDrawer bd = new ButtonDrawer(mData.bombs.size(), mData.BOUND);

		bd.addButtons(Minesweeper.getIntegerGrid(mData), table);

		stage.addActor(table);
	}

	@Override
	public void resize (int width, int height) {
		stage.getViewport().setWorldSize((int)table.getMinWidth(), (int)table.getMinHeight());
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
