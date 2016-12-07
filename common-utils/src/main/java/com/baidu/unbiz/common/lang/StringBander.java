/**
 * 
 */
package com.baidu.unbiz.common.lang;

import com.baidu.unbiz.common.Emptys;
import com.baidu.unbiz.common.StringPool;

/**
 * <code>StringBuilder</code>的替代品，性能比<code>StringBuilder</code>好
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015年11月18日 上午3:00:24
 */
public class StringBander implements java.io.Serializable, CharSequence {

    private static final long serialVersionUID = 6097165714463878977L;

    private static final int DEFAULT_ARRAY_CAPACITY = 16;

    private String[] array;
    private int index;
    private int length;

    /**
     * Creates an empty <code>StringBand</code>.
     */
    public StringBander() {
        array = new String[DEFAULT_ARRAY_CAPACITY];
    }

    public StringBander(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Invalid initial capacity");
        }
        array = new String[initialCapacity];
    }

    public StringBander(String s) {
        this();
        array[0] = s;
        index = 1;
        length = s.length();
    }

    public StringBander(Object o) {
        this(String.valueOf(o));
    }

    public StringBander append(boolean b) {
        return append(b ? StringPool.Word.TRUE : StringPool.Word.FALSE);
    }

    public StringBander append(double d) {
        return append(Double.toString(d));
    }

    public StringBander append(float f) {
        return append(Float.toString(f));
    }

    public StringBander append(int i) {
        return append(Integer.toString(i));
    }

    public StringBander append(long l) {
        return append(Long.toString(l));
    }

    public StringBander append(short s) {
        return append(Short.toString(s));
    }

    public StringBander append(char c) {
        return append(String.valueOf(c));
    }

    public StringBander append(byte b) {
        return append(Byte.toString(b));
    }

    public StringBander append(Object obj) {
        return append(String.valueOf(obj));
    }

    public StringBander append(String s) {
        if (s == null) {
            s = StringPool.Word.NULL;
        }

        if (index >= array.length) {
            expandCapacity();
        }

        array[index++] = s;
        length += s.length();

        return this;
    }

    public int capacity() {
        return array.length;
    }

    public int length() {
        return length;
    }

    public int index() {
        return index;
    }

    public void setIndex(int newIndex) {
        if (newIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(newIndex);
        }

        if (newIndex > array.length) {
            String[] newArray = new String[newIndex];
            System.arraycopy(array, 0, newArray, 0, index);
            array = newArray;
        }

        if (newIndex > index) {
            for (int i = index; i < newIndex; i++) {
                array[i] = Emptys.EMPTY_STRING;
            }
        } else if (newIndex < index) {
            for (int i = newIndex; i < index; i++) {
                array[i] = null;
            }
        }

        index = newIndex;
        length = calculateLength();
    }

    public char charAt(int pos) {
        int len = 0;
        for (int i = 0; i < index; i++) {
            int newlen = len + array[i].length();
            if (pos < newlen) {
                return array[i].charAt(pos - len);
            }
            len = newlen;
        }
        throw new IllegalArgumentException("Invalid char index");
    }

    public String stringAt(int index) {
        if (index >= this.index) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public String toString() {
        return new String(toChar());
    }

    private char[] toChar() {
        // special cases
        if (index == 0) {
            return Emptys.EMPTY_CHAR_ARRAY;
        }

        // join strings
        char[] destination = new char[length];
        int start = 0;
        for (int i = 0; i < index; i++) {
            String s = array[i];

            int len = s.length();
            s.getChars(0, len, destination, start);

            start += len;
        }

        return destination;
    }

    protected void expandCapacity() {
        String[] newArray = new String[array.length << 1];
        System.arraycopy(array, 0, newArray, 0, index);
        array = newArray;
    }

    protected int calculateLength() {
        int len = 0;
        for (int i = 0; i < index; i++) {
            len += array[i].length();
        }
        return len;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (start < 0) {
            throw new StringIndexOutOfBoundsException(start);
        }

        if (end > length) {
            throw new StringIndexOutOfBoundsException(end);
        }

        if (start > end) {
            throw new StringIndexOutOfBoundsException(end - start);
        }

        return new String(toChar(), start, end - start);

    }

}
