import java.util.Objects;

public class Point {
    private int row, col;

    public Point(int x, int y) {
        col = x;
        row = y;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * Return a new point north of the current location
     * @return
     */
    public Point north() {
        return new Point(col, row-1);
    }
    public Point east() {return new Point(col+1, row);}
    public Point south() {
        return new Point(col, row+1);
    }
    public Point west() {
        return new Point(col-1, row);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return row == point.row && col == point.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
