package com.example.wearnn.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wearnn.R
import com.example.wearnn.data.model.PaymentInfo

class PaymentActivity : AppCompatActivity(){

//    member declaration
    companion object {
        const val euroSign : Char = '\u20AC'
        const val percentage = "%"
    }
    override fun onStart() {
        super.onStart()

        setContentView(R.layout.payment_activity);
// Selecting the correct elements
        val paymentAmountElement : TextView = findViewById(R.id.paymentValue) as TextView
        val discountElement : TextView = findViewById(R.id.activityDiscountValue) as TextView
        val remainingPaymentElement : TextView = findViewById(R.id.remainingPaymentValue) as TextView
//capturing data
        val paymentValue: String = paymentAmountElement.text.toString();
        val discountPercentage: String = discountElement.text.toString();
        val remainingValue: String = remainingPaymentElement.text.toString();
//instantiating object class by converting to int
        val info = PaymentInfo(paymentValue.toDouble(), discountPercentage.toDouble(), remainingValue.toDouble())
//calculating discount
        val discountAmount = info.paymentAmountValue * info.discountValue / 100
        val newRemainingValue = info.paymentAmountValue - discountAmount
//sending new values to activity
        remainingPaymentElement.text = String.format("%.2f", newRemainingValue);


        println("**************" + newRemainingValue)


    }
}