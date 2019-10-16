package name.skm.graph_test.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
	/**
	 * Rigorous Test :-)
	 */
	@Test
	public void shouldAnswerWithTrue() {
		GraphLike<Integer> graph = new Graph<Integer>();
		graph.addEdge(1, 5);
		graph.addEdge(1, 6);
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

		Optional<List<Integer>> path = graph.getPath(1, 100);
		assertTrue( path.isPresent() );
		assertEquals(path.get(), Arrays.asList(1, 6, 16, 88, 100));
        System.out.println(path);
		Optional<List<Integer>> path2 = graph.getPath(1, 200);
        System.out.println(path2);
        assertFalse( path2.isPresent() );
        graph.setDirected(false);
        Optional<List<Integer>> path3 = graph.getPath(1, 100);
        assertTrue( path3.isPresent() );
        assertEquals(path3.get(), Arrays.asList(1, 5, 100));
        System.out.println(path3);
        Optional<List<Integer>> path4 = graph.getPath(1, 200);
        assertTrue( path4.isPresent() );
        assertEquals(path4.get(), Arrays.asList(1, 6, 200));
        System.out.println(path4);

        
		// assertTrue( true );
	}
}
