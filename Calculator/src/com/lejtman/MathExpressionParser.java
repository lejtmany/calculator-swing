/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lejtman;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.BinaryOperator;

/**
 *
 * @author agross11
 */
public class MathExpressionParser {

    public final static String NEGATIVE_SIGN = "n";
    
    public static double parse(String expr) {
        List<String> tokenList = tokenizeExpression(expr);
        doOperation(tokenList, "[*/]");
        doOperation(tokenList, "[+-]");       
        return Double.parseDouble(tokenList.get(0));
    }

    private static List<String> tokenizeExpression(String expr) {
        List<String> tokenList = new LinkedList();
        String[] tokenArray = expr.split("((?<=[*+/-])|(?=[*+/-]))");
        for (String token : tokenArray) {
            tokenList.add(token.replaceFirst(NEGATIVE_SIGN, "-").trim());
        }
        return tokenList;
    }

    private static void doOperation(List<String> tokenList, String pattern) {
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
                if (num2 == 0) {
                    throw new IllegalArgumentException("Divide by Zero Error");
                }
                operation = (a, b) -> a / b;
                break;
        }
        return evaluate(num1, num2, operation);
    }

    private static double evaluate(double num1, double num2, BinaryOperator<Double> operation) {
        return operation.apply(num1, num2);
    }
}
