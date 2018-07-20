package hu.bp.minesweeper.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;

public class Minesweeper {
	public static final int INT_BOMB = -1;
	public static final String STR_BOMB = "*";
	public static final String STR_ZEO = " ";

	private Minesweeper() {}

	public static MinesweeperData createMinesweeperData(int rows, int cols, int numberOfBombs) {
		List<Integer> bombs = getUniqueRandoms(rows * cols, numberOfBombs);

		return createMinesweeperData(rows, cols, bombs);
	}

	public static MinesweeperData createMinesweeperData(int rows, int cols, List<Integer> bombs) {
		Map<Integer, Long> bombsNeighboursWithCounts = getBombsNeighboursWithCounts(rows, cols, bombs);

		return new MinesweeperData(rows, cols, bombs, bombsNeighboursWithCounts);
	}

	public static List<Integer> getUniqueRandoms(int bound, int length) {
		List<Integer> numbersUpToBound = IntStream.range(0, bound).boxed().collect(Collectors.toList());
		Collections.shuffle(numbersUpToBound);

		length = Math.min(bound, length);

		return numbersUpToBound.subList(0, length);
	}

	public static Map<Integer, Long> getBombsNeighboursWithCounts(int rows, int cols, List<Integer> bombs) {
		return bombs.stream().
				map(linearCoord -> Coord.createCoord(rows, cols, linearCoord)).
				flatMap(coord -> coord.neighbours.stream()).
				filter(linearCoord -> !bombs.contains(linearCoord)).
				collect(Collectors.groupingBy(Integer::new, counting()));
	}

	public static int cellMapper(Integer i, List<Integer> bombs, Map<Integer, Long> bombNeighbours) {
		if (bombs.contains(i)) {
			return Minesweeper.INT_BOMB;
		}

		if (bombNeighbours.containsKey(i)) {
			return bombNeighbours.get(i).intValue();
		}

		return 0;
	}

	public static Integer[] getLinearGrid(MinesweeperData data) {
		return IntStream.range(0, data.BOUND).boxed().
				map(cell -> cellMapper(cell, data.bombs, data.bombsNeighboursWithCounts)).
				toArray(Integer[]::new);
	}

	//TODO: more clever with stream grid output not forEach
	public static Integer[][] getIntegerGrid(MinesweeperData data) {
		Integer[] linearGrid = getLinearGrid(data);

		Integer[][] grid = new Integer[data.ROWS][data.COLS];

		IntStream.range(0, data.BOUND).forEach(i ->
			{
				Coord c = Coord.createCoord(data.ROWS, data.COLS, i);
				grid[c.row][c.col] = linearGrid[i];
			}
		);

		return grid;
	}

	public static String cellToStringMapper(int cellValue) {
		if (cellValue == INT_BOMB) {
			return STR_BOMB;
		}

		if (cellValue == 0) {
			return STR_ZEO;
		}

		return "" + cellValue;
	}

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
