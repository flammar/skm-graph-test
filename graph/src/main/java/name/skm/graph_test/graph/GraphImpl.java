package name.skm.graph_test.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class GraphImpl<T> implements Graph<T> {

    /**
     * Can be arbitrary changed at any time, influences only path-searching function behavior when the latter is run.
     */
    private boolean directed = true;

    private Collection<T> vertices = new HashSet<>();

	private Collection<Edge<T>> edges = new HashSet<>();
	
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
	public Collection<T> getVertices() {
		return Collections.unmodifiableCollection(vertices);
	}

	/* (non-Javadoc)
	 * @see name.skm.graph_test.graph.GraphLike#getEdges()
	 */
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

}
