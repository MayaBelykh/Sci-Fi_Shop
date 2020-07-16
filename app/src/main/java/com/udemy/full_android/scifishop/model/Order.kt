package com.udemy.full_android.scifishop.model

class Order(val userName: String = "anon") {
    var goodsName: MutableList<String> = mutableListOf()
    var quantity: MutableList<Int> = mutableListOf()
    var priceItem: MutableList<Int> = mutableListOf()
    var orderPrice: MutableList<Int> = mutableListOf()

    companion object {
        var ordersList = mutableMapOf<String, Order>()
    }

    fun updateItem(name: String, quantity: Int, price: Int, orderPrice: Int) {
        if (quantity == 0) return
        if (goodsName.contains(name)) {
            goodsName.forEachIndexed { i, str ->
                if (str == name) {
                    this.quantity[i] = quantity
                    this.orderPrice[i] = orderPrice
                    this.priceItem[i] = price
                }
            }
        } else {
            goodsName.add(name)
            this.quantity.add(quantity)
            this.orderPrice.add(orderPrice)
            this.priceItem.add(price)

        }
    }

    fun removeItem(index: Int) {
        goodsName.removeAt(index)
        quantity.removeAt(index)
        orderPrice.removeAt(index)
        priceItem.removeAt(index)
    }

    override fun toString(): String = "Order [$userName]:\n$goodsName\n$quantity\n$orderPrice"
}