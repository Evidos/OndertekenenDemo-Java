import nl.evidos.ondertekenen.objects.Signer;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;

/**
 * Created by Yuri Meiburg on 28-1-2015.
 */
public class SignerTest {
    private static final String VALID_MOBILE = "+31612345678";

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void mobileNumberFormatNoCountryCode(){
        Signer s = new Signer("email").mobile("0612345678");
    }

    @Test
    public void mobileNumberFormatWithCountryCode(){
        Signer s = new Signer("email").mobile(VALID_MOBILE);
        assertEquals(VALID_MOBILE, s.getMobile());
    }

}
