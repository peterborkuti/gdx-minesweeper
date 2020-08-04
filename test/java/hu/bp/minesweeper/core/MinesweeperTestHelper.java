package hu.bp.minesweeper.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class MinesweeperTestHelper implements Iterable<MinesweeperData> {
	private List<MinesweeperData> testData = new ArrayList<>();

	public MinesweeperTestHelper(String testFileName) throws IOException {
		BufferedReader bfr = new BufferedReader(new FileReader("./neighbours.txt"));

		StringBuilder sb = new StringBuilder();
		String line = bfr.readLine();
		int rows = 0;
		int cols = 0;
		int lineNumber = 0;

		while (line != null) {
			if (line.startsWith(";")) {
				line = bfr.readLine();
				continue;
			}

			lineNumber++;

			if (line.length() != 0) {
				sb.append(line);
				cols = Math.max(cols, line.length());
				rows++;
			} else {
				processTest(rows, cols, sb.toString(), lineNumber);

				sb = new StringBuilder();
				rows = 0;
				cols = 0;
			}

			line = bfr.readLine();
		}

		if (sb.toString().length() != 0) {
			processTest(rows, cols, sb.toString(), lineNumber);
		}
	}

	private void processTest(int rows, int cols, String test, int lineNumber) {
		testData.add(getMinesweeperDataFromString(rows, cols, test, lineNumber));
	}

	public static MinesweeperData getMinesweeperDataFromString(int rows, int cols, String s, int... lineNumber) {
		List<Integer> bombs = getBombsFromString(s);

		Map<Integer, Long> neighboursExpected = getNeighboursFromString(s);

		String description = s;
		if (lineNumber.length > 0) {
			description = "Line:" + lineNumber[0] + "," + s;
		}

		return new MinesweeperData(rows, cols, bombs, neighboursExpected, description);
	}

	//TODO: should use indexOf for less step
	public static List<Integer> getBombsFromString(String grid) {
		return IntStream.range(0, grid.length()).
				filter(i -> Minesweeper.STR_BOMB.equals("" + grid.charAt(i))).boxed().
				collect(Collectors.toList());
	}

	public static Map<Integer, Long> getNeighboursFromString(String grid) {
		return IntStream.range(0, grid.length()).filter(i -> Character.isDigit(grid.charAt(i))).boxed().
				collect(Collectors.toMap(Integer::new, i -> Long.parseLong("" + grid.charAt(i))));
	}

	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	public Iterator<MinesweeperData> iterator() {
		return testData.iterator();
	}
}
