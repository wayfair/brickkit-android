package com.wayfair.brickkit.util;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This util class contains utility functions for the {@link LinkedList} class.
 */
public class LinkedListUtil {

    /**
     * Retrieves the first item from the {@link LinkedList}.  If it is not available, null is
     * returned.  If the {@link NoSuchElementException} is thrown, it is caught and the exception
     * is not logged out.
     *
     * @param linkedList to get the first item from
     * @param <T>        the item type
     * @return the item or null
     */
    public static <T> T getFirstItemQuietly(@Nullable LinkedList<T> linkedList) {
        T item = null;

        if (null != linkedList) {
            try {
                item = linkedList.getFirst();
            } catch (NoSuchElementException e) {
                // ignore
            }
        }

        return item;
    }
}
