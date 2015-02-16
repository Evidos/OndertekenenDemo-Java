package nl.yurimeiburg.ondertekenen.objects;

import java.io.Serializable;

/**
 * A basic interface for REST objects used in the communication with the Ondertekenen-service. The only method used
 * in this main.java.demo is the toString(), therefor this interface has no methods.
 * @author Yuri Meiburg
 */
public abstract class ModelObject implements Serializable {
    ErrorMessage errorMessage = null;

    public boolean isOk(){
        return errorMessage == null;
    }

    public ErrorMessage getErrorMessage(){
        return errorMessage;
    }

    public void setError(ErrorMessage errorMessage){
        this.errorMessage = errorMessage;
    }

}
