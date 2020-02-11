package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class SearchMaze {
  @EpiUserType(ctorParams = {int.class, int.class})

  public static class Coordinate {
    public int x, y;

    public Coordinate(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Coordinate that = (Coordinate)o;
      if (x != that.x || y != that.y) {
        return false;
      }
      return true;
    }
  }

  public enum Color { WHITE, BLACK }

  public static List<Coordinate> searchMaze(List<List<Color>> maze,
      Coordinate s, Coordinate e) {
    // check if both s, e are within maze
    if (!isFeasibleMove(maze, s) || !isFeasibleMove(maze, e)) return Collections.emptyList();

    // DFS from s -> e
    List<Coordinate> path = new ArrayList<Coordinate>();
    helperDFS(maze, s, e, path);

    return path;
  }
  
  static boolean helperDFS(List<List<Color>> maze, Coordinate curr, Coordinate e, List<Coordinate> path) {
    if (!isFeasibleMove(maze, curr))
      return false;

    path.add(curr);
    // set current node as visited
    maze.get(curr.x).set(curr.y, Color.BLACK);

    if (curr.equals(e))
      return true;
    
    // recursion on neighbors
    for (Coordinate nextMove : List.of(
        new Coordinate(curr.x - 1, curr.y),
        new Coordinate(curr.x + 1, curr.y),
        new Coordinate(curr.x, curr.y - 1),
        new Coordinate(curr.x, curr.y + 1))) {

      if (helperDFS(maze, nextMove, e, path))
        return true;
    }
    path.remove(curr);
    return false;
  }

  static boolean isFeasibleMove(List<List<Color>> maze, Coordinate curr) {
    if (!(0 <= curr.x && curr.x < maze.size() 
        && 0 <= curr.y && curr.y < maze.get(0).size() 
        && maze.get(curr.x).get(curr.y) == Color.WHITE))
      return false;
    else
      return true;
  }

  public static boolean pathElementIsFeasible(List<List<Integer>> maze,
      Coordinate prev, Coordinate cur) { if (!(0 <= cur.x && cur.x < maze.size() && 0 <= cur.y &&
          cur.y < maze.get(cur.x).size() && maze.get(cur.x).get(cur.y) == 0)) {
      return false;
    }
    return cur.x == prev.x + 1 && cur.y == prev.y ||
        cur.x == prev.x - 1 && cur.y == prev.y ||
        cur.x == prev.x && cur.y == prev.y + 1 ||
        cur.x == prev.x && cur.y == prev.y - 1;
  }

  @EpiTest(testDataFile = "search_maze.tsv")
  public static boolean searchMazeWrapper(List<List<Integer>> maze,
                                          Coordinate s, Coordinate e)
      throws TestFailure {
    List<List<Color>> colored = new ArrayList<>();
    for (List<Integer> col : maze) {
      List<Color> tmp = new ArrayList<>();
      for (Integer i : col) {
        tmp.add(i == 0 ? Color.WHITE : Color.BLACK);
      }
      colored.add(tmp);
    }
    List<Coordinate> path = searchMaze(colored, s, e);
    if (path.isEmpty()) {
      return s.equals(e);
    }

    if (!path.get(0).equals(s) || !path.get(path.size() - 1).equals(e)) {
      throw new TestFailure("Path doesn't lay between start and end points");
    }

    for (int i = 1; i < path.size(); i++) {
      if (!pathElementIsFeasible(maze, path.get(i - 1), path.get(i))) {
        throw new TestFailure("Path contains invalid segments");
      }
    }

    return true;
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "SearchMaze.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
