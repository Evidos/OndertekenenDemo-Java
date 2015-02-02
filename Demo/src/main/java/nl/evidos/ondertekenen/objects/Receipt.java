package nl.evidos.ondertekenen.objects;

import java.util.Arrays;

/**
 * Data class containing a Receipt object, following:
 * https://api.signhost.com/Help/
 * Note: This is a binary PDF.
 * @author Yuri Meiburg
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
