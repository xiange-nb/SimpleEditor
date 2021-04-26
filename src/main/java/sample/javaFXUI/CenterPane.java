package sample.javaFXUI;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

/**
 * @program: SimpleEditor
 * @description: 中心点
 * @author: wwx
 * @create: 2021-04-13 12:11
 **/
public class CenterPane extends Pane {

    private Line lineX=new Line();
    private Line lineY=new Line();
    private double width = 100;
    private double height = 100;



    public CenterPane() {
        this.setMinSize(width,height);
        this.setMaxSize(width,height);
        this.setStyle("-fx-border-width: 10");
        this.setStyle("-fx-border-color: crimson");
        // 将直线的起点坐标与 node1 的中心坐标进行绑定

    }
    public void Line(){
        // 将直线的起点坐标与 node1 的中心坐标进行绑定
        lineX.startXProperty().bind(this.layoutXProperty().add(this.widthProperty().divide(0)));
        lineX.startYProperty().bind(this.layoutYProperty().add(this.heightProperty().divide(2)));

        // 将直线的终点坐标与 node2 的中心坐标进行绑定
        lineX.endXProperty().bind(this.layoutXProperty().add(this.widthProperty().divide(0)));
        lineX.endYProperty().bind(this.layoutYProperty().add(this.heightProperty().divide(2)));

        // 将直线的起点坐标与 node1 的中心坐标进行绑定
        lineY.startXProperty().bind(this.layoutXProperty().add(this.widthProperty().divide(2)));
        lineY.startYProperty().bind(this.layoutYProperty().add(this.heightProperty().divide(0)));

        // 将直线的终点坐标与 node2 的中心坐标进行绑定
        lineY.endXProperty().bind(this.layoutXProperty().add(this.widthProperty().divide(2)));
        lineY.endYProperty().bind(this.layoutYProperty().add(this.heightProperty().divide(0)));
        this.getChildren().addAll(lineX, lineY);
    }
}
