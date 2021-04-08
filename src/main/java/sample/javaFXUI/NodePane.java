package sample.javaFXUI;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @program: SimpleEditor
 * @description:
 * @author: wwx
 * @create: 2021-04-07 13:35
 **/
public class NodePane extends Pane {


        private transient int code;
        private double X;
        private double Y;
        private static Line line1;
        private static Line line2;


        public static Line TheConnection(Pane node1, Pane node2){
            Line line = new Line();

            // 将直线的起点坐标与 node1 的中心坐标进行绑定
            line.startXProperty().bind(node1.layoutXProperty().add(node1.widthProperty().divide(2)));
            line.startYProperty().bind(node1.layoutYProperty().add(node1.heightProperty().divide(2)));

            // 将直线的终点坐标与 node2 的中心坐标进行绑定
            line.endXProperty().bind(node2.layoutXProperty().add(node2.widthProperty().divide(2)));
            line.endYProperty().bind(node2.layoutYProperty().add(node2.heightProperty().divide(2)));
            return line;
        }

}
