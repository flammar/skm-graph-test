package name.skm.graph_test.graph;

import java.util.Collection;

public interface Graph<T> {

	boolean isDirected();

	void setDirected(boolean directed);

	/**
	 * @param vertex
	 * @return <tt>true</tt> if this graph did not already contain the specified
	 *         vertex
	 */
	boolean addVertex(T vertex);

	/**
	 * @param from
	 * @param to
	 * @return <tt>true</tt> if this graph did not already contain an edge with the
	 *         specified vertices
	 */
	boolean addEdge(T from, T to);

	Collection<T> getVertices();

	Collection<Edge<T>> getEdges();

}