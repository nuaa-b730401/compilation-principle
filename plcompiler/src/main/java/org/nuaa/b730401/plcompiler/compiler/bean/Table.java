package org.nuaa.b730401.plcompiler.compiler.bean;

import java.util.List;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/7 13:25
 */
public class Table {
    /**
     * 默认表size
     */
    private static final int DEFAULT_MAX_TABLE_SIZE = 100;

    private String[] container;

    public Table() {
        this(DEFAULT_MAX_TABLE_SIZE);
    }

    public Table(int size) {
        container = new String[size];
    }

    public int insert(String value) {
        for (int i = 0; i < container.length; i++) {
            if (container[i] == null) {
                container[i] = value;
                return i;
            }
        }
        return -1;
    }

    public String get(int index) {
        if (index < container.length) {
            return container[index];
        }
        throw new IndexOutOfBoundsException("index out of bound(not find label in container)");
    }
}
