package nl.evidos.ondertekenen.objects;

/**
 * A data object used for the conversation with the service, consisting of <em>binary</em> data.
 * @author Yuri Meiburg
 */
public interface BinaryModelObject extends ModelObject {
    public void setData(byte [] data);
    public byte [] getData();
}
