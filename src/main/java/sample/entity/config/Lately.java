package sample.entity.config;

import java.util.Map;

/**
 * @program: SimpleEditor
 * @description:
 * @author: wwx
 * @create: 2021-03-18 15:30
 **/
public class Lately {
    private String Current;
    private Map<String,String> History;

    public String getCurrent() {
        return Current;
    }

    public void setCurrent(String current) {
        Current = current;
    }

    public Map<String, String> getHistory() {
        return History;
    }

    public void setHistory(Map<String, String> history) {
        History = history;
    }
}
