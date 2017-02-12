package com.evensel.swyftr.util;

/**
 * Created by Prishan Maduka on 2/12/2017.
 */
public class ValidatorUtil {

    public static boolean isEmptyText(String text){
        return text.isEmpty();
    }

    public static String isValidPassword(String text){

        String message = "Success";

        boolean hasUppercase = !text.equals(text.toLowerCase());
        boolean hasLowercase = !text.equals(text.toUpperCase());
        boolean hasSpecial   = !text.matches("[A-Za-z0-9 ]*");
        boolean hasNumber = text.matches(".*\\\\d.*");

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
