package name.skm.graph_test.graph;

import lombok.Data;
import lombok.NonNull;

@Data
public class Edge<T> {
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