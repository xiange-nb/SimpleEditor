package sample.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.StringConverter;
import sample.common.PublicConfigure;
import sample.common.WhileButton;
import sample.entity.ship.ShipJson;
import sample.javaFXUI.NodePane;

import java.io.File;

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

                double v = (imageView.getFitWidth() - w) / 2;
                double v1 = (imageView.getFitHeight() - h) / 2;

                double layoutX = coreImage.getLayoutX() + (coreImage.getFitWidth() / 2);

                double layoutY = coreImage.getLayoutY() + (coreImage.getFitHeight() / 2);
                lable1.setText(NumberUtil.roundStr(layoutY - event.getY() - v1, 1) + "--" +
                        NumberUtil.roundStr(layoutX - event.getX() - v, 1));
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
            NodePane nodePane=new NodePane();
            pane.setOnMouseClicked(event -> {

//                Pane pane1=new Pane();
//                pane1.setLayoutX(event.getX());
//                pane1.setLayoutY(event.getY());
//                pane1.setMaxWidth(200);
//                pane1.setMaxHeight(200);
//                pane1.setMinSize(200,200);
//                pane1.setStyle("-fx-background-color: crimson");
//                pane.getChildren().addAll(nodePane.NewNodePane(event.getX(),event.getY()),
//                        nodePane.getLine1(),nodePane.getLine2());
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