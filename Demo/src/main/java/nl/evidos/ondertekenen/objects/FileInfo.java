package nl.evidos.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;

/**
 * TODO
 * Created by Yuri Meiburg on 28-1-2015.
 */
public class FileInfo {
    @SerializedName("Name")
    private String name = null;

    public FileInfo(String fileName){
        name = fileName;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "name='" + name + '\'' +
                '}';
    }
}
