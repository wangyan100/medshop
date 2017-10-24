/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hellokoding.account.util;

/**
 *
 * @author yw
 */
public class Util {

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static float convertEuroPrice(String price) {
        price = price.trim();
        return Float.parseFloat(price.substring(0, price.indexOf("€")));
    }

}
