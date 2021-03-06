package com.lejtman;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathParser {

    private final static String sqrt = "sqrt";
    private final static String reciproc = "reciproc";

    public static String sqrt(double num) {
        return String.format("%s(%f)", sqrt, num);
    }

    public static String sqrt(String num) {
        if (num.trim().isEmpty())
            throw new IllegalArgumentException("No input string for square root");
        return String.format("%s(%s)", sqrt, num);
    }

    public static String reciproc(double num) {
        return String.format("%s(%f)", reciproc, num);
    }

    public static String reciproc(String num) {
        if (num.trim().isEmpty())
            throw new IllegalArgumentException("No input string for reciprocal");
        return String.format("%s(%s)", reciproc, num);
    }

    public static double parse(String expr) {
        List<String> tokenList = tokenizeExpression(expr);
        doBinaryOperation(tokenList, "[*/]");
        doBinaryOperation(tokenList, "[+-]");
        return Double.parseDouble(tokenList.get(0));
    }

    private static List<String> tokenizeExpression(String expr) {
        List<String> tokenList = new LinkedList();
        String[] tokenArray = expr.split("((?<!\\(-)(?<=[*+/-])|(?=[*+/-]))(?<!\\()");
        for (String token : tokenArray) {
            if (token.contains(sqrt))
                token = doUnaryOperation(sqrt, token);
            else if (token.contains(reciproc))
                token = doUnaryOperation(reciproc, token);
            else
                //remove parentheses from negative numbers
                token = token.replaceAll("[\\(\\)]", "");
            tokenList.add(token.trim());
        }
        return tokenList;
    }

    private static String doUnaryOperation(String oper, String token) {
        UnaryOperator<Double> operation = null;
        String betweenParentheses = getBetweenParentheses(token);
        double num = 0;
        if(!betweenParentheses.matches("-?\\d+(\\.\\d+)?"))
            betweenParentheses = doUnaryOperation(oper, betweenParentheses);
        
        num = Double.parseDouble(betweenParentheses);
        switch (oper) {
            case reciproc:
                operation = (a) -> 1 / a;
                break;
            case sqrt:
                operation = (a) -> Math.sqrt(a);
                break;}
        return operation.apply(num) + "";
    }
    
    private static String getBetweenParentheses(String s){
        int firstOpening, lastClosing;
        firstOpening = s.indexOf("(");
        lastClosing = s.lastIndexOf(")");
        return s.substring(firstOpening + 1, lastClosing);
    }

    private static double reciprocal(double num) {
        return 1 / num;
    }

    private static void doBinaryOperation(List<String> tokenList, String pattern) {
        double result;
        for (int i = 1; i < tokenList.size() - 1; i++) {
            if (tokenList.get(i).matches(pattern)) {
                result = parseBinaryExpression(tokenList.get(i), tokenList.get(i - 1), tokenList.get(i + 1));
                tokenList.remove(i);
                tokenList.remove(i);
                tokenList.set(i - 1, result + "");
                i = 0;
            }
        }
    }

    private static double parseBinaryExpression(String operator, String n1, String n2) {
        BinaryOperator<Double> operation = null;

        double num1 = Double.parseDouble(n1), num2 = Double.parseDouble(n2);
        switch (operator) {
            case "+":
                operation = (a, b) -> a + b;
                break;
            case "-":
                operation = (a, b) -> a - b;
                break;
            case "*":
                operation = (a, b) -> a * b;
                break;
            case "/":
                if (num2 == 0)
                    throw new IllegalArgumentException("Divide by Zero Error");
                operation = (a, b) -> a / b;
                break;
        }
        return operation.apply(num1, num2);
    }
}
