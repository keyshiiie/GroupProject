package utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class LinkedList<T> implements Collection<T>, Iterable<T> {
    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (T element : this) {
            if (Objects.equals(element, o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (current == null) {
                    throw new java.util.NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove() on iterator is not supported");
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (T element : this) {
            arr[i++] = element;
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> E[] toArray(E[] a) {
        if (a.length < size) {
            a = (E[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        } else if (a.length > size) {
            a[size] = null;
        }

        int i = 0;
        for (T element : this) {
            a[i++] = (E) element;
        }
        return a;
    }

    @Override
    public boolean add(T element) {
        Node<T> newNode = new Node<>(element);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        return true;
    }

    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == size) {
            add(element);
            return;
        }

        Node<T> current = nodeAt(index);
        Node<T> newNode = new Node<>(element);
        Node<T> prevNode = current.prev;

        newNode.next = current;
        current.prev = newNode;

        if (prevNode == null) {
            head = newNode;
        } else {
            prevNode.next = newNode;
            newNode.prev = prevNode;
        }

        size++;
    }

    @Override
    public boolean remove(Object o) {
        Node<T> current = head;
        int idx = 0;

        while (current != null) {
            if (Objects.equals(o, current.data)) {
                removeAt(idx);
                return true;
            }
            current = current.next;
            idx++;
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;
        for (T elem : c) {
            add(elem);
            modified = true;
        }
        return modified;
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        boolean modified = false;
        int i = index;
        for (T elem : c) {
            add(i++, elem);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object elem : c) {
            if (!contains(elem)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Node<T> current = head;
        int idx = 0;

        while (current != null) {
            Node<T> next = current.next;
            if (c.contains(current.data)) {
                removeAt(idx);
                modified = true;
            } else {
                idx++;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<T> current = head;
        int idx = 0;

        while (current != null) {
            Node<T> next = current.next;
            if (!c.contains(current.data)) {
                removeAt(idx);
                modified = true;
            } else {
                idx++;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public void clear() {
        Node<T> cur = head;
        while (cur != null) {
            Node<T> next = cur.next;
            cur.data = null;
            cur.next = null;
            cur.prev = null;
            cur = next;
        }
        head = tail = null;
        size = 0;
    }

    public T get(int index) {
        return nodeAt(index).data;
    }

    public T set(int index, T element) {
        Node<T> n = nodeAt(index);
        T old = n.data;
        n.data = element;
        return old;
    }

    public T removeAt(int index) {
        Node<T> toRemove = nodeAt(index);
        T old = toRemove.data;
        Node<T> prev = toRemove.prev;
        Node<T> next = toRemove.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
        }

        toRemove.next = null;
        toRemove.prev = null;
        toRemove.data = null;

        size--;
        return old;
    }

    private Node<T> nodeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    public T[] toArrayGeneric() {
        Object[] arr = new Object[size];
        int i = 0;
        for (T element : this) {
            arr[i++] = element;
        }
        return (T[]) arr;
    }

    public void fromArray(T[] arr) {
        clear();
        for (T element : arr) {
            add(element);
        }
    }
}