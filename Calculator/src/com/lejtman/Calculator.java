//Yosef Shalom Lejtman
package com.lejtman;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Calculator extends JFrame {

    private final JPanel buttonPad, numberPad, operatorPad, advancedOperatorPad, memoryPad;
    private double memory;
    private final CalculatorDisplay display;
    private final HashMap<String, JButton> buttonMap;

    public Calculator() {
        buttonMap = new HashMap<>();
        this.setTitle("Calculator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        buttonPad = new JPanel(new GridLayout(6,5, 5,5));
        operatorPad = new JPanel(new GridLayout(5, 1));
        advancedOperatorPad = new JPanel(new GridLayout(6, 1));
        memoryPad = new JPanel(new GridLayout(1, 4));
        numberPad = new JPanel(new GridLayout(4, 3));
        display = new CalculatorDisplay();

        createButtons();
        addButtons();

        this.add(display, BorderLayout.NORTH);
        this.add(buttonPad, BorderLayout.CENTER);
        
        this.pack();
        this.setSize(250, 300);       
        this.setVisible(true);
    }

    private void createButtons() {
        //operator buttons
        Point[] operatorCoordinates = {new Point(3,2), new Point(3,3),new Point(3,4),new Point(3,5)};
        String[] operators = {"+", "-", "*", "/"};
        for (int i = 0; i < operators.length; i++) {
            createButton(operatorPad, operators[i], new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    //don't allow 2 operators in a row
                    if (display.getState() == EntryState.MID_CALC)
                        return;

                    display.submitToTopDisplay(display.getEntryDisplayText());

                    //calculate what we have so far
                    display.setMidCalc(solve(display.getTopDisplayText()));

                    display.submitToTopDisplay(" " + e.getActionCommand() + " ");
                }
            });
        }

        
        
        //numeric buttons
        Point[] numberCoordinates = {new Point(0,5), new Point(0,4),new Point(1,4),new Point(2,4),new Point(0,3),new Point(1,3),new Point(2,3),new Point(0,2),new Point(1,2),new Point(2,2)};
        for (int i = 0; i < 10; i++) {
            createButton(numberPad, i + "", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    display.submitToEntryDisplay(e.getActionCommand());
                }
            });
        }

        createButton(numberPad, ".", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String entryText = display.getEntryDisplayText();
                if (!entryText.contains(".") || display.getState() != EntryState.ENTRY)
                    display.submitToEntryDisplay(".");
            }
        });

        createButton(advancedOperatorPad, "C", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.clearScreens();
                memory = 0;
            }
        });

        createButton(advancedOperatorPad, "CE", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.clearEntryDisplay();
            }
        });

        createButton(memoryPad, "MS", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = Double.parseDouble(display.getEntryDisplayText());
            }
        });

        createButton(memoryPad, "MR", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.setEntryDisplay(doubleToString(memory));
            }
        });

        createButton(memoryPad, "M+", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = memory + Double.parseDouble(display.getEntryDisplayText());
            }
        });

        createButton(memoryPad, "M-", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = memory - Double.parseDouble(display.getEntryDisplayText());
            }
        });

        createButton(memoryPad, "MC", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = 0;
            }
        });

        //left arrow button
        createButton(advancedOperatorPad, "\u2190", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.backSpaceEntryDisplay();
            }
        });

        //square root button
        createButton(advancedOperatorPad, "\u221A", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String sqrtString = MathParser.sqrt(display.getEntryDisplayText());
                display.setEntryDisplay(sqrtString + " ");
            }
        });

         createButton(advancedOperatorPad, "1/x", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String recipString = MathParser.reciproc(display.getEntryDisplayText());
                display.setEntryDisplay(recipString + " ");
            }
        });
        
        createButton(advancedOperatorPad, "%", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String lastEntry = getLastRegex(display.getTopDisplayText(), "(-?\\d+(\\.\\d+)?)(?!.*\\d)");
                display.setMidCalc(Double.parseDouble(lastEntry) * (Double.parseDouble(display.getEntryDisplayText()) * .01) + "");
                display.submitToTopDisplay(display.getEntryDisplayText() + " ");
            }

        });


        //plus-minus sign
        createButton(numberPad, "\u00B1", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (display.getState() == EntryState.ANSWER)
                    display.clearScreens();
                if (display.getState() == EntryState.MID_CALC)
                    display.clearEntryDisplay();

                toggleNegative(display.getEntryDisplayText());
            }

            private void toggleNegative(String entryText) {
                if (entryText.startsWith("-"))
                    display.setEntryDisplay(entryText.substring(1, entryText.length()));
                else
                    display.setEntryDisplay("-" + entryText.trim());
            }
        });

        createButton(operatorPad, "=", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String topText = display.getTopDisplayText();
                if (display.getState() == EntryState.ANSWER)
                    appendLastOperation(topText);
                else if (display.getState() != EntryState.MID_CALC)
                    display.submitToTopDisplay(display.getEntryDisplayText());

                display.setAnswer(solve(display.getTopDisplayText()));
            }

            private void appendLastOperation(String topText) {
                String lastOperation = "";
                Matcher m = Pattern.compile("[*+/-]").matcher(topText);
                while (m.find()) {
                    int start = m.start();
                    lastOperation = topText.substring(m.start());
                }
                display.appendToTopDisplay(lastOperation);
            }
        }
        );

    }

    private String solve(String expr) {
        Double result = null;
        try {
            result = MathParser.parse(expr);
        } catch (Exception ex) {
            display.displayError("ERROR");
            return " ";
        }
        return doubleToString(result);
    }

    private static String doubleToString(Double result) {
        return (result.intValue() == result) ? result.intValue() + "" : result + "";
    }

    private String getLastRegex(String s, String regex) {
        String lastEntry = "";
        Matcher m = Pattern.compile(regex).matcher(s);
        if (m.find())
            lastEntry = m.group();
        return lastEntry;
    }

    private void createButton(JPanel parent, String text, ActionListener listener) {
        JButton button = new JButton(text);
        parent.add(button);
        button.addActionListener(listener);
        button.setFont(new Font(button.getFont().getFontName(), Font.BOLD, 12));
        button.setSize(7, 7);
        buttonMap.put(text, button);
    }

    private void addButtons(){
        String[] buttonNames = {"MC", "MR", "MS", "M+", "M-", "\u2190", "CE", "C", "\u00B1",
            "\u221A", "7", "8", "9", "/", "%", "4", "5", "6", "*",
            "1/x", "1", "2", "3", "-", "=", "blank", "0", ".","+"};
        for(String name: buttonNames){
            if(name.equals("blank"))
                buttonPad.add(new JPanel());
            else
                buttonPad.add(buttonMap.get(name));
        }
    }

    public static void main(String[] args) {
        Calculator calc = new Calculator();
    }

}
