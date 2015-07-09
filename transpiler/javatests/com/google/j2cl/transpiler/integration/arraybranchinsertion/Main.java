package com.google.j2cl.transpiler.integration.arraybranchinsertion;

public class Main {
  public static void main(String... args) {
    testFullArray();
    testPartialArray();
  }

  private static void testFullArray() {
    Object[][] array2d = new HasName[2][2];
    assert array2d[0].length == 2;
    assert array2d.length == 2;

    // You can swap out an entire array in an array slot.
    array2d[0] = new HasName[2];
    assert array2d[0].length == 2;

    // When inserting an array into an array slot you *can* change the length.
    array2d[0] = new HasName[4];
    assert array2d[0].length == 4;

    // When inserting an array into an array slot you can tighten the leaf type as a class or
    // interface.
    array2d[0] = new Person[2];
    assert array2d[0].length == 2;
    array2d[0] = new HasFullName[2];
    assert array2d[0].length == 2;

    // When inserting an array into an array slot you can NOT broaden the leaf type.
    try {
      array2d[0] = new Object[2];
      assert false : "An expected failure did not occur.";
    } catch (ArrayStoreException e) {
      // expected
    }

    // You can NOT put an object in a slot that expects an array.
    try {
      ((Object[]) array2d)[0] = new Object();
      assert false : "An expected failure did not occur.";
    } catch (ArrayStoreException e) {
      // expected
    }

    // When inserting an array into an array slot you can not change the number of dimensions.
    try {
      array2d[0] = new HasName[2][2];
      assert false : "An expected failure did not occur.";
    } catch (ArrayStoreException e) {
      // expected
    }
  }

  private static void testPartialArray() {
    // You can create a partially initialized array.
    Object[][] partialArray = new Object[1][];
    assert partialArray.length == 1;

    // You can fill the uninitialized dimensions with the same type.
    partialArray[0] = new Object[100];
    assert partialArray[0].length == 100;

    // Or with a subtype.
    partialArray[0] = new Person[100];
    assert partialArray[0].length == 100;
  }
}
