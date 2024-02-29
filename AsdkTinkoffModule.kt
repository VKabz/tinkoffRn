package com.reactnativeasdktinkoff

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.bridge.*
import org.json.JSONObject
import ru.tinkoff.acquiring.sdk.AcquiringSdk
import ru.tinkoff.acquiring.sdk.TinkoffAcquiring
//import ru.tinkoff.acquiring.sdk.localization.AsdkSource
//import ru.tinkoff.acquiring.sdk.localization.Language
//import ru.tinkoff.acquiring.sdk.localization.AsdkSource
//import ru.tinkoff.acquiring.sdk.localization.Language
import ru.tinkoff.acquiring.sdk.models.DarkThemeMode
import ru.tinkoff.acquiring.sdk.models.enums.CheckType
import ru.tinkoff.acquiring.sdk.models.options.screen.PaymentOptions
import ru.tinkoff.acquiring.sdk.redesign.mainform.MainFormLauncher
import ru.tinkoff.acquiring.sdk.utils.Money


class AsdkTinkoffModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener {


    companion object {
      private const val PAYMENT_REQUEST_CODE = 0
        private const val GOOGLE_PAY_REQUEST_CODE = 1
        var byMainFormPayment: ActivityResultLauncher<MainFormLauncher.StartData>? = null

        lateinit var promise: Promise
        lateinit var data: JSONObject

      fun init(reactContext: Activity) {
       val currentActivity = reactContext

        if (currentActivity is AppCompatActivity) {
          byMainFormPayment = currentActivity.registerForActivityResult(MainFormLauncher.Contract, ActivityResultCallback {})
        }
      }
    }

    init {
      reactContext.addActivityEventListener(this)
    }


    override fun getName(): String {
        return "AsdkTinkoff"
    }

    @ReactMethod
    fun Pay(j: String, p: Promise) {
        promise = p
        data = JSONObject(j)


     val tinkoffAcquiring = TinkoffAcquiring(reactApplicationContext.applicationContext, "1609", "IBCgKCAQEAv5yse9ka3ZQE0feuGtemYv3IqOlLck812уzHUM7lTr0za6lXTszRSXfUO7jMb+L5C7e2QNFs+7sIX2OQJ6a+HG8kr+jwJ4tS3cVsWtd9NXpsU40PE4MeNr5RqiNXjcDxA+L4OsEm/BlyFOEOh2epGyYUd5/iO3OiQFRNicomT2saQYAeqIwuELPs1XpLk9HLx5qPbm8fRrQhjeUD5TLO8b+4yCnObe8vy/BMUwBfq+ieWADIjwWCMp2KTpMGLz48qnaD9kdrYJ0iyHqzb2mkDhdIzkim24A3lWoYitJCBrrB2xM05sm9+OdCI1f7nPNJbl5URHobSwR94IRGT7CJcUjvwIDAQAB")
      tinkoffAcquiring?.initSbpPaymentSession();

        val paymentOptions = PaymentOptions().setOptions {
          orderOptions {
            orderId = data["OrderId"] as String
            amount =  Money.ofCoins((data["Amount"] as Int).toLong())
            recurrentPayment = false
          }
          customerOptions {
            checkType = CheckType.NO.toString()
          }
          featuresOptions {
            useSecureKeyboard = true
            darkThemeMode = DarkThemeMode.DISABLED
          }
          setTerminalParams("1609", "IBCgKCAQEAv5yse9ka3ZQE0feuGtemYv3IqOlLck8zHUM7lTr0za6lXTszRSXfUO7jMb21у+L5C7e2QNFs+7sIX2OQJ6a+HG8kr+jwJ4tS3cVsWtd9NXpsU40PE4MeNr5RqiNXjcDxA+L4OsEm/BlyFOEOh2epGyYUd5/iO3OiQFRNicomT2saQYAeqIwuELPs1XpLk9HLx5qPbm8fRrQhjeUD5TLO8b+4yCnObe8vy/BMUwBfq+ieWADIjwWCMp2KTpMGLz48qnaD9kdrYJ0iyHqzb2mkDhdIzkim24A3lWoYitJCBrrB2xM05sm9+OdCI1f7nPNJbl5URHobSwR94IRGT7CJcUjvwIDAQAB")
        }


      //ПОСЛЕ ЭТОЙ СТРОЧКИ ПРОИСХОДИТ КРАШ
      byMainFormPayment?.launch(MainFormLauncher.StartData(paymentOptions))

    }
}
