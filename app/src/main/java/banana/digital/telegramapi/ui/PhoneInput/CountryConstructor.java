package banana.digital.telegramapi.ui.PhoneInput;

import java.io.Serializable;

public class CountryConstructor implements Serializable {
    String CountryName;
    int CountryCode;


    public CountryConstructor(String CountryName, int CountryCode){
        this.CountryName = CountryName;
        this.CountryCode = CountryCode;

    }


    public String getCategoryName(){
        return CountryName;
    }

    public int getCategoryBackground(){
        return CountryCode;
    }


}