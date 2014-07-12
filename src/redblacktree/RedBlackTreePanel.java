/**
 * @author John Paul Smith
 *
 */
package redblacktree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class RedBlackTreePanel extends JPanel {

    final int ROOT_V_OFFSET = 50;
    final int MAX_CAPACITY = 25;
    final int NODE_DIAM = 30;
    final int V_SPACER = (NODE_DIAM * 3);
    final int HIGHLIGHT_DELAY = 1000;
    final double DELAY_INCREMENT = .15;
    final Dimension PANEL_SIZE = new Dimension(1024, 708);
    final Color BG_COLOR = new Color(210, 210, 210);
    Node root, nil;
    Color opColor;
    ArrayList<Node> nodes;
    double redrawDelay, pauseDelay;
    int count, i, j;

    public RedBlackTreePanel() {

        setBorder(BorderFactory.createRaisedSoftBevelBorder());
        setPreferredSize(PANEL_SIZE);
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        opColor = Color.blue;
        root = nil = new Node(null, Color.black, null, nil, nil);
        count = 0;
        nodes = new ArrayList<>();
        redrawDelay = 5;
        pauseDelay = 500;
    }

    public synchronized boolean insert(Integer e) {

        if (count >= MAX_CAPACITY) {
            return false;
        }

        Node n = new Node(e, opColor, null, nil, nil), p = nil, q = root;

        while (q != nil) {

            p = q;

            if (n.compareTo(q) < 0) {
                q = q.left;
            } else {
                q = q.right;
            }
        }

        nodes.add(n);

        n.parent = p;

        double h = (getSize().width / (int) Math.pow(2, depthOf(n) + 1));

        if (p == nil) {

            root = n;
            n.x = (getSize().width / 2);
            n.y = ROOT_V_OFFSET;

            repaint();
            pause(pauseDelay);

            n.color = Color.black;

        } else if (n.compareTo(p) < 0) {

            p.left = n;

            n.x = p.x;
            n.y = p.y;

            for (int c = 0; c < V_SPACER; ++c) {

                n.x -= (h / V_SPACER);
                ++n.y;

                pause(redrawDelay + (DELAY_INCREMENT * c));
                repaint();
            }

            repaint();
            pause(pauseDelay);

            n.color = Color.red;

        } else {

            p.right = n;

            n.x = p.x;
            n.y = p.y;

            for (int c = 0; c < V_SPACER; ++c) {

                n.x += (h / V_SPACER);
                ++n.y;

                pause(redrawDelay + (DELAY_INCREMENT * c));
                repaint();
            }

            repaint();
            pause(pauseDelay);

            n.color = Color.red;
        }

        repaint();
        pause(pauseDelay);

        ++count;

        if (n.parent.color == Color.red) {
            insertFix(n);
        }

        pause(pauseDelay);

        root.color = Color.black;

        repaint();
        pause(pauseDelay);

        return true;
    }

    private void insertFix(Node n) {

        Node p;

        while (n.parent.color == Color.red) {

            if (n.parent == n.parent.parent.left) {

                p = n.parent.parent.right;

                if (p.color == Color.red) {

                    pause(pauseDelay);

                    p.color = n.parent.color = Color.black;

                    repaint();
                    pause(pauseDelay);

                    n.parent.parent.color = Color.red;

                    repaint();
                    pause(pauseDelay);

                    n = n.parent.parent;

                } else {

                    if (n == n.parent.right) {

                        n = n.parent;

                        leftRotate(n);
                    }

                    repaint();
                    pause(pauseDelay);

                    n.parent.color = Color.black;

                    repaint();
                    pause(pauseDelay);

                    n.parent.parent.color = Color.red;

                    repaint();
                    pause(pauseDelay);

                    rightRotate(n.parent.parent);
                }
            } else {

                p = n.parent.parent.left;

                if (p.color == Color.red) {

                    repaint();
                    pause(pauseDelay);

                    p.color = n.parent.color = Color.black;

                    repaint();
                    pause(pauseDelay);

                    n.parent.parent.color = Color.red;

                    repaint();
                    pause(pauseDelay);

                    n = n.parent.parent;

                } else {

                    if (n == n.parent.left) {

                        n = n.parent;

                        rightRotate(n);

                        repaint();
                        pause(pauseDelay);
                    }

                    repaint();
                    pause(pauseDelay);

                    n.parent.color = Color.black;

                    repaint();
                    pause(pauseDelay);

                    n.parent.parent.color = Color.red;

                    repaint();
                    pause(pauseDelay);

                    leftRotate(n.parent.parent);

                    repaint();
                    pause(pauseDelay);
                }
            }
        }
    }

    public synchronized Integer remove(Integer e) {

        Node n = findKey(e, root);

        return (n != null ? remove(n).t : null);
    }

    private Node remove(Node n) {

        Color originalColor = n.color;
        Node p, q;

        n.color = opColor;

        repaint();
        pause(pauseDelay);

        if (n.left == nil) {

            p = n.right;
            bypass(n, n.right);

            n.parent = n.left = n.right = null;

            repaint();
            pause(pauseDelay);

            nodes.remove(n);

            repaint();
            pause(pauseDelay);

            if (p != nil) {

                j = 1;

                int d = deepest(p, p);

                Node[] rightSubTree = new Node[(int) Math.pow(2, d + 1)];

                subTreeRightSide(p, p, rightSubTree, d);

                double rootNodeHSpacer = (getSize().width / Math.pow(2, deepest(p, root) + 2));

                for (int c = 0; c < V_SPACER; ++c) {

                    for (int w = 1; w < rightSubTree.length; ++w) {

                        if (rightSubTree[w] != null) {
                            updateCoordinates(rightSubTree[w], -1, -1, (w * rootNodeHSpacer), V_SPACER);
                        }
                    }

                    pause(redrawDelay + (DELAY_INCREMENT * c));
                    repaint();
                }
            }

        } else if (n.right == nil) {

            p = n.left;

            bypass(n, n.left);

            n.parent = n.left = n.right = null;

            repaint();
            pause(pauseDelay);

            nodes.remove(n);

            repaint();
            pause(pauseDelay);

            if (p != nil) {

                i = 1;

                int d = deepest(p, p);

                Node[] leftSubTree = new Node[(int) Math.pow(2, d + 1)];

                subTreeLeftSide(p, p, leftSubTree, d);

                double rootNodeHSpacer = (getSize().width / Math.pow(2, deepest(p, root) + 2));

                for (int c = 0; c < V_SPACER; ++c) {

                    for (int w = 1; w < leftSubTree.length; ++w) {

                        if (leftSubTree[w] != null) {
                            updateCoordinates(leftSubTree[w], 1, -1, (w * rootNodeHSpacer), V_SPACER);
                        }
                    }

                    pause(redrawDelay + (DELAY_INCREMENT * c));
                    repaint();
                }
            }
        } else {

            Color oColor = originalColor;

            q = min(n.right);

            originalColor = q.color;

            q.color = oColor;

            p = q.right;

            if (q.parent == n) {

                p.parent = q;

            } else {

                bypass(q, q.right);

                q.right = n.right;

                q.right.parent = q;
            }

            bypass(n, q);

            q.left = n.left;

            q.left.parent = q;

            q.x = n.x;
            q.y = n.y;

            n.parent = n.left = n.right = null;

            repaint();
            pause(pauseDelay);

            nodes.remove(n);

            repaint();
            pause(pauseDelay);

            if (p != nil) {

                j = 1;

                int d = deepest(p, p);

                Node[] rightSubTree = new Node[(int) Math.pow(2, d + 1)];

                subTreeRightSide(p, p, rightSubTree, d);

                double rootNodeHSpacer = (getSize().width / Math.pow(2, deepest(p, root) + 2));

                for (int c = 0; c < V_SPACER; ++c) {

                    for (int w = 1; w < rightSubTree.length; ++w) {

                        if (rightSubTree[w] != null) {
                            updateCoordinates(rightSubTree[w], -1, -1, (w * rootNodeHSpacer), V_SPACER);
                        }
                    }

                    pause(redrawDelay + (DELAY_INCREMENT * c));
                    repaint();
                }
            }
        }

        --count;

        pause(pauseDelay);

        if (originalColor == Color.black) {
            removeFix(p);
        }

        nil.parent = null;

        repaint();

        return n;
    }

    private void removeFix(Node n) {

        while (n != root && n.color == Color.black) {

            if (n == n.parent.left) {

                Node w = n.parent.right;

                if (w.color == Color.red) {

                    repaint();
                    pause(pauseDelay);

                    w.color = Color.black;

                    repaint();
                    pause(pauseDelay);

                    n.parent.color = Color.red;

                    repaint();
                    pause(pauseDelay);

                    leftRotate(n.parent);

                    repaint();
                    pause(pauseDelay);

                    w = n.parent.right;
                }
                if (w.left.color == Color.black && w.right.color == Color.black) {

                    w.color = Color.red;

                    repaint();
                    pause(pauseDelay);

                    n = n.parent;

                } else {
                    if (w.right.color == Color.black) {

                        repaint();
                        pause(pauseDelay);

                        w.left.color = Color.black;

                        repaint();
                        pause(pauseDelay);

                        w.color = Color.red;

                        repaint();
                        pause(pauseDelay);

                        rightRotate(w);

                        repaint();
                        pause(pauseDelay);

                        w = n.parent.right;
                    }

                    repaint();
                    pause(pauseDelay);

                    w.color = n.parent.color;

                    repaint();
                    pause(pauseDelay);

                    n.parent.color = w.right.color = Color.black;

                    repaint();
                    pause(pauseDelay);

                    leftRotate(n.parent);

                    repaint();
                    pause(pauseDelay);

                    n = root;
                }
            } else {

                Node p = n.parent.left;

                if (p.color == Color.red) {

                    repaint();
                    pause(pauseDelay);

                    p.color = Color.black;

                    repaint();
                    pause(pauseDelay);

                    n.parent.color = Color.red;

                    repaint();
                    pause(pauseDelay);

                    rightRotate(n.parent);

                    repaint();
                    pause(pauseDelay);

                    p = n.parent.left;
                }
                if (p.left.color == Color.black && p.right.color == Color.black) {

                    repaint();
                    pause(pauseDelay);

                    p.color = Color.red;

                    repaint();
                    pause(pauseDelay);

                    n = n.parent;
                } else {

                    if (p.left.color == Color.black) {

                        repaint();
                        pause(pauseDelay);

                        p.right.color = Color.black;

                        repaint();
                        pause(pauseDelay);

                        p.color = Color.red;

                        repaint();
                        pause(pauseDelay);

                        leftRotate(p);

                        repaint();
                        pause(pauseDelay);

                        p = n.parent.left;
                    }

                    repaint();
                    pause(pauseDelay);

                    p.color = n.parent.color;

                    repaint();
                    pause(pauseDelay);

                    n.parent.color = p.left.color = Color.black;

                    repaint();
                    pause(pauseDelay);

                    rightRotate(n.parent);

                    repaint();
                    pause(pauseDelay);

                    n = root;
                }
            }
        }

        repaint();
        pause(pauseDelay);

        n.color = Color.black;

        repaint();
        pause(pauseDelay);
    }

    private void bypass(Node a, Node b) {

        if (a.parent == nil) {
            root = b;
        } else if (a == a.parent.left) {
            a.parent.left = b;
        } else {
            a.parent.right = b;
        }

        b.parent = a.parent;
    }

    private void leftRotate(Node n) {

        Node q = n.right;

        n.right = q.left;

        repaint();
        pause(pauseDelay);

        if (q.left != nil) {
            q.left.parent = n;
        }

        q.parent = n.parent;

        if (n.parent == nil) {
            root = q;
        } else if (n == n.parent.left) {
            n.parent.left = q;
        } else {
            n.parent.right = q;
        }

        repaint();
        pause(pauseDelay);

        q.left = n;
        n.parent = q;

        repaint();
        pause(pauseDelay);

        double leftSubTreeSpacer = 0,
                rightSubTreeSpacer = 0;

        Node[] rightSubTree, leftSubTree;

        i = j = 1;

        if (q.right != nil) {

            int d = deepest(q.right, q.right);

            rightSubTree = new Node[(int) Math.pow(2, d + 1)];

            subTreeRightSide(q.right, q.right, rightSubTree, d);

            rightSubTreeSpacer = (getSize().width / Math.pow(2, deepest(q.right, root) + 2));

        } else {
            rightSubTree = new Node[0];
        }

        if (n.left != nil) {

            int d = deepest(n.left, n.left);

            leftSubTree = new Node[(int) Math.pow(2, d + 1)];

            subTreeLeftSide(n.left, n.left, leftSubTree, d);

            leftSubTreeSpacer = (getSize().width / Math.pow(2, deepest(n.left, root) + 1));

        } else {
            leftSubTree = new Node[0];
        }

        double rootNodeHSpacer = (getSize().width / Math.pow(2, depthOf(n) + 1));

        for (int c = 0; c < V_SPACER; ++c) {

            ++n.y;
            n.x -= (rootNodeHSpacer / V_SPACER);

            --q.y;
            q.x -= (rootNodeHSpacer / V_SPACER);

            for (int z = 1; z < rightSubTree.length; ++z) {

                if (rightSubTree[z] != null) {
                    updateCoordinates(rightSubTree[z], -1, -1, (z * rightSubTreeSpacer), V_SPACER);
                }
            }

            for (int z = 1; z < leftSubTree.length; ++z) {

                if (leftSubTree[z] != null) {
                    updateCoordinates(leftSubTree[z], -1, 1, (z * leftSubTreeSpacer), V_SPACER);
                }
            }

            if (n.right != nil) {
                shiftOver(n.right, -1, getSize().width / Math.pow(2, depthOf(n.right)));
            }

            pause(redrawDelay + (DELAY_INCREMENT * c));
            repaint();
        }
    }

    private void rightRotate(Node n) {

        Node q = n.left;

        n.left = q.right;

        repaint();
        pause(pauseDelay);

        if (q.right != nil) {
            q.right.parent = n;
        }

        q.parent = n.parent;

        if (n.parent == nil) {
            root = q;
        } else if (n == n.parent.left) {
            n.parent.left = q;
        } else {
            n.parent.right = q;
        }

        repaint();
        pause(pauseDelay);

        q.right = n;
        n.parent = q;

        repaint();
        pause(pauseDelay);

        double leftSubTreeSpacer = 0,
                rightSubTreeSpacer = 0;

        Node[] leftSubTree, rightSubTree;

        i = j = 1;

        if (q.left != nil) {

            int leftSubTreeDeepest = deepest(q.left, q.left);

            leftSubTree = new Node[(int) Math.pow(2, leftSubTreeDeepest + 1)];

            subTreeLeftSide(q.left, q.left, leftSubTree, leftSubTreeDeepest);

            leftSubTreeSpacer = (getSize().width / Math.pow(2, deepest(q.left, root) + 2));

        } else {
            leftSubTree = new Node[0];
        }

        if (n.right != nil) {

            int rightSubTreeDeepest = deepest(n.right, n.right);

            rightSubTree = new Node[(int) Math.pow(2, rightSubTreeDeepest + 1)];

            subTreeRightSide(n.right, n.right, rightSubTree, rightSubTreeDeepest);

            rightSubTreeSpacer = (getSize().width / Math.pow(2, deepest(n.right, root) + 1));

        } else {
            rightSubTree = new Node[0];
        }

        double rootNodeHSpacer = (getSize().width / Math.pow(2, depthOf(n) + 1));

        for (int c = 0; c < V_SPACER; ++c) {

            ++n.y;
            n.x += (rootNodeHSpacer / V_SPACER);

            --q.y;
            q.x += (rootNodeHSpacer / V_SPACER);

            for (int z = 1; z < leftSubTree.length; ++z) {

                if (leftSubTree[z] != null) {
                    updateCoordinates(leftSubTree[z], 1, -1, (z * leftSubTreeSpacer), V_SPACER);
                }
            }

            for (int z = 1; z < rightSubTree.length; ++z) {

                if (rightSubTree[z] != null) {
                    updateCoordinates(rightSubTree[z], 1, 1, (z * rightSubTreeSpacer), V_SPACER);
                }
            }

            if (n.left != nil) {
                shiftOver(n.left, 1, getSize().width / Math.pow(2, depthOf(n.left)));
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

    public synchronized Integer redBlackFind(Integer e) {

        Node n = redBlackFind(e, root);

        return (n == null ? null : n.t);
    }

    private Node redBlackFind(Integer e, Node n) {

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

        if (prev == n) {

            Color o = prev.color;

            prev.color = opColor;

            repaint();
            pause(HIGHLIGHT_DELAY);

            prev.color = o;
            repaint();
        }

        return (prev == n ? n : null);
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
        pauseDelay = (double) speed;
    }
}