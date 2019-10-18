package name.skm.graph_test.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

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
