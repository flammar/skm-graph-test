package name.skm.graph_test.graph;

import static org.junit.Assert.assertTrue;

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
		Graph<Integer> graph = new Graph<Integer>();
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
		System.out.println(graph.getPath(1, 100));
		System.out.println(graph.getPath(1, 200));

		// assertTrue( true );
	}
}
