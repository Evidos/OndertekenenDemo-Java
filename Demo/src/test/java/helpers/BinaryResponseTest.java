package helpers;

import nl.evidos.ondertekenen.objects.BinaryModelObject;

/**
 * Created by Yuri Meiburg on 12-2-2015.
 */
public class BinaryResponseTest implements BinaryModelObject {
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
