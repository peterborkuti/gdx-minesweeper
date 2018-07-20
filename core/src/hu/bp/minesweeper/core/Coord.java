package hu.bp.minesweeper.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Coord {
	/**
	 * Number of rows
	 */
	public final int rows;
	/**
	 * Number of columns
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
	 * Neighbours with linear coordinates
	 */
	public final List<Integer> neighbours;

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
	 * (row, col) is valid if it is between zero and (rows-1, cols-1)
	 * @param rows
	 * @param cols
	 * @param row
	 * @param col
	 * @return
	 */
	public static boolean validCoord(int rows, int cols, int row, int col) {
		return row >= 0 && row < rows && col >= 0 && col < cols;
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
		List<Integer>  neighbours = new ArrayList<>();

		for (int r = row - 1; r <= row + 1; r++) {
			for (int c = col - 1; c <= col + 1; c++) {
				if (validCoord(rows, cols, r, c) && !(row == r && col == c)) {
					neighbours.add(convertRowColToSerial(cols, r, c));
				}
			}
		}

		return neighbours;
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
