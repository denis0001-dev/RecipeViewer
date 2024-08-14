package ru.morozovit.utils.android;

import android.view.View;
import android.view.ViewGroup;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ViewList implements List<View> {
    private final ViewGroup viewGroup;

    public static class ViewListIterator implements Iterator<View> {
        private int index = 0;
        private final ViewList l;

        public ViewListIterator(ViewList l2) {
            this.l = l2;
        }

        public boolean hasNext() {
            return this.index < this.l.size();
        }

        public View next() {
            try {
                View view = (View) Objects.requireNonNull(this.l.get(this.index));
                this.index++;
                return view;
            } catch (NullPointerException e) {
                throw new NoSuchElementException("Element doesnt exist at index " + this.index);
            } catch (Throwable th) {
                this.index++;
                throw th;
            }
        }
    }

    public ViewList(ViewGroup viewGroup2) {
        this.viewGroup = viewGroup2;
    }

    public int size() {
        return this.viewGroup.getChildCount();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Object o) {
        for (int i = 0; i < size(); i++) {
            if (o == this.viewGroup.getChildAt(i)) {
                return true;
            }
        }
        return false;
    }

    public Iterator<View> iterator() {
        return new ViewListIterator(this);
    }

    public View[] toArray() {
        View[] arr = new View[size()];
        for (int i = 0; i < size(); i++) {
            arr[i] = get(i);
        }
        return arr;
    }

    public <T> T[] toArray(T[] tArr) {
        throw new UnsupportedOperationException();
    }

    public boolean add(View view) {
        try {
            this.viewGroup.addView(view);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    public boolean remove(Object view) {
        try {
            this.viewGroup.removeView((View) view);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    public boolean containsAll(Collection<?> collection) {
        for (Object o : collection) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends View> collection) {
        for (View o : collection) {
            if (!add(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(int i, Collection<? extends View> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> collection) {
        for (Object o : collection) {
            if (!remove(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    public void clear() {
        Iterator<View> it = iterator();
        while (it.hasNext()) {
            remove((Object) it.next());
        }
    }

    public View get(int i) {
        return this.viewGroup.getChildAt(i);
    }

    public View set(int i, View view) {
        try {
            ViewList viewList = (ViewList) clone();
            return null;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public void add(int i, View view) {
    }

    public View remove(int i) {
        return null;
    }

    public int indexOf(Object o) {
        return 0;
    }

    public int lastIndexOf(Object o) {
        return 0;
    }

    public ListIterator<View> listIterator() {
        return null;
    }

    public ListIterator<View> listIterator(int i) {
        return null;
    }

    public List<View> subList(int i, int i1) {
        return Collections.emptyList();
    }
}
