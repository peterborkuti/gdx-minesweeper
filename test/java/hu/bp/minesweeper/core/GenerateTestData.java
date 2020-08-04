package hu.bp.minesweeper.core;

import java.io.*;
import java.util.Date;
import java.util.Random;

public class GenerateTestData {
	public static void main(String args[]) throws IOException {
		Date date = new Date();
		BufferedWriter bfw = new BufferedWriter(new FileWriter("./neighbours-" + date.getTime() + ".txt"));

		Random rnd = new Random();

		for(int i = 0; i < 100; i++) {
			int rows = 3 + rnd.nextInt(10);
			int cols = 3 + rnd.nextInt(10);
			int numOfBombs = 3 + rnd.nextInt(rows * cols / 4);
			MinesweeperData data = Minesweeper.createMinesweeperData(rows, cols, numOfBombs);
			String strData = Minesweeper.getStringGrid(data);
			bfw.append(strData);
			bfw.newLine();
			bfw.newLine();
		}

		bfw.flush();
		bfw.close();
	}
}
