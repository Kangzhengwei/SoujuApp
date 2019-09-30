package souju.kzw.com.souju.Util;

/**
 * Created by kang4 on 2017/12/27.
 */


import android.text.TextUtils;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StringUtil {

    private static final String NORMALIZE_REGEXP = "[^\\p{ASCII}]";

    /**
     * Remove all whitespaces from string
     *
     * @param value
     * @return
     */
    public static String removeWhiteSpaces(String value) {
        return value.replaceAll("\\s+", "");
    }

    /**
     * replace all whitespaces from string by '_'
     *
     * @param value
     * @return
     */
    public static String replaceWhiteSpaces(String value) {
        return value.replaceAll("\\s+", "_");
    }

    /**
     * Normalize a string by removing special chars / accents / etc...
     *
     * @param
     * @return
     */
    public static String normalizeString(String value) {
        if (null == value) {
            return null;
        }
        return Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll(NORMALIZE_REGEXP, "");
    }

    public static boolean isEmpty(CharSequence value) {
        return value == null || TextUtils.isEmpty(value.toString().trim());
    }

    public static boolean isNotEmpty(CharSequence value) {
        return !isEmpty(value);
    }

    /**
     * random number in [from..to[
     *
     * @param from
     * @param to
     * @return
     */
    public static int getRandom(int from, int to) {
        if (from < to)
            return from + new Random().nextInt(Math.abs(to - from));
        return from - new Random().nextInt(Math.abs(to - from));
    }

    public static boolean isEquals(String str1, String str2) {
        if (isEmpty(str1) && isEmpty(str2)) {
            return true;
        } else if (isEmpty(str1)) {
            return false;
        } else if (isEmpty(str2)) {
            return false;
        } else {
            return str1 == str2 || str1.equals(str2);
        }
    }

    public static <T> List<T[]> chunk(T[] arr, int size) {
        if (size <= 0) {
            // Size must be > 0
            return null;
        }

        List<T[]> result = new ArrayList<T[]>();

        int from = 0;
        int to = size >= arr.length ? arr.length : size;

        while (from < arr.length) {
            T[] subArray = Arrays.copyOfRange(arr, from, to);
            from = to;
            to += size;
            if (to > arr.length) {
                to = arr.length;
            }
            result.add(subArray);
        }
        return result;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String $(String s) {
        int i = Integer.valueOf(s);
        String s0 = s;
        if (i < 10) {
            s0 = "0" + String.valueOf(i);
            return s0;
        }
        return s0;
    }

    public static boolean isNum(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }





    /**
     * 穿山甲字符串工具
     */

    private static final long K = 1024;
    private static final long M = K * K;
    private static final long G = M * K;
    private static final long T = G * K;

    public static String bytesToHuman(final long value) {
        final long[] dividers = new long[]{T, G, M, K, 1};
        final String[] units = new String[]{"TB", "GB", "MB", "KB", "B"};
        if (value < 1)
            return 0 + " " + units[units.length - 1];
        String result = null;
        for (int i = 0; i < dividers.length; i++) {
            final long divider = dividers[i];
            if (value >= divider) {
                result = format(value, divider, units[i]);
                break;
            }
        }
        return result;
    }

    private static String format(final long value,
                                 final long divider,
                                 final String unit) {
        final double result =
                divider > 1 ? (double) value / (double) divider : (double) value;
        return new DecimalFormat("#.##").format(result) + " " + unit;
    }
}
