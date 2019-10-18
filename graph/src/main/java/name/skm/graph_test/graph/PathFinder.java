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

/**
 * Class that encapsulated "getPath" logic and decouples it from the {@link GraphImpl} structure itself. 
 * 
 * @author Michael Skidan
 *
 */
public enum PathFinder {
	/**
	 * 
	 * @author Michael Skidan
	 * 
	 * Runs two breadth-search (Lee algorithm) waves towards each other.
	 */
	BIDIRECTIONAL, /**
	 * @author Michael Skidan
	 * 
	 * Runs one forward-directed breadth-search (Lee algorithm) wave to the target.
	 *
	 */
	FORWARD {

	    public <T> Optional<List<T>> getPath(GraphLike<T> graph, T from, T to) {
	        Collection<Edge<T>> edges = graph.getEdges();
            Collector<Edge<T>, ?, Map<T, Collection<T>>> toNewIndex = PathFinder.createToIndexCollector(HashMap::new);
            Map<T, Collection<T>> forwardIndex = edges.stream().collect(toNewIndex);
            if (!graph.isDirected()) {
            	Collector<Edge<T>, ?, Map<T, Collection<T>>> toForwardIndex =  PathFinder
            			.createToIndexCollector(() -> forwardIndex);
            	edges.stream().map(Edge::getReversed).collect(toForwardIndex);
            }
            Map<T, T> forwardTracks = new HashMap<>(Collections.singletonMap(from, null));
            Set<T> keySet = Collections.singleton(to);
            SearchState<T> searchState = SearchState.create(from, forwardIndex, forwardTracks, keySet);
            Optional<T> bridge = Optional.empty();
            for (; !searchState.queue.isEmpty() && !bridge.isPresent(); bridge = searchState
            		.stepAndReachTarget());
            return bridge.map((T t) -> PathFinder.addToResult(t, searchState.backTrace, new LinkedList<>()));
	    }
	};

    public <T> Optional<List<T>> getPath(GraphLike<T> graph, T from, T to) {
		Collection<Edge<T>> edges = graph.getEdges();
        Collector<Edge<T>, ?, Map<T, Collection<T>>> toNewIndex = PathFinder.createToIndexCollector(HashMap::new);
        Map<T, Collection<T>> forwardIndex = edges.stream().collect(toNewIndex);
        Collector<Edge<T>, ?, Map<T, Collection<T>>> toBackwardIndex = graph.isDirected() ? toNewIndex : PathFinder
        		.createToIndexCollector(() -> forwardIndex);
        // if !this.directed then shall appear physically the same object as
        // forwardIndex
        Map<T, Collection<T>> backwardIndex = edges.stream().map(Edge::getReversed).collect(toBackwardIndex);
        Map<T, T> forwardTracks = new HashMap<>(Collections.singletonMap(from, null));
        Map<T, T> backwardTracks = new HashMap<>(Collections.singletonMap(to, null));
        SearchState<T> forwardSearchState = SearchState.create(from, forwardIndex, forwardTracks, backwardTracks.keySet());
        SearchState<T> backwardSearchState = SearchState.create(to, backwardIndex, backwardTracks, forwardTracks.keySet());
        Optional<T> bridge = Optional.empty();
        while (!forwardSearchState.queue.isEmpty() && !backwardSearchState.queue.isEmpty() && !bridge.isPresent()) {
        	bridge = forwardSearchState.stepAndReachTarget();
        	if (!bridge.isPresent()) {
        		bridge = ((SearchState<T>) backwardSearchState).stepAndReachTarget();
        	}
        }
        return bridge.map((T t) -> {
        	LinkedList<T> result = PathFinder.addToResult(backwardSearchState.backTrace.get(t),
        			backwardSearchState.backTrace, new LinkedList<>());
        	Collections.reverse(result);
        	return PathFinder.addToResult(t, forwardSearchState.backTrace, result);
        });
	}

	private static <T> LinkedList<T> addToResult(T start, Map<T, T> backTrace, LinkedList<T> result) {
		for (T i = start; i != null; i = backTrace.get(i)) {
			result.addFirst(i);
		}
		return result;
	}

	private static <U> Collector<Edge<U>, ?, Map<U, Collection<U>>> createToIndexCollector(
			Supplier<Map<U, Collection<U>>> mapFactory) {
		return Collectors.groupingBy(Edge::getFrom, mapFactory,
				Collectors.mapping(Edge::getTo, Collectors.toCollection(HashSet::new)));
	}

}
