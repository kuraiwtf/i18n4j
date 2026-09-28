package dev.kurai.i18n4j.util;

/**
 * Something identified by a key.
 *
 * @param <K> the type of the key
 */
public interface Keyed<K> {

  /**
   * Returns the key identifying this object.
   *
   * @return the key
   */
  K key();
}
