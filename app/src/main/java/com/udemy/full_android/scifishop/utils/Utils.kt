package com.udemy.full_android.scifishop.utils

import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.udemy.full_android.scifishop.R
import com.udemy.full_android.scifishop.activities.MainActivity.Companion.ACT
import com.udemy.full_android.scifishop.activities.MainActivity.Companion.CON
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.random.nextULong

var log = { msg: Any? -> Log.d("LOG::::::::::::", msg.toString()) }

fun <T> Any?.cast() = this as? T
fun Any?.toast() = Toast.makeText(CON, this.toString(), Toast.LENGTH_LONG).show()

infix fun String.contain(sub: String) = this.contains(sub, true)
infix fun Float.round(prec: Int) = (this * (10F).pow(prec)).roundToInt() / (10F).pow(prec)

inline fun <reified T> get(id: Int): T = when (T::class) {
    String::class -> ACT.resources.getString(id) as T
    Int::class ->
        (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ACT.resources.getColor(id, CON.theme)
        else ACT.resources.getColor(id)) as T
    Drawable::class -> ACT.resources.getDrawable(id, CON.theme) as T
    else -> ACT.findViewById<View>(id) as T
}

fun draw(name: String): Drawable = get(
    when (name) {
        "Headphone \"Vulcan ears\"" -> R.drawable.uhi
        "Scheme of FireFly" -> R.drawable.firefly
        "Spock action figure" -> R.drawable.spock
        else -> R.drawable.spirk_love
    }
)

inline fun <reified T> random(start: T, end: T): T = when (T::class) {
    Int::class -> Random.nextInt(start as Int, end as Int) as T
    UInt::class -> Random.nextUInt(start as UInt, end as UInt) as T
    Long::class -> Random.nextLong(start as Long, end as Long) as T
    ULong::class -> Random.nextULong(start as ULong, end as ULong) as T
    Float::class, Double::class -> Random.nextDouble(start as Double, end as Double) as T
    else -> null as T
}

fun changeVisibility(vararg v: View) = v.forEach {
    if (it.visibility == ViewGroup.INVISIBLE) it.visibility = ViewGroup.VISIBLE
    else it.visibility = ViewGroup.INVISIBLE
}
