package name.skm.graph_test.graph;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GraphImpl<T> implements Graph<T>, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7740513236178739474L;

    /**
     * Can be arbitrary changed at any time, influences only path-searching function behavior when the latter is run.
     */
    private boolean directed = true;

    private Map<Vertex<T>, Vertex<T>> vertices = new HashMap<>();

	private Map<Edge<T>, Edge<T>> edges = new HashMap<>();
	
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

	/* (non-Javadoc)
	 * @see name.skm.graph_test.graph.GraphLike#getVertices()
	 */
	@Override
	public Collection<Vertex<T>> getVertices() {
		return Collections.unmodifiableCollection(vertices.keySet());
	}

	/* (non-Javadoc)
	 * @see name.skm.graph_test.graph.GraphLike#getEdges()
	 */
	@Override
	public Collection<Edge<T>> getEdges() {
		return Collections.unmodifiableCollection(edges.keySet());
	}

	/* (non-Javadoc)
	 * @see name.skm.graph_test.graph.GraphLike#addVertex(T)
	 */
    @Override
	public Vertex<T> addVertex(T t) {
        Vertex<T> vertex = new Vertex<>(t);
        return Optional.ofNullable(vertices.putIfAbsent(vertex, vertex)).orElse(vertex);
    }

    /* (non-Javadoc)
	 * @see name.skm.graph_test.graph.GraphLike#addEdge(T, T)
	 */
    @Override
	public Edge<T> addEdge(T from, T to) {
        Edge<T> edge = new Edge<>(addVertex(from), addVertex(to));
        return Optional.ofNullable(edges.putIfAbsent(edge, edge)).orElse(edge);
    }

}
