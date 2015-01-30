package nl.evidos.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;

/**
 * TODO
 * Created by Yuri Meiburg on 28-1-2015.
 */
public class FileInfo {
    @SerializedName("Name")
    private String name = null;
    @SerializedName("Id")
    private String id = null;

    public FileInfo(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
