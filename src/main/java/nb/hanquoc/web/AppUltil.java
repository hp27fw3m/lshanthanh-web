/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nb.hanquoc.web;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author h17
 */
public class AppUltil {

    private static final String[] numWords = {
            "không",
            "một",
            "hai",
            "ba",
            "bốn",
            "năm",
            "sáu",
            "bảy",
            "tám",
            "chín"
    };

    private static final String[] groupWords = {
            "",
            "ngàn",
            "triệu",
            "tỷ"
    };

    public static String convert(int num) {
        if (num == 0) {
            return numWords[0];
        }

        int i = 0;
        String words = "";

        while (num > 0) {
            int groupNum = num % 1000;
            if (groupNum != 0) {
                String groupStr = convertGroup(groupNum);
                words = groupStr + " " + groupWords[i] + " " + words;
            }
            num /= 1000;
            i++;
        }

        return words.trim();
    }

    private static String convertGroup(int num) {
        String result = "";

        int hundreds = num / 100;
        if (hundreds > 0) {
            result += numWords[hundreds] + " trăm";
        }

        int tens = (num % 100) / 10;
        if (tens > 0) {
            if (!result.isEmpty()) {
                result += " ";
            }
            if (tens == 1) {
                int ones = num % 10;
                result += numWords[tens * 10 + ones];
                return result;
            } else {
                result += numWords[tens * 10];
            }
        }

        int ones = num % 10;
        if (ones > 0) {
            if (!result.isEmpty()) {
                result += " ";
            }
            result += numWords[ones];
        }

        return result;
    }

    private static final int max = 999999;
    private static final int min = 111111;

    public static int downloadCode() {

        int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
        return random_int;
    }

    private static final SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static String dateTimeToString(Date date) {
        return simpleDateTimeFormat.format(date);
    }

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static String dateToString(Date date) {
        return simpleDateFormat.format(date);
    }

    public static LocalDate stringToDate(String str) {
        try {
            Date date = simpleDateFormat.parse(str);
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return localDate;
        } catch (Exception ex) {
            System.out.println("Err: stringToDate(" + str + ")");
            return null;
        }
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String toTitleCase(String input) {
        input = input.toLowerCase();
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

}