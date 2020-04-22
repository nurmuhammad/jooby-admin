package com.smartbox.admin;

import com.smartbox.admin.crypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;

public class $ {
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    static Random random = new Random();
    static BCryptPasswordEncoder crypt = new BCryptPasswordEncoder();

    public static String encode(String password) {
        return crypt.encode(password);
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return crypt.matches(rawPassword, encodedPassword);
    }

    public static int timestamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static Date date(int timestamp) {
        return new Date(timestamp * 1000L);
    }


    public static boolean isEmail(String email) {
        if (email == null)
            return false;
        Pattern pat = Pattern.compile(EMAIL_REGEX);
        return pat.matcher(email).matches();
    }

    public static boolean isEmpty(String value) {
        if (value == null) return true;
        return value.trim().length() == 0;
    }

    public static boolean isEmpty(Object value) {
        if (value == null) return true;
        if (value instanceof String)
            return ((String) value).trim().length() == 0;
        if (value instanceof Collection)
            return ((Collection) value).isEmpty();
        if (value instanceof Map)
            return ((Map) value).isEmpty();
        if (value instanceof Object[]) {
            return ((Object[]) value).length == 0;
        }
        return false;
    }

    public static String notEmptyString(String value, String ifEmpty) {
        if ($.isEmpty(value)) return ifEmpty;
        return value;
    }

    public static Map<String, String> settings2map(String settings) {
        //TODO : change to line breaker
        Map<String, String> resultMap = new LinkedHashMap<>();
        if (settings == null || settings.length() < 2) return resultMap;
        String[] strings = settings.split(";;;\n");

        for (String string : strings) {
            if ($.isEmpty(string)) continue;
            String[] keyValue = string.trim().split(":==");
            if (keyValue.length != 2) {
                continue;
            }
            if ($.isEmpty(keyValue[0])) continue;
            if ($.isEmpty(keyValue[1])) continue;
            resultMap.put(keyValue[0].trim(), keyValue[1].trim());
        }
        return resultMap;
    }

    public static String map2settings(Map map) {
        //TODO : change to line breaker
        if (map == null) return "";
        StringBuilder builder = new StringBuilder();
        for (Object key : map.keySet()) {
            if (key == null) continue;
            Object value = map.get(key);
            if (value == null) {
                value = "<NULL>";
            }
            builder.append(key).append(":==").append(value).append(";;;\n");
        }
        return builder.toString();
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static int now() {
        return (int) Instant.now().getEpochSecond();
    }

    public static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static int convertSizeToByte(String value) {
        if (isEmpty(value)) return 0;
        Double size = Double.valueOf(value.substring(0, value.length() - 3));
        if (value.endsWith("TB")) {
            return ((Double) (size * 1024 * 1024 * 1024 * 1024)).intValue();
        } else if (value.endsWith("GB")) {
            return ((Double) (size * 1024 * 1024 * 1024)).intValue();
        } else if (value.endsWith("MB")) {
            return ((Double) (size * 1024 * 1024)).intValue();
        } else if (value.endsWith("KB")) {
            return ((Double) (size * 1024)).intValue();
        } else {
            return size.intValue();
        }
    }

    public static String newFileName(String fileName) {
        if (!fileName.contains(".")) return fileName + "_" + $.now();
        int lastDot = fileName.lastIndexOf('.');
        return fileName.substring(0, lastDot) + "_" + $.now() + fileName.substring(lastDot);
    }

    public static boolean checkUzPhoneFormat(String phone) {
        return Pattern.compile("^\\+\\d{3}-\\d{2}-\\d{3}-\\d{2}-\\d{2}$")
                .matcher(phone)
                .matches();
    }

    public static boolean checkUzPhoneFormat2(String phone) {
        return Pattern.compile("^(\\+998)\\d{9}$")
                .matcher(phone)
                .matches();
    }

    public static boolean checkUzDateFormat(String date) {
        return Pattern.compile("^[0-3]{1}[0-9]{1}[.][01]{1}[0-9]{1}[.]\\d{4}$")
                .matcher(date)
                .matches();
    }

    public static boolean checkEmailFormat(String email) {
        return Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")
                .matcher(email)
                .matches();
    }

    public static String clearPhone(String phone) {
        return clearString(phone, "[^0-9]");
    }

    public static String clearString(String value) {
        return clearString(value, "[^a-zA-Z0-9А-Яа-яЎҚҒҲўқғҳ]");
    }

    public static String clearStringWithWhiteSpace(String value) {
        return clearString(value, "[^a-zA-Z0-9А-Яа-яЎҚҒҲўқғҳ ]").trim();
    }

    public static String clearStringSystem(String value) {
        return clearString(transliterate(value.trim().toLowerCase()), "[^a-z0-9_]");
    }

    public static String clearString(String value, String acceptRegex) {
        if (value == null) return "";
        return value.replaceAll(acceptRegex, "");
    }

    static Map<Character, String> map = new LinkedHashMap<>();

    static {
        char[] abcCyr = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'ў', 'қ', 'ғ', 'ҳ', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'Ў', 'Қ', 'Ғ', 'Ҳ'};
        String[] abcLat = {"a", "b", "v", "g", "d", "e", "yo", "j", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sh", "'", "i", "", "e", "yu", "ya", "o'", "q", "g'", "h", "A", "B", "V", "G", "D", "E", "Yo", "J", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sh", "'", "I", "", "E", "Yu", "Ya", "O'", "Q", "G'", "H"};
        for (int x = 0; x < abcCyr.length; x++) {
            map.put(abcCyr[x], abcLat[x]);
        }
    }

    public static String transliterate(String message) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            if (map.containsKey(message.charAt(i))) {
                builder.append(map.get(message.charAt(i)));
            } else {
                builder.append(message.charAt(i));
            }
        }
        return builder.toString();
    }

}