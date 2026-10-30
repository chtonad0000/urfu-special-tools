package ru.urfu.specialtools.collection;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Objects;
import java.util.RandomAccess;

public final class DynamicArrayList<E> extends AbstractList<E> implements RandomAccess {
    private static final int INITIAL_CAPACITY = 4;

    private Object[] elements = new Object[INITIAL_CAPACITY];
    private int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        Objects.checkIndex(index, size);
        return elementAt(index);
    }

    @Override
    public E set(int index, E element) {
        E previous = get(index);
        elements[index] = element;
        return previous;
    }

    @Override
    public boolean add(E element) {
        add(size, element);
        return true;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
        modCount++;
    }

    @Override
    public E remove(int index) {
        E removed = get(index);
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
        modCount++;
        return removed;
    }

    @Override
    public void clear() {
        Arrays.fill(elements, 0, size, null);
        size = 0;
        modCount++;
    }

    private void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity > elements.length) {
            int newCapacity = Math.max(minimumCapacity, elements.length * 2);
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    @SuppressWarnings("unchecked")
    private E elementAt(int index) {
        return (E) elements[index];
    }
}
