package name.skm.graph_test.graph;

import lombok.Data;
import lombok.NonNull;

public class Graph<T> {

	private boolean directed = false;

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

	public boolean isDirected() {
		return directed;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
	}

}
