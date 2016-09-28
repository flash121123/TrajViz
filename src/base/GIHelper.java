package base;

/**
 * I use this for temporal fixtures.
 * 
 * @author psenin
 * 
 */
public class GIHelper {

  /**
   * Constructor.
   */
  public GIHelper() {
    assert true;
  }

  /**
   * Computes the mean value.
   * 
   * @param values array of values.
   * @return the mean value.
   */
  public static double mean(int[] values) {
    double sum = 0.0;
    for (int i : values) {
      sum = sum + (double) i;
    }
    return sum / (double) values.length;

  }
}