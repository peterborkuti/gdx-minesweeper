package hu.bp.minesweeper.core;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Immutable data for displaying a minesweeper grid
 */
public class MinesweeperData {
	/**
	 * number of grid rows
	 */
	public final int ROWS;

	/**
	 * number of grid columns
	 */
	public final int COLS;

	/**
	 * upper bound of linear coordinates , it is ROWS * COLS
	 */
	public final int BOUND;

	/**
	 * the bombs linear coordinates
	 */
	public final List<Integer> bombs;

	/**
	 * Map<LinearCoordinate, number of neighbourhood bombs>
	 * The cells which have non-zero value and not bombs
	 */
	public final Map<Integer, Long> bombsNeighboursWithCounts;

	/**
	 * free-form string. Mainly for debugging
	 */
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

	/**
	 * For testing purposes. It returns with the linear coordinates of the bombs in ascending order
	 * @return
	 */
	public Integer[] getBombsNeighboursLinearCoords() {
		Integer[] arr = bombsNeighboursWithCounts.keySet().toArray(new Integer[0]);

		Arrays.sort(arr);

		return arr;
	}

	/**
	 * For testing purposes. It returns with values of the cells in the bombs surroundings. Ascending orders for
	 * easy testing. This is meaningless, but good for testing.
	 * @return
	 */
	public Long[] getBombsNeighboursCounts() {
		Long[] arr = bombsNeighboursWithCounts.values().toArray(new Long[0]);

		Arrays.sort(arr);

		return arr;
	}
}
