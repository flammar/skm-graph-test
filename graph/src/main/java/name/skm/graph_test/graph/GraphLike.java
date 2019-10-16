package name.skm.graph_test.graph;

import java.util.List;
import java.util.Optional;

public interface GraphLike<T> {

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

	Optional<List<T>> getPath(T from, T to);

}