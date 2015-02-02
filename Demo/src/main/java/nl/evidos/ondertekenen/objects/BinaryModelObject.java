package nl.evidos.ondertekenen.objects;

/**
 * Created by Yuri Meiburg on 2-2-2015.
 */
public interface BinaryModelObject extends ModelObject {
    public void setData(byte [] data);
    public byte [] getData();
}
