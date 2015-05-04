//Yosef Shalom Lejtman
package com.lejtman;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Calculator extends JFrame {

    private final JPanel buttonPad, numberPad, operatorPad, advancedOperatorPad, memoryPad;
    private double memory;
    private final CalculatorDisplay display;

    public Calculator() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 500);
        this.setLayout(new BorderLayout());

        buttonPad = new JPanel(new BorderLayout());
        operatorPad = new JPanel(new GridLayout(5, 1));
        advancedOperatorPad = new JPanel(new GridLayout(6, 1));
        memoryPad = new JPanel(new GridLayout(1, 4));
        numberPad = new JPanel(new GridLayout(4, 3));
        display = new CalculatorDisplay();

        addButtons();

        buttonPad.add(numberPad, BorderLayout.CENTER);
        buttonPad.add(operatorPad, BorderLayout.EAST);
        buttonPad.add(advancedOperatorPad, BorderLayout.WEST);
        buttonPad.add(memoryPad, BorderLayout.NORTH);

        this.add(display, BorderLayout.NORTH);
        this.add(buttonPad, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    private void addButtons() {
        //operator buttons
        String[] operators = {"+", "-", "*", "/"};
        for (String operator : operators) {
            addButton(operatorPad, operator, new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (display.getState() != EntryState.MID_CALC)
                        display.submitToTopDisplay(display.getEntryDisplayText());

                    if (!display.getTopDisplayText().trim().isEmpty())
                        display.setMidCalc(solve(display.getTopDisplayText()));

                    display.submitToTopDisplay(" " + e.getActionCommand() + " ");
                }
            });
        }

        //numeric buttons
        for (int i = 0; i < 10; i++) {
            addButton(numberPad, i + "", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    display.submitToEntryDisplay(e.getActionCommand());
                }
            });
        }

        addButton(numberPad, ".", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String entryText = display.getEntryDisplayText();
                if (!entryText.contains(".") || display.getState() != EntryState.ENTRY)
                    display.submitToEntryDisplay(".");
            }
        });

        addButton(advancedOperatorPad, "C", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.clearScreens();
                memory = 0;
            }
        });

        addButton(advancedOperatorPad, "CE", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.clearEntryDisplay();
            }
        });

        addButton(memoryPad, "MS", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = Double.parseDouble(display.getEntryDisplayText());
            }
        });

        addButton(memoryPad, "MR", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.setEntryDisplay(doubleToString(memory));
            }
        });

        addButton(memoryPad, "M+", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = memory + Double.parseDouble(display.getEntryDisplayText());
            }
        });

        addButton(memoryPad, "M-", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = memory - Double.parseDouble(display.getEntryDisplayText());
            }
        });

        addButton(memoryPad, "MC", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = 0;
            }
        });

        //left arrow button
        addButton(advancedOperatorPad, "\u2190", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.backSpaceEntryDisplay();
            }
        });

        //square root button
        addButton(advancedOperatorPad, "\u221A", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String sqrtString = MathParser.sqrt(display.getEntryDisplayText());
                display.submitToTopDisplay(String.format(sqrtString) + " ");
                display.setMidCalc(MathParser.parse(sqrtString) + "");
            }
        });

        addButton(advancedOperatorPad, "%", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String lastEntry = "";
                Matcher m = Pattern.compile("(-?\\d+(\\.\\d+)?)(?!.*\\d)").matcher(display.getTopDisplayText());
                if (m.find())
                    lastEntry = m.group();
                display.setMidCalc(Double.parseDouble(lastEntry) * (Double.parseDouble(display.getEntryDisplayText()) * .01) + "");
                display.submitToTopDisplay(display.getEntryDisplayText() + " ");
            }
        });

        addButton(advancedOperatorPad, "1/x", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String recipString = MathParser.reciproc(display.getEntryDisplayText());
                display.submitToTopDisplay(recipString);
                display.setMidCalc(MathParser.parse(recipString) + "");
            }
        });

        //plus-minus sign
        addButton(numberPad, "\u00B1", new ActionListener() {

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

        addButton(operatorPad, "=", new ActionListener() {

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
                Matcher m = Pattern.compile("[*+/-]").matcher(topText);
                String lastOperation = "";
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

    private void addButton(JPanel parent, String text, ActionListener listener) {
        JButton button = new JButton(text);
        parent.add(button);
        button.addActionListener(listener);
    }

    public static void main(String[] args) {
        Calculator calc = new Calculator();
    }

}
