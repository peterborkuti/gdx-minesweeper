package hu.bp.minesweeper.core;


import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
		String fileName = "neighbours.txt";
		MinesweeperTestHelper helper = new MinesweeperTestHelper(fileName);

		helper.forEach(test -> {
			MinesweeperData data = Minesweeper.createMinesweeperData(test.ROWS, test.COLS, test.bombs);

			assertArrayEquals("in file:" + fileName + " " + test.description, test.getBombsNeighboursLinearCoords(), data.getBombsNeighboursLinearCoords());
			assertArrayEquals("in file:" + fileName + " " + test.description, test.getBombsNeighboursCounts(), data.getBombsNeighboursCounts());
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
	public void getIntegerGridAllRowsEqualLength() {
		MinesweeperData data = Minesweeper.createMinesweeperData(20, 15, 5);

		Integer[][] grid = Minesweeper.getIntegerGrid(data);

		long numberOfGivenLengthRows = Arrays.stream(grid).
				map(row -> Arrays.stream(row).map(Minesweeper::cellToStringMapper).collect(Collectors.joining(""))).
				filter(row -> row.length() == 15).
				count();

		assertEquals(20, numberOfGivenLengthRows);
	}

	@Test
	public void getStringGrid() {
		MinesweeperData data = MinesweeperTestHelper.getMinesweeperDataFromString(2, 2, "*12 ");
		assertEquals("*1\n2" + Minesweeper.STR_ZEO, Minesweeper.getStringGrid(data));
	}

	@Test
	public void isIndexChoosedRandomly() {
		AtomicInteger selectedCounter = new AtomicInteger(0);
		assertTrue(
				"selects when random is sure",
				Minesweeper.isIndexChoosedRandomly(0, 10, 1, 0F, selectedCounter)
		);
		assertEquals(1, selectedCounter.get());

		selectedCounter = new AtomicInteger(0);
		assertTrue(
				"selects all when bound equals length",
				Minesweeper.isIndexChoosedRandomly(0, 3, 3, 1F, selectedCounter)
		);
		assertTrue(
				"selects all when bound equals length",
				Minesweeper.isIndexChoosedRandomly(1, 3, 3, 1F, selectedCounter)
		);
		assertTrue(
				"selects all when bound equals length",
				Minesweeper.isIndexChoosedRandomly(2, 3, 3, 1F, selectedCounter)
		);
		assertEquals(3, selectedCounter.get());

		selectedCounter = new AtomicInteger(0);
		for (int i = 0; i < 5; i++) {
			Minesweeper.isIndexChoosedRandomly(i, 5, 3, 0.2F, selectedCounter);
		};
		assertEquals("it should select exactly length", 3, selectedCounter.get());

	}


	@Test
	public void getUniqueRandomsOptimizedBoundEqualsLength() {
		List<Integer> randoms = Minesweeper.getUniqueRandomsOptimized(3, 3);
		Collections.sort(randoms);

		Integer[] sortedRandoms = randoms.toArray(new Integer[0]);
		assertArrayEquals(new Integer[]{0, 1, 2}, sortedRandoms);
	}

	@Test
	public void getUniqueRandomsOptimizedBoundGreaterThanLength() {
		int bound = 10;
		int length = 3;
		List<Integer> randoms = Minesweeper.getUniqueRandomsOptimized(bound, length);

		assertEquals(length, randoms.size());

		Set<Integer> set = new HashSet<>(randoms);

		assertEquals("numbers should unique", randoms.size(), set.size());

		randoms.forEach(r -> assertTrue("numbers should be between 0 and bound", r >= 0 && r < bound));
	}

	public void performanceTest() {
		Date start = new Date();
		for (int i = 0; i < 100000; i++) {
			Minesweeper.getUniqueRandomsOptimized(100, 10);
		}
		System.out.println(new Date().getTime() - start.getTime());

		start = new Date();
		for (int i = 0; i < 100000; i++) {
			Minesweeper.getUniqueRandoms(100, 10);
		}
		System.out.println(new Date().getTime() - start.getTime());
	}
}