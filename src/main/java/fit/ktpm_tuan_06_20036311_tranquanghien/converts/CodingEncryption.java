package fit.ktpm_tuan_06_20036311_tranquanghien.converts;

import java.util.Base64;

public class CodingEncryption {
    public String decode(String textEnCoded){
        return new String(Base64.getDecoder().decode(textEnCoded.getBytes()));
    }
    public String enCode(String text){
        return new String(Base64.getEncoder().encode(text.getBytes()));
    }
}
