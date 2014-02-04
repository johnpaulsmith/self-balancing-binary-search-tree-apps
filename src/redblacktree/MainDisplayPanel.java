/**
 * @author John Paul Smith
 *
 */

package redblacktree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainDisplayPanel extends JPanel implements ActionListener, ChangeListener {

    final String NUMERIC_REGEX_PATTERN = "[0-9]+?";/*match integer-only Strings*/
    final int MAX_LENGTH = 3;/*length of input string, ie. 3-digit integers or less*/

    RedBlackTreePanel treePanel;
    ControlPanel controlPanel;
    ArrayList<Integer> values;

    public MainDisplayPanel() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        values = new ArrayList<>();

        treePanel = new RedBlackTreePanel();
        controlPanel = new ControlPanel();

        controlPanel.inputField.addActionListener(this);
        controlPanel.addButton.addActionListener(this);
        controlPanel.searchButton.addActionListener(this);
        controlPanel.rmButton.addActionListener(this);
        controlPanel.clearButton.addActionListener(this);

        controlPanel.speedControl.addChangeListener(this);

        treePanel.setSpeed(controlPanel.MAX_SPEED_VAL - controlPanel.speedControl.getValue());

        add(treePanel);
        add(controlPanel);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == controlPanel.inputField || ae.getSource() == controlPanel.addButton) {

            String input = controlPanel.inputField.getText();

            controlPanel.inputField.setText("");

            if (input.matches(NUMERIC_REGEX_PATTERN) && input.length() <= MAX_LENGTH) {
                new Adder(Integer.parseInt(input)).start();
            }
        }

        if (ae.getSource() == controlPanel.searchButton) {

            String input = controlPanel.inputField.getText();

            controlPanel.inputField.setText("");

            if (input.matches(NUMERIC_REGEX_PATTERN) && input.length() <= MAX_LENGTH) {
                new Finder(Integer.parseInt(input)).start();
            }
        }

        if (ae.getSource() == controlPanel.rmButton) {

            String input = controlPanel.inputField.getText();

            controlPanel.inputField.setText("");

            if (input.matches(NUMERIC_REGEX_PATTERN) && input.length() <= MAX_LENGTH) {
                new Remover(Integer.parseInt(input)).start();
            }
        }

        if (ae.getSource() == controlPanel.clearButton) {

            Thread c = Thread.currentThread();

            Thread[] t = new Thread[c.getThreadGroup().activeCount()];

            c.getThreadGroup().enumerate(t);

            for (Thread x : t) { /*simply return if there are other Threads queued or executing operations on the tree*/
                if (x instanceof Adder || x instanceof Remover || x instanceof Finder) {
                    return;
                }
            }

            treePanel.clearTree();
            values.clear();
            controlPanel.outputArea.setText("");
        }
    }

    @Override
    public void stateChanged(ChangeEvent ce) {

        if (ce.getSource() == controlPanel.speedControl) {

            JSlider source = (JSlider) ce.getSource();

            if (!source.getValueIsAdjusting()) {
                treePanel.setSpeed(controlPanel.MAX_SPEED_VAL - source.getValue());
            }
        }
    }

    private class Adder extends Thread {

        int i;

        public Adder(int i) {

            super("RB-Tree-Insert");

            this.i = i;
        }

        @Override
        public void run() {

            if (treePanel.insert(new Integer(i))) {/*do nothing if the integer cannot be inserted (due to over-capacity of tree)*/

                values.add(new Integer(i));

                String output = values.toString();

                controlPanel.outputArea.setText(output.substring(1, output.length() - 1));
            }
        }
    }

    private class Remover extends Thread {

        int i;

        public Remover(int i) {

            super("RB-Tree-Delete");

            this.i = i;
        }

        @Override
        public void run() {

            treePanel.remove(new Integer(i));

            values.remove(new Integer(i));

            String output = values.toString();

            controlPanel.outputArea.setText(output.substring(1, output.length() - 1));
        }
    }

    private class Finder extends Thread {

        int i;

        public Finder(int i) {

            super("RB-Tree-Find");

            this.i = i;
        }

        @Override
        public void run() {
            treePanel.redBlackFind(new Integer(i));
        }
    }
}
