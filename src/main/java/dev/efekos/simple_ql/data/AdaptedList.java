/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package dev.efekos.simple_ql.data;

import dev.efekos.simple_ql.implementor.Implementor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.UnaryOperator;

public class AdaptedList<T> implements TableRowTypeAdapter {

    private final List<T> list;
    private final Implementor<T,String> implementor;

    public static <TY> AdaptedList<TY> clone(AdaptedList<TY> list){
        return new AdaptedList<>(list.list, list.implementor);
    }

    public AdaptedList(Implementor<T,String> implementor) {
        this.list = new ArrayList<>();
        this.implementor = implementor;
    }

    public AdaptedList(List<T> list, Implementor<T,String> implementor) {
        this.list = list;
        this.implementor = implementor;
    }

    public static final char ARRAY_SEPARATOR = '\uE492';
    public static final char IMPLEMENTOR_SEPARATOR = '\uE302';
    public static final char ARRAY_START = '\uE305';
    public static final char ARRAY_END = '\uE306';

    @Override
    public String adapt() {
        if(list.isEmpty())return implementor.getClass().getName()+IMPLEMENTOR_SEPARATOR+ARRAY_START+ARRAY_END;
        StringBuilder builder = new StringBuilder()
                .append(implementor.getClass().getName())
                .append(IMPLEMENTOR_SEPARATOR)
                .append(ARRAY_START);

        for (int i = 0; i < list.size(); i++) {
            T row = list.get(i);
            if(i!=0)builder.append(ARRAY_SEPARATOR);
            builder.append(implementor.write(row));
        }

        builder.append(ARRAY_END);
        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    public static AdaptedList<Object> readAdapted(String i){
        String[] split = i.split(IMPLEMENTOR_SEPARATOR + "");
        String implementorClassName = split[0];
        if(implementorClassName==null||implementorClassName.isEmpty())throw new IllegalStateException("implementorClassName is "+implementorClassName);
        try {
            Class<?> implementorClass = Class.forName(implementorClassName);
            Constructor<?> constructor = implementorClass.getConstructor();
            constructor.setAccessible(true);
            Implementor<Object, String> implementorInstance = (Implementor<Object, String>) constructor.newInstance();
            String[] adaptedTypes = split[1].replace(ARRAY_START + "", "").replace(ARRAY_END + "", "").split(ARRAY_SEPARATOR + "");
            ArrayList<Object> res = new ArrayList<>();

            for (String s : adaptedTypes) res.add(implementorInstance.read(s));
            return new AdaptedList<>(res,implementorInstance);
        } catch (ClassNotFoundException e){
            throw new IllegalStateException("Implementor class "+implementorClassName+" not found",e);
        } catch (NoSuchMethodException e){
            throw new IllegalStateException("Implementor class constructor "+implementorClassName+" not found",e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e){
            throw new IllegalStateException("Implementor class "+implementorClassName+" cannot be instantiated",e);
        }
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(T o) {
        return list.contains(o);
    }

    public Iterator<T> iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public <T1> T1[] toArray(T1[] a) {
        return list.toArray(a);
    }

    public boolean add(T t) {
        return list.add(t);
    }

    public boolean remove(T o) {
        return list.remove(o);
    }

    public boolean containsAll(Collection<T> c) {
        return new HashSet<>(list).containsAll(c);
    }

    public boolean addAll(Collection<? extends T> c) {
        return list.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return list.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    public void replaceAll(UnaryOperator<T> operator) {
        list.replaceAll(operator);
    }

    public void sort(Comparator<? super T> c) {
        list.sort(c);
    }

    public void clear() {
        list.clear();
    }

    @Override
    public boolean equals(Object o) {
        return list.equals(o);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    public T get(int index) {
        return list.get(index);
    }

    public T set(int index, T element) {
        return list.set(index, element);
    }

    public void add(int index, T element) {
        list.add(index, element);
    }

    public T remove(int index) {
        return list.remove(index);
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    public Spliterator<T> spliterator() {
        return list.spliterator();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
