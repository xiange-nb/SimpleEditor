package sample.common;

import cn.hutool.core.io.FileUtil;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PublicConfigure {

    @FXML
    public static Stage stage=new Stage();

    //主配置文件
    public static String MAINCONFIG=null;

    //目录
    public static String CATALOG=null;

    public static String ship_data_csv="\\data\\hulls\\ship_data.csv";

    public static String ship="\\data\\hulls\\";

    public static String getFilePath(){
        if (MAINCONFIG==null){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择mod");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Json", "*.json*")
            );
            File file = fileChooser.showOpenDialog(stage);
            if (file==null){
                return null;
            }
            MAINCONFIG=file.getPath();
            CATALOG=file.getParent();
            return MAINCONFIG;
        }
        return MAINCONFIG;
    }
    public static String setFilePath(){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择mod");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Json", "*.json*")
            );
            File file = fileChooser.showOpenDialog(stage);
            if (file==null){
                return null;
            }
            MAINCONFIG=file.getPath();
            CATALOG=file.getParent();
            return MAINCONFIG;
    }

    public static String setFilePath(String path){
        File file = FileUtil.file(path);
        if (file==null){
            return null;
        }
        MAINCONFIG=file.getPath();
        CATALOG=file.getParent();
        return MAINCONFIG;
    }

    public static String getCatalog(){
        if (CATALOG==null){
            getFilePath();
        }
        return CATALOG;
    }
    public static String getShipPath(){
        return getCatalog()+ship_data_csv;
    }
    public static String getShipFile(){
        return getCatalog()+ship;
    }
    public static String getShipImagePath(){
        return CATALOG+"\\";
    }

}
