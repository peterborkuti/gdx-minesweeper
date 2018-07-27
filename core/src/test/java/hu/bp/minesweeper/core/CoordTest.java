package hu.bp.minesweeper.core;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CoordTest {

	@Test
	public void createCoord() {
		Coord coord = Coord.createCoord(3, 4, 6);

		assertEquals(3, coord.rows);
		assertEquals(4, coord.cols);
		assertEquals(1, coord.row);
		assertEquals(2, coord.col);
		assertEquals(6, coord.linearCoord);
	}

	@Test
	public void convertRowColToSerial() {
		assertEquals(5, Coord.convertRowColToSerial(3, 1, 2));
	}

	private Integer[] stringToIntArray(String s) {
		return Arrays.stream(s.split(",")).map(String::trim).mapToInt(Integer::new).boxed().toArray(Integer[]::new);
	}

	@Test
	public void testStringToIntArray() {
		Integer[] arr = stringToIntArray("0, 1, 2");

		assertArrayEquals(new Integer[]{0,1,2}, arr);
	}

	@Test
	public void getNeighbours() {
		int rows = 3;
		int cols = 3;

		Map<Integer, String> tests = new HashMap<>();
		tests.put(Coord.convertRowColToSerial(cols,0, 0), "1, 3, 4");
		tests.put(Coord.convertRowColToSerial(cols,0, 1), "0, 2, 3, 4, 5");
		tests.put(Coord.convertRowColToSerial(cols,0, 2), "1, 4, 5");
		tests.put(Coord.convertRowColToSerial(cols,1, 0), "0, 1, 4, 6, 7");
		tests.put(Coord.convertRowColToSerial(cols,1, 1), "0, 1, 2, 3, 5, 6, 7, 8");
		tests.put(Coord.convertRowColToSerial(cols,1, 2), "1, 2, 4, 7, 8");
		tests.put(Coord.convertRowColToSerial(cols,2, 0), "3, 4, 7");
		tests.put(Coord.convertRowColToSerial(cols,2, 1), "3, 4, 5, 6, 8");
		tests.put(Coord.convertRowColToSerial(cols,2, 2), "4, 5, 7");

		tests.forEach((linearCoord, validNeighboursString) -> {
			Integer[] validNeighbours = stringToIntArray(validNeighboursString);
			Coord coord = Coord.createCoord(rows, cols, linearCoord);
			Integer[] neighbours = coord.neighbours.toArray(new Integer[0]);
			String msg = coord.toString() + " valid: " + validNeighboursString;
			assertArrayEquals(msg, validNeighbours, neighbours);
		});
	}
}