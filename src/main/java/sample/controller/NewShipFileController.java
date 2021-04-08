package sample.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.util.StringConverter;
import sample.common.PublicConfigure;
import sample.common.WhileButton;
import sample.entity.ship.ShipJson;
import sample.javaFXUI.NodePane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: SimpleEditor
 * @description:
 * @author: wwx
 * @create: 2021-03-24 11:42
 **/
public class NewShipFileController {
    @FXML
    private TextArea textArea;
    @FXML
    private AnchorPane supPane;
    @FXML
    private ProgressIndicator prog;
    @FXML
    private Pane pane;
    @FXML
    private ImageView imageView;
    @FXML
    private ImageView coreImage;
    @FXML
    private Label lable1;
    @FXML
    private Label lable2;
    @FXML
    private Label lable3;
    @FXML
    private Button ButLeft;
    @FXML
    private Button ButCenter;
    @FXML
    private Button ButTop;
    @FXML
    private Button ButRight;
    @FXML
    private Button ButBottom;
    @FXML
    private ComboBox comboBox;

    @FXML
    private TextArea testAreatest;

    ObservableList<comboBoxEnt> options= FXCollections.observableArrayList();
    List<NodePane> weapons=new ArrayList<>();
    public void initialize() {
        comboBox.setConverter(new StringConverter<comboBoxEnt>() {
            @Override
            public String toString(comboBoxEnt object) {
                return object.key;
            }
            @Override
            public comboBoxEnt fromString(String string) {
                return null;
            }
        });
        options.addAll(new comboBoxEnt("碰撞边界","1"));
    }

    public void initData(String shipName) {
        supPane.setId(shipName);
        File shipfile = FileUtil.file(PublicConfigure.getShipFile() + shipName + ".ship");
        FileReader fileReader = new FileReader(shipfile);
        ShipJson shipJson = JSONObject.parseObject(fileReader.readString(), ShipJson.class);
        textArea.setText(JSONObject.toJSONString(shipJson,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.PrettyFormat
        ));
        if (shipJson.getSpriteName() != null) {
            File file = FileUtil.file(PublicConfigure.getShipImagePath() + shipJson.getSpriteName());
            Image image = new Image("file:/" + file.getPath());
            final double w = image.getWidth();
            final double h = image.getHeight();
            imageView.setFitHeight(h);
            imageView.setFitWidth(w);
            imageView.setImage(image);

            pane.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    imageView.setLayoutY((newValue.doubleValue() - imageView.getFitHeight()) / 2);
                    coreImage.setLayoutY((newValue.doubleValue() - coreImage.getFitHeight()) / 2);

                }
            });
            pane.widthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    imageView.setLayoutX((newValue.doubleValue() - imageView.getFitWidth()) / 2);
                    coreImage.setLayoutX((newValue.doubleValue() - coreImage.getFitWidth()) / 2);

                }
            });

            lable2.setText("中心=" + h / 2 + "--" + w / 2);
            lable3.setText("长" + h + "宽" + w);


            coreImage.setImage(new Image("image/core.png"));
            coreImage.setFitHeight(100);
            coreImage.setFitWidth(100);



            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1), pane);
            translateTransition.setToX(0);
            translateTransition.setToY(0);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1), pane);
            // 中心放大至两倍
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            pane.setCacheHint(CacheHint.SPEED);
            scaleTransition.setOnFinished(event -> {
                // 缩放后设置回QUALITY模式 显示清晰
                pane.setCacheHint(CacheHint.QUALITY);
            });

            pane.setOnMouseMoved(event -> {
                double vw = (imageView.getFitWidth() - w) / 2;
                double vh = (imageView.getFitHeight() - h) / 2;
                double layoutX = coreImage.getLayoutX() + (coreImage.getFitWidth() / 2);
                double layoutY = coreImage.getLayoutY() + (coreImage.getFitHeight() / 2);

                lable1.setText(RelativeCoordinates_plus(layoutY , event.getY() , vh) + "--" +
                        RelativeCoordinates_plus(layoutX , event.getX() , vw)+"\n"
                +event.getY()+"--"+event.getX());
            });
            pane.setOnScroll(event -> {
                double toX = scaleTransition.getToX();
                double toY = scaleTransition.getToY();
                    if (event.getDeltaY() > 0) {
                        toX += 0.1;
                        toY += 0.1;
                    } else {
                        toX -= 0.1;
                        toY -= 0.1;
                    }
                    scaleTransition.setToX(toX);
                    scaleTransition.setToY(toY);
                    scaleTransition.play();
            });

            supPane.setOnKeyPressed(eventKEY -> {
                if (eventKEY.getCode()== KeyCode.CONTROL){
                    pane.setOnMouseClicked(event -> {
                        double vw = (imageView.getFitWidth() - w) / 2;
                        double vh = (imageView.getFitHeight() - h) / 2;
                        double layoutX = coreImage.getLayoutX() + (coreImage.getFitWidth() / 2);
                        double layoutY = coreImage.getLayoutY() + (coreImage.getFitHeight() / 2);
                        NodePane nodePane=new NodePane(event.getX(),event.getY());
                        nodePane.setRX(RelativeCoordinates_plus(layoutX , event.getX() , vw));
                        nodePane.setRY(RelativeCoordinates_plus(layoutY , event.getY() , vh));
                        nodePane.setCode(weapons.size());
                        weapons.add(nodePane);
                        pane.getChildren().addAll(nodePane);
                        Connection(nodePane);
                        draggable(nodePane);
                    });
                }
            });

            supPane.setOnKeyReleased(eventKEY -> {
                if (eventKEY.getCode()== KeyCode.CONTROL){
                       pane.setOnMouseClicked(event -> {

                       });
                }
            });


            ButRight.setOnAction(event -> {
                double toX = translateTransition.getToX();
                translateTransition.setToX(toX += 5);
                translateTransition.play();
            });
            ButLeft.setOnAction(event -> {
                double toX = translateTransition.getToX();
                translateTransition.setToX(toX -= 5);
                translateTransition.play();
            });
            ButTop.setOnAction(event -> {
                double toY = translateTransition.getToY();
                translateTransition.setToY(toY -= 5);
                translateTransition.play();
            });
            ButBottom.setOnAction(event -> {
                double toY = translateTransition.getToY();
                translateTransition.setToY(toY += 5);
                translateTransition.play();
            });
            ButCenter.setOnAction(event -> {
                translateTransition.setToX(0);
                translateTransition.setToY(0);
                translateTransition.play();
            });
            new WhileButton(ButRight, ButBottom, ButTop, ButLeft);
            comboBox.setItems(options);

        }

    }
    private void draggable(Node node) {
        final Position pos = new Position();
        // 提示用户该结点可点击
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> node.setCursor(Cursor.HAND));
        node.addEventHandler(MouseEvent.MOUSE_EXITED, event -> node.setCursor(Cursor.DEFAULT));
        // 提示用户该结点可拖拽
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            node.setCursor(Cursor.MOVE);
            pos.x = event.getX();
            pos.y = event.getY();
        });
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> node.setCursor(Cursor.DEFAULT));
        // 实现拖拽功能
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double distanceX = event.getX() - pos.x;
            double distanceY = event.getY() - pos.y;
            double x = node.getLayoutX() + distanceX;
            double y = node.getLayoutY() + distanceY;
            // 计算出 x、y 后将结点重定位到指定坐标点 (x, y)
            node.relocate(x, y);
        });
    }

    private static class Position {
        double x;
        double y;
    }
    public void saveShipFile() {
        ShipJson shipJson = JSONObject.parseObject(textArea.getText(), ShipJson.class);
        File shipfile = FileUtil.file(PublicConfigure.getShipFile() + supPane.getId() + ".ship");
        FileWriter fileWriter = new FileWriter(shipfile);
        fileWriter.write(JSONObject.toJSONString(shipJson, SerializerFeature.PrettyFormat));
        prog.setProgress(1.0);
    }

    public void textAreaCliked() {
        prog.setProgress(0.0);
    }

    public double RelativeCoordinates_plus(double layout,double event,double v){
        return NumberUtil.round(layout - event - v,1).doubleValue();
    }

    public double RelativeCoordinates_minus(double layout,double event,double v ){
        double v1=0;
        if (event>0){
             v1 = layout - event;
        }
        if (event<0){
             v1 = layout +Math.abs(event);
        }
        return NumberUtil.round(v1,1).doubleValue();
    }

    public void Connection(NodePane nodePane){
        pane.getChildren().removeIf(node -> StrUtil.equals(node.getTypeSelector(),"Line"));
        int code = nodePane.getCode();
        if (code>0){
          for (NodePane np:weapons){
              if (np.getCode()<weapons.size()-1){
                  pane.getChildren().addAll(TheConnection(np,weapons.get(np.getCode()+1)));
              }else {
                  pane.getChildren().add(TheConnection(np,weapons.get(0)));
              }
          }
        }
    }

    public Line TheConnection(Pane node1, Pane node2){
        Line line = new Line();

        // 将直线的起点坐标与 node1 的中心坐标进行绑定
        line.startXProperty().bind(node1.layoutXProperty().add(node1.widthProperty().divide(2)));
        line.startYProperty().bind(node1.layoutYProperty().add(node1.heightProperty().divide(2)));

        // 将直线的终点坐标与 node2 的中心坐标进行绑定
        line.endXProperty().bind(node2.layoutXProperty().add(node2.widthProperty().divide(2)));
        line.endYProperty().bind(node2.layoutYProperty().add(node2.heightProperty().divide(2)));
        line.setStroke(Paint.valueOf("#f8ffe1"));
        return line;
    }


    class comboBoxEnt{
           private String key;
           private String val;

        public comboBoxEnt() {
        }
        public comboBoxEnt(String key, String val) {
            this.key = key;
            this.val = val;
        }
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public String getVal() {
            return val;
        }
        public void setVal(String val) {
            this.val = val;
        }
    }
}