package tech.rahulsriram.care;

/**
 * Created by jebineinstein on 22/8/16.
 */
public class DataModel {

    String number;
    String name;
    String latitude;
    String longitude;
    String item;
    String description;

    public DataModel(String number,String name,String latitude,String longitude,String version, String id_) {
        this.number=number;
        this.name = name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.item = version;
        this.description = id_;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getItem() {
        return item;
    }

    public String getDescription() {
        return description;
    }
}