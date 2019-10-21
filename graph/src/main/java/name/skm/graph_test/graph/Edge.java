package name.skm.graph_test.graph;

import java.io.Serializable;

public class Edge<T> implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 3504876715525336156L;
    private Vertex<T> from;
    private Vertex<T> to;

    public Edge(T from, T to) {
        this(new Vertex<>(from), new Vertex<>(to));
    }

    public Edge(Vertex<T> from, Vertex<T> to) {
        super();
        this.from = from;
        this.to = to;
    }

    public Edge<T> getReversed() {
        return new Edge<T>(to, from);
    }

    public Vertex<T> getFrom() {
        return from;
    }

    public Vertex<T> getTo() {
        return to;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((from == null) ? 0 : from.hashCode());
        result = prime * result + ((to == null) ? 0 : to.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Edge<?> other = (Edge<?>) obj;
        if (from == null) {
            if (other.from != null)
                return false;
        } else if (!from.equals(other.from))
            return false;
        if (to == null) {
            if (other.to != null)
                return false;
        } else if (!to.equals(other.to))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Edge [from=" + from + ", to=" + to + "]";
    }
}