package app.modules.model;

import app.core.suite.Subjective;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Values {

    public static String electronicFormat(double value, String unit) {
        String formatted;
        DecimalFormat valueFormat = new DecimalFormat("#.");
        if(value < 1e-12) {
            formatted = valueFormat.format(value / 1e-15) + "f";
        } else if(value < 1e-9) {
            formatted =  valueFormat.format(value / 1e-12) + "p";
        } else if(value < 1e-6) {
            formatted = valueFormat.format(value / 1e-9) + "n";
        } else if(value < 1e-3) {
            formatted = valueFormat.format(value / 1e-6) + "\u005B";
        } else if(value < 1.0) {
            formatted = valueFormat.format(value / 1e-3) + "m";
        } else if(value < 1e3) {
            formatted = valueFormat.format(value);
        } else if(value < 1e6) {
            formatted = valueFormat.format(value / 1e3) + "k";
        } else if(value < 1e9) {
            formatted = valueFormat.format(value / 1e6) + "M";
        } else {
            formatted = valueFormat.format(value / 1e9) + "G";
        }
        return formatted + unit;
    }

    public static String electronicFormat(double value) {
        return electronicFormat(value, "");
    }

    public static double electronicParse(String string) throws ParseException {
        Pattern pattern = Pattern.compile("$\\s*(?<number1>\\d+)(?:[.,](?<number2>\\d+))?\\s*(?<fold>[fpnumkMG])?");
        Matcher matcher = pattern.matcher(string);
        if(matcher.matches()) {
            String number = matcher.group("number1") + ".";
            String secondPart = matcher.group("number2");
            number += secondPart == null ? "0" : secondPart;
            double ret = Double.parseDouble(number);
            String fold = matcher.group("fold");
            if(fold == null) {
                return ret;
            } else {
                switch(fold) {
                    case "f": return ret * 1e-15;
                    case "p": return ret * 1e-12;
                    case "n": return ret * 1e-9;
                    case "m": return ret * 1e-3;
                    case "k": return ret * 1e3;
                    case "M": return ret * 1e6;
                    case "G": return ret * 1e9;
                    default: return ret * 1e-6;
                }
            }
        } else throw new ParseException("Cannot parse " + string + " as double", 0);
    }
}
