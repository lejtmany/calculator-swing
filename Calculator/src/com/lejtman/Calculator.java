//Yosef Shalom Lejtman
package com.lejtman;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Calculator extends JFrame {

    private final JPanel buttonPad, numberPad, operatorPad, advancedOperatorPad, memoryPad;
    private double memory;
    private final CalculatorDisplay display;
    ButtonGridAdder buttonAdder; 

    public Calculator() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        buttonAdder = new ButtonGridAdder(5, 6);
        buttonPad = new JPanel(new BorderLayout());
        operatorPad = new JPanel(new GridLayout(5, 1));
        advancedOperatorPad = new JPanel(new GridLayout(6, 1));
        memoryPad = new JPanel(new GridLayout(1, 4));
        numberPad = new JPanel(new GridLayout(4, 3));
        display = new CalculatorDisplay();

        addButtons();
        buttonAdder.addButtonsTo(buttonPad);

        buttonPad.add(numberPad, BorderLayout.WEST);
        buttonPad.add(operatorPad, BorderLayout.EAST);
        buttonPad.add(advancedOperatorPad, BorderLayout.CENTER);
        buttonPad.add(memoryPad, BorderLayout.NORTH);

        this.add(display, BorderLayout.NORTH);
        this.add(buttonPad, BorderLayout.CENTER);
        
        this.pack();
        this.setSize(250, 300);       
        this.setVisible(true);
    }

    private void addButtons() {
        //operator buttons
        Point[] operatorCoordinates = {new Point(3,2), new Point(3,3),new Point(3,4),new Point(3,5)};
        String[] operators = {"+", "-", "*", "/"};
        for (int i = 0; i < operators.length; i++) {
            addButton(operatorPad, operators[i], new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    //don't allow 2 operators in a row
                    if (display.getState() == EntryState.MID_CALC)
                        return;

                    display.submitToTopDisplay(display.getEntryDisplayText());

                    //calculate what we have so far
                    display.setMidCalc(solve(display.getTopDisplayText()));

                    display.submitToTopDisplay(" " + e.getActionCommand() + " ");
                }
            }, operatorCoordinates[i]);
        }

        
        
        //numeric buttons
        Point[] numberCoordinates = {new Point(0,5), new Point(0,4),new Point(1,4),new Point(2,4),new Point(0,3),new Point(1,3),new Point(2,3),new Point(0,2),new Point(1,2),new Point(2,2)};
        for (int i = 0; i < 10; i++) {
            addButton(numberPad, i + "", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    display.submitToEntryDisplay(e.getActionCommand());
                }
            }, numberCoordinates[i]);
        }

        addButton(numberPad, ".", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String entryText = display.getEntryDisplayText();
                if (!entryText.contains(".") || display.getState() != EntryState.ENTRY)
                    display.submitToEntryDisplay(".");
            }
        },new Point(5,2));

        addButton(advancedOperatorPad, "C", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.clearScreens();
                memory = 0;
            }
        },new Point(2,1));

        addButton(advancedOperatorPad, "CE", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.clearEntryDisplay();
            }
        },new Point(1,1));

        addButton(memoryPad, "MS", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = Double.parseDouble(display.getEntryDisplayText());
            }
        },new Point(2,0));

        addButton(memoryPad, "MR", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.setEntryDisplay(doubleToString(memory));
            }
        },new Point(1,0));

        addButton(memoryPad, "M+", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = memory + Double.parseDouble(display.getEntryDisplayText());
            }
        },new Point(3,0));

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
        },new Point(4,0));

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
                display.setEntryDisplay(sqrtString + " ");
            }
        });

         addButton(advancedOperatorPad, "1/x", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String recipString = MathParser.reciproc(display.getEntryDisplayText());
                display.setEntryDisplay(recipString + " ");
            }
        });
        
        addButton(advancedOperatorPad, "%", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String lastEntry = getLastRegex(display.getTopDisplayText(), "(-?\\d+(\\.\\d+)?)(?!.*\\d)");
                display.setMidCalc(Double.parseDouble(lastEntry) * (Double.parseDouble(display.getEntryDisplayText()) * .01) + "");
                display.submitToTopDisplay(display.getEntryDisplayText() + " ");
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

    private void addButton(JPanel parent, String text, ActionListener listener, int col , int row) {
        JButton button = new JButton(text);
        parent.add(button);
        button.addActionListener(listener);
        buttonAdder.addButton(button, col, row);
    }
    
    private void addButton(JPanel parent, String text, ActionListener listener, Point coordinates){
        addButton(parent, text, listener, coordinates.x, coordinates.y);
    }

    public static void main(String[] args) {
        Calculator calc = new Calculator();
    }

}
