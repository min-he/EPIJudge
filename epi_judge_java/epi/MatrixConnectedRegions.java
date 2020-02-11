package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayDeque;

class Coordinate {
  int x;
  int y;

  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }
}

public class MatrixConnectedRegions {
  public static void flipColor(int x, int y, List<List<Boolean>> image) {
    // check if x, y is within image
    if (!(0 <= x && x < image.size()
          && 0 <= y && y < image.get(0).size()))
      return;

    // BFS
    Queue<MyCoordinate> queue = new ArrayDeque<MyCoordinate>();
    queue.add(new MyCoordinate(x, y));
    final boolean color = image.get(x).get(y);

    while (!queue.isEmpty()) {
      MyCoordinate curr = queue.remove();
      image.get(x).set(y, !color);

      for (MyCoordinate nextMove : List.of(
        new MyCoordinate(curr.x - 1, y),
        new MyCoordinate(curr.x + 1, y),
        new MyCoordinate(curr.x, y - 1),
        new MyCoordinate(curr.x, y + 1)
      )) {
        if (0 <= nextMove.x && nextMove.x < image.size()
          && 0 <= nextMove.y && nextMove.y < image.get(0).size()
            && image.get(nextMove.x).get(nextMove.y) == color) {
          queue.add(nextMove);
          }
      }
    }
    return;
  }
  @EpiTest(testDataFile = "painting.tsv")
  public static List<List<Integer>> flipColorWrapper(TimedExecutor executor,
                                                     int x, int y,
                                                     List<List<Integer>> image)
      throws Exception {
    List<List<Boolean>> B = new ArrayList<>();
    for (int i = 0; i < image.size(); i++) {
      B.add(new ArrayList<>());
      for (int j = 0; j < image.get(i).size(); j++) {
        B.get(i).add(image.get(i).get(j) == 1);
      }
    }

    executor.run(() -> flipColor(x, y, B));

    image = new ArrayList<>();
    for (int i = 0; i < B.size(); i++) {
      image.add(new ArrayList<>());
      for (int j = 0; j < B.get(i).size(); j++) {
        image.get(i).add(B.get(i).get(j) ? 1 : 0);
      }
    }

    return image;
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "MatrixConnectedRegions.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
