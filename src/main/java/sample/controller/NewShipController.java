package sample.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.csvreader.CsvReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.common.PublicConfigure;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

/**
 * @program: SimpleEditor
 * @description:
 * @author: wwx
 * @create: 2021-03-23 11:24
 **/
public class NewShipController {


    @FXML
    private ComboBox comboBox;
    @FXML
    private TextField textField2;
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField3;
    @FXML
    private Button button2;


    public void initialize() {
        String filePath = PublicConfigure.getShipPath();
        ObservableSet<String> data = FXCollections.observableSet(new HashSet<String>());
        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(filePath, ',', StandardCharsets.UTF_8);
            // 读表头
            csvReader.readHeaders();
            while (csvReader.readRecord()){
                if (!csvReader.get("designation").equals(""))
                    data.add(csvReader.get("designation"));
            }
            comboBox.getItems().addAll(data);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "未找到ship_data.csv文件，此mod没有船", ButtonType.YES);
            alert.showAndWait();
        }
    }

    public void comboBoxAction() {
        Object value = comboBox.getValue();
        textField3.setText(value.toString());
    }

    @FXML
    public void button2Action(){
        Stage stage = (Stage) button2.getScene().getWindow();
        //new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST);
        Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST ));
    }

    @FXML
    public void Button1Action() {
        String text1 = textField1.getText();
        String text2 = textField2.getText();
        String text3 = textField3.getText();
        if (StrUtil.isBlank(text1) || StrUtil.isBlank(text2) || StrUtil.containsBlank(text1) || StrUtil.containsBlank(text2)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "必填项不能为空,或包含空格", ButtonType.YES);
            alert.showAndWait();
            return;
        }
        String filePath = PublicConfigure.getShipPath();
        CsvWriter csvWriter = CsvUtil.getWriter(filePath, CharsetUtil.CHARSET_UTF_8, true);
        File CSV = FileUtil.file(filePath);
        if (!CSV.exists()) {
            FileUtil.touch(filePath);
            File file = FileUtil.file("config\\CSV.properties");
            FileReader reader = new FileReader(file);
            String[] head = reader.readString().split(",");
            csvWriter.writeLine(head);
        }
        String[] conten = {text1, text2, text3};
        csvWriter.writeLine(conten);
        csvWriter.close();
        button2Action();
    }
}
