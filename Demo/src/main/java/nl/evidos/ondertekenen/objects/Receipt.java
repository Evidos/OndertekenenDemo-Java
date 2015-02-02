package nl.evidos.ondertekenen.objects;

import java.util.Arrays;

/**
 * TODO
 * Created by Yuri Meiburg on 23-1-2015.
 */
public class Receipt implements BinaryModelObject{
    private byte[] data;

    @Override
    public String toString() {
        return "Receipt{" +
                "data=" + Arrays.toString(data) +
                '}';
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
