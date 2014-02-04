/**
 * @author John Paul Smith
 *
 */
package splaytreeapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;

public class ControlPanel extends JPanel {

    final int MAX_SPEED_VAL = 1000,
            MIN_SPEED_VAL = 0;
    JPanel buttonPanel, outputPanel;
    JButton addButton, rmButton, clearButton, searchButton, splayButton;
    JSlider speedControl;
    JTextField inputField;
    JTextArea outputArea;
    JLabel valueLabel, slowLabel, fastLabel, valuesLabel;

    public ControlPanel() {

        addButton = new JButton("Add");
        rmButton = new JButton("Delete");
        searchButton = new JButton("Find");
        clearButton = new JButton("Clear");
        splayButton = new JButton("Splay");

        speedControl = new JSlider(JSlider.HORIZONTAL, MIN_SPEED_VAL, (int) (MAX_SPEED_VAL * .9), 400);
        speedControl.setOpaque(false);

        inputField = new JTextField(5);

        outputArea = new JTextArea();
        outputArea.setBackground(new Color(210, 210, 210));
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        outputArea.setBorder(null);

        valueLabel = new JLabel("Value");
        slowLabel = new JLabel("Slower");
        fastLabel = new JLabel("Faster");
        valuesLabel = new JLabel("Values in tree:");

        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(210, 210, 210));

        buttonPanel.add(valueLabel);
        buttonPanel.add(inputField);
        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(rmButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(slowLabel);
        buttonPanel.add(speedControl);
        buttonPanel.add(fastLabel);
        buttonPanel.add(splayButton);

        outputPanel = new JPanel();
        outputPanel.setBackground(new Color(210, 210, 210));

        outputPanel.add(valuesLabel);
        outputPanel.add(outputArea);

        setLayout(new BorderLayout());

        add(buttonPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);

        setPreferredSize(new Dimension(1024, 80));
        setBorder(BorderFactory.createRaisedSoftBevelBorder());
    }
}
