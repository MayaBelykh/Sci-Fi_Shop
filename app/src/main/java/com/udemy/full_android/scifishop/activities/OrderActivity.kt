package com.udemy.full_android.scifishop.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import com.udemy.full_android.scifishop.model.Order.Companion.ordersList
import com.udemy.full_android.scifishop.R
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.t_goods_row.*
import kotlinx.android.synthetic.main.t_goods_row.view.*

lateinit var userName: String

class OrderActivity : AppCompatActivity() {

    var inflater = { id: Int, parent: ViewGroup? ->
        (this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(id, parent, false)
    }

    companion object {
        lateinit var O_CON: Context
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        O_CON = this
        userName = intent.getStringExtra("userName") ?: "Anon"
        t_goods.removeAllViews()
        ordersList[userName]?.apply {
            t_goods.weightSum = 3f
            goodsName.forEachIndexed { i, name ->
                if (quantity[i] != 0)
                    t_goods.addView((inflater(R.layout.t_goods_row, t_goods)).apply {
                        tv_o_number.text = (i + 1).toString()
                        tv_o_items_name.text = name
                        iv_o_photo_item.setImageDrawable(
                            com.udemy.full_android.scifishop.utils.draw(
                                name
                            )
                        )
                        tv_o_quantity.text = quantity[i].toString()
                        tv_o_price.text = orderPrice[i].toString() + "$"
                        elevation = 10f
                    })
            }
            tv_final_order_price.text = "${ordersList[userName]?.orderPrice?.sum()}$"
        } ?: viewEmptyCart()
        if (t_goods.childCount == 0) viewEmptyCart()
    }

    fun viewEmptyCart() {
        t_goods.addView(TextView(this).apply {
            text = "Корзина пуста"
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            gravity = Gravity.CENTER_HORIZONTAL
            tv_final_order_price.text = "0$"
        })
    }

    fun returnHome(view: View) {
        startActivity(Intent(O_CON, MainActivity::class.java).apply {
            //            putExtra("userName", userName)
        })
    }

    @SuppressLint("SetTextI18n")
    fun clearCart(view: View) {
        ordersList.remove(userName)
        t_goods.apply {
            removeAllViews()
            addView(TextView(O_CON).apply {
                text = "Корзина пуста"
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                gravity = Gravity.CENTER_HORIZONTAL
            })
        }
        tv_final_order_price.text = "0$"

//        "Корзина очищена".toast()
    }

    @SuppressLint("SetTextI18n")
    fun delItem(view: View) {
        var index =
            ((view.parent.parent as LinearLayout)[0] as TextView).text.toString().toInt() - 1
        if (t_goods.childCount > 1) {
            t_goods.removeViewAt(index)
            for (i in 0 until t_goods.childCount)
                ((t_goods.getChildAt(i) as LinearLayout)[0] as TextView).text = "${i + 1}"
            ordersList[userName]?.removeItem(index)
            tv_final_order_price.text = "${ordersList[userName]?.orderPrice?.sum()}$"
        } else clearCart(view)
    }

    @SuppressLint("SetTextI18n")
    fun onClickOrderChangeQuantity(view: View) {
        var itemName =
            (((view.parent.parent as LinearLayout)[1] as LinearLayout)[0] as TextView).text.toString()
        var vQuantity = (((view.parent.parent as LinearLayout)[2] as LinearLayout)[1] as TextView)
        var vPrice = (((view.parent.parent as LinearLayout)[2] as LinearLayout)[3] as TextView)
        var price: Int
        var count: Int
        var i = 0

        ordersList[userName]?.goodsName?.forEachIndexed { index, name ->
            if (name == itemName) i = index
        }
        ordersList[userName]?.apply {
            count = quantity[i]
            price = priceItem[i]

            if (count == 0 && view.id == b_o_minus.id) return
            count = when (view.id) {
                b_o_minus.id -> --count
                b_o_plus.id -> ++count
                else -> count
            }

            vQuantity.text = count.toString()
            vPrice.text = "${(price * count)}$"
            quantity[i] = count
            orderPrice[i] = price * count
            tv_final_order_price.text = "${orderPrice.sum()}$"
        }
    }

    fun buyGoods(view: View) {
        startActivity(Intent(O_CON, FinalActivity::class.java).apply {
                        putExtra("userName", userName)
        })
    }

}
