package engine.util;

/**
 * Enum representing the directions in a 2D space.
 * 
 * @version 1.0
 */
public enum Direction {
  NORTH(0, 1),
  EAST(1, 0),
  SOUTH(0, -1),
  WEST(-1, 0),
  NORTH_EAST(1, 1),
  SOUTH_EAST(1, -1),
  SOUTH_WEST(-1, -1),
  NORTH_WEST(-1, 1),
  L_EAST_NORTH(2, 1),
  L_NORTH_EAST(1, 2),
  L_EAST_SOUTH(2, -1),
  L_SOUTH_EAST(1, -2),
  L_WEST_SOUTH(-2, -1),
  L_SOUTH_WEST(-1, -2),
  L_WEST_NORTH(-2, 1),
  L_NORTH_WEST(-1, 2);

  private final int xOff;
  private final int yOff;

  Direction(int xOff, int yOff) {
    this.xOff = xOff;
    this.yOff = yOff;
  }

  /**
   * Method used to get the offset of two coordinates represented by a Direction.
   *
   * @param x The x coordinate.
   * @param y The y coordinate.
   * @return The Direction representing the offset of the two coordinates.
   *         If the offset is not valid, it returns null.
   */
  public static Direction offSetOf(int x, int y) {
    int length = Math.abs(ChessMath.gcd(x, y));
    if (length != 0) {
      x /= length;
      y /= length;
    }

    for (Direction direction : values()) {
      if (direction.xOff == x && direction.yOff == y) {
        return direction;
      }
    }

    return null;
  }

  /**
   * Method used to get the offset between two points represented by a Direction
   *
   * @param from The starting position.
   * @param to   The ending position.
   * @return The Direction representing the offset between the two points. If the
   *         offset is
   *         not valid, it returns null.
   */
  public static Direction offSetOf(Point from, Point to) {
    return offSetOf(to.x() - from.x(), to.y() - from.y());
  }

  /**
   * Method used to get the length of the offset of two coordinates. If the
   * direction
   * is not valid, it returns 0.
   *
   * @param from The starting position.
   * @param to   The ending position.
   * @return The offset length of the two coordinates if the direction is valid, 0
   *         otherwise.
   */
  public static int moveDistance(Point from, Point to) {
    if (Direction.offSetOf(from, to) == null) {
      return 0;
    }

    int xDiff = to.x() - from.x();
    int yDiff = to.y() - from.y();
    return Math.abs(ChessMath.gcd(xDiff, yDiff));
  }

  /**
   * Checks if the given direction is valid.
   *
   * @param dir The direction to check.
   * @return True if the direction is valid, false otherwise.
   */
  public static boolean exists(Direction dir) {
    return null != dir;
  }

  /**
   * Method used to check if a direction is valid is a diagonal direction.
   *
   * @return True if the direction is diagonal, false otherwise.
   */
  public boolean isDiagonal() {
    return Math.abs(xOff) == Math.abs(yOff);
  }

  /**
   * Method used to get the x offset of the current direction.
   *
   * @return The x offset of the direction.
   */
  public int xOffset() {
    return xOff;
  }

  /**
   * Method used to get the y offset of the current direction.
   *
   * @return The y offset of the direction.
   */
  public int yOffset() {
    return yOff;
  }

  /**
   * Method used to get the opposite direction of the current direction.
   *
   * @return The opposite direction of the current direction.
   */
  public Direction opposite() {
    return offSetOf(-xOff, -yOff);
  }
}
