package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.List;
import java.util.*;

 class MyCoordinate {
  Integer x;
  Integer y;

  public MyCoordinate(Integer x, Integer y) {
    this.x = x;
    this.y = y;
  }
}

public class MatrixEnclosedRegions {

  public static void fillSurroundedRegions(List<List<Character>> board) {
    // BFS
    Queue<Coordinate> queue = new ArrayDeque<Coordinate>();

    // find WHITE from first row and last row
    for (int x = 0; x < board.size(); ++x) {
      if (board.get(x).get(0) == 'W')
        queue.add(new Coordinate(x, 0));
      if (board.get(x).get(board.get(0).size() - 1) == 'W')
        queue.add(new Coordinate(x, board.get(0).size() - 1));
    }
    for (int y = 0; y < board.get(0).size(); ++y) {
      if (board.get(0).get(y) == 'W')
        queue.add(new Coordinate(0, y));
      if (board.get(board.size() - 1).get(y) == 'W')
        queue.add(new Coordinate(board.size() - 1, y));
    }
    
    while (!queue.isEmpty()) {
      Coordinate curr = queue.remove();
      for (Coordinate nextMove : List.of(new Coordinate(curr.x - 1, curr.y), new Coordinate(curr.x + 1, curr.y),
          new Coordinate(curr.x, curr.y - 1), new Coordinate(curr.x, curr.y + 1))) {
        if (0 <= nextMove.x && nextMove.x < board.size() && 0 <= nextMove.y && nextMove.y < board.get(0).size()
            && board.get(nextMove.x).get(nextMove.y) == 'W')
          queue.add(nextMove);
      }
      board.get(curr.x).set(curr.y, 'T');

    }
    
    for (int row = 0; row < board.size(); ++row) {
      for (int col = 0; col < board.get(0).size(); ++col) {
        board.get(row).set(col, (board.get(row).get(col) == 'T') ? 'W' : 'B');
      }
    }
    return;
  }


  @EpiTest(testDataFile = "matrix_enclosed_regions.tsv")
  public static List<List<Character>>
  fillSurroundedRegionsWrapper(List<List<Character>> board) {
    fillSurroundedRegions(board);
    return board;
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "MatrixEnclosedRegions.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
