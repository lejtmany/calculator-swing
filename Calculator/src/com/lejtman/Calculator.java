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

/**
 *
 * @author lejtman
 */
public class Calculator extends JFrame {

    private final JPanel buttonPad;
    private String memory, lastOperation;
    private final CalculatorDisplay display;

    public Calculator() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 600);
        this.setLayout(new BorderLayout());
        buttonPad = new JPanel();
        addButtons();
        buttonPad.setLayout(new GridLayout(5, 5));
        display = new CalculatorDisplay();
        this.add(display, BorderLayout.NORTH);
        this.add(buttonPad, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    private void addButtons() {
        //operator buttons
        String[] operators = {"+", "-", "*", "/"};
        for (String operator : operators) {
            addButton(operator, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    // String tdText = display.getTopDisplayText();
                    if (!display.isMidCalc()) {
                        display.addToTopDisplay(display.getEntryDisplayText() + " ");
                    }

                    try {
                        if (!display.getTopDisplayText().trim().isEmpty()) {
                            display.setMidCalc(solve(display.getTopDisplayText()));
                        }
                    } catch (Exception ex) {
                        display.displayError("ERROR");
                    }

                    display.addToTopDisplay(" " + e.getActionCommand());
                }
            });
        }

        //numeric buttons
        for (int i = 0; i < 10; i++) {
            addButton(i + "", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    display.submitToEntryDisplay(e.getActionCommand());
                }
            });
        }

        addButton(".", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String entryText = display.getEntryDisplayText();
                if (!entryText.contains(".") || display.isAnswer() || display.isMidCalc()) {
                    display.submitToEntryDisplay(".");
                }
            }
        });

        addButton("C", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.clearScreens();
                memory = "";
                lastOperation = "";
            }
        });

        addButton("CE", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.clearEntryDisplay();
            }
        });

        addButton("MS", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = display.getEntryDisplayText();
            }
        });

        addButton("MR", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DecimalFormat df = new DecimalFormat("#");
                display.submitToEntryDisplay(df.format(Double.parseDouble(memory)));
            }
        });

        addButton("M+", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = Double.parseDouble(memory) + Double.parseDouble(display.getEntryDisplayText()) + "";
            }
        });

        addButton("M-", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = Double.parseDouble(memory) - Double.parseDouble(display.getEntryDisplayText()) + "";
            }
        });

        addButton("MC", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = "";
            }
        });

        //left arrow button
        addButton("\u2190", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.backSpaceEntryDisplay();
            }
        });

        //square root button
        addButton("\u221A", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String sqrtString = MathExpressionParser.sqrt(display.getEntryDisplayText());
                display.addToTopDisplay(String.format(sqrtString));
                display.setMidCalc(MathExpressionParser.parse(sqrtString) + "");
            }
        });

        addButton("%", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String lastEntry = "";
                Matcher m = Pattern.compile("(-?\\d+(\\.\\d+)?)(?!.*\\d)").matcher(display.getTopDisplayText());
                if (m.find()) {
                    lastEntry = m.group();
                }
                display.setMidCalc(Double.parseDouble(lastEntry) * (Double.parseDouble(display.getEntryDisplayText()) * .01) + "");
                display.addToTopDisplay(display.getEntryDisplayText());
            }
        });

        addButton("1/x", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String recipString = MathExpressionParser.reciproc(display.getEntryDisplayText());
                display.addToTopDisplay(recipString);
                display.setMidCalc(MathExpressionParser.parse(recipString) + "");
            }
        });

        //plus-minus sign
        addButton("\u00B1", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String entryText = display.getEntryDisplayText();
                if (display.isAnswer()) {
                    display.clearScreens();
                }
                if (display.isMidCalc()) {
                    display.clearEntryDisplay();
                }
                if (entryText.startsWith(MathExpressionParser.NEGATIVE_SIGN)) {
                    display.setEntryDisplay(entryText.substring(1, entryText.length()));
                } else {
                    display.setEntryDisplay(MathExpressionParser.NEGATIVE_SIGN + entryText.trim());
                }
            }
        });

        addButton("=", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String topText = display.getTopDisplayText();
                if (display.isAnswer()) {
                    Matcher m = Pattern.compile("[*+/-]").matcher(topText);
                    while (m.find()) {
                        int start = m.start();
                        lastOperation = topText.substring(m.start());
                    }
                    display.addToTopDisplay(lastOperation);
                } else {
                    display.addToTopDisplay(display.getEntryDisplayText());
                }
                try {
                    display.setAnswer(solve(display.getTopDisplayText()));
                } catch (Exception ex) {
                    display.displayError("ERROR");
                }
            }
        }
        );

    }

    private String solve(String expr) {
        Double result = MathExpressionParser.parse(expr);
        return (result.intValue() == result) ? result.intValue() + "" : result + "";
    }

    private void addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        buttonPad.add(button);
        button.addActionListener(listener);
    }

    public static void main(String[] args) {
        Calculator calc = new Calculator();
    }

}
