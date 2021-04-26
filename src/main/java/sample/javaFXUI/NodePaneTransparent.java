package sample.javaFXUI;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 * @program: SimpleEditor
 * @description:
 * @author: wwx
 * @create: 2021-04-07 13:35
 **/
public class NodePaneTransparent extends Pane {

        private int code;
        private double RX;
        private double RY;
        private double AX;
        private double AY;
        private int size=20;

    public NodePaneTransparent() {
    }

    public NodePaneTransparent(double aX, double aY) {
        AX=aX;AY=aY;
        this.setLayoutX(AX-(size/2));
        this.setLayoutY(AY-(size/2));
        //this.setStyle("-fx-background-color: crimson");
        //this.setMinSize(size,size);
        Circle circle = new Circle(10);
        circle.setStyle("-fx-fill: rgb(77,223,51)");
        circle.setCenterX(size/2);
        circle.setCenterY(size/2);
        circle.setOpacity(0.5);
        this.getChildren().addAll(circle);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getRX() {
        return RX;
    }

    public void setRX(double RX) {
        this.RX = RX;
    }

    public double getRY() {
        return RY;
    }

    public void setRY(double RY) {
        this.RY = RY;
    }

    public double getAX() {
        return AX;
    }

    public void setAX(double AX) {
        this.AX = AX;
    }

    public double getAY() {
        return AY;
    }

    public void setAY(double AY) {
        this.AY = AY;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
