package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;


public class FormSet {
    @SerializedName("FormSetType")
    FormSetType type;
    @SerializedName("Location")
    Location location;
}
