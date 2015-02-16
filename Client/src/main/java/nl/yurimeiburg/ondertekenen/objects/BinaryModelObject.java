package nl.yurimeiburg.ondertekenen.objects;

/**
 * A data object used for the conversation with the service, consisting of <em>binary</em> data.
 * @author Yuri Meiburg
 */
public abstract class BinaryModelObject extends ModelObject {
    public abstract void setData(byte [] data);
    public abstract byte [] getData();
}
