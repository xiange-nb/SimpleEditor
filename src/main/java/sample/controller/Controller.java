package sample.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.common.PublicConfigure;
import sample.entity.config.Lately;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @FXML
    private MenuItem menu1;
    @FXML
    private TableView<mod_info> tableView;
    @FXML
    private TableColumn tableColumn1;
    @FXML
    private TableColumn tableColumn2;
    @FXML
    private TitledPane titledpane1;

    @FXML
    private Button buttonA1;
    @FXML
    private Button buttonB1;

    @FXML
    private Menu latelymenu;

    Map<String,String> map2 =new HashMap<>();





    public void initialize() {
        FileUtil.touch("src\\main\\resources\\config","lately.properties");
        File latelyfile = FileUtil.file("config\\lately.properties");
        if (!latelyfile.exists())return;
        FileReader fileReader = new FileReader("config\\lately.properties");
        String result = fileReader.readString();
        if (StrUtil.isNotBlank(result)){
            try {
                Lately lately = JSONObject.parseObject(result, Lately.class);
                Map<String,String> map = lately.getHistory();
                for (String ly:map.keySet()) {
                    File file = FileUtil.file(map.get(ly));
                    if (file.exists())
                    map2.put(ly.toString(),map.get(ly));
                }
                for (String ly2:map2.keySet() ) {
                    MenuItem menuItem = new MenuItem(ly2);
                    menuItem.setOnAction(event -> ImportData(map2.get(ly2)));
                    latelymenu.getItems().add(menuItem);
                }
                FileWriter fileWriter=new FileWriter(latelyfile);
                lately.setHistory(map2);
                ImportData(lately.getCurrent());
                fileWriter.write(JSONObject.toJSONString(lately));
                log.info("初始化成功");
            }catch (Exception e){

            }
        }

    }
    //菜单导入快速导入--主配置文件读取
    public void ImportData(String path){
        String filePath = PublicConfigure.setFilePath(path);
        String json = readJsonFile(filePath);
        Map<String,Object> map = JSONObject.parseObject(json, Map.class);
        ObservableList<mod_info> data = FXCollections.observableArrayList();
        tableColumn1.setCellValueFactory(
                new PropertyValueFactory("key"));
        tableColumn2.setCellValueFactory(
                new PropertyValueFactory("value"));
        tableView.setItems(data);

        for (String s: map.keySet()) {
            data.add(new mod_info(s,map.get(s).toString()));
        }
        FileReader fileReader = new FileReader("config\\lately.properties");
        File latelyfile = FileUtil.file("config\\lately.properties");
        String result = fileReader.readString();
        Lately lately = JSONObject.parseObject(result, Lately.class);
        lately.setCurrent(path);
        FileWriter fileWriter=new FileWriter(latelyfile);
        fileWriter.write(JSONObject.toJSONString(lately));
        titledpane1.setExpanded(true);
    }

    //菜单导入--主配置文件读取
    public void ImportData(){
        try {
            String filePath = PublicConfigure.setFilePath();
            if (StrUtil.isBlank(filePath))return;
            String json = readJsonFile(filePath);
            Map<String,Object> map = JSONObject.parseObject(json, Map.class);
            ObservableList<mod_info> data = FXCollections.observableArrayList();
            tableColumn1.setCellValueFactory(
                    new PropertyValueFactory("key"));
            tableColumn2.setCellValueFactory(
                    new PropertyValueFactory("value"));
            tableView.setItems(data);
            for (String s: map.keySet()) {
                data.add(new mod_info(s,map.get(s).toString()));
            }
            titledpane1.setExpanded(true);

            File modfilepath = FileUtil.file(filePath);
            File latelyfile = FileUtil.file("config\\lately.properties");
            FileWriter fileWriter=new FileWriter(latelyfile);
            String parent = modfilepath.getParent();
            String s = FileNameUtil.mainName(parent);
            map2.put(s,filePath);


            MenuItem menuItem = new MenuItem(s);
            menuItem.setOnAction(event -> ImportData(filePath));

            FileReader fileReader = new FileReader("config\\lately.properties");
            String result = fileReader.readString();
            Lately lately = JSONObject.parseObject(result, Lately.class);
            lately.setCurrent(filePath);
            lately.setHistory(map2);
            fileWriter.write(JSONObject.toJSONString(lately));

            titledpane1.setExpanded(true);

            latelymenu.getItems().add(menuItem);
        }catch (Exception e){
            log.error("mod_info.json文件解析失败");
            Alert alert = new Alert(Alert.AlertType.ERROR, "mod_info.json文件解析失败，请检查文件合适或者删除掉不必要的注释，确保文件属于json格式", ButtonType.YES);
            alert.showAndWait();
        }

    }

    public void Button1Clicked(){
        PublicConfigure.getFilePath();
    }

    public void NewEditShip1(){
        Stage primaryStage=new Stage();
        Parent root = null;
        Stage SupStage= (Stage) buttonB1.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Ship.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("船");
        Image image=new Image("/image/s_icon32.png");
        primaryStage.getIcons().add(image);
        primaryStage.setScene(new Scene(root));
        primaryStage.initOwner(SupStage);
        primaryStage.show();
        if (StrUtil.isBlank(PublicConfigure.MAINCONFIG)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "未找到mod文件，请先选择mod", ButtonType.YES);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES)
                primaryStage.close();
            return;
        }
    }


    public static String readJsonFile(String fileName) {
        FileReader fileReader = new FileReader(fileName);
        return fileReader.readString();
    }

    public static class mod_info{
        private SimpleStringProperty key;
        private SimpleStringProperty  value;

        public mod_info(String key, String value) {
            this.key =new SimpleStringProperty(key);
            this.value =new SimpleStringProperty(value);
        }

        public String getKey() {
            return key.get();
        }

        public SimpleStringProperty keyProperty() {
            return key;
        }

        public void setKey(String key) {
            this.key.set(key);
        }

        public String getValue() {
            return value.get();
        }

        public SimpleStringProperty valueProperty() {
            return value;
        }

        public void setValue(String value) {
            this.value.set(value);
        }
    }
}
