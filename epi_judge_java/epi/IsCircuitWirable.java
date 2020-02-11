package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class IsCircuitWirable {

  /**
   * d: distance that indicate the vertex state
   * -1: unvisited
   * 0: first vertex of the graph visited
   * 1: next layer
   * 2: next.next layer ...
   * if a vertex has edge to visited layer, and both vertices have
   * odd (or even) distance to the first vertex, we say placement
   * is imposable.
   */
  public static class GraphVertex {
    public int d = -1;
    public List<GraphVertex> edges = new ArrayList<>();
  }

  public static boolean isAnyPlacementFeasible(List<GraphVertex> graph) {
    // if graph is null or empty, we call it placeable.
    if (graph == null || graph.size() == 0)
      return true;
    Queue<GraphVertex> queue = new ArrayDeque<GraphVertex>();
    
    // BFS to process the graph layer by layer.
    for (GraphVertex vertex : graph) {
      vertex.d = (vertex.d == -1) ? 0 : vertex.d;
      queue.add(vertex);
      while (!queue.isEmpty()) {
        GraphVertex curr = queue.remove();
        for (GraphVertex v : curr.edges) {
          if (v.d == -1) {
            v.d = curr.d + 1;
            queue.add(v);
          } else if (curr.d % 2 == v.d % 2) {
            return false;
          }
        }
      }
    }

    return true;
  }
  @EpiUserType(ctorParams = {int.class, int.class})
  public static class Edge {
    public int from;
    public int to;

    public Edge(int from, int to) {
      this.from = from;
      this.to = to;
    }
  }

  @EpiTest(testDataFile = "is_circuit_wirable.tsv")
  public static boolean isAnyPlacementFeasibleWrapper(TimedExecutor executor,
                                                      int k, List<Edge> edges)
      throws Exception {
    if (k <= 0)
      throw new RuntimeException("Invalid k value");
    List<GraphVertex> graph = new ArrayList<>();
    for (int i = 0; i < k; i++)
      graph.add(new GraphVertex());
    for (Edge e : edges) {
      if (e.from < 0 || e.from >= k || e.to < 0 || e.to >= k)
        throw new RuntimeException("Invalid vertex index");
      graph.get(e.from).edges.add(graph.get(e.to));
    }

    return executor.run(() -> isAnyPlacementFeasible(graph));
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "IsCircuitWirable.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
