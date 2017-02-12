package com.evensel.swyftr.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Prishan Maduka on 2/12/2017.
 */
public class ValidatorUtil {

    public static String isValidPassword(String text){

        String message = "Success";

        boolean hasUppercase = !text.equals(text.toLowerCase());
        boolean hasLowercase = !text.equals(text.toUpperCase());
        boolean hasSpecial   = !text.matches("[A-Za-z0-9 ]*");


        String regex = "(.)*(\\d)(.)*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        boolean hasNumber = matcher.matches();

        if(text.length()<8){
            message = "Password should contain at least 8 characters";
        }else if(!hasUppercase){
            message = "Password should contain at least 1 uppercase character";
        }else if(!hasLowercase){
            message = "Password should contain at least 1 lowercase character";
        }else if(!hasSpecial){
            message = "Password should contain at least 1 special character";
        }else if(!hasNumber){
            message = "Password should contain at least 1 digit";
        }

        return message;
    }

    public static boolean isValidEmailAddress(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
