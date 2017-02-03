package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Location {
    @SerializedName("Search")
    String search;
    @SerializedName("Occurence")
    Integer occurence;
    @SerializedName("Top")
    Integer top;
    @SerializedName("Right")
    Integer right;
    @SerializedName("Bottom")
    Integer bottom;
    @SerializedName("Left")
    Integer left;
    @SerializedName("Width")
    Integer width;
    @SerializedName("Height")
    Integer height;
    @SerializedName("PageNumber")
    Integer pageNumber;

}
