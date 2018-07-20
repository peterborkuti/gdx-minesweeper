package hu.bp.minesweeper.core;


import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MinesweeperTest {

	@Test
	public void getUniqueRandomsBoundEqualsLength() {
		List<Integer> randoms = Minesweeper.getUniqueRandoms(3, 3);
		Collections.sort(randoms);

		Integer[] sortedRandoms = randoms.toArray(new Integer[0]);
		assertArrayEquals(new Integer[]{0, 1, 2}, sortedRandoms);
	}

	@Test
	public void getUniqueRandomsBoundGreaterThanLength() {
		int bound = 10;
		int length = 3;
		List<Integer> randoms = Minesweeper.getUniqueRandoms(bound, length);

		assertEquals(length, randoms.size());

		Set<Integer> set = new HashSet<>(randoms);

		assertEquals("numbers should unique", randoms.size(), set.size());

		randoms.forEach(r -> assertTrue("numbers should be between 0 and bound", r >= 0 && r < bound));
	}

	@Test
	public void testMinesweeper() throws IOException {
		MinesweeperTestHelper helper = new MinesweeperTestHelper("neighbours.txt");

		helper.forEach(test -> {
			MinesweeperData data = Minesweeper.createMinesweeperData(test.ROWS, test.COLS, test.bombs);

			assertArrayEquals("Coords:" + test.description, test.getBombsNeighboursLinearCoords(), data.getBombsNeighboursLinearCoords());
			assertArrayEquals("Counts:" + test.description, test.getBombsNeighboursCounts(), data.getBombsNeighboursCounts());
		});
	}

	@Test
	public void cellMapper() {
		MinesweeperData data = MinesweeperTestHelper.getMinesweeperDataFromString(2, 2, "*12 ");
		assertEquals(Minesweeper.INT_BOMB, Minesweeper.cellMapper(0, data.bombs, data.bombsNeighboursWithCounts));
		assertEquals(1, Minesweeper.cellMapper(1, data.bombs, data.bombsNeighboursWithCounts));
		assertEquals(2, Minesweeper.cellMapper(2, data.bombs, data.bombsNeighboursWithCounts));
		assertEquals(0, Minesweeper.cellMapper(3, data.bombs, data.bombsNeighboursWithCounts));
	}

	@Test
	public void cellToStringMapper() {
		assertEquals(Minesweeper.STR_BOMB, Minesweeper.cellToStringMapper(Minesweeper.INT_BOMB));
		assertEquals("1", Minesweeper.cellToStringMapper(1));
		assertEquals("2", Minesweeper.cellToStringMapper(2));
		assertEquals(Minesweeper.STR_ZEO, Minesweeper.cellToStringMapper(0));
	}

	@Test
	public void getStringGrid() {
		MinesweeperData data = MinesweeperTestHelper.getMinesweeperDataFromString(2, 2, "*12 ");
		assertEquals("*1\n2 ", Minesweeper.getStringGrid(data));
	}
}