package dk.bondegaard.generator.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class NumUtils {

    private static final NavigableMap<Long, String> suffixesLong = new TreeMap<>();
    private static final NavigableMap<Double, String> suffixesDouble = new TreeMap<>();

    static {
        suffixesLong.put(1_000L, "k");
        suffixesLong.put(1_000_000L, "M");
        suffixesLong.put(1_000_000_000L, "B");
        suffixesLong.put(1_000_000_000_000L, "T");
        suffixesLong.put(1_000_000_000_000_000L, "Q");
        suffixesLong.put(1_000_000_000_000_000_000L, "S");

        suffixesDouble.put(1_000.0, "k");
        suffixesDouble.put(1_000_000.0, "M");
        suffixesDouble.put(1_000_000_000.0, "B");
        suffixesDouble.put(1_000_000_000_000.0, "T");
        suffixesDouble.put(1_000_000_000_000_000.0, "Q");
        suffixesDouble.put(1_000_000_000_000_000_000.0, "S");
    }



    public static String formatNumber(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatNumber(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatNumber(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixesLong.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String formatNumber(double value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Double.MIN_VALUE) return formatNumber(Double.MIN_VALUE + 1);
        if (value < 0) return "-" + formatNumber(-value);
        if (value < 1000) return moneyFormat(value); //deal with easy case

        Map.Entry<Double, String> e = suffixesDouble.floorEntry(value);
        Double divideBy = e.getKey();
        String suffix = e.getValue();

        double truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? moneyFormat(truncated / 10d) + suffix : moneyFormat(truncated / 10) + suffix;
    }


    public static String moneyFormat(double d) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.##", otherSymbols);
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        return decimalFormat.format(d);
    }

    public static boolean numIsBetween(double target, double min, double max) {
        return target >= min && target <= max;
    }

}
