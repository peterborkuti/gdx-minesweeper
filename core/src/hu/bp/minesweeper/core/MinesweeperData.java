package hu.bp.minesweeper.core;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MinesweeperData {
	public final int ROWS;
	public final int COLS;
	public final int BOUND;
	public final List<Integer> bombs;
	public final Map<Integer, Long> bombsNeighboursWithCounts;
	public final String description;

	public MinesweeperData(int rows, int cols, List<Integer> bombs, Map<Integer, Long> bombsNeighboursWithCounts, String description) {
		this.ROWS = rows;
		this.COLS = cols;
		this.BOUND = rows * cols;
		this.bombs = bombs;
		this.bombsNeighboursWithCounts = bombsNeighboursWithCounts;
		this.description = description;
	}

	public MinesweeperData(int rows, int cols, List<Integer> bombs, Map<Integer, Long> bombsNeighboursWithCounts) {
		this(rows, cols, bombs, bombsNeighboursWithCounts, "");
	}

	public Integer[] getBombsNeighboursLinearCoords() {
		Integer[] arr = bombsNeighboursWithCounts.keySet().toArray(new Integer[0]);

		Arrays.sort(arr);

		return arr;
	}

	public Long[] getBombsNeighboursCounts() {
		Long[] arr = bombsNeighboursWithCounts.values().toArray(new Long[0]);

		return arr;
	}
}
