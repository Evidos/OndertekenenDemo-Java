package helpers;

import nl.yurimeiburg.ondertekenen.objects.BinaryModelObject;

/**
 * Created by Yuri Meiburg on 12-2-2015.
 */
public class BinaryResponseTest extends BinaryModelObject {
    byte [] data;
    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    public String getStringData() {
        return new String(data);
    }
}