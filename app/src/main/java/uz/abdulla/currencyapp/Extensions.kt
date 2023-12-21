package uz.abdulla.currencyapp

class A(val a : String, val b : Int, val c : Int)

class B(val a :String, val b : Float)

fun A.toB() : B = B(
    b = this.b.toFloat(),
    a = this.a
)

fun a(){
    val  ll = listOf<A>()
    val llb = ll.map { it.toB() }
}