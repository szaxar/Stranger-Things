package ST3.presenter.map;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object object) {
        return (object instanceof Point) && x == ((Point) object).x && y == ((Point) object).y;
    }
}
