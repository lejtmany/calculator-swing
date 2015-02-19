/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lejtman;

import java.util.function.BinaryOperator;

/**
 *
 * @author agross11
 */
public class MathExpressionParser {

    public double parse(String expr) {
        String[] components = expr.split(" ");
        double num1 = Double.parseDouble(components[0]);
        double num2 = Double.parseDouble(components[2]);
        BinaryOperator<Double> operation = null;
        switch (components[1]) {
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
                operation = (a, b) -> a / b;
                break;
        }
        return evaluate(num1, num2, operation);
    }

    private double evaluate(double num1, double num2, BinaryOperator<Double> operation) {
        return operation.apply(num1, num2);
    }
}
