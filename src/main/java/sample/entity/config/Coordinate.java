package sample.entity.config;

/**
 * @program: SimpleEditor
 * @description:
 * @author: wwx
 * @create: 2021-03-26 13:49
 **/
public class Coordinate {
    private Double X;
    private Double Y;

    public static Coordinate getRelativeXY(Double X,Double Y){
        return new Coordinate(X,Y);
    }

    public Coordinate() {
    }

    public Coordinate(Double x, Double y) {
        X = x;
        Y = y;
    }

    public Double getX() {
        return X;
    }

    public void setX(Double x) {
        X = x;
    }

    public Double getY() {
        return Y;
    }

    public void setY(Double y) {
        Y = y;
    }
}
