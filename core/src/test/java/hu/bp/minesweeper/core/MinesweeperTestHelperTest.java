package hu.bp.minesweeper.core;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MinesweeperTestHelperTest {

	@Test
	public void getBombsFromString() {
		List<Integer> bombs = MinesweeperTestHelper.getBombsFromString("*1111");
		assertEquals(1, bombs.size());
		assertEquals(0, bombs.get(0).longValue());
	}

	@Test
	public void getNeighboursFromString() {
		Map<Integer, Long> neighbours = MinesweeperTestHelper.getNeighboursFromString("5432");

		Integer[] keys = neighbours.keySet().toArray(new Integer[0]);
		Arrays.sort(keys);

		assertArrayEquals(new Integer[]{0, 1, 2, 3}, keys);
		Long[] values = neighbours.values().toArray(new Long[0]);

		assertArrayEquals(new Long[]{5L, 4L, 3L, 2L}, values);

	}
}