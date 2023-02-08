package com.unideb.qsa.domain.context;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Comparator for sorting {@link ConfigValue} objects based on their qualifiers. If the objects are equal to qualifiers, then {@link
 * ConfigConditionComparator#equals(Object)} and their values are used to further compare the objects.
 */
public final class ConfigConditionComparator implements Comparator<ConfigValue> {

    private final List<String> configCondition;

    public ConfigConditionComparator(List<String> configCondition) {
        this.configCondition = configCondition;
    }

    @Override
    public int compare(ConfigValue configValue1, ConfigValue configValue2) {
        var qualifiersCv1 = configValue1.getQualifiers();
        var qualifiersCv2 = configValue2.getQualifiers();
        for (var qualifierField : configCondition) {
            var result = isQualifiersDifferent(qualifiersCv1, qualifiersCv2, qualifierField);
            if (result != null) {
                return result;
            }
        }
        return compareSameQualifierKeys(configValue1, configValue2);
    }

    private Integer isQualifiersDifferent(Map<String, Set<String>> qualifiersCv1, Map<String, Set<String>> qualifiersCv2, String qualifierField) {
        Integer result = null;
        if (qualifiersCv1.containsKey(qualifierField) && !qualifiersCv2.containsKey(qualifierField)) {
            result = -1;
        } else if (!qualifiersCv1.containsKey(qualifierField) && qualifiersCv2.containsKey(qualifierField)) {
            result = 1;
        }
        return result;
    }

    private int compareSameQualifierKeys(ConfigValue configValue1, ConfigValue configValue2) {
        int valueStringCompare = compareBasedOnValueString(configValue1, configValue2);
        return (valueStringCompare != 0) ? valueStringCompare : compareBasedOnQualifierValues(configValue1, configValue2);
    }

    private int compareBasedOnValueString(ConfigValue configValue1, ConfigValue configValue2) {
        return configValue1.getValue().compareTo(configValue2.getValue());
    }

    private int compareBasedOnQualifierValues(ConfigValue configValue1, ConfigValue configValue2) {
        for (var qualifierField : configCondition) {
            if (configValue1.getQualifiers().containsKey(qualifierField)) {
                var qualifierValues1 = configValue1.getQualifiers().get(qualifierField);
                var qualifierValues2 = configValue2.getQualifiers().get(qualifierField);
                var qualifierValuesNumberCompare = compareBasedOnNumberOfQualifierValues(qualifierValues1, qualifierValues2);
                if (qualifierValuesNumberCompare != 0) {
                    return qualifierValuesNumberCompare;
                }
                var qualifierValuesNaturalOrderingCompare = compareBasedOnQualifierValuesNaturalOrdering(qualifierValues1, qualifierValues2);
                if (qualifierValuesNaturalOrderingCompare != 0) {
                    return qualifierValuesNaturalOrderingCompare;
                }

            }
        }
        return (configValue1.equals(configValue2)) ? 0 : 1;
    }

    private int compareBasedOnNumberOfQualifierValues(Set<String> qualifierValues1, Set<String> qualifierValues2) {
        return qualifierValues1.size() - qualifierValues2.size();
    }

    private int compareBasedOnQualifierValuesNaturalOrdering(Set<String> qualifierValues1, Set<String> qualifierValues2) {
        var iterator1 = qualifierValues1.iterator();
        var iterator2 = qualifierValues2.iterator();
        while (iterator1.hasNext()) {
            int qualifierValueNaturalOrdering = iterator1.next().compareTo(iterator2.next());
            if (qualifierValueNaturalOrdering != 0) {
                return qualifierValueNaturalOrdering;
            }
        }
        return 0;
    }
}
