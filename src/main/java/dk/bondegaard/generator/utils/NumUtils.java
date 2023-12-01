/*
 * Copyright (c) 2023 bondegaard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
