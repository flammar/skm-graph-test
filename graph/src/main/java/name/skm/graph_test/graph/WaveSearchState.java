package name.skm.graph_test.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class WaveSearchState<T> {
    public Map<T, Collection<T>> index;
    public Map<T, T> backTrace;
    public Queue<T> queue;
    public Collection<T> targets;

    public WaveSearchState(Map<T, Collection<T>> index, Map<T, T> backTrace, Queue<T> queue, Collection<T> targets) {
        this.index = index;
        this.backTrace = backTrace;
        this.queue = queue;
        this.targets = targets;
    }

    public static <T> WaveSearchState<T> create(T from, Map<T, Collection<T>> index, Map<T, T> tracks, Collection<T> targets) {
        return new WaveSearchState<T>(index, tracks, new LinkedList<>(Arrays.asList(from)), targets);
    }
}