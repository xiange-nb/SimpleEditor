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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
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
import sample.javaFXUI.NodePaneTransparent;

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
    private CheckBox checkBoxImage;

    @FXML
    private TextArea testAreatest;

    private static double mouseX=0;
    private static double mouseY=0;

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


            pane.heightProperty().addListener((observable, oldValue, newValue) -> {
                imageView.setLayoutY((newValue.doubleValue() - imageView.getFitHeight()) / 2);
                coreImage.setLayoutY((newValue.doubleValue() - coreImage.getFitHeight()) / 2);
            });
            pane.widthProperty().addListener((observable, oldValue, newValue) -> {
                imageView.setLayoutX((newValue.doubleValue() - imageView.getFitWidth()) / 2);
                coreImage.setLayoutX(((newValue.doubleValue() - coreImage.getFitWidth()) / 2));
            });

            lable2.setText("中心=" + h / 2 + "--" + w / 2);
            lable3.setText("长" + h + "宽" + w);

            coreImage.setImage(new Image("image/core.png"));
            coreImage.setFitHeight(50);
            coreImage.setFitWidth(50);

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
                MouseMoved(event);
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
                double layoutX = coreImage.getLayoutX() + (coreImage.getFitWidth() / 2);
                double layoutY = coreImage.getLayoutY() + (coreImage.getFitHeight() / 2);
                if (eventKEY.getCode()== KeyCode.CONTROL){
                    NodePaneTransparent node=new NodePaneTransparent(mouseX,mouseY);
                    node.setId("NodePane1");
                    node.setRX(RelativeCoordinates_plus(layoutX,mouseX));
                    node.setRY(RelativeCoordinates_plus(layoutY,mouseY));
                    NodePaneTransparent node2=new NodePaneTransparent(RelativeCoordinates_minus(layoutX,node.getRX()>0? 0-node.getRX():Math.abs(node.getRX())),mouseY);
                    node2.setId("NodePane2");
                    if (checkBoxImage.isSelected()){
                        pane.getChildren().addAll(node,node2);
                        TransparentLine(node,node2);
                    }else{
                        pane.getChildren().addAll(node);
                        TransparentLine(node);
                    }

                    pane.setOnMouseMoved(event -> {
                        MouseMoved(event);
                        node.relocate(mouseX-10,mouseY-10);
                        node.setRX(RelativeCoordinates_plus(layoutX,mouseX));
                        if (checkBoxImage.isSelected()){
                            double v = mouseX - 10;
                            node2.relocate(RelativeCoordinates_minus(layoutX,node.getRX()>0? 0-node.getRX():Math.abs(node.getRX()))-10,mouseY-10);
                        }

                    });


                    pane.setOnMouseClicked(event -> {
                        NodePane nodePane=new NodePane(event.getX(),event.getY());
                        nodePane.setRX(RelativeCoordinates_plus(layoutX , event.getX()));
                        nodePane.setRY(RelativeCoordinates_plus(layoutY , event.getY()));
                        nodePane.setCode(weapons.size());
                        weapons.add(nodePane);
                        pane.getChildren().addAll(nodePane);
                        Connection(nodePane);
                        draggable(nodePane);
                        if (checkBoxImage.isSelected()){
                            TransparentLine(node,node2);
                        }else{
                            TransparentLine(node);
                        }
                    });
                }
            });

            supPane.setOnKeyReleased(eventKEY -> {
                if (eventKEY.getCode()== KeyCode.CONTROL){
                    pane.setOnMouseClicked(event -> {

                    });
                    pane.setOnMouseMoved(event -> {
                        MouseMoved(event);
                    });
                    pane.getChildren().removeIf(n ->{
                        if (StrUtil.equals("NodePaneTransparent",n.getTypeSelector())){
                            if (StrUtil.indexOfIgnoreCase(n.getId(),"NodePane")>=0)return true;
                        }else
                        if (StrUtil.indexOfIgnoreCase(n.getId(),"LineA")>=0)return true;
                        return false;
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

    public void TransparentLine(NodePaneTransparent node){
        pane.getChildren().removeIf(n -> StrUtil.indexOfIgnoreCase(n.getId(),"LineA")>=0);
        if (weapons.size()!=0){
            Line line1 = TheConnection(node, weapons.get(0));
            line1.setId("LineA1");
            line1.setOpacity(0.3);
            line1.setStroke(Paint.valueOf("#09f60e"));
            line1.setStrokeWidth(3);
            Line line2 = TheConnection(node, weapons.get(weapons.size() - 1));
            line2.setId("LineA2");
            line2.setOpacity(0.3);
            line2.setStroke(Paint.valueOf("#09f60e"));
            line2.setStrokeWidth(3);
            pane.getChildren().addAll(line1, line2);
        }
    }

    public void TransparentLine(NodePaneTransparent node1,NodePaneTransparent node2){
        pane.getChildren().removeIf(n -> StrUtil.indexOfIgnoreCase(n.getId(),"LineA")>=0);
        if (weapons.size()!=0){
            Line line1 = TheConnection(node1, weapons.get(0));
            line1.setId("LineA1");
            line1.setOpacity(0.3);
            line1.setStroke(Paint.valueOf("#09f60e"));
            line1.setStrokeWidth(3);
            Line line2 = TheConnection(node1, node2);
            line2.setId("LineA1");
            line2.setOpacity(0.3);
            line2.setStroke(Paint.valueOf("#09f60e"));
            line2.setStrokeWidth(3);
            Line line3 = TheConnection(node2, weapons.get(weapons.size() - 1));
            line3.setId("LineA3");
            line3.setOpacity(0.3);
            line3.setStroke(Paint.valueOf("#09f60e"));
            line3.setStrokeWidth(3);
            pane.getChildren().addAll(line1, line2,line3);
        }
    }

    public void MouseMoved(MouseEvent event){
        mouseX=event.getX();
        mouseY=event.getY();
        double layoutX = coreImage.getLayoutX() + (coreImage.getFitWidth() / 2);
        double layoutY = coreImage.getLayoutY() + (coreImage.getFitHeight() / 2);
        lable1.setText(RelativeCoordinates_plus(layoutY, event.getY()) + "==" +
                RelativeCoordinates_plus(layoutX, event.getX()));
    }

    private void draggable(NodePane node) {
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
        node.addEventHandler(MouseEvent.MOUSE_RELEASED,event -> {
            double layoutX = coreImage.getLayoutX() + (coreImage.getFitWidth() / 2);
            double layoutY = coreImage.getLayoutY() + (coreImage.getFitHeight() / 2);
            double distanceX = event.getX() - pos.x;
            double distanceY = event.getY() - pos.y;
            double x = node.getLayoutX() + distanceX;
            double y = node.getLayoutY() + distanceY;
            node.setRX(RelativeCoordinates_plus(layoutX,x+10));
            node.setRY(RelativeCoordinates_plus(layoutY,y+10));
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

    public double RelativeCoordinates_plus(double layout,double event){
        return NumberUtil.round(layout - event ,1).doubleValue();
    }

    public double RelativeCoordinates_minus(double layout,double event ){
        double v1=0;
        if (event>0){
             v1 = layout - event;
        }
        if (event<0){
             v1 = layout +Math.abs(event);
        }
        return NumberUtil.round(v1,1).doubleValue();
    }





    public void Connection(NodePane nodePane) {
        pane.getChildren().removeIf(node -> StrUtil.equals(node.getTypeSelector(), "Line")
                && StrUtil.indexOfIgnoreCase(node.getId(), "LineA") < 0);
        int code = nodePane.getCode();
        if (code > 0) {
            for (NodePane np : weapons) {
                if (np.getCode() < weapons.size() - 1) {
                    pane.getChildren().addAll(TheConnection(np, weapons.get(np.getCode() + 1)));
                } else {
                    pane.getChildren().add(TheConnection(np, weapons.get(0)));
                }
            }
        }
    }


    public Line TheConnection(Pane node1, Pane node2){
        Line line = new Line();
        line.setStrokeWidth(3);
        line.setOpacity(0.5);
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