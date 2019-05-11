# Cajon - Concise AssertJ Optimizing Nitpicker [![Build Status](https://travis-ci.org/chrisly42/cajon-plugin.svg?branch=master)](https://travis-ci.org/chrisly42/cajon-plugin) [![Coverage Status](https://coveralls.io/repos/github/chrisly42/cajon-plugin/badge.svg?branch=master)](https://coveralls.io/github/chrisly42/cajon-plugin?branch=master)

Cajon is an IntelliJ IDEA Plugin for shortening and optimizing [AssertJ](https://assertj.github.io/doc/) assertions.

## Purpose

First, code is easier to read, when it is concise and reflects the intention clearly.
AssertJ has plenty of different convenience methods that describing various intentions precisely.
Why write longer, more complex code that can be expressed in brevity?

Second, when using the available special assertion methods of AssertJ, a failure of a condition
can be expressed in better detail and with more meaningful descriptions.
This makes finding bugs and fixing failed tests more efficient.
Nobody likes to read failures of the kind "failed because true is not false".

For example:

```
assertThat(collection.size()).isEqualTo(5);
```

If the collection has more or less than five elements, the assertion will fail, but will not
tell you about the contents, making it hard to guess what went wrong.

Instead, if you wrote the same assertion the following way:

```
assertThat(collection).hasSize(5);
```

Then AssertJ would tell you the _actual contents_ of the collection on failure.

## Conversion of JUnit assertions to AssertJ

The plugin also supports the conversion of the most common JUnit 4 assertions to AssertJ.

## Lookup and refactoring of string-based extracting()

AssertJ allows [extracting POJO fields/properties on iterables/arrays](http://joel-costigliola.github.io/assertj/assertj-core-features-highlight.html#extracted-properties-assertion).

Using strings is not safe for refactoring (and before Java 8 Lambdas were available,
creating extractor functions just for testing purpose was a bit too tedious).

This plugin adds support for referencing these fields (so you can ctrl(/cmd)-click on the 
string to go to the definition) and also allows safe refactoring on the 
fields (refactoring a getter method without a corresponding field will not work 
correctly right now).

## Usage

The plugin will report inspections in your opened editor file as warnings.
You can then quick-fix these with your quick-fix hotkey (usually Alt-Return or Opt-Return).

Or, you can use the "Run Inspection by Name..." action to run one inspection on a bigger scope (e.g. the whole project).
Applying a quick fix might result in further optimization possibilities, so 
you might need to perform a couple of fixes before you get to the final result.

Check out this example where every line represents the result after a Cajon quickfix:
```
assertFalse(!(array.length == collection.size()));

assertThat(!(array.length == collection.size())).isFalse();

assertThat(array.length == collection.size()).isTrue();

assertThat(array.length).isEqualTo(collection.size());

assertThat(array).hasSameSizeAs(collection);
```

You can toggle the various inspections in the Settings/Editor/Inspections in the AssertJ group.

## Implemented inspections and quickfixes

- JoinAssertThatStatements
  
  Joins multiple ```assertThat()``` statements with same actual expression together.

  ```
  from: assertThat(expected).someCondition();
        assertThat(expected).anotherCondition();
    to: assertThat(expected).someCondition().anotherCondition();
  ```
  Joining will work on actual expressions inside ```assertThat()``` that are equivalent expressions,
  except for method calls with known side-effect methods such as ```Iterator.next()``` and
  pre/post-increment/decrement operations -- please notify me about others.

  The comments of the statements will be preserved. When using ```.extracting()``` or similar, the statements will not be merged.

- AssertThatObjectIsNullOrNotNull

  Uses ```isNull()``` and ```isNotNull()``` instead.

  ```
  from: assertThat(object).isEqualTo(null);
    to: assertThat(object).isNull();

  from: assertThat(object).isNotEqualTo(null);  
    to: assertThat(object).isNotNull();
  ```

- AssertThatBooleanCondition

  Uses ```isTrue()``` and ```isFalse()``` instead.

  ```
  from: assertThat(booleanValue).isEqualTo(true/false/Boolean.TRUE/Boolean.FALSE);  
    to: assertThat(booleanValue).isTrue()/isFalse();
  ```

- AssertThatInvertedBooleanCondition

  Inverts the boolean condition to make it more readable.
  
  ```
  from: assertThat(!booleanValue).isEqualTo(true/false/Boolean.TRUE/Boolean.FALSE);  
  from: assertThat(!booleanValue).isTrue()/isFalse();  
    to: assertThat(booleanValue).isFalse()/isTrue();
  ```

- AssertThatInstanceOf

  Moves ```instanceof``` expressions out of ```assertThat()```.
  
  ```
  from: assertThat(object instanceof classname).isEqualTo(true);
  from: assertThat(object instanceof classname).isTrue();
    to: assertThat(object).isInstanceOf(classname.class);

  from: assertThat(object instanceof classname).isEqualTo(false);
  from: assertThat(object instanceof classname).isFalse();
    to: assertThat(object).isNotInstanceOf(classname.class);
  ```

- AssertThatStringIsEmpty

  Uses ```isEmpty()``` for empty string assertions.

  ```
  from: assertThat(charSequence/string).isEqualTo("");
  from: assertThat(charSequence/string).hasSize(0);
    to: assertThat(charSequence/string).isEmpty();
  ```

  The ```assertThat(string.length()).isEqualTo(0);``` case is handled in the AssertThatSize inspection.

- AssertThatStringExpression
  
  Moves string operations inside ```assertThat()``` out.
  
  ```
  from: assertThat(stringActual.isEmpty()).isTrue();
    to: assertThat(stringActual).isEmpty();

  from: assertThat(stringActual.equals(stringExpected)).isTrue();
  from: assertThat(stringActual.contentEquals(charSeqExpected)).isTrue();
    to: assertThat(stringActual).isEqualTo(stringExpected);

  from: assertThat(stringActual.equalsIgnoreCase(stringExpected)).isTrue();
    to: assertThat(stringActual).isEqualToIgnoringCase(stringExpected);

  from: assertThat(stringActual.contains(stringExpected)).isTrue();
    to: assertThat(stringActual).contains(stringExpected);

  from: assertThat(stringActual.startsWith(stringExpected)).isTrue();
    to: assertThat(stringActual).startsWith(stringExpected);

  from: assertThat(stringActual.endsWith(stringExpected)).isTrue();
    to: assertThat(stringActual).endsWith(stringExpected);
  ```
  Analogously with ```isFalse()```.

- AssertThatCollectionOrMapExpression

  Moves collection and map operations inside ```assertThat()``` out.

  ```
  from: assertThat(collection.isEmpty()).isTrue();
    to: assertThat(collection).isEmpty();

  from: assertThat(collection.contains("foobar")).isTrue();
    to: assertThat(collection).contains("foobar");
    
  from: assertThat(collection.containsAll(otherCollection)).isTrue();
    to: assertThat(collection).containsAll(otherCollection);

  from: assertThat(map.isEmpty()).isTrue();
    to: assertThat(map).isEmpty();

  from: assertThat(map.containsKey(key)).isTrue();
    to: assertThat(map).containsKey(key);
    
  from: assertThat(map.containsValue(value)).isTrue();
    to: assertThat(map).containsValue(value);
  ```
  Analogously with ```isFalse()``` (except for ```containsAll()```).

- AssertThatEnumerableIsEmpty

  Uses ```isEmpty()``` for ```hasSize(0)``` iterable assertions instead.
  
  ```
  from: assertThat(enumerable).hasSize(0);
    to: assertThat(enumerable).isEmpty();
  ```

- AssertThatSize

  Makes assertions on sizes of arrays, collections, strings, 
  or ```CharSequence```s more concise.

  ```
  from: assertThat(array.length).isEqualTo(0);
  from: assertThat(array.length).isLessThanOrEqualTo(0);
  from: assertThat(array.length).isLessThan(1);
  from: assertThat(array.length).isZero();
    to: assertThat(array).isEmpty();

  from: assertThat(array.length).isGreaterThan(0);
  from: assertThat(array.length).isGreaterThanOrEqualTo(1);
  from: assertThat(array.length).isNotZero();
    to: assertThat(array).isNotEmpty();

  from: assertThat(array.length).isEqualTo(anotherArray.length);
    to: assertThat(array).hasSameSizeAs(anotherArray);
    
  from: assertThat(array).hasSize(anotherArray.length);
    to: assertThat(array).hasSameSizeAs(anotherArray);
  ```

  and additionally with AssertJ 13.2.0 or later

  ```
  from: assertThat(array.length).isLessThanOrEqualTo(expression);
    to: assertThat(array).hasSizeLessThanOrEqualTo(expression);
    
  from: assertThat(array.length).isLessThan(expression);
    to: assertThat(array).hasSizeLessThan(expression);

  from: assertThat(array.length).isGreaterThan(expression);
    to: assertThat(array).hasSizeGreaterThan(expression);

  from: assertThat(array.length).isGreaterThanOrEqualTo(expression);
    to: assertThat(array).hasSizeGreaterThanOrEqualTo(expression);
  ```
  and analogously for collections, strings and CharSequences, e.g:

  ```
  from: assertThat("string".length()).isLessThan(1);
    to: assertThat("string").isEmpty();

  from: assertThat("string".length()).isEqualTo(collection.size())
    to: assertThat("string").hasSameSizeAs(collection);
    
  from: assertThat("string".length()).hasSize("strong".length())
    to: assertThat("string").hasSameSizeAs("strong");
  ```

- AssertThatBinaryExpression

  Splits a boolean condition represented by binary expression out of ```assertThat()```.

  ```
  from: assertThat(primActual == primExpected).isTrue();
    to: assertThat(primActual).isEqualTo(primExpected);

  from: assertThat(10 < primActual).isNotEqualTo(false);
    to: assertThat(primActual).isGreaterThan(primExpected);

  from: assertThat(objActual != objExpected).isEqualTo(true);
    to: assertThat(objActual).isNotSameAs(objExpected);

  from: assertThat(null == objActual).isFalse();
    to: assertThat(objActual).isNotNull();

  from: assertThat(objActual.equals(objExpected).isTrue();
    to: assertThat(objActual).isEqualTo(objExpected);
  ```
  ...and many, many more combinations (more than 150).

- AssertThatJava8Optional

  Examines the statement for Java 8 ```Optional``` type and whether the statement
  effectively tries to assert the presence, absence or content and then 
  replaces the statement by better assertions.
  
  ```
  from: assertThat(opt.isPresent()).isEqualTo(true);
  from: assertThat(opt.isPresent()).isNotEqualTo(false);
  from: assertThat(opt.isPresent()).isTrue();
    to: assertThat(opt).isPresent();

  from: assertThat(opt.isPresent()).isEqualTo(false);
  from: assertThat(opt.isPresent()).isNotEqualTo(true);
  from: assertThat(opt.isPresent()).isFalse();
    to: assertThat(opt).isNotPresent();
    
  from: assertThat(opt.get()).isEqualTo("foo");
    to: assertThat(opt).contains("foo");
     
  from: assertThat(opt.get()).isSameAs("foo");
    to: assertThat(opt).containsSame("foo"); 

  from: assertThat(opt).isEqualTo(Optional.of("foo"));
  from: assertThat(opt).isEqualTo(Optional.ofNullable("foo"));
    to: assertThat(opt).contains("foo"); 

  from: assertThat(opt).isEqualTo(Optional.empty());
    to: assertThat(opt).isNotPresent();

  from: assertThat(opt).isNotEqualTo(Optional.empty());
    to: assertThat(opt).isPresent();
  ```

- AssertThatGuavaOptional

  Examines the statement for Google Guava ```Optional``` type and whether the statement
  effectively tries to assert the presence, absence or content and then 
  replaces the statement by better assertions.

  ```
  from: assertThat(opt.isPresent()).isEqualTo(true);
  from: assertThat(opt.isPresent()).isNotEqualTo(false);
  from: assertThat(opt.isPresent()).isTrue();
    to: assertThat(opt).isPresent();

  from: assertThat(opt.isPresent()).isEqualTo(false);
  from: assertThat(opt.isPresent()).isNotEqualTo(true);
  from: assertThat(opt.isPresent()).isFalse();
    to: assertThat(opt).isAbsent();
    
  from: assertThat(opt.get()).isEqualTo("foo");
    to: assertThat(opt).contains("foo");
     
  from: assertThat(opt).isEqualTo(Optional.of("foo"));
  from: assertThat(opt).isEqualTo(Optional.fromNullable("foo"));
    to: assertThat(opt).contains("foo"); 

  from: assertThat(opt).isEqualTo(Optional.absent());
    to: assertThat(opt).isAbsent();

  from: assertThat(opt).isNotEqualTo(Optional.absent());
    to: assertThat(opt).isPresent();
  ```

  AssertJ for Guava needs to be available in the classpath.

- AssumeThatInsteadOfReturn

  Tries to detect bogus uses of return statements in test methods and replaces them by ```assumeThat()``` calls.
  
  Novices will use these to skip test execution by bailing out early on some preconditions not met.
  However, this suggests that the test has actually been run and passed instead of showing the test
  as being skipped.

  Return statements in ```if``` statements in main test methods (must be annotated with JUnit 4 or 
  Jupiter @Test annotations) will be verified to have at least one ```assertThat()``` statement in the code flow.
  Method calls within the same class will be examined for ```assertThat()``` statements, too.
  However, at most 50 statements and down to five recursions will be tolerated before giving up.
  
  Currently, the quickfix may lose some comments during operation. The other branch of the ```if``` statement
  will be inlined (blocks with declarations will remain a code block due to variable scope).
  
  The generated ```assumeThat()``` statement could be optimized further (similar to ```assertThat()```).

  Example:
  
  ```
      @Test
      public void check_fuel_emission() {
          if (System.getProperty("manufacturer").equals("Volkswagen")) {
              return;
          }
          double nitroxppm = doWltpDrivingCycle();
          assertThat(nitroxppm).isLessThan(500.0);
      }
  ```
  will be transformed to
  ```
      @Test
      public void check_fuel_emission() {
          assumeThat(System.getProperty("manufacturer").equals("Volkswagen")).isFalse();
          double nitroxppm = doWltpDrivingCycle();
          assertThat(nitroxppm).isLessThan(500.0);
      }
  ```

- JUnitAssertToAssertJ

  Tries to convert most of the JUnit 4 assertions to AssertJ format.

  Does not support Hamcrest-Matchers.
  If you need that kind of conversion, you might want to check out the
  [Assertions2AssertJ plugin](https://plugins.jetbrains.com/plugin/10345-assertions2assertj) by Ric Emery.
  
  ```
  assertTrue(condition);
  assertTrue(message, condition);
  assertFalse(condition);
  assertFalse(message, condition);
  assertNull(object);
  assertNull(message, object);
  assertNonNull(object);
  assertNonNull(message, object);
  assertEquals(expected, actual);
  assertEquals(message, expected, actual);
  assertEquals(expectedDoubleOrFloat, actualDoubleOrFloat, delta);
  assertEquals(message, expectedDoubleOrFloat, actualDoubleOrFloat, delta);
  assertNotEquals(unexpected, actual);
  assertNotEquals(message, unexpected, actual);
  assertNotEquals(unexpectedDoubleOrFloat, actualDoubleOrFloat, delta);
  assertNotEquals(message, unexpectedDoubleOrFloat, actualDoubleOrFloat, delta);
  assertSame(expected, actual);
  assertSame(message, expected, actual);
  assertNotSame(unexpected, actual);
  assertNotSame(message, unexpected, actual);
  assertArrayEquals(expected, actual);
  assertArrayEquals(message, expectedArray, actualArray);
  assertArrayEquals(expectedDoubleOrFloatArray, actualDoubleOrFloatArray, delta);
  assertArrayEquals(message, expectedDoubleOrFloatArray, actualDoubleOrFloatArray, delta);
  ```

### Implemented referencing

  ```
  .extracting("field")
  .extracting("outerField.fieldInsideObjectTypeOfOuterField.andSoOn")
  .extracting("property") // where the class has a getProperty() (or isProperty() for boolean) method
  .extracting("bareMethod") // supported with AssertJ 13.12.0
  .extracting(Extractors.byName("fieldOrPropertyOrBareMethod")
  .extracting(Extractors.byName("fieldOrPropertyOrBareMethod.orAPathLikeAbove")
  .extracting(Extractors.resultOf("bareMethod")
  .extractingResultOf("bareMethod")
  .flatExtracting("fieldOrPropertyOrBareMethod.orAPathLikeAbove")
  .flatExtracting(Extractors.byName("fieldOrPropertyOrBareMethod.orAPathLikeAbove")
  .flatExtracting(Extractors.resultOf("bareMethod")
  ```
  Works on both POJOs and ```Iterable```s/```Array```s. 
  Implementation is very basic though and does not work with fancy cascaded .extracting() sequences.
  If there's demand, I will add it.

## Development notice

Cajon is written in Kotlin 1.3.

Cajon is probably the only plugin that uses JUnit 5 Jupiter for unit testing so far (or at least the only one that I'm aware of ;) ).
The IntelliJ framework actually uses the JUnit 3 TestCase for plugin testing and it took me quite a while to make it work with JUnit 5.
Feel free to use the code (in package ```de.platon42.intellij.jupiter```) for your projects (with attribution).

## Planned features
- Joining .contains() expressions
- Removing .isPresent().contains() combinations for Optionals
- Extraction with property names to lambda with Java 8
  ```
  from: assertThat(object).extracting("propOne", "propNoGetter", "propTwo.innerProp")...
    to: assertThat(object).extracting(type::getPropOne, it -> it.propNoGetter, it -> it.getPropTwo().getInnerProp())...
  ```

## Changelog

#### V1.1 (unreleased)
- Improved JoinAssertThatStatements detection of expressions with side-effects and added pre/post-increment/decrement detection.

#### V1.0 (06-May-19)
- First release to be considered stable enough for production use.
- Fixed a NPE in AssumeThatInsteadOfReturn inspection quickfix for empty else branches.
- Fixed missing description for AssumeThatInsteadOfReturn inspection.
- Added new AssertThatCollectionOrMapExpression inspection that tries to pull out methods such as ```isEmpty()``` or ```contains()``` out of an actual ```assertThat()``` expression.

#### V0.8 (05-May-19)
- Fixed missing description for JoinAssertThatStatements and detection of equivalent expressions (sorry, released it too hastily).
- Fixed ```isEmpty()``` for enumerables and strings and ```isNull()``` for object conversions to be applied only if it is the terminal method call as ```isEmpty()``` and ```isNull()``` return void.
- Heavily reworked inspections for edge cases, such as multiple ```isEqualTo()``` calls inside a single statement.
- Some inspections could generate bogus code for weird situations, this has been made more fool-proof.
- Corrected highlighting for many inspections.
- Fixed family names for inspections in batch mode.
- Reworded many inspection messages for better understanding.
- Added a first version of a new inspection that tries to detect bogus uses of return statements in test methods and replaces them by ```assumeThat()``` calls.

#### V0.7 (28-Apr-19)
- Another fix for AssertThatGuavaOptional inspection regarding using the same family name for slightly different quick fix executions
  (really, Jetbrains, this sucks for no reason).
- Extended AssertThatSize inspection to transform ```hasSize()``` into ```hasSameSizeAs()```, if possible.
- Implemented first version of JoinAssertThatStatements inspection that will try to merge ```assertThat()``` statements with the same
  actual object together, preserving comments.

#### V0.6 (22-Apr-19)
- New AssertThatStringExpression inspection that will move ```isEmpty()```, ```equals()```, ```equalsIgnoreCase()```, ```contains()```,
  ```startsWith()```, and ```endsWith()``` out of actual expression.
- Extended AssertThatSize inspection to take ```String```s and ```CharSequences``` into account, too.
- New AssertThatInvertedBooleanCondition inspection that will remove inverted boolean expressions inside ```assertThat()```.
- Renamed a few inspections to better/shorter names.
- New AssertThatInstanceOf inspection that moves instanceof expressions out of ```assertThat()```.

#### V0.5 (18-Apr-19)
- Fixed incompatibility with IDEA versions < 2018.2 (affected AssertThatSizeInspection). Minimal version is now 2017.3.
- Fixed missing Guava imports (if not already present) for AssertThatGuavaInspection. This was a major PITA to get right.
- Added support for referencing and refactoring inside ```.extracting()``` methods with fields, properties and methods (though
  getter renaming does not work that perfect, but I'm giving up for now as the IntelliJ SDK docs are seriously lacking).
- Fixed an exception in batch mode if the description string was the same but for different fixes. 
  Now descriptions are different for quick fixes triggered by AssertThatJava8OptionalInspection and AssertThatGuavaOptionalInspection.

#### V0.4 (11-Apr-19)
- Reduced minimal supported IDEA version from 2018.2 to 2017.2.
- New inspection AssertThatJava8Optional that operates on Java 8 ```Optional``` objects and tries to use ```contains()```, ```containsSame()```, ```isPresent()```, and ```isNotPresent()``` instead.
- New inspection AssertThatGuavaOptional that operates on Guava ```Optional``` objects and tries to use ```contains()```, ```isPresent()```, and ```isAbsent()``` instead.
- Added support in AssertThatBinaryExpressionIsTrueOrFalse for ```is(Not)EqualTo(Boolean.TRUE/FALSE)```.

#### V0.3 (07-Apr-19)
- New inspection AssertThatBinaryExpressionIsTrueOrFalse that will find and fix common binary expressions and ```equals()``` statements (more than 150 combinations) inside ```assertThat()```.
- Merged AssertThatObjectIsNull and AssertThatObjectIsNotNull to AssertThatObjectIsNullOrNotNull.
- Support for ```hasSizeLessThan()```, ```hasSizeLessThanOrEqualTo()```, ```hasSizeGreaterThanOrEqualTo()```, and ```hasSizeGreaterThan()``` for AssertThatSizeInspection (with AssertJ >=13.2.0).
- Really fixed highlighting for JUnit conversion. Sorry.

#### V0.2 (01-Apr-19)
- Fixed descriptions and quick fix texts.
- Fixed highlighting of found problems and also 'Run inspection by Name' returning nothing.

#### V0.1 (31-Mar-19)
- Initial release.