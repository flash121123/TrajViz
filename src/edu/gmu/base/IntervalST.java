package edu.gmu.base;

import java.util.LinkedList;


import edu.gmu.core.agi.AGrammarRuleRecord;
import edu.gmu.core.agi.RuleInterval;

/**
 * 
 * Using Random Binary Search Tree for storing Intervals
 * 
 * The code is wrote on the top of 
 * http://algs4.cs.princeton.edu/93intersection/IntervalST.java.html
 * 
 * Interval Tree is used during the post-processing to merge overlapped patterns
 */

public class IntervalST<Value>  {

    private Node root;  

    // Binary Search Tree node structure
    private class Node {
        Interval interval;     //concisely version interval for saving memory 
        Value value;              
        Node left, right;         
        int N;                    // size of subtree 
        int max;                  // max end point in subtree rooted at this node

        Node(Interval interval, Value value) {
            this.interval = interval;
            this.value    = value;
            this.N        = 1;
            this.max      = interval.get_end();
        }
    }

    public boolean contains(Interval interval) {
        return (get(interval) != null);
    }

    // return value associated with the given key
    public Value get(Interval interval) {
        return get(root, interval);
    }

    private Value get(Node x, Interval interval) {
        if (x == null)                  return null;
        int cmp = interval.compareTo(x.interval);
        if      (cmp < 0) return get(x.left, interval);
        else if (cmp > 0) return get(x.right, interval);
        else              return x.value;
    }

    public boolean put(Interval interval, Value value) {
    	boolean flag=true;
        if (contains(interval)) { 
        	System.out.println("duplicate"); 
        	remove(interval);  
        	flag=false;
        	}
        root = randomizedInsert(root, interval, value);
        return flag;
    }

    
    // make new node the root with uniform probability
    private Node randomizedInsert(Node x, Interval interval, Value value) {
        if (x == null) return new Node(interval, value);
        if (Math.random() * size(x) < 1.0) return rootInsert(x, interval, value);
        int cmp = interval.compareTo(x.interval);
        if (cmp < 0)  x.left  = randomizedInsert(x.left,  interval, value);
        else          x.right = randomizedInsert(x.right, interval, value);
        countAndInterval(x);
        return x;
    }

    private Node rootInsert(Node x, Interval interval, Value value) {
        if (x == null) return new Node(interval, value);
        int cmp = interval.compareTo(x.interval);
        if (cmp < 0) { x.left  = rootInsert(x.left,  interval, value); x = rotR(x); }
        else         { x.right = rootInsert(x.right, interval, value); x = rotL(x); }
        return x;
    }

    private Node joinLR(Node a, Node b) { 
        if (a == null) return b;
        if (b == null) return a;

        if (Math.random() * (size(a) + size(b)) < size(a))  {
            a.right = joinLR(a.right, b);
            countAndInterval(a);
            return a;
        }
        else {
            b.left = joinLR(a, b.left);
            countAndInterval(b);
            return b;
        }
    }

    public Value remove(Interval interval) {
        Value value = get(interval);
        root = remove(root, interval);
        return value;
    }

    private Node remove(Node h, Interval interval) {
        if (h == null) return null;
        int cmp = interval.compareTo(h.interval);
        if      (cmp < 0) h.left  = remove(h.left,  interval);
        else if (cmp > 0) h.right = remove(h.right, interval);
        else              h = joinLR(h.left, h.right);
        countAndInterval(h);
        return h;
    }


    public Interval search(Interval interval) {
        return search(root, interval);
    }

    // look in subtree rooted at x
    public Interval search(Node x, Interval interval) {
        while (x != null) {
            if (interval.intersects(x.interval)) return x.interval;
            else if (x.left == null)             x = x.right;
            else if (x.left.max < interval.get_start())  x = x.right;
            else                                 x = x.left;
        }
        return null;
    }

    public Iterable<Interval> searchAll(Interval interval) {
        LinkedList<Interval> list = new LinkedList<Interval>();
        searchAll(root, interval, list);
        return list;
    }

    public boolean searchAll(Node x, Interval interval, LinkedList<Interval> list) {
         boolean found1 = false;
         boolean found2 = false;
         boolean found3 = false;
         if (x == null)
            return false;
        if (interval.intersects(x.interval)) {
            list.add(x.interval);
            found1 = true;
        }
        if (x.left != null && x.left.max >= interval.get_start())
            found2 = searchAll(x.left, interval, list);
        if (found2 || x.left == null || x.left.max < interval.get_start())
            found3 = searchAll(x.right, interval, list);
        return found1 || found2 || found3;
    }

    private int size(Node x) { 
        if (x == null) return 0;
        else           return x.N;
    }

    private void countAndInterval(Node x) {
        if (x == null) return;
        x.N = 1 + size(x.left) + size(x.right);
        x.max = Math.max(x.interval.get_end(), Math.max(max(x.left), max(x.right)));
    }

    private int max(Node x) {
        if (x == null) return Integer.MIN_VALUE;
        return x.max;
    }

    // right rotate
    private Node rotR(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        countAndInterval(h);
        countAndInterval(x);
        return x;
    }

    // left rotate
    private Node rotL(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        countAndInterval(h);
        countAndInterval(x);
        return x;
    }

	public void remove(AGrammarRuleRecord xx) {
		for(RuleInterval xs : xx)
		{
			this.remove(xs);
		}
	}

	private void remove(RuleInterval xs) {
		Interval x=new Interval(xs.getStart(),xs.getEnd());
		if(this.contains(x))
			this.remove(x);
	}

	@Override
	public String toString() {
		return this.searchAll(new Interval(0,5000000)).toString();
	}


}
