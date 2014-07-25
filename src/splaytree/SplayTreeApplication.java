/**
 * @author John Paul Smith
 *
 */
package splaytree;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class SplayTreeApplication extends JFrame {

    public SplayTreeApplication() {

        super("Splay Tree");

        add(new MainDisplayPanel());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
    }

    public static void main(String[] args) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

                if (info.getName().equals("Nimbus")) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            /**
             * Do nothing if Nimbus is unavailable. The UIManager will use the
             * cross-platform default ("Metal") in this case.
             */
        }

        new SplayTreeApplication().setVisible(true);
    }
}