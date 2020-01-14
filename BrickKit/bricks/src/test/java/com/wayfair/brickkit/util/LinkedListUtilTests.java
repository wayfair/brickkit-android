package com.wayfair.brickkit.util;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * This class tests the methods in {@link LinkedListUtil}.
 */
public class LinkedListUtilTests {
    @Test
    public void testIfGetItemQuietly_returnsFirstItemAsANonNullItem() {
        LinkedList<String> list = new LinkedList<String>();
        list.add("one");
        list.add("two");
        list.add("three");

        String firstItem = LinkedListUtil.getFirstItemQuietly(list);
        assertEquals("one", firstItem);
    }

    @Test
    public void testIfGetItemQuietly_returnsFirstItemAsANullItem_forAnEmptyCollection() {
        LinkedList<String> list = new LinkedList<String>();
        String firstItem = LinkedListUtil.getFirstItemQuietly(list);
        assertNull(firstItem);
    }

    @Test
    public void testIfGetItemQuietly_returnsFirstItemAsANullItem_forANullCollection() {
        LinkedList<String> list = null;
        //noinspection ConstantConditions
        String firstItem = LinkedListUtil.getFirstItemQuietly(list);
        assertNull(firstItem);
    }
}
