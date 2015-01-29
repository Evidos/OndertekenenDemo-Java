package nl.evidos.ondertekenen.objects;


import com.google.gson.annotations.SerializedName;

/**
 * TODO
 * Created by Yuri Meiburg on 28-1-2015.
 */
public class ErrorMessage implements ModelObject {
    @SerializedName("Message")
    private String message=null;

    public ErrorMessage(String message){
        this.message = message;
    }
    @Override
    public String toString() {
        return "ErrorMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
