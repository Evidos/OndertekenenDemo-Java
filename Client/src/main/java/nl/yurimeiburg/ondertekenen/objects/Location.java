package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Location {
    @SerializedName("Search")
    String search;
    @SerializedName("Occurence")
    int occurence;
    @SerializedName("Top")
    int top;
    @SerializedName("Right")
    int right;
    @SerializedName("Bottom")
    int bottom;
    @SerializedName("Left")
    int left;
    @SerializedName("Width")
    int width;
    @SerializedName("Height")
    int height;
    @SerializedName("PageNumber")
    int pageNumber;

}
