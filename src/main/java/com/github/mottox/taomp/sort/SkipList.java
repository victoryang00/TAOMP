package com.github.mottox.taomp.sort;

import java.util.NoSuchElementException;
import java.util.Random;


/**
 * 跳表，常用于不能频繁更新的文件系统及数据库的KV配对。
 *
 * @author Victor Yang
 */
public class SkipList<T> {
    /**
     * 随机生成器
     */
    protected static final Random randomGen = new Random();
    /**
     * 默认概率0.5
     */
    protected static final double p = 0.5;

    private Node<T> head;

    private double prob;

    private int size;

    public SkipList() {
        this(p);
    }

    public SkipList(double prob) {
        this.head = new Node<T>(0, null, 0);
        this.prob = prob;
        this.size = 0;
    }

    public T get(int key) {
        check(key);
        Node<T> node = find(key);
        if (node.key == key)
            return node.item;
        else
            return null;
    }

    public void add(int key, T value) {
        check(key);
        Node<T> node = find(key);
        if (node.key != 0 && node.key == key) {
            node.item = value;
            return;
        }
        Node<T> new_node = new Node<T>(key, value, node.level);
        horizon_insert(node, new_node);
        int current_level = node.level;
        int head_level = head.level;
        while (randomGen.nextDouble() < prob) {
            if (current_level >= head_level) {
                Node<T> new_head = new Node<T>(0, null, head_level + 1);
                vertical_insert(new_head, head);
                head = new_head;
                head_level = head.level;
            }
            while (node.up == null) {
                node = node.previous;
            }
            Node<T> temp = new Node<T>(key, value, node.level);
            horizon_insert(node, temp);
            vertical_insert(node, new_node);
            new_node = temp;
            current_level++;
        }
        size++;
    }

    public void remove(int key) {
        check(key);
        Node<T> node = find(key);
        if (node == null || node.key != key) {
            throw new NoSuchElementException("No such element");
        }
        while (node.down != null) {
            node = node.down;
        }
        Node<T> prev = null;
        Node<T> next = null;
        for (; node != null; node = node.up) {
            prev = node.previous;
            next = node.next;
            if (prev != null)
                prev.next = next;
            else
                next.previous = prev;
        }

        while (head.next == null && head.down != null) {
            head = head.down;
            head.up = null;
        }
        size--;
    }

    public boolean contains(int key) {
        return get(key) != null;
    }

    public int size() {
        return size;
    }

    public boolean empty() {
        return size == 0;
    }


    protected Node<T> find(int key) {
        Node<T> node = head;
        Node<T> next = null;
        Node<T> down = null;
        int nodekey = 0;
        while (true) {
            next = node.next;
            while (next != null && (next.key <= key)) {
                node = next;
                next = node.next;
            }
            nodekey = node.key;
            if (nodekey != 0 && nodekey != key) {
                break;
            }
            down = node.down;
            if (down != null) {
                node = down;
            } else {
                break;
            }
        }
        return node;
    }

    protected void check(int key) {
        if (key == 0)
            throw new IllegalArgumentException("The inserted key is null");
    }

    protected void horizon_insert(Node<T> x, Node<T> y) {
        y.previous = x;
        y.next = x.next;
        if (x.next != null)
            x.next.previous = y;
        x.next = y;
    }

    protected void vertical_insert(Node<T> x, Node<T> y) {
        x.down = y;
        y.up = x;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> node = head;

        // Move into the lower left bottom
        while (node.down != null)
            node = node.down;

        while (node.previous != null)
            node = node.previous;

        // Head node with each level the key is null
        // so need to move into the next node
        if (node.next != null)
            node = node.next;

        while (node != null) {
            sb.append(node.toString()).append("\n");
            node = node.next;
        }

        return sb.toString();
    }



    private static class Node<T> {
        private T item;
        private int key;
        private int level;
        Node<T> up, down, next, previous;

        Node(int key, int level) {
            this.key = key;
            this.level = level;
        }

        Node(int key, T item, int level) {
            this.key = key;
            this.level = level;
            this.item = item;
        }

        Node(T item) {
            this.item = item;
            this.key = item.hashCode();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Node[").append("key:");
            if (this.key == 0) {
                sb.append("None");
            } else
                sb.append(this.key);

            sb.append(",");
            if (this.item == null) {
                sb.append("None");
            } else
                sb.append(this.item);
            sb.append("]");
            return sb.toString();
        }

    }
}
