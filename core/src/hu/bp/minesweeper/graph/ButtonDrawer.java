package hu.bp.minesweeper.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ButtonDrawer {
	Skin skin;

	public ButtonDrawer() {
		skin = new Skin(Gdx.files.internal("skin/number-cruncher/skin/number-cruncher-ui.json"));
	}

	public void addButtons(Integer[][] grid, Table table) {
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				table.add(getButton(grid[r][c]));
			}
			table.row();
		}
	}

	private TextButton getButton(Integer label) {
		Gdx.app.log("ButtonDrawer","adding button:" + label);
		return new TextButton("" + label, skin);
	}
}
