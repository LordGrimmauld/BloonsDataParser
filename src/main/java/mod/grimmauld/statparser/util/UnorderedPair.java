package mod.grimmauld.statparser.util;

import java.util.Objects;

public class UnorderedPair <F, S> {

	F first;
	S second;

	protected UnorderedPair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	public static <F, S> UnorderedPair<F, S> of(F first, S second) {
		return new UnorderedPair<>(first, second);
	}

	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

	public void setFirst(F first) {
		this.first = first;
	}

	public void setSecond(S second) {
		this.second = second;
	}

	public UnorderedPair<F, S> copy() {
		return UnorderedPair.of(first, second);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof UnorderedPair) {
			final UnorderedPair<?, ?> other = (UnorderedPair<?, ?>) obj;
			return (Objects.equals(first, other.first) && Objects.equals(second, other.second)) ||
				(Objects.equals(first, other.second) && Objects.equals(second, other.first));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (nullHash(first) * 31) ^ nullHash(second);
	}

	int nullHash(Object o) {
		return o == null ? 0 : o.hashCode();
	}

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	public UnorderedPair<S, F> swap() {
		return UnorderedPair.of(second, first);
	}
}
