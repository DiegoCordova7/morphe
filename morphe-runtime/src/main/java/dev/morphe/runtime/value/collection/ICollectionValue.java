package dev.morphe.runtime.value.collection;

import dev.morphe.runtime.value.IValue;

/**
 * Represents a collection value in the Morphe runtime.
 *
 * <p>Collection values expose their number of elements and whether
 * they contain any elements.</p>
 */
public interface ICollectionValue extends IValue {

  /**
   * Returns the number of elements in this collection.
   *
   * @return the number of elements
   */
  int size();

  /**
   * Returns whether this collection contains no elements.
   *
   * @return {@code true} if the collection is empty; {@code false} otherwise
   */
  boolean isEmpty();
}