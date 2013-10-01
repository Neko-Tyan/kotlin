package o

trait A<T>

fun <T> foo(a: A<T>, aN: A<T<!BASE_WITH_NULLABLE_UPPER_BOUND!>?<!>>): T = throw Exception("$a $aN")
fun <T> bar(a: A<T>, aN: A<T<!BASE_WITH_NULLABLE_UPPER_BOUND!>?<!>>): T = throw Exception("$a $aN")

fun <T> doA(a: A<T>): T = throw Exception("$a")

fun test(a: A<Int>, aN: A<Int?>) {
    val aa = doA(aN)
    <!TYPE_MISMATCH!>aa<!> : Int
    aa : Int?

    val foo = foo(aN, aN)
    //T = Int?, T? = Int? => T = Int?
    foo : Int?

    val bar = bar(a, aN)
    //T = Int, T? = Int? => T = Int
    bar: Int
}

//-------------------

trait Out<out T>

fun <T> baz(a: A<T>, o: Out<T<!BASE_WITH_NULLABLE_UPPER_BOUND!>?<!>>): T = throw Exception("$a $o")

fun <T> doOut(o: Out<T<!BASE_WITH_NULLABLE_UPPER_BOUND!>?<!>>): T = throw Exception("$o")

fun test(a: A<Int>, aN: A<Int?>, o: Out<Int?>) {
    val out = doOut(o)
    //T? >: Int? => T >: Int
    out: Int

    val baz1 = baz(aN, o)
    //T = Int?, T? >: Int? => T = Int?
    <!TYPE_MISMATCH!>baz1<!>: Int
    baz1: Int?


    val baz2 = baz(a, o)
    //T = Int, T? >: Int? => T = Int
    baz2: Int
}

//-------------------


trait In<in T>

fun <T> hhh(a: A<T>, i: In<T>): T = throw Exception("$a $i")

fun <T> doIn(i: In<T<!BASE_WITH_NULLABLE_UPPER_BOUND!>?<!>>): T = throw Exception("$i")

fun test(a: A<Int>, i: In<Int?>) {
    val _in = doIn(i)
    //T? <: Int? => T <: Int?
    <!TYPE_MISMATCH!>_in<!> : Int
    _in : Int?

    val h = hhh(a, i)
    //T = Int, T? <: Int? => T = Int
    h: Int
}

//-------------------

fun <T: Any> foo(t: T?): T = throw Exception("$t")
fun <T: Any> foo(o: Out<T?>): T { throw Exception("$o") }
fun <T: Any> foo(i: In<T?>) { throw Exception("$i") }
fun <T: Any> foo(i: A<T?>) { throw Exception("$i") }

fun test(out: Out<Int>, i: In<Int>, inv: A<Int>) {
    // T? >: Int => T = Int
    foo(1)
    val r = foo(out)
    r: Int

    // T? <: Int => error
    foo(<!TYPE_MISMATCH!>i<!>)

    // T? >: Int => error
    foo(<!TYPE_MISMATCH!>inv<!>)
}