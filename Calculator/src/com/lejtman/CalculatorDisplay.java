package com.lejtman;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CalculatorDisplay extends JPanel {

    private final JLabel topDisplay;
    private final JLabel entryDisplay;
    private boolean isMidCalc;
    private boolean isAnswer;

    public CalculatorDisplay() {
        topDisplay = new JLabel(" ");
        entryDisplay = new JLabel(" ");
        this.setLayout(new BorderLayout());
        this.add(topDisplay, BorderLayout.NORTH);
        this.add(entryDisplay, BorderLayout.CENTER);
        this.setVisible(true);
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
        if (isAnswer) {
            clearScreens();
        }
        addToDisplay(topDisplay, s);
    }

    public void submitToEntryDisplay(String s) {
        if (isAnswer()) {
            clearScreens();
            isAnswer = false;
        } else if (isMidCalc()) {
            clearEntryDisplay();
            isMidCalc = false;
        }
        addToDisplay(entryDisplay, s);
    }

    public void setEntryDisplay(String s) {
        entryDisplay.setText(s);
        isAnswer = false;
        isMidCalc = false;
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

    public void clearEntryDisplay() {
        clearDisplay(entryDisplay);
        isMidCalc = false;
        isAnswer = false;
    }

    private void clearDisplay(JLabel label) {
        label.setText(" ");
    }

    public void backSpaceEntryDisplay() {
        if (isMidCalc || isAnswer) {
            return;
        }
        String entryText = entryDisplay.getText();
        //check that doesnt go to zero and collapse line
        if (entryText.length() <= 1) {
            entryDisplay.setText(" ");
        } else {
            entryDisplay.setText(entryText.substring(0, entryText.length() - 1));
        }
    }

    public String getEntryDisplayText() {
        return entryDisplay.getText();
    }

    public String getTopDisplayText() {
        return topDisplay.getText();
    }

    public void displayError(String errorMessage) {
        clearScreens();
        topDisplay.setText(errorMessage);
        isAnswer = true;
    }

    public boolean isMidCalc() {
        return isMidCalc;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

}
