package com.smask.nccrubicinimaterial3.utils

import android.util.Log
import co.nedim.maildroidx.MaildroidX
import co.nedim.maildroidx.MaildroidXType

fun myMailSender(body: String) {
    MaildroidX.Builder()
        .smtp("smtp.gmail.com")
        .smtpUsername("servizi.rubicini@gmail.com")
        .smtpPassword("zuzmgnavaqalumow")
        .port("465")
        .type(MaildroidXType.HTML)
        .to("smask80@gmail.com")
        //.to("info@rubiciniautonoleggio.it")
        .from("RubiciniApp")
        .subject("Email di prova da nuova Android App")
        .body(body)
//        .attachment()
//        .isJavascriptDisabled()
//        .isisStartTLSEnabled()
        //or
//        .attachments() //List<String>
        .onCompleteCallback(object : MaildroidX.onCompleteCallback {
            override val timeout: Long = 3000
            override fun onSuccess() {
                Log.d("MaildroidX", "SUCCESS")
            }

            override fun onFail(errorMessage: String) {
                Log.d("MaildroidX", "FAIL")
            }
        })
        .mail()
}