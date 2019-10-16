package name.skm.graph_test.graph;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;

public class WaveSearchState<T> {
    public Map<T, Collection<T>> index;
    public Map<T, T> backTrace;
    public Queue<T> queue;
    public Collection<T> target;

    public WaveSearchState(Map<T, Collection<T>> index, Map<T, T> backTrace, Queue<T> queue, Collection<T> target) {
        this.index = index;
        this.backTrace = backTrace;
        this.queue = queue;
        this.target = target;
    }
}