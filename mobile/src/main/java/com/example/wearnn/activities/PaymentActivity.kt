package com.example.wearnn.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wearnn.R
import com.example.wearnn.data.model.PaymentInfo

class PaymentActivity : AppCompatActivity() {

    companion object {
        const val euroSign: Char = '\u20AC'
        const val percentage = "%"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_activity)
        calculateDiscountAndUpdateUI()
    }

    private fun calculateDiscountAndUpdateUI() {
        val paymentAmountElement: TextView = findViewById(R.id.paymentValue) as TextView
        val discountElement: TextView = findViewById(R.id.activityDiscountValue) as TextView
        val remainingPaymentElement: TextView = findViewById(R.id.remainingPaymentValue) as TextView

        val paymentValueText: String = paymentAmountElement.text.toString()
        val discountPercentageText: String = discountElement.text.toString()
        val remainingValueText: String = remainingPaymentElement.text.toString()

        // Convert input to double, handle conversion errors gracefully
        val paymentAmount = paymentValueText.toDoubleOrNull() ?: 0.0
        val discountPercentage = discountPercentageText.toDoubleOrNull() ?: 0.0
        val remainingValue = remainingValueText.toDoubleOrNull() ?: 0.0

        val info = PaymentInfo(paymentAmount, discountPercentage, remainingValue)

        val discountAmount = info.paymentAmountValue * info.discountValue / 100
        val newRemainingValue = info.paymentAmountValue - discountAmount

        remainingPaymentElement.text = String.format("%.2f", newRemainingValue)
        // You might want to display the new remaining value to the user in the UI instead of println
        println("Remaining payment after discount: $newRemainingValue")
    }
}