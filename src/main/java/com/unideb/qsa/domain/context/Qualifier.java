package com.unideb.qsa.domain.context;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * Immutable object for a config qualifier.
 */
public final class Qualifier {

    private final Map<String, String> qualifierMap;

    private Qualifier(Builder builder) {
        qualifierMap = builder.qualifierMap;
    }

    /**
     * Checks if the {@link Qualifier} contains the key.
     *
     * @param key key
     * @return true if it contains the key
     */
    public boolean containsKey(String key) {
        return qualifierMap.containsKey(key);
    }

    /**
     * Returns the associated value of the key.
     *
     * @param key key
     * @return value
     */
    public String get(String key) {
        return qualifierMap.get(key);
    }

    /**
     * Converts the qualifiers into an {@link ImmutableMap}.
     *
     * @return map based on the given keys and values
     */
    public ImmutableMap<String, String> asMap() {
        return ImmutableMap.copyOf(qualifierMap);
    }

    @Override
    public String toString() {
        return "Qualifier{"
               + "qualifierMap=" + qualifierMap
               + "}";
    }

    /**
     * Builder for {@link Qualifier}.
     */
    public static class Builder {

        private Map<String, String> qualifierMap = new HashMap<>();

        /**
         * Adds the given key and value to the qualifiers.
         *
         * @param key   key
         * @param value value
         * @return {@link Qualifier.Builder}
         */
        public Builder put(String key, String value) {
            checkArgument(key != null && value != null, "All keys and values in the context must be non null: key=%s, value=%s", key, value);
            qualifierMap.put(key, value);
            return this;
        }

        /**
         * Adds all the keys and values from the map to the qualifiers.
         *
         * @param qualifierMap map
         * @return {@link Qualifier.Builder}
         */
        public Builder putAll(Map<String, String> qualifierMap) {
            this.qualifierMap.putAll(qualifierMap);
            return this;
        }

        /**
         * Builds {@link Qualifier}.
         *
         * @return {@link Qualifier}
         */
        public Qualifier build() {
            return new Qualifier(this);
        }
    }
}
