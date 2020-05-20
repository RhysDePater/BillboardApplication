package BillboardControlPanel.UnitTest;

import BillboardControlPanel.Helper.ControllerHelper;
import org.junit.jupiter.api.Test;

class ControllerHelperTest extends ControllerHelper {

    static byte[] salt = "[B@51c8530f".getBytes(); //[B@289d1c02
    static String password = "password";
    static String securePassword = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

    @Test
    public void createHashedPasswordTest() {
        String hashedPassword = createSecurePassword(password);
        System.out.println(salt);
        System.out.println(hashedPassword);
        System.out.println(securePassword);
    }

}