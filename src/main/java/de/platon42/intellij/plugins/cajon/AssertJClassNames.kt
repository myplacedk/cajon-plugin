package de.platon42.intellij.plugins.cajon

import org.jetbrains.annotations.NonNls

class AssertJClassNames {
    companion object {
        @NonNls
        const val ASSERTIONS_CLASSNAME = "org.assertj.core.api.Assertions"

        @NonNls
        const val ABSTRACT_ASSERT_CLASSNAME = "org.assertj.core.api.AbstractAssert"
        @NonNls
        const val ABSTRACT_BOOLEAN_ASSERT_CLASSNAME = "org.assertj.core.api.AbstractBooleanAssert"
        @NonNls
        const val ABSTRACT_INTEGER_ASSERT_CLASSNAME = "org.assertj.core.api.AbstractIntegerAssert"
        @NonNls
        const val ABSTRACT_COMPARABLE_ASSERT_CLASSNAME = "org.assertj.core.api.AbstractComparableAssert"
        @NonNls
        const val ABSTRACT_STRING_ASSERT_CLASSNAME = "org.assertj.core.api.AbstractStringAssert"
        @NonNls
        const val ABSTRACT_CHAR_SEQUENCE_ASSERT_CLASSNAME = "org.assertj.core.api.AbstractCharSequenceAssert"
        @NonNls
        const val ABSTRACT_ITERABLE_ASSERT_CLASSNAME = "org.assertj.core.api.AbstractIterableAssert"
        @NonNls
        const val ABSTRACT_ENUMERABLE_ASSERT_CLASSNAME = "org.assertj.core.api.EnumerableAssert"

        @NonNls
        const val GUAVA_OPTIONAL_CLASSNAME = "com.google.common.base.Optional"
        @NonNls
        const val GUAVA_ASSERTIONS_CLASSNAME = "org.assertj.guava.api.Assertions"
    }
}