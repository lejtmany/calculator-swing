package com.lejtman;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CalculatorDisplay extends JPanel {

    private final JLabel topDisplay;
    private final JLabel entryDisplay;
    private EntryState state;

    public CalculatorDisplay() {
        topDisplay = new JLabel(" ");
        entryDisplay = new JLabel("0", SwingConstants.RIGHT);
        entryDisplay.setFont(new Font(entryDisplay.getFont().getName(), Font.BOLD, 20));
        topDisplay.setFont(new Font(entryDisplay.getFont().getName(), Font.BOLD, 12));
        this.setLayout(new BorderLayout());
        this.add(topDisplay, BorderLayout.NORTH);
        this.add(entryDisplay, BorderLayout.CENTER);
        this.setVisible(true);
        state = EntryState.ENTRY;
    }

    public void setMidCalc(String s) {
        entryDisplay.setText(s);
        state = EntryState.MID_CALC;
    }

    public void setAnswer(String s) {
        entryDisplay.setText(s);
        state = EntryState.ANSWER;
    }

    public void submitToTopDisplay(String s) {
        if (state == EntryState.ANSWER)
            clearScreens();
        if (s.startsWith("-"))
            s = String.format("(%s)", s.trim());
        addToDisplay(topDisplay, s);
    }

    public void appendToTopDisplay(String text) {
        addToDisplay(topDisplay, text);
    }

    public void submitToEntryDisplay(String s) {
        if (state == EntryState.ANSWER)
            clearScreens();

        else if (state == EntryState.MID_CALC)
            clearEntryDisplay();

        if (entryDisplay.getText().matches("\\s*-?0\\s*"))
            setEntryDisplay(entryDisplay.getText().contains("-") ? "-" : "");

        addToDisplay(entryDisplay, s);
        state = EntryState.ENTRY;
    }

    public void setEntryDisplay(String s) {
        entryDisplay.setText(s);
        state = EntryState.ENTRY;
    }

    private <T extends JLabel> void addToDisplay(T label, String s) {
        label.setText(label.getText() + s);
    }

    public void clearScreens() {
        clearDisplay(topDisplay);
        clearEntryDisplay();
        //  state = EntryState.ENTRY;
    }

    public void clearEntryDisplay() {
        setEntryDisplay("0");
        state = EntryState.ENTRY;
    }

    private void clearDisplay(JLabel label) {
        label.setText(" ");
    }

    public void backSpaceEntryDisplay() {
        if (state != EntryState.ENTRY)
            return;
        String entryText = entryDisplay.getText();
        //check that doesnt go to zero and collapse line
        if (entryText.length() <= 1)
            setMidCalc("0");
        else
            entryDisplay.setText(entryText.substring(0, entryText.length() - 1));
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
        state = EntryState.ENTRY;
    }

    public EntryState getState() {
        return state;
    }

}
