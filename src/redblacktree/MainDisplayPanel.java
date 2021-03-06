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

        treePanel.setSpeed(controlPanel.MAX_SPEED - controlPanel.speedControl.getValue());

        add(treePanel);
        add(controlPanel);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == controlPanel.inputField || ae.getSource() == controlPanel.addButton) {

            String input = controlPanel.inputField.getText();

            controlPanel.inputField.setText("");

            if (input.matches(NUMERIC_REGEX_PATTERN) && input.length() <= MAX_LENGTH) {
                new Thread(new NodeAdder(Integer.parseInt(input)), NodeAdder.ADDER_NAME).start();
            }
        }

        if (ae.getSource() == controlPanel.searchButton) {

            String input = controlPanel.inputField.getText();

            controlPanel.inputField.setText("");

            if (input.matches(NUMERIC_REGEX_PATTERN) && input.length() <= MAX_LENGTH) {
                new Thread(new NodeFinder(Integer.parseInt(input)), NodeFinder.FINDER_NAME).start();
            }
        }

        if (ae.getSource() == controlPanel.rmButton) {

            String input = controlPanel.inputField.getText();

            controlPanel.inputField.setText("");

            if (input.matches(NUMERIC_REGEX_PATTERN) && input.length() <= MAX_LENGTH) {
                new Thread(new NodeRemover(Integer.parseInt(input)), NodeRemover.REMOVER_NAME).start();
            }
        }

        if (ae.getSource() == controlPanel.clearButton) {

            Thread c = Thread.currentThread();

            Thread[] t = new Thread[c.getThreadGroup().activeCount()];

            c.getThreadGroup().enumerate(t);

            for (Thread x : t) { /*simply return if there are other Threads queued or executing operations on the tree*/

                if (x.getName().equals(NodeAdder.ADDER_NAME)
                        || x.getName().equals(NodeRemover.REMOVER_NAME)
                        || x.getName().equals(NodeFinder.FINDER_NAME)) {

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
                treePanel.setSpeed(controlPanel.MAX_SPEED - source.getValue());
            }
        }
    }

    private class NodeAdder implements Runnable {

        static final String ADDER_NAME = "Node-Insert";
        int i;

        public NodeAdder(int i) {
            this.i = i;
        }

        @Override
        public void run() {

            if (treePanel.insert(new Integer(i))) {/*do nothing if the integer cannot be inserted due to over-capacity of tree*/

                values.add(new Integer(i));

                String output = values.toString();

                controlPanel.outputArea.setText(output.substring(1, output.length() - 1));
            }
        }
    }

    private class NodeRemover implements Runnable {

        static final String REMOVER_NAME = "Node-Delete";
        int i;

        public NodeRemover(int i) {
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

    private class NodeFinder implements Runnable {

        static final String FINDER_NAME = "Node-Find";
        int i;

        public NodeFinder(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            treePanel.find(new Integer(i));
        }
    }
}