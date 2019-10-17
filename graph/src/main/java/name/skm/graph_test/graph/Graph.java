package name.skm.graph_test.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.NonNull;

public class Graph<T> implements GraphLike<T> {

    private boolean directed = true;

    private Collection<T> vertices = new HashSet<>();

	private Collection<Edge<T>> edges = new HashSet<>();

	@Data
	public static class Edge<T> {
	    public Edge(T from, T to) {
	        this.from = from;
	        this.to = to;
	    }
	
	    @NonNull
	    private T from;
	    @NonNull
	    private T to;
	
	    public Edge<T> getReversed() {
	        return new Edge<T>(to, from);
	    }
	}

	/* (non-Javadoc)
	 * @see name.skm.graph_test.graph.GraphLike#isDirected()
	 */
	@Override
	public boolean isDirected() {
	    return directed;
	}

	/* (non-Javadoc)
	 * @see name.skm.graph_test.graph.GraphLike#setDirected(boolean)
	 */
	@Override
	public void setDirected(boolean directed) {
	    this.directed = directed;
	}

	@Override
	public Collection<T> getVertices() {
		return Collections.unmodifiableCollection(vertices);
	}

	@Override
	public Collection<Edge<T>> getEdges() {
		return Collections.unmodifiableCollection(edges);
	}

	/* (non-Javadoc)
	 * @see name.skm.graph_test.graph.GraphLike#addVertex(T)
	 */
    @Override
	public boolean addVertex(T vertex) {
        return vertices.add(vertex);
    }

    /* (non-Javadoc)
	 * @see name.skm.graph_test.graph.GraphLike#addEdge(T, T)
	 */
    @Override
	public boolean addEdge(T from, T to) {
        addVertex(from);
        addVertex(to);
        Edge<T> edge = new Edge<>(from, to);
        return edges.add(edge);
    }

    /* (non-Javadoc)
	 * @see name.skm.graph_test.graph.GraphLike#getPath(T, T)
	 */
    @Override
	public Optional<List<T>> getPath(T from, T to) {
        Collector<Edge<T>, ?, Map<T, Collection<T>>> toNewIndex = createToIndexCollector(HashMap::new);
        Map<T, Collection<T>> forwardIndex = edges.stream().collect(toNewIndex);
        // if !this.directed then shall appear physically the same object as
        // forwardIndex
        Map<T, Collection<T>> backwardIndex = edges.stream().map(Edge::getReversed)
                .collect((Collector<Edge<T>, ?, Map<T, Collection<T>>>) (directed ? toNewIndex : createToIndexCollector(() -> forwardIndex)));
        Map<T, T> forwardTracks = new HashMap<>(Collections.singletonMap(from, null));
        Map<T, T> backwardTracks = new HashMap<>(Collections.singletonMap(to, null));
        return Utils.performPathSearch(WaveSearchState.create(from, forwardIndex, forwardTracks, backwardTracks.keySet()),
                WaveSearchState.create(to, backwardIndex, backwardTracks, forwardTracks.keySet()));
    }

    private Collector<Edge<T>, ?, Map<T, Collection<T>>> createToIndexCollector(Supplier<Map<T, Collection<T>>> mapFactory) {
        return Collectors.groupingBy(Edge::getFrom, mapFactory, Collectors.mapping(Edge::getTo, Collectors.toCollection(HashSet::new)));
    }

}
