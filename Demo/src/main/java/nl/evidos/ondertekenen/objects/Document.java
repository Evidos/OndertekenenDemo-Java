package nl.evidos.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Data class containing a Document object. This is a binary PDF file, following:
 * https://api.signhost.com/Help/
 * @author Yuri Meiburg
 */
public class Document extends BinaryModelObject {

    @SerializedName("Id")
    private String documentId;

    /* Not serialized data, because it is never part of a JSON document */
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
