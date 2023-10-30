package engine.util;

/**
 * Record representing a point in a 2D space.
 * 
 * @version 1.0
 */
public record Point(int x, int y) {

  /**
   * Method used to get the next point from the current point in the given
   * direction.
   *
   * @param direction The direction
   * @return The next point in the given direction.
   */
  public Point withAdded(Direction direction) {
    return new Point(x + direction.xOffset(), y + direction.yOffset());
  }
}
