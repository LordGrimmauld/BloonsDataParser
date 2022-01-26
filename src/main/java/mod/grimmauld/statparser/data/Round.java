package mod.grimmauld.statparser.data;

public class Round {
	public int round;
	public int rbe;
	public int cashThisRound;
	public int accumulatedBefore;

	public int getRound() {
		return round;
	}

	public void setAccumulatedBefore(int accumulatedBefore) {
		this.accumulatedBefore = accumulatedBefore;
	}
}
