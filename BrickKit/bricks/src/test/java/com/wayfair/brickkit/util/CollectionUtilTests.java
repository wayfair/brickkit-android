package com.wayfair.brickkit.util;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * This class tests the methods in {@link CollectionUtil}.
 */
public class CollectionUtilTests {
    @Test
    public void testIfITheIndexIsWithinTheCollectionsBounds_withValidIndex_returnsTrue() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("one");
        list.add("two");
        list.add("three");

        boolean isInBounds = CollectionUtil.isTheIndexWithinTheCollectionsBounds(1, list);
        assertTrue(isInBounds);
    }

    @Test
    public void testIfITheIndexIsWithinTheCollectionsBounds_withInvalidIndex_returnsFalse() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("one");
        list.add("two");
        list.add("three");

        boolean isInBounds = CollectionUtil.isTheIndexWithinTheCollectionsBounds(1, list);
        assertTrue(isInBounds);
    }
}