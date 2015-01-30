package nl.evidos.ondertekenen.objects;

/**
 * TODO
 * Created by Yuri Meiburg on 14-1-2015.
 */
public class Document implements ModelObject {
    private byte [] documentId;

    public String toString() {

        return "Document{" +
                "documentId=" + documentId +
                '}';
    }
}
