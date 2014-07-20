Red-Black Tree and Splay Tree Animation 
=======================================

John Paul Smith

Desktop teaching aid applications for demonstrating several self-balancing 
binary search trees. Allows visual animated demonstrations of operations 
performed on binary search trees that utilize the "red-black" and "splay" 
algorithms to allow optimal height balance on the tree.

For descriptions of the "red-black" algorithms, see:

Cormen, Thomas H., Leiserson, Charles E., Rivest, Ronald L., 
and Stein, Clifford. 
Introduction to Algorithms, The MIT Press, Third Edition, 2009, pg. 308 - 338.

For the "splay" algorithms and a detailed proof of the amortized balance, see:

Sleator, Daniel Dominic and Tarjan, Robert Endre. 
Self-Adjusting Binary Search Trees, Journal of the Association 
for Computing Machinery, Vol. 32, No. 3, July 1985, pg. 652-686 

Author's Notes
-----------

This program should be displayed on a monitor capable of supporting 1600 x 900 
resolution (or better) for best results. 

If your monitor supports 2048 x 1536 resolution (or better) the 
*TreePanel.java and ControlPanel.java files may be altered to reflect a
width of 2048 pixels instead of 1024 pixels. 

All animation is based on the width of *TreePanel.java being exactly a power
of two, and any width values that are not exactly powers of two will cause a
less-than-symmetric tree to be displayed (at best) and unpredictable behavior
(at worst). 

The splay tree program features a button that allows the user to turn the 
splaying on and off. A splay tree without splaying is simply a plain old
binary search tree, so this program doubles as a non-balancing BST demo as 
well. 

Duplicate keys are disallowed in these data structures. Inserting duplicates
can cause a violation of the binary search tree property when rotations to the
left are performed. 
