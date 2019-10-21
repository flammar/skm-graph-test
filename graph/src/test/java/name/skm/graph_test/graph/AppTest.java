package name.skm.graph_test.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

	private GraphImpl<Integer> graph;

    @Test
	public void testForward() {
        testPathFinder(PathFinder.FORWARD);
    }

    @Test
    public void testBidirectional() {
        testPathFinder(PathFinder.BIDIRECTIONAL);
    }

	@Before
	public void before() {
        graph = new GraphImpl<Integer>();
		graph.addEdge(1, 5);
		graph.addEdge(1, 6);
        // repeating edges
        graph.addEdge(1, 6);
		// self-looped edges
        graph.addEdge(6, 6);
        graph.addEdge(5, 5);
		graph.addEdge(88, 100);
		graph.addEdge(99, 100);
		graph.addEdge(5, 10);
		graph.addEdge(5, 11);
		graph.addEdge(88, 99);
		graph.addEdge(77, 99);
		graph.addEdge(77, 88);
		graph.addEdge(6, 16);
		graph.addEdge(6, 17);
		graph.addEdge(6, 18);
		graph.addEdge(16, 88);
        graph.addEdge(200, 6);
        graph.addEdge(100, 5);
        graph.addEdge(null, 5);
        graph.addEdge(null, 6);
    }

	private void testPathFinder(PathFinder pf) {
        graph.setDirected(true);
		assertPathIs(getNormalPath(pf), Arrays.asList(1, 6, 16, 88, 100));
        assertPathIs(getNormalNullPath(pf), Arrays.asList(null, 6, 16, 88, 100));
        assertPathIs(getBidirOnlyPath(pf), null);
        assertPathIs(getBidirOnlyNullPath(pf), null);

        assertPathIs(toValuePathOptional(pf.getPath(graph, 1, 1)), Arrays.asList((Integer)1));
        assertPathIs(toValuePathOptional(pf.getPath(graph, null, null)), Arrays.asList((Integer)null));
        
		// when the graph is not directed paths become passable and/or shorter
        graph.setDirected(false);
        assertPathIs(getNormalPath(pf), Arrays.asList(1, 5, 100));
        assertPathIs(getNormalNullPath(pf), Arrays.asList(null, 5, 100));
        assertPathIs(getBidirOnlyPath(pf), Arrays.asList(1, 6, 200));
        assertPathIs(getBidirOnlyNullPath(pf), Arrays.asList(null, 6, 200));

        assertPathIs(toValuePathOptional(pf.getPath(graph, 1, 1)), Arrays.asList((Integer)1));
        assertPathIs(toValuePathOptional(pf.getPath(graph, null, null)), Arrays.asList((Integer)null));

	}

    public void assertPathIs(Optional<List<Integer>> path, List<Integer> list) {
        if(list != null) {
            assertTrue( path.isPresent() );
//          System.out.println(path);
            assertEquals(path.get(), list);
        } else {
            assertFalse( path.isPresent() );
        }
    }

    private Optional<List<Integer>> getBidirOnlyPath(PathFinder pf) {
        return toValuePathOptional(pf.getPath(graph, 1, 200));
    }

    private Optional<List<Integer>> getBidirOnlyNullPath(PathFinder pf) {
        return toValuePathOptional(pf.getPath(graph, null, 200));
    }

    private Optional<List<Integer>> getNormalPath(PathFinder pf) {
        return toValuePathOptional(pf.getPath(graph, 1, 100));
    }

    private Optional<List<Integer>> getNormalNullPath(PathFinder pf) {
        return toValuePathOptional(pf.getPath(graph, null, 100));
    }

    private static <T> Optional<List<T>> toValuePathOptional(Optional<List<Vertex<T>>> path) {
        return path.map((l) -> l.stream().map(Vertex::getValue).collect(Collectors.toList()));
    }

}
