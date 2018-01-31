package com.example.u.sfields;

import android.app.Application;

/**
 * Created by sandalkotawala on 25/11/17.
 */

public class ReportResultClass extends Application {
    private static boolean result[] = new boolean[55];
    private static int fixationLoss = 0;
    private static int falsePositive = 0;
    private static String mrNumber = "P-123456";
    private static int age = 67;
    private static long time_duration = 231734;
    private static String eye = "Right";
    private static int falseNegative = 0;
    private static String time = "";
    private static String date = "";


    public static int getFixationLoss() {
        return fixationLoss;
    }

    public static void setFixationLoss(int fixationLoss) {
        ReportResultClass.fixationLoss = fixationLoss;
    }

    public static int getFalsePositive() {
        return falseNegative;
    }

    public static void getFalsePositive(int falsePositive) {
        ReportResultClass.falsePositive = falsePositive;
    }

    public static String getMrNumber() {
        return mrNumber;
    }

    public static void setMrNumber(String mrNumber) {
        ReportResultClass.mrNumber = mrNumber;
    }

    public static int getAge() {
        return age;
    }

    public static void setAge(int age) {
        ReportResultClass.age = age;
    }

    public static String getEye() {
        return eye;
    }

    public static void setEye(String eye) {
        ReportResultClass.eye = eye;
    }

    public static int getFalseNegative() {
        return falseNegative;
    }

    public static void setFalseNegative(int falseNegativePoints) {
        ReportResultClass.falseNegative = falseNegativePoints;
    }

    public static String getTime() {
        return time;
    }

    public static void setTime(String time) {
        ReportResultClass.time = time;
    }

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        ReportResultClass.date = date;
    }

    public static boolean[] getResult() {
        return result;
    }

    public static void setResult(boolean[] result) {
        ReportResultClass.result = result;
    }

    public static long getTime_duration() {
        return time_duration;
    }

    public static void setTime_duration(long time_duration) {
        ReportResultClass.time_duration = time_duration;
    }

}
