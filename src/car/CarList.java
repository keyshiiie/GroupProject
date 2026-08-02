package car;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CarList implements Iterable<Car>{
    private final ArrayList<Car> list;

    public CarList() {
        list = new ArrayList<>();
    }

    public CarList(int initialCapacity) {
        list = new ArrayList<>(initialCapacity);
    }

    public CarList(List<Car> cars) {
        list = new ArrayList<>(cars);
    }

    public boolean add(Car car) {
        return list.add(car);
    }

    public void add(int index, Car car) {
        list.add(index, car);
    }

    public Car remove(int index) {
        return list.remove(index);
    }

    public boolean remove(Object o) {
        return list.remove(o);
    }

    public Car get(int index) {
        return list.get(index);
    }

    public Car set(int index, Car car) {
        return list.set(index, car);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void clear() {
        list.clear();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    public Iterator<Car> iterator() {
        return list.iterator();
    }
    
    public ListIterator<Car> listIterator() {
        return list.listIterator();
    }

    public ListIterator<Car> listIterator(int index) {
        return list.listIterator(index);
    }
    
    public List<Car> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
    
    public void ensureCapacity(int minCapacity) {
        list.ensureCapacity(minCapacity);
    }

    public void trimToSize() {
        list.trimToSize();
    }
    @Override
    public String toString() {
        return list.toString();
    }

    public boolean addAll(Collection<? extends Car> c) {
        return list.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends Car> c) {
        return list.addAll(index, c);
    }

    public void addAll(CarList carList) {
        for (Car car : carList) {
            list.add(car);
        }
    }
}