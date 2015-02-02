package nl.evidos.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * TODO
 * Created by Yuri Meiburg on 14-1-2015.
 */
public class Document implements BinaryModelObject {

    @SerializedName("Id")
    private String documentId;

    private byte [] data;

    @Override
    public String toString() {
        return "Document{" +
                "documentId='" + documentId + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    @Override
    public void setData(byte [] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
