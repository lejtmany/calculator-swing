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
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author lejtman
 */
public class Calculator extends JFrame {

    //private final JLabel topDisplay, entryDisplay;
    private final JPanel displayPanel, buttonPad;
    private String memory;
   // private boolean entryIsAnswer, entryIsMidCalc;
    private CalendarDisplay display;

    public Calculator() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 600);
        this.setLayout(new BorderLayout());
        displayPanel = new JPanel(new BorderLayout());
       // topDisplay = new JLabel(" ");
    //    entryDisplay = new JLabel(" ");
        buttonPad = new JPanel();
        addButtons();
        buttonPad.setLayout(new GridLayout(5, 5));
      //  displayPanel.add(topDisplay, BorderLayout.NORTH);
       // displayPanel.add(entryDisplay, BorderLayout.CENTER);
        this.add(displayPanel, BorderLayout.NORTH);
        this.add(buttonPad, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
        display = new CalendarDisplay();
        displayPanel.add(display, BorderLayout.NORTH);
    }

    private void addButtons() {
        //operator buttons
        String[] operators = {"+", "-", "*", "/"};
        for (String operator : operators) {
            addButton(operator, new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (entryIsMidCalc) {
                        topDisplay.setText(topDisplay.getText() + " " + e.getActionCommand());
                        entryIsMidCalc = false;
                    } else if (entryIsAnswer) {
                        topDisplay.setText(entryDisplay.getText() + " " + e.getActionCommand());
                        entryIsAnswer = false;
                    } else {
                        topDisplay.setText(topDisplay.getText() + " " + entryDisplay.getText() + " " + e.getActionCommand());
                    }
                    entryDisplay.setText(" ");
                }
            });
        }

        //numeric buttons
        for (int i = 0; i < 10; i++) {
            addButton(i + "", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    display.addToEntryDisplay(e.getActionCommand());
                }
            });
        }

        addButton(".", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {               
                String entryText = display.getEntryText();
                if (!entryText.contains(".")) {
                    display.addToEntryDisplay(".");
                }
            }
        });

        addButton("C", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                display.clearScreens();
                memory = "";
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
                memory = display.getEntryText();
            }
        });

        addButton("MR", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DecimalFormat df = new DecimalFormat("#");
                display.addToEntryDisplay(df.format(Double.parseDouble(memory)));
            }
        });

        addButton("M+", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = Double.parseDouble(memory) + Double.parseDouble(display.getEntryText()) + "";
            }
        });

        addButton("M-", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                memory = Double.parseDouble(memory) - Double.parseDouble(display.getEntryText()) + "";
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
                String entryText = display.getEntryText();
                display.setEntryDisplay(entryText.substring(0, entryText.length() - 1));
            }
        });

        //square root button
        addButton("\u221A", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (entryIsAnswer) {
                    topDisplay.setText(" ");
                    entryIsAnswer = false;
                }
                String sqrtString = MathExpressionParser.sqrt(entryDisplay.getText());
                topDisplay.setText(String.format("%s %s", topDisplay.getText(), sqrtString));
                entryDisplay.setText(MathExpressionParser.parse(sqrtString) + "");
                entryIsMidCalc = true;
            }
        });

        addButton("%", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String lastEntry = "";
                Matcher m = Pattern.compile("(-?\\d+(\\.\\d+)?)(?!.*\\d)").matcher(topDisplay.getText());
                if (m.find()) {
                    lastEntry = m.group();
                }
                entryDisplay.setText(Double.parseDouble(lastEntry) * (Double.parseDouble(entryDisplay.getText()) * .01) + "");
                topDisplay.setText(topDisplay.getText() + " " + entryDisplay.getText());
                entryIsMidCalc = true;
            }
        });

        addButton("1/x", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (entryIsAnswer) {
                    topDisplay.setText(" ");
                    entryIsAnswer = false;
                }
                String recipString = MathExpressionParser.reciproc(entryDisplay.getText());
                topDisplay.setText(String.format("%s %s", topDisplay.getText(), recipString));
                entryDisplay.setText(MathExpressionParser.parse(recipString) + "");
                entryIsMidCalc = true;
            }
        });

        //plus-minus sign
        addButton("\u00B1", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (entryIsAnswer) {
                    topDisplay.setText(" ");
                    entryDisplay.setText(" ");
                    entryIsAnswer = false;
                }
                if (entryIsMidCalc) {
                    entryDisplay.setText(" ");
                    entryIsMidCalc = false;
                }
                String entryText = entryDisplay.getText();
                if (entryText.startsWith(MathExpressionParser.NEGATIVE_SIGN)) {
                    entryDisplay.setText(entryText.substring(1, entryText.length()));
                } else {
                    entryDisplay.setText(MathExpressionParser.NEGATIVE_SIGN + entryText.trim());
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
                } catch (Exception ex) {
                    topDisplay.setText("ERROR");
                    entryDisplay.setText(" ");
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

    private class CalendarDisplay extends JPanel{

        private final JLabel topDisplay;
        private final JLabel entryDisplay;
        private boolean isMidCalc;
        private boolean isAnswer;

        public CalendarDisplay() {
            topDisplay = new JLabel(" ");
            entryDisplay = new JLabel(" ");
            isMidCalc = false;
            isAnswer = false;
        }

        public void setMidCalc(String s) {
            entryDisplay.setText(s);
            isMidCalc = true;
        }

        public void setAnswer(String s) {
            entryDisplay.setText(s);
            isAnswer = true;
        }

        public void addToTopDisplay(String s) {
            addToDisplay(topDisplay, s);
        }

        public void addToEntryDisplay(String s) {
           if(isAnswer){
               clearScreens();
               isAnswer = false;
           }
           else if(isMidCalc){
               clearEntryDisplay();
           }
            addToDisplay(entryDisplay, s);
        }
        
        public void setEntryDisplay(String s){
            entryDisplay.setText(s);
        }

        private <T extends JLabel> void addToDisplay(T label, String s) {
            label.setText(label.getText() + s);
        }

        public void clearScreens() {
            clearDisplay(topDisplay);
            clearEntryDisplay();
            isMidCalc = false;
            isAnswer = false;
        }
        
        public void clearEntryDisplay(){
            clearDisplay(entryDisplay);
            isMidCalc = false;
        }
                      
        private void clearDisplay(JLabel label){
            label.setText(" ");
        }

        public void backSpaceEntryDisplay() {
            String entryText = entryDisplay.getText();
            entryDisplay.setText(entryText.substring(0, entryText.length() - 1));
        }
        
        public String getEntryText(){
            return entryDisplay.getText();
        }

    }

   
    public static void main(String[] args) {
        Calculator calc = new Calculator();
    }

}
