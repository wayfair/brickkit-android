package com.wayfair.brickkit.util;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the methods in {@link CollectionUtil}.
 */
public class CollectionUtilTests {
    @Test
    public void testIfITheIndexIsWithinTheCollectionsBounds_withValidIndex_returnsTrue() {
        ArrayList<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");

        assertTrue(CollectionUtil.isTheIndexWithinTheCollectionsBounds(1, list));
    }

    @Test
    public void testIfITheIndexIsWithinTheCollectionsBounds_withInvalidIndex_returnsFalse() {
        ArrayList<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");

        assertFalse(CollectionUtil.isTheIndexWithinTheCollectionsBounds(4, list));
    }
}
