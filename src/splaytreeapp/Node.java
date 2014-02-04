/**
 * @author John Paul Smith
 * 
 */

package splaytreeapp;

import java.awt.Color;

public class Node implements Comparable<Node> {
    
    Node parent;    
    Node left, right;
    Integer t;
    Color color;    
    double x, y;     
    
    public Node(Integer t, Color color, Node parent, Node left, Node right) {
        this.t = t;
        this.color = color;
        this.parent = parent;
        this.left = left;
        this.right = right;     
        x = y = 0;
    }     
    
    @Override
    public int compareTo(Node n) {                       
        return t.compareTo(n.t);
    }   
}
