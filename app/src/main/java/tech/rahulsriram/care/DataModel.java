package tech.rahulsriram.care;

/**
 * Created by jebineinstein on 22/8/16.
 */
public class DataModel {

    String name;
    String item;
    String description;

    public DataModel(String name, String version, String id_) {
        this.name = name;
        this.item = version;
        this.description = id_;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return item;
    }

    public String getDescription() {
        return description;
    }
}