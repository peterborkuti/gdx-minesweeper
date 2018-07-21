package hu.bp.minesweeper.graph;

public class Counter implements Runnable {
	private int counter;
	private Runnable runnable;

	/**
	 * It decreases its counter's value when its run() method run.
	 * When counter reaches zero, it runs the runnable.
	 * @param counterValue
	 * @param runnable
	 */
	public Counter(int counterValue, Runnable runnable) {
		this.counter = counterValue;
		this.runnable = runnable;
	}

	@Override
	public void run() {
		counter--;
		if (counter == 0) {
			runnable.run();
		}
	}
}
