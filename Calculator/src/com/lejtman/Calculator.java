//Yosef Shalom Lejtman
package com.lejtman;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author lejtman
 */
public class Calculator extends JFrame {

    private JLabel topDisplay, entryDisplay;
    private JPanel displayPanel, buttonPad;
    private String memory, lastEntry;
    private boolean entryIsAnswer;

    public Calculator() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 600);
        this.setLayout(new BorderLayout());
        displayPanel = new JPanel(new BorderLayout());
        topDisplay = new JLabel(" ");
        entryDisplay = new JLabel(" ");
        buttonPad = new JPanel();
        addButtons();
        buttonPad.setLayout(new GridLayout(5, 5));
        displayPanel.add(topDisplay, BorderLayout.NORTH);
        displayPanel.add(entryDisplay, BorderLayout.CENTER);
        this.add(displayPanel, BorderLayout.NORTH);
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
                    if (!entryIsAnswer) {
                        topDisplay.setText(topDisplay.getText() + " " + entryDisplay.getText() + " " + e.getActionCommand());
                    }
                    if (entryIsAnswer) {
                        topDisplay.setText(entryDisplay.getText() + " " + e.getActionCommand());
                        entryIsAnswer = false;
                    }
                    lastEntry = entryDisplay.getText();
                    entryDisplay.setText(" ");
                }
            });
        }

        //numeric buttons
        for (int i = 0; i < 10; i++) {
            addButton(i + "", new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (entryIsAnswer) {
                        entryDisplay.setText(" ");
                        topDisplay.setText(" ");
                        entryIsAnswer = false;
                    }
                    entryDisplay.setText(entryDisplay.getText() + e.getActionCommand());
                }
            });
        }

        addButton(".", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (entryIsAnswer) {
                    entryDisplay.setText(" ");
                    topDisplay.setText(" ");
                    entryIsAnswer = false;
                }
                String entryText = entryDisplay.getText();
                if (!entryText.contains(".")) {
                    entryDisplay.setText(entryText + ".");
                }
            }
        });

        addButton("C", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                topDisplay.setText(" ");
                entryDisplay.setText(" ");
                memory = "";
                lastEntry = "";
            }
        });

        addButton("CE", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                entryDisplay.setText(" ");
            }
        });

        addButton("MS", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = entryDisplay.getText();
            }
        });

        addButton("MR", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DecimalFormat df = new DecimalFormat("#");
                entryDisplay.setText(df.format(Double.parseDouble(memory)));
            }
        });

        addButton("M+", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = Double.parseDouble(memory) + Double.parseDouble(entryDisplay.getText()) + "";
            }
        });

        addButton("M-", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = Double.parseDouble(memory) - Double.parseDouble(entryDisplay.getText()) + "";
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
                String entryText = entryDisplay.getText();
                entryDisplay.setText(entryText.substring(0, entryText.length() - 1));
            }
        });

        //square root button
        addButton("\u221A", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                topDisplay.setText(String.format("%s %s", topDisplay.getText(), MathExpressionParser.sqrt(entryDisplay.getText())));
                entryDisplay.setText(Math.sqrt(Double.parseDouble(entryDisplay.getText())) + "");
                lastEntry = entryDisplay.getText();
                entryIsAnswer = true;
            }
        });

        addButton("%", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                entryDisplay.setText(Double.parseDouble(lastEntry) * (Double.parseDouble(entryDisplay.getText()) * .01) + "");
                entryIsAnswer = true;
            }
        });

        addButton("1/x", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                topDisplay.setText(String.format("%s %s", topDisplay.getText(), MathExpressionParser.reciproc(entryDisplay.getText())));
                entryDisplay.setText(1 / Double.parseDouble(entryDisplay.getText()) + "");
                entryIsAnswer = true;
            }
        });

        //plus-minus sign
        addButton("\u00B1", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String entryText = entryDisplay.getText();
                if (entryText.trim().isEmpty()) {
                    return;
                }
                if (entryText.startsWith(MathExpressionParser.NEGATIVE_SIGN)) {
                    entryDisplay.setText(entryText.substring(1, entryText.length()));
                } else {
                    entryDisplay.setText(MathExpressionParser.NEGATIVE_SIGN + entryText);
                }
            }
        });

        addButton("=", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                topDisplay.setText(topDisplay.getText() + " " + entryDisplay.getText());
                try {
                    Double result = MathExpressionParser.parse(topDisplay.getText());
                    entryDisplay.setText("" + ((result.intValue() == result) ? result.intValue() + "" : result));
                } catch (IllegalArgumentException ex) {
                    topDisplay.setText(ex.getMessage());
                } finally {
                    entryIsAnswer = true;
                }
            }
        }
        );

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
