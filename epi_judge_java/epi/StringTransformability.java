package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.*;
public class StringTransformability {
  @EpiTest(testDataFile = "string_transformability.tsv")

  /**
   * Given a dictionary D and two strings s and t, determine if s produces
   * t. If it does, output the length of a shortest production sequence;
   * otherwise output -1.
   */
  public static int transformString(Set<String> D, String s, String t) {
    // BFS as this is to find shortest path.
    if (s == null || t == null || s.length() != t.length())
      return -1;
    else if (s.equals(t))
      return 0;

    int stepCount = 0;
    Queue<String> queue = new ArrayDeque<String>();
    queue.add(s);
    D.remove(s);

    while (!queue.isEmpty()) {
      int levelCount = queue.size();
      ++stepCount;
      for (int i = 0; i < levelCount; ++i) {
        String curr = queue.remove();
        
        for (int j = 0; j < curr.length(); ++j) {
          char tempC = curr.charAt(j);
          StringBuilder sb = new StringBuilder(curr);
          for (char c = 'a'; c <= 'z'; ++c) {
            if (c == tempC)
              continue;
            
            sb.setCharAt(j, c);
            String tempS = sb.toString();

            if (tempS.equals(t))  // hit target and return step count
              return stepCount;

            if (D.contains(tempS)) {
              queue.add(tempS);
              D.remove(tempS);
            }
          }
        }
      }
    }
    return -1;
  }


  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "StringTransformability.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
