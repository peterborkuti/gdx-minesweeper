package hu.bp.minesweeper.core;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Coord {
	/**
	 * Number of rows of the grid
	 */
	public final int rows;

	/**
	 * Number of columns of the grid
	 */
	public final int cols;

	/**
	 * Actual row (zero based)
	 */
	public final int row;
	/**
	 * Actual column (zero based)
	 */
	public final int col;

	/**
	 * place when storing in array not matrix
	 */
	public final int linearCoord;

	/**
	 * Neighbours with linear coordinates. The index is the linear coordinate, the value is the number of
	 * bombs in the neighbourhood
	 */
	public final List<Integer> neighbours;

	/**
	 * Coordinates of a 3x3 matrix without the center cell relative to the center cell
	 */
	private final static List<Pair<Integer, Integer>> relativeCoordinatesOfNeighbourhood = Arrays.asList(
			new Pair[]{
				new Pair(-1, -1), new Pair(-1, 0), new Pair(-1, 1),
				new Pair(0, -1),                   new Pair(0, 1),
				new Pair(1, -1),  new Pair(1, 0),  new Pair(1, 1),
			});

	private Coord(int rows, int cols, int row, int col, int linearCoord, List<Integer> neighbours) {
		this.rows = rows;
		this.cols = cols;
		this.row = row;
		this.col = col;
		this.linearCoord = linearCoord;
		this.neighbours = neighbours;

	}

	/**
	 * Create a coordinate based on the number of rows, number of columns and
	 * the linear coordinate
	 * @param rows
	 * @param cols
	 * @param linearCoord
	 * @return
	 */
	public static Coord createCoord(int rows, int cols, int linearCoord) {
		int row = linearCoord / cols;
		int col = linearCoord - row * cols;

		return new Coord(rows, cols, row, col, linearCoord, getNeighbours(rows, cols, row, col));
	}

	/**
	 * Converts (row,col) coordinate to linear coordinate
	 * @param cols
	 * @param row
	 * @param col
	 * @return
	 */
	public static int convertRowColToSerial(int cols, int row, int col) {
		return row * cols + col;
	}

	/**
	 * creates a list of valid neighbourhood linear coordinates of a given (row, col)
	 * coordinate
	 * @param rows
	 * @param cols
	 * @param row
	 * @param col
	 * @return
	 */
	public static List<Integer> getNeighbours(int rows, int cols, int row, int col) {
		return relativeCoordinatesOfNeighbourhood.stream().
				map(pair -> new Pair<Integer, Integer>(row + pair.getKey(), col + pair.getValue())).
				filter(pair -> Coord.valid(rows, cols, pair)).
				map(pair -> convertRowColToSerial(cols, pair.getKey(), pair.getValue())).
				collect(Collectors.toList());
	}

	/**
	 * (row, col) is valid if it is between zero and (rows-1, cols-1)
	 * @return
	 */
	public static boolean valid(int rows, int cols, Pair<Integer, Integer> coord) {
		return coord.getKey() >= 0 && coord.getKey() < rows && coord.getValue() >= 0 && coord.getValue() < cols;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Coord coord = (Coord) o;
		return rows == coord.rows &&
				cols == coord.cols &&
				row == coord.row &&
				col == coord.col &&
				linearCoord == coord.linearCoord;
	}

	@Override
	public int hashCode() {
		return Objects.hash(rows, cols, linearCoord);
	}

	@Override
	public String toString() {
		return "Coord{" +
				"rows=" + rows +
				", cols=" + cols +
				", row=" + row +
				", col=" + col +
				", linearCoord=" + linearCoord +
				", neighbours=" + neighbours +
				'}';
	}
}
