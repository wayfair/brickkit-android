package com.wayfair.brickkit.util;

import androidx.annotation.Nullable;

import java.util.Collection;

/**
 * This util class contains utility functions for the {@link Collection} classes.
 */
public class CollectionUtil {

    /**
     * Verifies whether or not an index is within the bounds of a collection.
     *
     * @param index      to check the collection with
     * @param collection to check the index with
     * @return true if the index is within the collection's bounds
     */
    public static boolean isTheIndexWithinTheCollectionsBounds(int index,
                                                               @Nullable Collection<?> collection) {
        // The IntRange annotation was intentionally not added to the "index" param to enable a full
        // check here.
        return null != collection && (index >= 0 && index < collection.size());
    }

}
