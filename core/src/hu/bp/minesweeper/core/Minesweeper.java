package hu.bp.minesweeper.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.counting;

/**
 * Creates a random minesweeper "grid". For instantiating the data for a minesweeper game, use
 * the create* factory methods.
 */
public class Minesweeper {
	/**
	 * integer cell value for a bomb
	 */
	public static final int INT_BOMB = -1;

	/**
	 * String representation of the bomb
	 */
	public static final String STR_BOMB = "*";

	/**
	 * When there is no bombs in the neighbourhood this will
	 * be the cell's string representation
	 */
	public static final String STR_ZEO = "_";

	/**
	 * Don't instantiate this class, use the create* factory methods
	 */
	private Minesweeper() {}

	/**
	 * Creates minesweeper data based on the size of the grid and the desired number of bombs
	 * @param rows
	 * @param cols
	 * @param numberOfBombs
	 * @return
	 */
	public static MinesweeperData createMinesweeperData(int rows, int cols, int numberOfBombs) {
		List<Integer> bombs = getUniqueRandoms(rows * cols, numberOfBombs);

		return createMinesweeperData(rows, cols, bombs);
	}

	/**
	 * Creates a minesweeper datat based on the size of the grid and the linear coordinates of the bombs
	 * It is useful for testing
	 * @param rows
	 * @param cols
	 * @param bombs
	 * @return
	 */
	public static MinesweeperData createMinesweeperData(int rows, int cols, List<Integer> bombs) {
		Map<Integer, Long> bombsNeighboursWithCounts = getBombsNeighboursWithCounts(rows, cols, bombs);

		return new MinesweeperData(rows, cols, bombs, bombsNeighboursWithCounts);
	}

	/**
	 * Generates "length" pieces of unique random numbers which are from between 0 and bound - 1.
	 * Unique means, that the number will not be repeated.
	 *
	 * @param bound
	 * @param length
	 * @return
	 */
	public static List<Integer> getUniqueRandoms(int bound, int length) {
		List<Integer> numbersUpToBound = IntStream.range(0, bound).boxed().collect(Collectors.toList());
		Collections.shuffle(numbersUpToBound);

		length = Math.min(bound, length);

		return numbersUpToBound.subList(0, length);
	}

	/**
	 * Based on the size of the grid and the linear coordinates of the bombs it computes the
	 * numbers in the bombs-neighbourhood cells
	 * @param rows
	 * @param cols
	 * @param bombs
	 * @return
	 */
	public static Map<Integer, Long> getBombsNeighboursWithCounts(int rows, int cols, List<Integer> bombs) {
		return bombs.stream().
				map(linearCoord -> Coord.createCoord(rows, cols, linearCoord)).
				flatMap(coord -> coord.neighbours.stream()).
				filter(linearCoord -> !bombs.contains(linearCoord)).
				collect(Collectors.groupingBy(Integer::new, counting()));
	}

	/**
	 * Maps a linear coordinate to its value, which could be
	 * - bomb
	 * - neighbourhood of bomb (which is a the number how many bombs are in its neighbourhood)
	 * - 0 when there is no bomb in the neighbourhood
	 * @param linearCoordinate
	 * @param bombs
	 * @param bombNeighbours
	 * @return
	 */
	public static int cellMapper(Integer linearCoordinate, List<Integer> bombs, Map<Integer, Long> bombNeighbours) {
		if (bombs.contains(linearCoordinate)) {
			return Minesweeper.INT_BOMB;
		}

		if (bombNeighbours.containsKey(linearCoordinate)) {
			return bombNeighbours.get(linearCoordinate).intValue();
		}

		return 0;
	}

	/**
	 * Gives all the cells of the grid. The grid is reshaped linearly, rows after rows.
	 * @param data
	 * @return
	 */
	public static Integer[] getLinearGrid(MinesweeperData data) {
		return IntStream.range(0, data.BOUND).boxed().
				map(cell -> cellMapper(cell, data.bombs, data.bombsNeighboursWithCounts)).
				toArray(Integer[]::new);
	}

	/**
	 * Gives all the cells of the grid as an Integer matrix. The values of the cells
	 * shows:
	 * - it contains a bomb (Minesweeper.INT_BOMB)
	 * - there is no bomb in the neighbourhood (0)
	 * - positive number which is the number of bombs in the neighbourhood
	 * @param data
	 * @return
	 */
	//TODO: more clever with stream grid output not forEach
	public static Integer[][] getIntegerGrid(MinesweeperData data) {
		Integer[] linearGrid = getLinearGrid(data);

		Integer[][] grid = new Integer[data.ROWS][data.COLS];

		IntStream.range(0, data.BOUND).forEach(i ->
			{
				Coord c = Coord.createCoord(data.ROWS, data.COLS, i);
				grid[c.row][c.col] = linearGrid[i] == null ? 0 : linearGrid[i];
			}
		);

		return grid;
	}

	/**
	 * Maps a cell value to its string value
	 * @param cellValue
	 * @return
	 */

	public static String cellToStringMapper(int cellValue) {
		if (cellValue == INT_BOMB) {
			return STR_BOMB;
		}

		if (cellValue == 0) {
			return STR_ZEO;
		}

		return "" + cellValue;
	}

	/**
	 * Gives all the cells in a String, the rows are separated with "\n"
	 * @param data
	 * @return
	 */
	public static String getStringGrid(MinesweeperData data) {
		Integer[][] intGrid = getIntegerGrid(data);

		return
			Arrays.stream(intGrid).
					map(
						intArr -> Arrays.stream(intArr).
								map(intCell -> cellToStringMapper(intCell)).
								collect(Collectors.joining(""))
					).
					collect(Collectors.joining("\n"));
	}
}
