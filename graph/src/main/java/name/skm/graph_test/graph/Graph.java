package name.skm.graph_test.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

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

    private Collection<T> vertices = new HashSet<>();
    private Collection<Edge<T>> edges = new HashSet<>();

    /**
     * @param vertex
     * @return <tt>true</tt> if this graph did not already contain the specified
     *         vertex
     */
    public boolean addVertix(T vertex) {
        return vertices.add(vertex);
    }

    /**
     * @param from
     * @param to
     * @return <tt>true</tt> if this graph did not already contain an edge with the
     *         specified vertices
     */
    public boolean addEdge(T from, T to) {
        Edge<T> edge = new Edge<>(from, to);
        return edges.add(edge);
    }

    public Optional<List<T>> getPath(T from, T to) {
        Map<T, Collection<T>> forwardIndex = new HashMap<>();
        Map<T, Collection<T>> backwardIndex = directed ? new HashMap<>() : forwardIndex;
        for (Edge<T> e : edges) {
            putPair(forwardIndex, e.getFrom(), e.getTo());
            putPair(backwardIndex, e.getTo(), e.getFrom());
        }
        Map<T, T> forwardTracks = new HashMap<>(Collections.singletonMap(from, null));
        Map<T, T> backwardTracks = new HashMap<>(Collections.singletonMap(to, null));
        Queue<T> forwardQueue = new LinkedList<>(Arrays.asList(from));
        Queue<T> backwardQueue = new LinkedList<>(Arrays.asList(to));

        Optional<T> bridge = Optional.empty();
        while (!forwardQueue.isEmpty() && !backwardQueue.isEmpty() && !bridge.isPresent()) {
            bridge = processOneElement(forwardIndex, forwardTracks, forwardQueue, backwardTracks);
            if (bridge.isPresent())
                break;
            bridge = processOneElement(backwardIndex, backwardTracks, backwardQueue, forwardTracks);
        }
        return bridge.map((T t) -> {
            LinkedList<T> result = new LinkedList<T>();
            for (T i = t; i != null; i = backwardTracks.get(i))
                result.addLast(i);
            for (T i = t; i != null; i = forwardTracks.get(i))
                result.addFirst(i);
            return result;
        });
    }

    private Optional<T> processOneElement(Map<T, Collection<T>> index, Map<T, T> passed, Queue<T> queue, Map<T, T> backPassed) {
        T current = queue.poll();
        return index.getOrDefault(current, Collections.emptySet()).stream().filter((T t) -> !passed.containsKey(t)).map((T t) -> {
            passed.put(t, current);
            queue.add(t);
            return t;
        }).filter(backPassed::containsKey).findAny();
    }

    void putPair(Map<T, Collection<T>> map, T from, T to) {
        map.computeIfAbsent(from, (T a) -> new HashSet<T>());
        map.get(from).add(to);
    }

}
