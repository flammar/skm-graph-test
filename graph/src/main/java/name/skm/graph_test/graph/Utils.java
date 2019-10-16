package name.skm.graph_test.graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Utils {

	private Utils() {
	}

	static <T> Optional<List<T>> performPathSearch(WaveSearchState<T> forwardWaveState, WaveSearchState<T> backwardWaveState) {
	    Optional<T> bridge = Optional.empty();
	    if (backwardWaveState != null) {
			while (!forwardWaveState.queue.isEmpty() && !backwardWaveState.queue.isEmpty() && !bridge.isPresent()) {
				bridge = forwardWaveState.stepAndReachTarget();
				if (!bridge.isPresent()) {
					bridge = backwardWaveState.stepAndReachTarget();
				}
			}
		} else {
			for (; !forwardWaveState.queue.isEmpty() && !bridge.isPresent(); bridge = forwardWaveState
					.stepAndReachTarget());
		}
		return bridge.map((T t) -> buildPath(forwardWaveState, t, backwardWaveState));
	}

	private static <T> List<T> buildPath(WaveSearchState<T> forwardWaveState, T t, WaveSearchState<T> backwardWaveState) {
		LinkedList<T> result = new LinkedList<>(Arrays.asList(t));
		if (backwardWaveState != null) {
			for (T i = backwardWaveState.backTrace.get(t); i != null; i = backwardWaveState.backTrace.get(i)) {
				result.addLast(i);
			}
		}
		for (T i = forwardWaveState.backTrace.get(t); i != null; i = forwardWaveState.backTrace.get(i)) {
			result.addFirst(i);
		}
		return result;
	}

}
