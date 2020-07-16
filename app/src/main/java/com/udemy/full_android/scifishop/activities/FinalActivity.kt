package com.udemy.full_android.scifishop.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.udemy.full_android.scifishop.model.Order.Companion.ordersList
import com.udemy.full_android.scifishop.R
import com.udemy.full_android.scifishop.utils.toast
import kotlinx.android.synthetic.main.activity_final.*

lateinit var userNameF: String

class FinalActivity : AppCompatActivity() {
    companion object {
        lateinit var F_CON: Context
    }

    var inflater = { id: Int, parent: ViewGroup? ->
        (this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(id, parent, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)

        F_CON = this

        userNameF = intent.getStringExtra("userName") ?: "Anon"
        t_f_goods.removeAllViews()
        ordersList[userNameF]?.apply {
            t_f_goods.weightSum = 3f
            goodsName.forEachIndexed { i, name ->
                if (quantity[i] != 0)
                    t_f_goods.addView((inflater(R.layout.t_goods_final_row, t_f_goods)).apply {
                        findViewById<TextView>(R.id.tv_f_number).text = (i + 1).toString()
                        findViewById<TextView>(R.id.tv_f_items_name).text = name
                        findViewById<ImageView>(R.id.iv_f_photo_item).setImageDrawable(
                            com.udemy.full_android.scifishop.utils.draw(name)
                        )
                        findViewById<TextView>(R.id.tv_f_quantity).text = quantity[i].toString()
                        findViewById<TextView>(R.id.tv_f_price).text =
                            orderPrice[i].toString() + "$"
                        elevation = 10f
                    })
            }
            findViewById<TextView>(R.id.tv_f_final_order_price).text =
                "${ordersList[userNameF]?.orderPrice?.sum()}$"
        } ?: viewEmptyCart()
        if (t_f_goods.childCount == 0) viewEmptyCart()
    }

    fun viewEmptyCart() {
        t_f_goods.addView(TextView(this).apply {
            text = "Корзина пуста"
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            gravity = Gravity.CENTER_HORIZONTAL
            findViewById<TextView>(R.id.tv_f_final_order_price).text = "0$"
        })
    }

    fun returnHome(view: View) {
        startActivity(Intent(F_CON, MainActivity::class.java).apply {
            //                                    putExtra("userNameF", userName)
        })
    }

    fun payForGoods(view: View) {}

    fun enterPromoCode(view: View) {
        if (et_f_promo.text.toString().toUpperCase() != "I<3ST") "Недействительный промокод".toast()
        else {
            var tvPrice = findViewById<TextView>(R.id.tv_f_final_order_price)
            var text = tvPrice.text.toString()
            ib_f_promocode.setImageResource(R.drawable.toggle_on)
            tvPrice.text = (text.substring(0, text.length - 1).toInt() * 0.7).toString() + "$"
            "Промокод применен".toast()
        }
    }
}
