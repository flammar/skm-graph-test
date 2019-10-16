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
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.common.base.Suppliers;

import lombok.Data;
import lombok.NonNull;

public class Graph<T> {

    private boolean directed = true;

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
    public boolean addVertex(T vertex) {
        return vertices.add(vertex);
    }

    /**
     * @param from
     * @param to
     * @return <tt>true</tt> if this graph did not already contain an edge with the
     *         specified vertices
     */
    public boolean addEdge(T from, T to) {
        addVertex(from);
        addVertex(to);
        Edge<T> edge = new Edge<>(from, to);
        return edges.add(edge);
    }

    public Optional<List<T>> getPath(T from, T to) {
        Collector<Edge<T>, ?, Map<T, Collection<T>>> toIndex = createToIndexCollector(HashMap::new);
        Map<T, Collection<T>> forwardIndex = edges.stream().collect(toIndex);
        // if !this.directed then shall appear physically the same object as forwardIndex
        Map<T, Collection<T>> backwardIndex = edges.stream().map(Edge::getReversed).collect(( Collector<Edge<T>, ?, Map<T, Collection<T>>>)(directed ? toIndex : createToIndexCollector(Suppliers.ofInstance(forwardIndex))));
        Map<T, T> forwardTracks = new HashMap<>(Collections.singletonMap(from, null));
        Map<T, T> backwardTracks = new HashMap<>(Collections.singletonMap(to, null));
        WaveSearchState<T> forwardWaveState = new WaveSearchState<T>(forwardIndex, forwardTracks, new LinkedList<>(Arrays.asList(from)), backwardTracks.keySet());
        WaveSearchState<T> backwardWaveState = new WaveSearchState<T>(backwardIndex, backwardTracks, new LinkedList<>(Arrays.asList(to)), forwardTracks.keySet());
        return performPathSearch(forwardWaveState, backwardWaveState);
    }

    private Collector<Edge<T>, ?, Map<T, Collection<T>>> createToIndexCollector(Supplier<Map<T, Collection<T>>> mapFactory) {
        return Collectors.groupingBy(Edge::getFrom, mapFactory, Collectors.mapping(Edge::getTo, Collectors.toCollection(HashSet::new)));
    }

    private static <T> Optional<List<T>> performPathSearch(WaveSearchState<T> forwardWaveState, WaveSearchState<T> backwardWaveState) {
        Optional<T> bridge = Optional.empty();
        while (!forwardWaveState.queue.isEmpty() && !backwardWaveState.queue.isEmpty() && !bridge.isPresent()) {
            bridge = stepAndReachTarget(forwardWaveState);
            if (!bridge.isPresent()) {
                bridge = stepAndReachTarget(backwardWaveState);
            }
        }
        return bridge.map((T t) -> {
            LinkedList<T> result = new LinkedList<>(Arrays.asList(t));
            for (T i = backwardWaveState.backTrace.get(t); i != null; i = backwardWaveState.backTrace.get(i)) {
                result.addLast(i);
            }
            for (T i = forwardWaveState.backTrace.get(t); i != null; i = forwardWaveState.backTrace.get(i)) {
                result.addFirst(i);
            }
            return result;
        });
    }

    private static <T> Optional<T> stepAndReachTarget(WaveSearchState<T> parameterObject) {
        T current = parameterObject.queue.poll();
        return parameterObject.index.getOrDefault(current, Collections.emptySet()).stream().filter((T t) -> !parameterObject.backTrace.containsKey(t)).map((T t) -> {
            parameterObject.backTrace.put(t, current);
            parameterObject.queue.add(t);
            return t;
        }).filter(parameterObject.target::contains).findAny();
    }

}
