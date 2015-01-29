package nl.evidos.ondertekenen.objects;

/**
 * TODO
 * Created by Yuri Meiburg on 28-1-2015.
 */
public class Response<T extends ModelObject> {
    ModelObject result = null;
    int statusCode = -1;

    public static <T extends ModelObject>Response success(T result){
        return new Response<T>(result);
    }

    public static <T extends ModelObject>Response error(int statusCode, ErrorMessage result){
        return new Response<T>(statusCode, result);
    }

    public static <T extends ModelObject>Response unavailable(String errorMessage){

        return new Response<T>(-1, new ErrorMessage(errorMessage));
    }

    private Response(T result){
        this.result = result;
        statusCode = 200;
    }

    private Response(int errorCode, ErrorMessage result){
        this.result = result;
        this.statusCode = errorCode;
    }

    public boolean isOk(){
        return statusCode == 200;
    }

    public T getResult(){
        return (statusCode == 200) ? (T) result : null;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ErrorMessage getError(){
        if(result.getClass().isAssignableFrom(ErrorMessage.class)){
            return (ErrorMessage) result;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Response{" +
                "result=" + result +
                ", statusCode=" + statusCode +
                '}';
    }
}
