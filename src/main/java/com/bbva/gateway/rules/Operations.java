package com.bbva.gateway.rules;

import java.util.Arrays;
import java.util.Objects;

public class Operations {

    private Operations(){

    }

    /**
     * Check if the operator1 is equal to operator2
     */
    public static boolean equals(String operator1, String operator2){
        if (operator1 == null || operator2 == null) {
            return false;
        }
        return Objects.equals(operator1, operator2);
    }


    /**
     * Check if the operator1 is not equal to operator2
     */
    public static boolean notEquals(String operator1, String operator2){
        if (operator1 == null || operator2 == null) {
            return false;
        }
        return !Objects.equals(operator1, operator2);
    }


    /**
     * Check if the operator1 is greater than operator2
     */
    public static boolean greater(String operator1, String operator2){
        try {
            return Double.parseDouble(operator1) > Double.parseDouble(operator2);
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if the operator1 is less than operator2
     */
    public static boolean less(String operator1, String operator2){
        try {
            return Double.parseDouble(operator1) < Double.parseDouble(operator2);
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if the operator1 starts with any element operator2
     */
    public static boolean startwith(String operator1, String operator2){
        if (operator1 == null || operator2 == null) {
            return false;
        }

        return Arrays.stream(operator2.split(","))
                .map(String::trim)
                .anyMatch(operator1::startsWith);
    }
    /**
     * Check if the operator1 ends with any element operator2
     */
    public static boolean endwith(String operator1, String operator2){
        if (operator1 == null || operator2 == null) {
            return false;
        }
        return Arrays.stream(operator2.split(","))
                .map(String::trim)
                .anyMatch(operator1::endsWith);
    }

    /**
     * Check if the operator1 is in the operator2
     */
    public static boolean in(String operator1, String operator2){
        if (operator1 == null || operator2 == null) {
            return false;
        }
        return Arrays.stream(operator2.split(","))
                .map(String::trim)
                .anyMatch(operator1::equals);
    }


    /**
     * Check if the operator1 is not in the operator2
     */
    public static boolean notIn(String operator1, String operator2){
        if (operator1 == null || operator2 == null) {
            return false;
        }
        return Arrays.stream(operator2.split(","))
                .map(String::trim)
                .noneMatch(operator1::equals);
    }



    /**
     * Check if the operator1 is in the range of operator2
     */
    public static boolean range(String operator1, String operator2){
        try {
            String[] parts = operator2.split("-");
            if (parts.length != 2) {
                return false;
            }
            double op1 = Double.parseDouble(operator1);
            return Double.parseDouble(parts[0]) < op1 && Double.parseDouble(parts[1]) > op1;
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
    }
}
