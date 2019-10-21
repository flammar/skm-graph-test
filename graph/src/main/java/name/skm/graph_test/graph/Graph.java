package name.skm.graph_test.graph;

import java.util.Collection;

public interface Graph<T> {

	boolean isDirected();

	void setDirected(boolean directed);

	/**
	 * Creates or finds and adds vertex for the value
	 * 
	 * @param value
	 * @return {@link Vertex} which contains the value
	 */
	Vertex<T> addVertex(T value);

	/**
	 * Creates or finds and adds edge connecting the values
	 * 
	 * @param from
	 * @param to
	 * @return {@link Edge} containing vertices with the values 
	 */
	Edge<T> addEdge(T from, T to);

	Collection<Vertex<T>> getVertices();

	Collection<Edge<T>> getEdges();

}