package name.skm.graph_test.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

/**
 * Represents breadth-first search ("Wave algorithm", "Lee algorithm") step
 * state.
 * 
 * @author Michael Skidan
 *
 * @param <T>
 */
public class SearchState<T> {
    /**
     * "From-to" "Multimap" of all possible "steps".
     */
    public final Map<T, Collection<T>> index;
    /**
     * "To-from"-directed {@link Map} of all performed steps, also used as
     * {@link Set} of all passed-through vertices to prevent performing multiple
     * steps to one and the same vertex.
     */
    public final Map<T, T> backTrace;
    /**
     * Current "wave front" of the search process.
     */
    public final Queue<T> queue;
    /**
     * Targets the search process must reach. Implemented as {@link Collection}
     * (usually {@link Set}) because this can be another wave's passed-through
     * vertices set that can be dynamically changed from outside. In the case of a
     * single target should be a singleton {@link Set} (
     * {@link Collections#singleton(Object)}).
     */
    public final Collection<T> targets;

    public SearchState(Map<T, Collection<T>> index, Map<T, T> backTrace, Queue<T> queue, Collection<T> targets) {
        this.index = index;
        this.backTrace = backTrace;
        this.queue = queue;
        this.targets = targets;
    }

    /**
     * Performing search algorithm step:
     * 
     * <ol>
     * <li>polling next vertex from the wave front</li>
     * <li>getting its children list</li>
     * <li>filtering away those ones that have been already passed-through</li>
     * <li>from the filtered-through rest:
     * <ol>
     * <li>adding each to the back-trace map of passed-through vertices</li>
     * <li>adding each to the wavefront-representing queue</li>
     * <li>trying to find at least one that belongs to the targets collection and
     * returning {@link Optional} containing the first such one.</li>
     * </ol>
     * </li>
     * </ol>
     * 
     * @return
     */
    public <U> Optional<T> stepAndReachTarget() {
        T current = this.queue.poll();
        Optional<T> result = this.index.getOrDefault(current, Collections.emptySet()).stream().filter((T t) -> !this.backTrace.containsKey(t)).map((T t) -> {
            this.backTrace.put(t, current);
            this.queue.add(t);
            return t;
        }).filter(this.targets::contains).findAny();
        return result;
    }

    public static <T> SearchState<T> create(T from, Map<T, Collection<T>> index, Map<T, T> tracks, Collection<T> targets) {
        return new SearchState<T>(index, tracks, new LinkedList<>(Arrays.asList(from)), targets);
    }
}