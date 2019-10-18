package name.skm.graph_test.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import name.skm.graph_test.graph.Graph.Edge;

public enum PathFinder {
	BIDIRECTIONAL, FORWARD {

		@Override
		public <T> Optional<List<T>> getPath(Collection<Edge<T>> edges, T from, T to, boolean directed) {
			Collector<Edge<T>, ?, Map<T, Collection<T>>> toNewIndex = PathFinder.createToIndexCollector(HashMap::new);
			Map<T, Collection<T>> forwardIndex = edges.stream().collect(toNewIndex);
			if (!directed) {
				Collector<Edge<T>, ?, Map<T, Collection<T>>> toForwardIndex =  PathFinder
						.createToIndexCollector(() -> forwardIndex);
				edges.stream().map(Edge::getReversed).collect(toForwardIndex);
			}
			Map<T, T> forwardTracks = new HashMap<>(Collections.singletonMap(from, null));
			Set<T> keySet = Collections.singleton(to);
			WaveSearchState<T> searchState = WaveSearchState.create(from, forwardIndex, forwardTracks, keySet);
			Optional<T> bridge = Optional.empty();
			for (; !searchState.queue.isEmpty() && !bridge.isPresent(); bridge = searchState
					.stepAndReachTarget());
			return bridge.map((T t) -> PathFinder.addToResult(t, searchState.backTrace, new LinkedList<>()));
		}
	};

	public <T> Optional<List<T>> getPath(Collection<Edge<T>> edges, T from, T to, boolean directed2) {
		Collector<Edge<T>, ?, Map<T, Collection<T>>> toNewIndex = PathFinder.createToIndexCollector(HashMap::new);
		Map<T, Collection<T>> forwardIndex = edges.stream().collect(toNewIndex);
		Collector<Edge<T>, ?, Map<T, Collection<T>>> toBackwardIndex = directed2 ? toNewIndex : PathFinder
				.createToIndexCollector(() -> forwardIndex);
		// if !this.directed then shall appear physically the same object as
		// forwardIndex
		Map<T, Collection<T>> backwardIndex = edges.stream().map(Edge::getReversed).collect(toBackwardIndex);
		Map<T, T> forwardTracks = new HashMap<>(Collections.singletonMap(from, null));
		Map<T, T> backwardTracks = new HashMap<>(Collections.singletonMap(to, null));
		WaveSearchState<T> forwardSearchState = WaveSearchState.create(from, forwardIndex, forwardTracks, backwardTracks.keySet());
		WaveSearchState<T> backwardSearchState = WaveSearchState.create(to, backwardIndex, backwardTracks, forwardTracks.keySet());
		Optional<T> bridge = Optional.empty();
		while (!forwardSearchState.queue.isEmpty() && !backwardSearchState.queue.isEmpty() && !bridge.isPresent()) {
			bridge = forwardSearchState.stepAndReachTarget();
			if (!bridge.isPresent()) {
				bridge = ((WaveSearchState<T>) backwardSearchState).stepAndReachTarget();
			}
		}
		return bridge.map((T t) -> {
			LinkedList<T> result = PathFinder.addToResult(backwardSearchState.backTrace.get(t),
					backwardSearchState.backTrace, new LinkedList<>());
			Collections.reverse(result);
			return PathFinder.addToResult(t, forwardSearchState.backTrace, result);
		});
	}

	static <T> List<T> buildPath(WaveSearchState<T> forwardWaveState, T t, WaveSearchState<T> backwardWaveState) {
		LinkedList<T> result = new LinkedList<>();
		if (backwardWaveState != null) {
			addToResult(backwardWaveState.backTrace.get(t), backwardWaveState.backTrace, result);
		Collections.reverse(result);
		}
		addToResult(t, forwardWaveState.backTrace, result);
		return result;
	}

	private static <T> LinkedList<T>  addToResult(T start, Map<T, T> backTrace, LinkedList<T> result) {
		for (T i = start; i != null; i = backTrace.get(i)) {
			result.addFirst(i);
		}
		return result;
	}

	static <U> Collector<Edge<U>, ?, Map<U, Collection<U>>> createToIndexCollector(
			Supplier<Map<U, Collection<U>>> mapFactory) {
		return Collectors.groupingBy(Edge::getFrom, mapFactory,
				Collectors.mapping(Edge::getTo, Collectors.toCollection(HashSet::new)));
	}

}
