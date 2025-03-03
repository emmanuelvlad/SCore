package com.ssomar.score.utils;

import com.ssomar.score.SCore;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringConverter {

    public static final char COLOR_CHAR = '\u00A7';

    public static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String coloredString(String s) {
        String convert = s.replaceAll("& ", "|<-_->|");
        convert = convert.replaceAll("&", "§");
        convert = convert.replaceAll("\\|<-_->\\|", "& ");
        if (SCore.is1v16Plus()) convert = translateHexCodes(convert);
        return convert;
    }

    public static List<String> coloredString(List<String> list) {
        List<String> convert = new ArrayList<>();
        for (String s : list) {
            convert.add(coloredString(s));
        }
        return convert;
    }

    public static List<String> replaceVariable(List<String> list, String player, String item, String quantity, int time) {
        List<String> newList = new ArrayList<>();
        for (String s : list) {
            newList.add(replaceVariable(s, player, item, quantity, time));
        }
        return newList;
    }

    public static String replaceVariable(String s, String player, String item, String quantity, int time) {
        String convert = s;
        String convertItem = item;
        String convertPlayer = player;

        if (!convert.isEmpty()) {
            convert = convert.replace("$", "REGEX-DOLARS");
            if (convert.charAt(0) == '/') {
                convert = convert.replaceFirst("/", "");
            }
            convertItem = convertItem.replace("$", "REGEX-DOLARS");
            convertPlayer = convertPlayer.replace("$", "REGEX-DOLARS");
            convert = convert.replaceAll("%player%", convertPlayer);
            convert = convert.replaceAll("%item%", convertItem);
            convert = convert.replaceAll("%quantity%", quantity);
            convert = convert.replaceAll("REGEX-DOLARS", "\\$");
            int M = time / 60;
            int S = time % 60;
            int H = M / 60;
            M = M % 60;
            convert = convert.replaceAll("%time%", H + "H " + M + "M " + S + "S");
        }
        return convert;
    }


    public static List<String> decoloredString(List<String> list) {
        List<String> result = new ArrayList<>();
        for (String s : list) {
            result.add(decoloredString(s));
        }
        return result;
    }

    public static String decoloredString(String s) {
        String convert = replaceHexCodes(s);

        StringBuilder sb = new StringBuilder();
        char[] sChar = convert.toCharArray();
        boolean isAnd = false;
        for (int i = 0; i < sChar.length; i++) {
            if (isAnd) {
                if (sChar[i] == ' ') {
                    sb.append("& ");
                }
                isAnd = false;
            } else if (sChar[i] == '&') {
                isAnd = true;
            } else if (sChar[i] == '§') {
                i++;
            } else {
                sb.append(sChar[i]);
            }
        }
        return removeHexCodes(sb.toString());
    }

    public static List<String> deconvertColor(List<String> list) {
        List<String> result = new ArrayList<>();
        for (String s : list) {
            result.add(deconvertColor(s));
        }
        return result;
    }

    public static String deconvertColor(String s) {

        if (s == null || s.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        char[] sChar = s.toCharArray();
        for (char c : sChar) {
            if (c == '§') {
                sb.append('&');
            } else {
                sb.append(c);
            }
        }
        return replaceHexCodes(sb.toString());
    }

    public static String translateHexCodes(String textToTranslate) {

        if (textToTranslate.contains("BRUT_HEX")) {
            textToTranslate = textToTranslate.replaceAll("§", "&");
            return textToTranslate.replaceAll("BRUT_HEX", "");
        }

        String convert = textToTranslate;
        Matcher matcher = HEX_PATTERN.matcher(convert);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String color = convert.substring(matcher.start(), matcher.end());
            convert = convert.replace(color, ChatColor.of(color) + "");
            matcher = HEX_PATTERN.matcher(convert);
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());

    }

    public static String replaceHexCodes(String s) {

        String textToTranslate = s;
        boolean start1 = false;
        boolean startValid = false;
        boolean requireET = false;
        boolean requireLetter = false;
        List<String> hexCode = new ArrayList<>();
        for (char c : textToTranslate.toCharArray()) {
            if (c == '&') {
                if (!startValid) start1 = true;
                if (requireET) {
                    requireLetter = true;
                    requireET = false;
                } else if (requireLetter) {
                    startValid = false;
                }

            } else {

                if (start1) {
                    if (c == 'x') {
                        startValid = true;
                        requireET = true;
                    }
                } else if (requireET) {
                    startValid = false;
                } else if (requireLetter && startValid) {
                    hexCode.add(c + "");
                    if (hexCode.size() == 6) {
                        break;
                    } else {
                        requireET = true;
                        requireLetter = false;
                    }
                }

                start1 = false;
            }
        }

        if (hexCode.size() == 6) {
            StringBuilder toReplace = new StringBuilder("&x");
            for (String code : hexCode) {
                toReplace.append("&").append(code);
            }
            StringBuilder replacement = new StringBuilder("#");
            for (String code : hexCode) {
                replacement.append(code);
            }
            textToTranslate = textToTranslate.replaceAll(toReplace.toString(), replacement.toString());

            textToTranslate = replaceHexCodes(textToTranslate);
        }

        return textToTranslate;
    }

    public static String removeHexCodes(String textToTranslate) {
        Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String color = textToTranslate.substring(matcher.start(), matcher.end());
            textToTranslate = textToTranslate.replace(color, "");
            matcher = HEX_PATTERN.matcher(textToTranslate);
        }
        return matcher.appendTail(buffer).toString();
    }

    public static void main(String[] args) {
        String s = "#44D800&lPioche à spawner #35A800";

        s = removeHexCodes(s);
        System.out.println(s);
    }

}

