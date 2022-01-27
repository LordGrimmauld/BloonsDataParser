package mod.grimmauld.statparser.util;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class UnorderedPair<F, S> {

	F first;
	S second;

	protected UnorderedPair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	public static <F, S> UnorderedPair<F, S> of(F first, S second) {
		return new UnorderedPair<>(first, second);
	}

	public static <T> Predicate<UnorderedPair<T, T>> matchesAtLeastOne(Predicate<T> predicate) {
		return ttUnorderedPair -> predicate.test(ttUnorderedPair.first) || predicate.test(ttUnorderedPair.second);
	}

	public static <T> Predicate<UnorderedPair<T, T>> matchesBoth(Predicate<T> predicate) {
		return ttUnorderedPair -> predicate.test(ttUnorderedPair.first) && predicate.test(ttUnorderedPair.second);
	}

	public static <S, T> Function<UnorderedPair<S, S>, UnorderedPair<T, T>> applyOnBoth(Function<S, T> function) {
		return ssUnorderedPair -> UnorderedPair.of(function.apply(ssUnorderedPair.first), function.apply(ssUnorderedPair.second));
	}

	public F getFirst() {
		return first;
	}

	public void setFirst(F first) {
		this.first = first;
	}

	public S getSecond() {
		return second;
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
