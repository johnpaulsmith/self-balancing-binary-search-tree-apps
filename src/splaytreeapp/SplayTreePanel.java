/**
 * @author John Paul Smith
 *
 */
package splaytreeapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class SplayTreePanel extends JPanel {

    final int MAX_CAPACITY = 25;
    final int NODE_DIAM = 30;
    final int V_SPACER = (NODE_DIAM * 3);
    final int HIGHLIGHT_DELAY = 1000;
    final double DELAY_INCREMENT = .1;
    Node root, nil;
    Color nodeColor, opColor;
    ArrayList<Node> nodes;
    double redrawDelay, pauseDelay;
    int count, i, j;
    boolean splayEnabled;

    public SplayTreePanel() {

        setBorder(BorderFactory.createRaisedSoftBevelBorder());
        setPreferredSize(new Dimension(1024, 708));
        setLayout(new BorderLayout());
        setBackground(new Color(210, 210, 210));

        nodeColor = Color.blue;
        opColor = Color.cyan;
        splayEnabled = false;
        root = nil = new Node(null, nodeColor, null, nil, nil);
        count = 0;
        nodes = new ArrayList<>();
        redrawDelay = 4;
        pauseDelay = 320;
    }

    public synchronized boolean insert(Integer e) {

        if (count >= MAX_CAPACITY) {
            return false;
        }

        Node n = new Node(e, opColor, null, nil, nil), y = nil, x = root;

        while (x != nil) {

            y = x;

            if (n.compareTo(x) < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        nodes.add(n);

        n.parent = y;

        double h = (getSize().width / (int) Math.pow(2, depthOf(n) + 1));

        if (y == nil) {
            root = n;
            n.x = (getSize().width / 2);
            n.y = 50;

            repaint();
            pause(pauseDelay);

        } else if (n.compareTo(y) < 0) {

            y.left = n;

            n.x = y.x;
            n.y = y.y;

            for (int c = 0; c < V_SPACER; ++c) {

                n.x -= (h / V_SPACER);
                ++n.y;

                pause(redrawDelay + (DELAY_INCREMENT * c));
                repaint();
            }

            repaint();
            pause(pauseDelay);

        } else {

            y.right = n;

            n.x = y.x;
            n.y = y.y;

            for (int c = 0; c < V_SPACER; ++c) {

                n.x += (h / V_SPACER);
                ++n.y;

                pause(redrawDelay + (DELAY_INCREMENT * c));
                repaint();
            }

            repaint();
            pause(pauseDelay);
        }

        ++count;

        if (splayEnabled) {
            splay(n);
        }

        pause(pauseDelay);

        n.color = nodeColor;

        repaint();
        pause(pauseDelay);

        return true;
    }

    public synchronized Integer remove(Integer e) {

        Node n = splayRemoveFind(e, root);

        return (n != null ? remove(n).t : null);
    }

    private Node splayRemoveFind(Integer e, Node n) {

        Node prev = n;

        while (n != nil) {
            prev = n;

            if (e.compareTo(n.t) == 0) {
                break;
            } else if (e.compareTo(n.t) < 0) {
                n = n.left;
            } else {
                n = n.right;
            }
        }

        if (prev != n) {

            prev.color = opColor;

            repaint();
            pause(HIGHLIGHT_DELAY);

            splay(prev);
            prev.color = nodeColor;

            repaint();
        }

        return (prev == n ? n : null);
    }

    private Node remove(Node z) {

        Node y = z, x, p;

        z.color = opColor;
        repaint();
        pause(pauseDelay);

        if (z.left == nil) {

            pause(pauseDelay);

            p = z.parent;
            x = z.right;
            bypass(z, z.right);

            z.parent = z.left = z.right = null;

            repaint();
            pause(pauseDelay);

            nodes.remove(z);

            repaint();
            pause(pauseDelay);

            if (x != nil) {

                j = 1;

                int deepest = deepest(x, x);

                Node[] A = new Node[(int) Math.pow(2, deepest + 1)];

                subTreeRightSide(x, x, A, deepest);

                double xSpacer = (getSize().width / Math.pow(2, deepest(x, root) + 2));

                for (int c = 0; c < V_SPACER; ++c) {

                    for (int w = 1; w < A.length; ++w) {

                        if (A[w] != null) {
                            updateCoordinates(A[w], -1, -1, (w * xSpacer), V_SPACER);
                        }
                    }

                    pause(redrawDelay + (DELAY_INCREMENT * c));
                    repaint();
                }
            }

            pause(pauseDelay);

            if (p != nil) {

                if (splayEnabled) {

                    p.color = Color.cyan;

                    repaint();
                    pause(pauseDelay);

                    splay(p);

                    p.color = nodeColor;

                    repaint();
                    pause(pauseDelay);
                }
            }

        } else if (z.right == nil) {

            pause(pauseDelay);

            p = z.parent;
            x = z.left;
            bypass(z, z.left);

            z.parent = z.left = z.right = null;

            repaint();
            pause(pauseDelay);

            nodes.remove(z);

            repaint();
            pause(pauseDelay);

            if (x != nil) {

                i = 1;

                int deepest = deepest(x, x);

                Node[] A = new Node[(int) Math.pow(2, deepest + 1)];

                subTreeLeftSide(x, x, A, deepest);

                double xSpacer = (getSize().width / Math.pow(2, deepest(x, root) + 2));

                for (int c = 0; c < V_SPACER; ++c) {

                    for (int w = 1; w < A.length; ++w) {

                        if (A[w] != null) {
                            updateCoordinates(A[w], 1, -1, (w * xSpacer), V_SPACER);
                        }
                    }

                    pause(redrawDelay + (DELAY_INCREMENT * c));
                    repaint();
                }
            }

            pause(pauseDelay);

            if (p != nil) {

                if (splayEnabled) {

                    p.color = Color.cyan;
                    repaint();
                    pause(pauseDelay);

                    splay(p);

                    p.color = nodeColor;
                    repaint();
                    pause(pauseDelay);
                }
            }

        } else {//node to be removed has two children           

            pause(pauseDelay);

            y = min(z.right);
            x = y.right;

            if (y.parent == z) {
                x.parent = y;
            } else {
                bypass(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            bypass(z, y);
            y.left = z.left;
            y.left.parent = y;

            y.x = z.x;
            y.y = z.y;

            z.parent = z.left = z.right = null;

            repaint();
            pause(pauseDelay);

            nodes.remove(z);

            repaint();
            pause(pauseDelay);

            if (x != nil) {

                j = 1;

                int deepest = deepest(x, x);

                Node[] A = new Node[(int) Math.pow(2, deepest + 1)];

                subTreeRightSide(x, x, A, deepest);

                double xSpacer = (getSize().width / Math.pow(2, deepest(x, root) + 2));

                for (int c = 0; c < V_SPACER; ++c) {

                    for (int w = 1; w < A.length; ++w) {

                        if (A[w] != null) {
                            updateCoordinates(A[w], -1, -1, (w * xSpacer), V_SPACER);
                        }
                    }

                    pause(redrawDelay + (DELAY_INCREMENT * c));
                    repaint();
                }
            }

            pause(pauseDelay);

            if (splayEnabled) {

                x.parent.color = opColor;
                repaint();
                pause(HIGHLIGHT_DELAY);

                splay(x.parent);

                pause(HIGHLIGHT_DELAY);
                x.parent.color = nodeColor;
                repaint();
            }
        }

        --count;

        pause(pauseDelay);

        nil.parent = null;

        return z;
    }

    private void bypass(Node x, Node y) {

        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.parent = x.parent;
    }

    private void splay(Node x) {

        while (x != root) {

            if (x.parent.parent == nil) {

                if (x == x.parent.left) {/*"zig" case*/

                    rightRotate(x.parent);

                    repaint();

                    pause(pauseDelay);

                } else {

                    leftRotate(x.parent);

                    repaint();

                    pause(pauseDelay);
                }
            } else {

                if (x.parent == x.parent.parent.left) {

                    if (x == x.parent.left) { /*"zig-zig" case*/

                        rightRotate(x.parent.parent);

                        repaint();
                        pause(pauseDelay);

                        rightRotate(x.parent);

                        repaint();
                        pause(pauseDelay);
                    } else { /*"zig-zag" case*/

                        leftRotate(x.parent);

                        repaint();
                        pause(pauseDelay);

                        rightRotate(x.parent);

                        repaint();
                        pause(pauseDelay);
                    }

                } else {

                    if (x == x.parent.right) {/*"zig-zig" case*/

                        leftRotate(x.parent.parent);

                        repaint();
                        pause(pauseDelay);

                        leftRotate(x.parent);

                        repaint();
                        pause(pauseDelay);
                    } else {/*"zig-zag" case*/

                        rightRotate(x.parent);

                        repaint();
                        pause(pauseDelay);

                        leftRotate(x.parent);

                        repaint();
                        pause(pauseDelay);
                    }
                }
            }
        }
    }

    public synchronized Integer splayFind(Integer e) {

        Node n = splayFind(e, root);

        return (n == null ? null : n.t);
    }

    private Node splayFind(Integer e, Node n) {

        Node prev = n;

        while (n != nil) {

            prev = n;

            if (e.compareTo(n.t) == 0) {
                break;
            } else if (e.compareTo(n.t) < 0) {
                n = n.left;
            } else {
                n = n.right;
            }
        }

        if (splayEnabled) {

            prev.color = opColor;

            repaint();
            pause(HIGHLIGHT_DELAY);

            splay(prev);

        } else if (prev == n) {

            prev.color = opColor;

            repaint();
            pause(HIGHLIGHT_DELAY);
        }

        prev.color = nodeColor;

        repaint();

        return (prev == n ? n : null);
    }

    private void leftRotate(Node x) {

        Node y = x.right;

        x.right = y.left;

        repaint();
        pause(pauseDelay);

        if (y.left != nil) {
            y.left.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        repaint();
        pause(pauseDelay);

        y.left = x;
        x.parent = y;

        repaint();
        pause(pauseDelay);

        double xLeftSpacer = 0,
                yRightSpacer = 0;

        Node[] A, B;

        i = j = 1;

        if (y.right != nil) {

            int yRightDeepest = deepest(y.right, y.right);

            A = new Node[(int) Math.pow(2, yRightDeepest + 1)];

            subTreeRightSide(y.right, y.right, A, yRightDeepest);

            yRightSpacer = (getSize().width / Math.pow(2, deepest(y.right, root) + 2));

        } else {
            A = new Node[0];
        }

        if (x.left != nil) {

            int xLeftDeepest = deepest(x.left, x.left);

            B = new Node[(int) Math.pow(2, xLeftDeepest + 1)];

            subTreeLeftSide(x.left, x.left, B, xLeftDeepest);

            xLeftSpacer = (getSize().width / Math.pow(2, deepest(x.left, root) + 1));

        } else {
            B = new Node[0];
        }

        double xSpacer = (getSize().width / Math.pow(2, depthOf(x) + 1));

        for (int c = 0; c < V_SPACER; ++c) {

            ++x.y;
            x.x -= (xSpacer / V_SPACER);

            --y.y;
            y.x -= (xSpacer / V_SPACER);

            for (int z = 1; z < A.length; ++z) {

                if (A[z] != null) {
                    updateCoordinates(A[z], -1, -1, (z * yRightSpacer), V_SPACER);
                }
            }

            for (int z = 1; z < B.length; ++z) {

                if (B[z] != null) {
                    updateCoordinates(B[z], -1, 1, (z * xLeftSpacer), V_SPACER);
                }
            }

            if (x.right != nil) {
                shiftOver(x.right, -1, getSize().width / Math.pow(2, depthOf(x.right)));
            }

            pause(redrawDelay + (DELAY_INCREMENT * c));
            repaint();
        }
    }

    private void rightRotate(Node x) {

        Node y = x.left;

        x.left = y.right;

        repaint();
        pause(pauseDelay);

        if (y.right != nil) {
            y.right.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        repaint();
        pause(pauseDelay);

        y.right = x;
        x.parent = y;

        repaint();
        pause(pauseDelay);

        double yLeftSpacer = 0,
                xRightSpacer = 0;

        Node[] A, B;

        i = j = 1;

        if (y.left != nil) {

            int yLeftDeepest = deepest(y.left, y.left);

            A = new Node[(int) Math.pow(2, yLeftDeepest + 1)];

            subTreeLeftSide(y.left, y.left, A, yLeftDeepest);

            yLeftSpacer = (getSize().width / Math.pow(2, deepest(y.left, root) + 2));

        } else {
            A = new Node[0];
        }

        if (x.right != nil) {

            int xRightDeepest = deepest(x.right, x.right);

            B = new Node[(int) Math.pow(2, xRightDeepest + 1)];

            subTreeRightSide(x.right, x.right, B, xRightDeepest);

            xRightSpacer = (getSize().width / Math.pow(2, deepest(x.right, root) + 1));

        } else {
            B = new Node[0];
        }

        double xSpacer = (getSize().width / Math.pow(2, depthOf(x) + 1));

        for (int c = 0; c < V_SPACER; ++c) {

            ++x.y;
            x.x += (xSpacer / V_SPACER);

            --y.y;
            y.x += (xSpacer / V_SPACER);

            for (int z = 1; z < A.length; ++z) {

                if (A[z] != null) {
                    updateCoordinates(A[z], 1, -1, (z * yLeftSpacer), V_SPACER);
                }
            }

            for (int z = 1; z < B.length; ++z) {

                if (B[z] != null) {
                    updateCoordinates(B[z], 1, 1, (z * xRightSpacer), V_SPACER);
                }
            }

            if (x.left != nil) {
                shiftOver(x.left, 1, getSize().width / Math.pow(2, depthOf(x.left)));
            }

            pause(redrawDelay + (DELAY_INCREMENT * c));
            repaint();
        }
    }

    private void shiftOver(Node n, int x, double d) {

        n.x += (x * (d / V_SPACER));

        if (n.left != nil) {
            shiftOver(n.left, x, d);
        }
        if (n.right != nil) {
            shiftOver(n.right, x, d);
        }
    }

    public int deepest() {
        return deepest(root, root);
    }

    private int deepest(Node n, Node rt) {

        int l = (n.left != nil ? deepest(n.left, rt) : relativeDepthOf(n, rt));
        int r = (n.right != nil ? deepest(n.right, rt) : relativeDepthOf(n, rt));

        return (l > r ? l : r);
    }

    private void updateCoordinates(Node n, int x, int y, double xSpacer, double ySpacer) {
        n.y += y;
        n.x += (x * (xSpacer / ySpacer));
    }

    private void subTreeLeftSide(Node n, Node r, Node[] A, int deepest) {

        int nDepth = relativeDepthOf(n, r);
        int maxSubTreeSize = (int) Math.pow(2, deepest - nDepth) - 1;

        if (n.left != nil) {
            subTreeLeftSide(n.left, r, A, deepest);
        } else {
            for (int z = 0; z < maxSubTreeSize; ++z) {
                A[i++] = null;
            }
        }

        A[i++] = n;

        if (n.right != nil) {
            subTreeLeftSide(n.right, r, A, deepest);
        } else {
            for (int z = 0; z < maxSubTreeSize; ++z) {
                A[i++] = null;
            }
        }
    }

    private void subTreeRightSide(Node n, Node r, Node[] A, int deepest) {

        int nDepth = relativeDepthOf(n, r);
        int maxSubTreeSize = (int) Math.pow(2, deepest - nDepth) - 1;

        if (n.right != nil) {
            subTreeRightSide(n.right, r, A, deepest);
        } else {
            for (int x = 0; x < maxSubTreeSize; ++x) {
                A[j++] = null;
            }
        }

        A[j++] = n;

        if (n.left != nil) {
            subTreeRightSide(n.left, r, A, deepest);
        } else {
            for (int x = 0; x < maxSubTreeSize; ++x) {
                A[j++] = null;
            }
        }
    }

    public Integer min() {
        return (count > 0 ? min(root).t : null);
    }

    private Node min(Node n) {

        while (n.left != nil) {
            n = n.left;
        }

        return n;
    }

    public Integer max() {
        return (count > 0 ? max(root).t : null);
    }

    private Node max(Node n) {

        while (n.right != nil) {
            n = n.right;
        }

        return n;
    }

    public boolean containsElement(Integer e) {
        return (findKey(e) != null);
    }

    private Node findKey(Integer e) {
        return (count == 0 ? null : findKey(e, root));
    }

    private Node findKey(Integer e, Node n) {

        while (n != nil) {
            if (e.compareTo(n.t) == 0) {
                return n;
            } else if (e.compareTo(n.t) < 0) {
                n = n.left;
            } else {
                n = n.right;
            }
        }

        return null;
    }

    public int sizeOfTree() {
        return count;
    }

    public boolean isEmpty() {
        return (count == 0);
    }

    private int depthOf(Node n) {

        return relativeDepthOf(n, root);
    }

    private int relativeDepthOf(Node n, Node r) {

        int d = 0;

        while (n != r) {
            n = n.parent;
            ++d;
        }

        return d;
    }

    public void clearTree() {

        if (count > 0) {

            postOrderPrune(root);

            root = nil;

            nodes.clear();

            count = 0;

            repaint();
        }
    }

    private void postOrderPrune(Node n) {

        if (n.left != nil) {
            postOrderPrune(n.left);
        }

        if (n.right != nil) {
            postOrderPrune(n.right);
        }

        n.parent = null;
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        for (Node n : nodes) {
            drawEdge(n, g);
        }

        for (Node n : nodes) {
            drawNode(n, g);
        }
    }

    private void drawNode(Node n, Graphics g) {

        int offset = (NODE_DIAM >> 1);

        g.setColor(n.color);
        g.fillOval((int) n.x - offset, (int) n.y - offset, NODE_DIAM, NODE_DIAM);

        g.setColor(Color.white);
        g.drawString(n.t.toString(), (int) n.x - (((n.t.toString().length() * 6) >> 1) + 1), (int) n.y + 6);

        g.setColor(Color.black);
        g.drawOval((int) n.x - offset, (int) n.y - offset, NODE_DIAM, NODE_DIAM);
    }

    private void drawEdge(Node n, Graphics g) {

        if (n.left != nil && n.left != null) {
            g.setColor(Color.black);
            g.drawLine((int) n.x, (int) n.y, (int) n.left.x, (int) n.left.y);
        }

        if (n.right != nil && n.left != null) {
            g.setColor(Color.black);
            g.drawLine((int) n.x, (int) n.y, (int) n.right.x, (int) n.right.y);
        }
    }

    private void pause(double d) {

        double t = System.currentTimeMillis() + d;

        while (System.currentTimeMillis() < t) {
            continue;
        }
    }

    public void setSpeed(int speed) {
        redrawDelay = (double) speed / 100.0;
        pauseDelay = ((double) speed / 100.0 * 80.0);
    }

    public void toggleSplay() {
        splayEnabled = (splayEnabled ? false : true);
    }

    public boolean splayIsEnabled() {
        return splayEnabled;
    }

    public int getHeightOfTree() {
        return (count != 0 ? deepest() : -1);
    }
}
