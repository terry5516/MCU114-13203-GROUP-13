
package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class OrderViewModel : ViewModel() {
    val order = MutableLiveData(Order())

    // 添加伴生對象來實現單例
    companion object {
        private var instance: OrderViewModel? = null

        fun getInstance(): OrderViewModel {
            return instance ?: synchronized(this) {
                instance ?: OrderViewModel().also { instance = it }
            }
        }
    }

    fun updateMainMeal(meal: String) {
        order.value?.mainMeal = meal
        order.value = order.value
    }

    fun addSideDish(side: String) {
        order.value?.sideDishes?.add(side)
        order.value = order.value
    }

    fun removeSideDish(side: String) {
        order.value?.sideDishes?.remove(side)
        order.value = order.value
    }

    fun updateDrink(drink: String) {
        order.value?.drink = drink
        order.value = order.value
    }

    fun clearOrder() {
        order.value = Order()
    }
}