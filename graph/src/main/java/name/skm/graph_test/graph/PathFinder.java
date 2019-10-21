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

	    protected <T> Optional<List<Vertex<T>>> getPath1(Graph<T> graph, Vertex<T> from, Vertex<T> to) {
	        Collection<Edge<T>> edges = graph.getEdges();
            Collector<Edge<T>, ?, Map<Vertex<T>, Collection<Vertex<T>>>> toNewIndex = PathFinder.createToIndexCollector(HashMap::new);
            Map<Vertex<T>, Collection<Vertex<T>>> forwardIndex = edges.stream().collect(toNewIndex);
            if (!graph.isDirected()) {
            	Collector<Edge<T>, ?, Map<Vertex<T>, Collection<Vertex<T>>>> toForwardIndex =  PathFinder
            			.createToIndexCollector(() -> forwardIndex);
            	edges.stream().map(Edge::getReversed).collect(toForwardIndex);
            }
            Set<Vertex<T>> keySet = Collections.singleton(to);
            SearchState<Vertex<T>> searchState = SearchState.create(from, forwardIndex, new HashMap<>(Collections.singletonMap(from, null)), keySet);
            Optional<Vertex<T>> bridge = Optional.empty();
            for (; !searchState.queue.isEmpty() && !bridge.isPresent(); bridge = searchState
            		.stepAndReachTarget());
            return bridge.map((Vertex<T> t) -> PathFinder.addToResult(t, searchState.backTrace, new LinkedList<>()));
	    }
	};

    public <T> Optional<List<Vertex<T>>> getPath(Graph<T> graph, T from, T to) {
        Vertex<T> fromV = new Vertex<T>(from);
        Vertex<T> toV = new Vertex<T>(to);
        return fromV.equals(toV) ? Optional.of(Collections.singletonList(fromV)) : getPath1(graph, fromV, toV);
    }
    
    protected <T> Optional<List<Vertex<T>>> getPath1(Graph<T> graph, Vertex<T> from, Vertex<T> to) {
		Collection<Edge<T>> edges = graph.getEdges();
        Collector<Edge<T>, ?, Map<Vertex<T>, Collection<Vertex<T>>>> toNewIndex = PathFinder.createToIndexCollector(HashMap::new);
        Map<Vertex<T>, Collection<Vertex<T>>> forwardIndex = edges.stream().collect(toNewIndex);
        Collector<Edge<T>, ?, Map<Vertex<T>, Collection<Vertex<T>>>> toBackwardIndex = graph.isDirected() ? toNewIndex : PathFinder
        		.createToIndexCollector(() -> forwardIndex);
        // if !this.directed then shall appear physically the same object as
        // forwardIndex
        Map<Vertex<T>, Collection<Vertex<T>>> backwardIndex = edges.stream().map(Edge::getReversed).collect(toBackwardIndex);
        Map<Vertex<T>, Vertex<T>> forwardTracks = new HashMap<>(Collections.singletonMap(from, null));
        Map<Vertex<T>, Vertex<T>> backwardTracks = new HashMap<>(Collections.singletonMap(to, null));
        SearchState<Vertex<T>> forwardSearchState = SearchState.create(from, forwardIndex, forwardTracks, backwardTracks.keySet());
        SearchState<Vertex<T>> backwardSearchState = SearchState.create(to, backwardIndex, backwardTracks, forwardTracks.keySet());
        Optional<Vertex<T>> bridge = Optional.empty();
        while (!forwardSearchState.queue.isEmpty() && !backwardSearchState.queue.isEmpty() && !bridge.isPresent()) {
        	bridge = forwardSearchState.stepAndReachTarget();
        	if (!bridge.isPresent()) {
        		bridge = ((SearchState<Vertex<T>>) backwardSearchState).stepAndReachTarget();
        	}
        }
        return bridge.map((Vertex<T> t) -> {
        	LinkedList<Vertex<T>> result = PathFinder.addToResult(backwardSearchState.backTrace.get(t),
        			backwardSearchState.backTrace, new LinkedList<>());
        	Collections.reverse(result);
        	return PathFinder.addToResult(t, forwardSearchState.backTrace, result);
        });
	}

	private static <U> LinkedList<U> addToResult(U start, Map<U, U> backTrace, LinkedList<U> result) {
		for (U i = start; i != null; i = backTrace.get(i)) {
			result.addFirst(i);
		}
		return result;
	}

	private static <U> Collector<Edge<U>, ?, Map<Vertex<U>, Collection<Vertex<U>>>> createToIndexCollector(
			Supplier<Map<Vertex<U>, Collection<Vertex<U>>>> mapFactory) {
		return Collectors.groupingBy(Edge::getFrom, mapFactory,
				Collectors.mapping(Edge::getTo, Collectors.toCollection(HashSet::new)));
	}

}
