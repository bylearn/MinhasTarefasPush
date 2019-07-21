package br.com.bylearn.minhastarefaspush.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import br.com.bylearn.minhastarefaspush.firebase.MeuFirebaseMensagemService

class GlobalReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        if (intent == null) return

        if (intent.action == "br.com.bylearn.minhastarefaspush.firebase.acao.start"){

            ContextCompat.startForegroundService(context, Intent(context, MeuFirebaseMensagemService::class.java))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startService(Intent(context, MeuFirebaseMensagemService::class.java))

        }
    }

}