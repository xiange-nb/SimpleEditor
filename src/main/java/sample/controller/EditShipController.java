package sample.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.csvreader.CsvReader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.common.PublicConfigure;
import sample.entity.ship.ShipJson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


/**
 * @program: SimpleEditor
 * @description:
 * @author: wwx
 * @create: 2021-03-15 15:18
 **/
public class EditShipController {

    private static final Logger log = LoggerFactory.getLogger(EditShipController.class);
    ObservableList<ship_info> data = FXCollections.observableArrayList();
    @FXML
    private AnchorPane superPane;
    @FXML
    private TableColumn name;
    @FXML
    private TableColumn designation;
    @FXML
    private TableView<ship_info> tabview;
    @FXML
    private TextField textField;
    @FXML
    private TableColumn id;
    @FXML
    private ImageView shipJsonImage;
    @FXML
    private FlowPane flowPane;
    @FXML
    private ImageView shipImage;
    @FXML
    private ImageView csvFileImage;
    @FXML
    private Label csvFileLabel;
    @FXML
    private Label shipFileLabel;
    @FXML
    private Label shipLabel;
    @FXML
    private AnchorPane pane1;
    @FXML
    private AnchorPane pane2;
    @FXML
    private AnchorPane pane3;

    public void initialize() {

        name.setCellValueFactory(
                new PropertyValueFactory("name"));
        designation.setCellValueFactory(
                new PropertyValueFactory("designation"));
        id.setCellValueFactory(
                new PropertyValueFactory("id"));
        tabview.setItems(data);
        if (!StrUtil.isBlank(PublicConfigure.MAINCONFIG))
            ShipList();
        tabviewClicked();
        filterDistance();

    }

    public void ShipList() {
        String filePath = PublicConfigure.getShipPath();
        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(filePath, ',', StandardCharsets.UTF_8);
            data.clear();
            // 读表头
            csvReader.readHeaders();
            while (csvReader.readRecord()) {
                if (!csvReader.get("name").equals("") | !csvReader.get("designation").equals("") | !csvReader.get("id").equals(""))
                    data.add(new ship_info(csvReader.get("name"), csvReader.get("designation"), csvReader.get("id")));
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "未找到ship_data.csv文件，无法加载此mod船文件", ButtonType.YES);
            alert.showAndWait();
        }
    }


    public void filterDistance() {
        FilteredList<ship_info> filteredData = new FilteredList<>(data, p -> true);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ship_info -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(ship_info.getName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(ship_info.getName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            SortedList<ship_info> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tabview.comparatorProperty());
            tabview.setItems(sortedData);
        });
    }

    public void tabviewClicked() {
        tabview.setOnMouseClicked(event -> {
            MouseButton button = event.getButton();
            if (StrUtil.equals(button.name(), "PRIMARY")) {//鼠标左键
                ship_info selectedItem = tabview.getSelectionModel().getSelectedItem();
                if (selectedItem==null)return;
                if (StrUtil.isBlank(selectedItem.getId())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "所选项id为空", ButtonType.YES);
                    alert.showAndWait();
                    return;
                }
                File CSVFile = FileUtil.file("image\\CSV.jpeg");
                Image CSVimage = new Image("file:/" + CSVFile.getPath());
                csvFileImage.setImage(CSVimage);
                csvFileLabel.setText("ship_data.csv");

                File shipfile = FileUtil.file(PublicConfigure.getShipFile() + selectedItem.getId() + ".ship");
                File CannotFindFile = FileUtil.file("image\\CannotFindFile.jpeg");
                Image Cannotimage = new Image("file:/" + CannotFindFile.getPath());
                if (shipfile.exists()) {
                    FileReader fileReader = new FileReader(shipfile);
                    ShipJson shipJson = JSONObject.parseObject(fileReader.readString(), ShipJson.class);
                    File file = FileUtil.file("image\\jsonIcon.jpg");
                    Image image2 = new Image("file:/" + file.getPath());
                    shipJsonImage.setImage(image2);
                    shipFileLabel.setText(shipfile.getName());

                    File imagef = FileUtil.file(PublicConfigure.getShipImagePath() + shipJson.getSpriteName());
                    if (imagef.exists() && !StrUtil.isBlank(shipJson.getSpriteName())) {
                        Image image = new Image("file:/" + imagef.getPath());
                        shipImage.setImage(image);
                        shipLabel.setText(imagef.getName());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "未找到图片（" + imagef.getName() + "），检查图片名称路径是否正确", ButtonType.YES);
                        alert.showAndWait();
                        shipImage.setImage(Cannotimage);
                        shipLabel.setText("找不到文件");
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "未找到（" + selectedItem.getId() + ".ship）文件，检查文件名称是否正确", ButtonType.YES);
                    alert.showAndWait();
                    shipJsonImage.setImage(Cannotimage);
                    shipImage.setImage(Cannotimage);
                    shipFileLabel.setText("找不到文件");
                    shipLabel.setText("找不到文件");
                }
            }
        });
    }

    public void Menu1() {
        Stage newShip = new Stage();
        Parent root = null;
        Stage SupStage = (Stage) flowPane.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/NewShip.fxml"));
            newShip.setTitle("新建船");
            Image image = new Image("/image/s_icon32.png");
            newShip.getIcons().add(image);
            newShip.setScene(new Scene(root));
            newShip.initOwner(SupStage);
            newShip.initModality(Modality.WINDOW_MODAL);
            newShip.setOnCloseRequest(event -> ShipList());
            newShip.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editShipFile() {
        ship_info selectedItem = tabview.getSelectionModel().getSelectedItem();
        try {
            if (selectedItem != null) {
                File shipfile = FileUtil.file(PublicConfigure.getShipFile() + selectedItem.getId() + ".ship");
                if (shipfile.exists()){
                    Stage supstage = (Stage) superPane.getScene().getWindow();
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NewShipFile.fxml"));
                    Parent root = fxmlLoader.load();
                    NewShipFileController controller = fxmlLoader.getController();
                    controller.initData(selectedItem.getId());
                    stage.setTitle("编辑ship文件");
                    Image image = new Image("/image/s_icon32.png");
                    stage.getIcons().add(image);
                    stage.initOwner(supstage);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.setScene(new Scene(root));
                    stage.show();
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "船体配置（ship）文件不存在，新建或上传后再进行此操作", ButtonType.YES);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "先在左侧选中要修改的船", ButtonType.YES);
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newShipFile() {
        ship_info selectedItem = tabview.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "新建 " + selectedItem.getId() + ".ship 文件会覆盖原文件", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.YES) {
                File file = FileUtil.file(PublicConfigure.getShipFile() + selectedItem.getId() + ".ship");
                FileUtil.touch(file);
                FileWriter fileWriter = new FileWriter(file);
                String s = JSONObject.toJSONString(new ShipJson(),
                        SerializerFeature.WriteMapNullValue,
                        SerializerFeature.WriteNullListAsEmpty,
                        SerializerFeature.WriteNullStringAsEmpty,
                        SerializerFeature.PrettyFormat
                );
                fileWriter.write(s);
                File jsonfile = FileUtil.file("image\\jsonIcon.jpg");
                Image image2 = new Image("file:/" + jsonfile.getPath());
                shipJsonImage.setImage(image2);
                shipFileLabel.setText(file.getName());
                File CannotFindFile = FileUtil.file("image\\CannotFindFile.jpeg");
                Image Cannotimage = new Image("file:/" + CannotFindFile.getPath());
                shipImage.setImage(Cannotimage);
                shipLabel.setText("找不到文件");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "先在左侧选中要修改的船", ButtonType.YES);
            alert.showAndWait();
        }
    }

    public void uploadShipFile(){
        ship_info selectedItem = tabview.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "上传会覆盖已存在的文件", ButtonType.YES, ButtonType.CLOSE);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.YES) {
                Stage stage = new Stage();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("选择ship文件");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("ship", "*.ship*")
                );
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    File shipfile = FileUtil.file(PublicConfigure.getShipFile() + selectedItem.getId() + ".ship");
                    FileUtil.copy(file, shipfile,true);

                    FileReader fileReader = new FileReader(shipfile);
                    ShipJson shipJson = JSONObject.parseObject(fileReader.readString(), ShipJson.class);
                    File file1 = FileUtil.file(PublicConfigure.getShipImagePath() + shipJson.getSpriteName());
                    if (file1.exists()){
                        shipImage.setImage(new Image("file:/"+file1.getPath()));
                        shipLabel.setText(file1.getName());

                        File jsonfile = FileUtil.file("image\\jsonIcon.jpg");
                        Image image2 = new Image("file:/" + jsonfile.getPath());
                        shipJsonImage.setImage(image2);
                        shipFileLabel.setText(shipfile.getName());
                    }else {
                        File CannotFindFile = FileUtil.file("image\\CannotFindFile.jpeg");
                        Image Cannotimage = new Image("file:/" + CannotFindFile.getPath());
                        shipImage.setImage(Cannotimage);
                        shipLabel.setText("找不到文件");
                    }

                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "先在左侧选中要修改的船", ButtonType.YES);
            alert.showAndWait();
        }
    }


    public void uploadImage() {
        ship_info selectedItem = tabview.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            File shipfile = FileUtil.file(PublicConfigure.getShipFile() + selectedItem.getId() + ".ship");
            if (shipfile.exists()) {
                FileReader fileReader = new FileReader(shipfile);
                ShipJson shipJson = JSONObject.parseObject(fileReader.readString(), ShipJson.class);
                File file1 = FileUtil.file(PublicConfigure.getShipImagePath() + shipJson.getSpriteName());
                if (file1.exists()) {
                    Alert alert1 = new Alert(Alert.AlertType.WARNING, "上传新图片会覆盖原图片", ButtonType.YES, ButtonType.CLOSE);
                    Optional<ButtonType> buttonType = alert1.showAndWait();
                    if (buttonType.get() == ButtonType.YES) {
                        if (!StrUtil.isBlank(shipJson.getSpriteName())) {
                            Stage stage = new Stage();
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("选择贴图");
                            fileChooser.getExtensionFilters().addAll(
                                    new FileChooser.ExtensionFilter("PNG", "*.png*")
                            );
                            File file = fileChooser.showOpenDialog(stage);
                            if (file != null) {
                                String suffix = FileUtil.getSuffix(file1);
                                if (StrUtil.equalsIgnoreCase(suffix, "png")) {
                                    FileUtil.copy(file, file1, true);
                                    shipImage.setImage(new Image("file:/" + file1.getPath()));
                                    shipLabel.setText(file1.getName());
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR, "图片路径是目录并非文件，请添加具体文件名", ButtonType.YES);
                                    alert.showAndWait();
                                }
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "未在船体配置文件中找到图片存放位置，请先在船体配置文件中配置图片路径", ButtonType.YES);
                            alert.showAndWait();
                        }
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "船体配置（ship）文件不存在，新建或上传后再进行此操作", ButtonType.YES);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "先在左侧选中要修改的船", ButtonType.YES);
            alert.showAndWait();
        }
    }

    public static class ship_info {
        private SimpleStringProperty name;
        private SimpleStringProperty designation;
        private SimpleStringProperty id;

        public ship_info(String name, String designation, String swp_ray_drone) {
            this.name = new SimpleStringProperty(name);
            this.designation = new SimpleStringProperty(designation);
            this.id = new SimpleStringProperty(swp_ray_drone);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public String getDesignation() {
            return designation.get();
        }

        public void setDesignation(String designation) {
            this.designation.set(designation);
        }

        public SimpleStringProperty designationProperty() {
            return designation;
        }

        public String getId() {
            return id.get();
        }

        public void setId(String id) {
            this.id.set(id);
        }

        public SimpleStringProperty idProperty() {
            return id;
        }
    }

}
