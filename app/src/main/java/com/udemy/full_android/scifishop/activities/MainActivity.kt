package com.udemy.full_android.scifishop.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.udemy.full_android.scifishop.*
import com.udemy.full_android.scifishop.model.Order
import com.udemy.full_android.scifishop.model.Order.Companion.ordersList
import com.udemy.full_android.scifishop.utils.changeVisibility
import com.udemy.full_android.scifishop.utils.draw
import com.udemy.full_android.scifishop.utils.random
import com.udemy.full_android.scifishop.utils.toast
import kotlinx.android.synthetic.main.activity_main.*

typealias AV_Listener = AdapterView.OnItemSelectedListener

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var countQuantity = 1
    var price = 0
    lateinit var spin: Spinner
    lateinit var goodsPrice: HashMap<String, Int>
    lateinit var goodsImage: HashMap<String, Drawable>

    companion object {
        lateinit var CON: Context
        lateinit var ACT: Activity
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CON = this
        ACT = this
        var goodsArray = arrayListOf(
            "Headphone \"Vulcan ears\"",
            "Scheme of FireFly",
            "Spock action figure"
        )
        goodsImage = getMapOfGoodsImage(goodsArray)
        goodsPrice = getMapOfGoodsPrice(goodsArray)
        spin = setSpinner(this, this, goodsArray)
        tv_sale_code.text = "Промокод на скидку: I<3ST"
    }

    fun setSpinner(con: Context, listener: AV_Listener, aList: ArrayList<String>) = spinner.apply {
        onItemSelectedListener = listener
        adapter = ArrayAdapter(con, android.R.layout.simple_spinner_item, aList)
            .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }

    fun getMapOfGoodsImage(goodsArrayList: ArrayList<String>): HashMap<String, Drawable> {
        var goodsMap: HashMap<String, Drawable> = hashMapOf()
        goodsArrayList.forEach { goodsMap[it] = draw(it) }
        return goodsMap
    }

    fun getMapOfGoodsPrice(goodsArrayList: ArrayList<String>): HashMap<String, Int> {
        var goodsMap: HashMap<String, Int> = hashMapOf()
        goodsArrayList.forEach { goodsMap[it] = random(1, 1000) }
        return goodsMap
    }

    @SuppressLint("SetTextI18n")
    fun onClickChangeQuantity(view: View) {
        if (countQuantity == 0 && view.id == b_minus.id) return
        countQuantity = when (view.id) {
            b_minus.id -> --countQuantity
            b_plus.id -> ++countQuantity
            else -> countQuantity
        }
        tv_count.text = countQuantity.toString()
        tv_order_price.text = "${(price * countQuantity)}$"

    }

    fun onClickAddToCart(view: View) {
        if (tv_count.text != "0") {
            var userName = et_user_name.text.toString()
            if (userName.hashCode() == 0) userName = "NULL"
            var price = tv_order_price.text.toString()
            if (!ordersList.containsKey(userName)) ordersList[userName] =
                Order(userName)
            ordersList[userName]!!.updateItem(
                name = spin.selectedItem.toString(),
                quantity = tv_count.text.toString().toInt(),
                price = price.substring(
                    0,
                    price.length - 1
                ).toInt() / tv_count.text.toString().toInt(),
                orderPrice = price.substring(0, price.length - 1).toInt()
            )
            "${spin.selectedItem} в корзине".toast()
            Log.d("LOG____________", ordersList[userName].toString())
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var itemName = spin.selectedItem.toString()
        price = goodsPrice[itemName] ?: 0
        countQuantity = 1
        tv_count.text = countQuantity.toString()
        tv_order_price.text = "${(price * countQuantity)}$"
        iv_item.setImageDrawable(goodsImage[itemName])
        iv_item.scaleType = if (itemName.contains("Fire")) ImageView.ScaleType.CENTER_CROP
        else ImageView.ScaleType.CENTER_INSIDE
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun goToCart(view: View) {
        var userName = et_user_name.text.toString()
        if (userName.hashCode() == 0) userName = "NULL"
        startActivity(Intent(CON, OrderActivity::class.java).apply {
            putExtra("userName", userName)
        })
    }

    fun showSaleCode(view: View) {
        changeVisibility(iv_sale, tv_sale, tv_sale_code)
    }
}
