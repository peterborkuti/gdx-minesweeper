package hu.bp.minesweeper.core;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
	 * Creates minesweeper data based on the size of the grid and the desired number of bombs.
	 *
	 * This is for "end users". So use this when you wnat to generate a filled-in minesweeper grid.
	 *
	 * Example: 10x10 grid with 20 bombs
	 * MinesweeperData data = Minesweeper.createMinesweeperData(10, 10, 20);
	 * String grid = Minesweeper.getStringGrid(data);
	 * System.out.println(grid);
	 *
	 * Example: 7x4 grid with 5 bombs
	 * System.out.println(Minesweeper.getStringGrid(Minesweeper.createMinesweeperData(7, 4, 5)));
	 *
	 * Example: 6x5 grid with 4 bombs and Integer[][] gridmatrix
	 * Integer[][] grid = Minesweeper.getStringGrid(Minesweeper.createMinesweeperData(6, 5, 4));
	 *
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
	 * It is useful for testing. End users may do not want to use this method.
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
	 * This method uses this algorithm:
	 * - generating numbers from 0 to bound -1
	 * - stands them in random order
	 * - selects the first "length" pieces.
	 *
	 * I know a better algorithm which takes exactly bound steps:
	 * for ( i = 0; i < bound; i++) {
	 *
	 * }
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
	 * Decides whether the index should be chosen or not. Its a bit tricky:
	 * The index should be chosen with the probability of
	 *   the number of elements to be selected divided by how many elements remains
	 *
	 *   length - selectedCounter
	 *   ------------------------
	 *        bound - index
	 *
	 * @param index
	 * @param bound
	 * @param length
	 * @param randomFloat
	 * @param selectedCounter
	 * @return
	 */
	public static boolean isIndexChoosedRandomly(final int index, final int bound, final int length, Float randomFloat, AtomicInteger selectedCounter) {
		if (length == selectedCounter.get()) return false;

		if (randomFloat <= (float)(length - selectedCounter.get()) / (float)(bound - index)) {
			selectedCounter.incrementAndGet();

			return true;
		}

		return false;
	}

	/**
	 * Get unique random numbers based on another algorithm what I learned in the University (ELTE)
	 * @param bound
	 * @param length
	 * @return
	 */
	public static List<Integer> getUniqueRandomsOptimized(final int bound, final int length) {
		AtomicInteger n = new AtomicInteger(0);
		Random rnd = new Random();

		return IntStream.range(0, bound).
				filter(i -> isIndexChoosedRandomly(i, bound, length, rnd.nextFloat(), n)).
				boxed().collect(Collectors.toList());
	}

	/**
	 * Based on the size of the grid and the linear coordinates of the bombs it computes the
	 * numbers in the bombs-neighbourhood cells.
	 *
	 * The algorithm:
	 * When there is only one bomb:
	 * 1100
	 * *100
	 * 1100
	 * 0000
	 *
	 * All of the valid neighbourhood cells should contain 1
	 *
	 * When there are two bombs far enough from each other:
	 * 1*10000
	 * 1110000
	 * 0000011
	 * 000001*
	 * 0000011
	 *
	 * Same, all of the valid bomb-neighborhood cells should contain 1
	 *
	 * Interesting case: two bombs, which have common neighbourhood cells
	 *
	 * 11100
	 * 1*211
	 * 112*1
	 * 00111
	 *
	 * Let's "spearate" the bombs:
	 *
	 * 11100       00000
	 * 1*100       00111
	 * 11100       001*1
	 * 00000       00111
	 *
	 * The algorithm:
	 * 1) create a list of the neighbourhood of all the bombs
	 * 2) count the appearance for each cells in this list
	 * 3) the value of the neighbourhood cells will be its counter
	 *
	 * One addition:
	 * Imagine, that one of the bomb's neighbourhood cell may contain another bomb.
	 * In this case, this cell should not be in the list, so
	 * filter out the bomb-cells from the neighbourhood cells.
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
