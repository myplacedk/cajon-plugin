# Cajon - Concise AssertJ Optimizing Nitpicker

Cajon is an IntelliJ Plugin for shortening and optimizing AssertJ assertions.

## Why?

First, code is easier to read, when is concise and reflects the intention clearly.
AssertJ has plenty of different convenience methods that make describing the various intentions.
Why write longer, more complex code that can be expressed in brevity?

Second, AssertJ is able to output more meaningful descriptions when an assertion fails.
This makes finding bugs and fixing failed tests easier.

## Implemented

- AssertThatObjectIsNull
  > from: assertThat(object).isEqualTo(null);  
  >   to: assertThat(object).isNull();
- AssertThatObjectIsNotNull
  > from: assertThat(object).isNotEqualTo(null);
  >   to: assertThat(object).isNotNull();

## TODO
- AssertThatBooleanIsTrueOrFalse
  > from: assertThat(booleanValue).isEqualTo(true/false/Boolean.TRUE/Boolean.FALSE);
  >   to: assertThat(booleanValue).isTrue()/isFalse();
- AssertThatStringEmpty
  > from: assertThat(string).isEqualTo("")
  >   to: assertThat(string).isEmpty();
- AssertThatArrayHasLiteralSize
  > from: assertThat(array.length).isEqualTo(literal);  literal > 0
  >   to: assertThat(array).hasSize(literal);
- AssertThatArrayHasEqualSize
  > from: assertThat(array.length).isEqualTo(anotherArray.length);
  >   to: assertThat(array).hasSameSizeAs(anotherArray);
  > from: assertThat(array.length).isEqualTo(iterable.size());
  >   to: assertThat(array).hasSameSizeAs(iterable);
- AssertThatArrayIsEmpty
  > from: assertThat(array.length).isEqualTo(0);
  > from: assertThat(array.length).isLessThanOrEqualTo(0);
  > from: assertThat(array.length).isLessThan(1);
  > from: assertThat(array).hasSize(0);
  >   to: assertThat(array).isEmpty();
- AssertThatArrayIsNotEmpty
  > from: assertThat(array.length).isGreaterThan(0);
  >   to: assertThat(array).isNotEmpty();
- AssertThatIterableHasLiteralSize
  > from: assertThat(iterable.size()).isEqualTo(literal);  literal > 0
  >   to: assertThat(iterable).hasSize(literal);
- AssertThatIterableHasEqualSize
  > from: assertThat(iterable.size()).isEqualTo(anotherArray.length);
  >   to: assertThat(iterable).hasSameSizeAs(anotherArray);
  > from: assertThat(iterable.size()).isEqualTo(iterable.size());
  >   to: assertThat(iterable).hasSameSizeAs(iterable);
- AssertThatIterableIsNotEmpty
  > from: assertThat(array.length).isGreaterThan(0);
  > from: assertThat(array.length).isGreaterThanOrEqualTo(1);
  >   to: assertThat(array).isNotEmpty();
- AssertThatIterableIsEmpty
  > from: assertThat(iterable.size()).isEqualTo(0);
  > from: assertThat(iterable.size()).isLessThanOrEqualTo(0);
  > from: assertThat(iterable.size()).isLessThan(1);
  > from: assertThat(iterable).hasSize(0);
  >   to: assertThat(iterable).isEmpty();
