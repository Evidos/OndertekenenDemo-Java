package nl.yurimeiburg.ondertekenen.objects;


import com.google.gson.annotations.SerializedName;

public class FileMetaData {
    @SerializedName("DisplayName")
    private String displayName;
    @SerializedName("Signers")
    private FormSet[] signers;
    @SerializedName("FormSets")
    private FormSet[] formSets;

}
