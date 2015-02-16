package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Data class containing a File meta-object, following:
 * https://api.signhost.com/Help/
 * @author Yuri Meiburg
 */
public class FileInfo {
    @SerializedName("Name")
    private String name = null;
    @SerializedName("Id")
    private String id = null;

    public FileInfo(String name){
        this.name = name;
    }

    public FileInfo withId(String id){
        this.id = id;
        return this;
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
