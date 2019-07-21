package br.com.bylearn.minhastarefaspush.firebase

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import br.com.bylearn.minhastarefaspush.R
import br.com.bylearn.minhastarefaspush.data.dao.TarefasDao
import br.com.bylearn.minhastarefaspush.receiver.TarefaNovaEvento
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.greenrobot.eventbus.EventBus

class MeuFirebaseMensagemService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        remoteMessage?.notification?.let {
            val descricao = it.body
            if (descricao != null && descricao.isNotEmpty()){
                TarefasDao().criarTarefa(descricao)
                mostrarNotificacao(it)
                EventBus.getDefault().post(TarefaNovaEvento())
            }
        }
    }

    private fun mostrarNotificacao(notification: RemoteMessage.Notification) {
        val builder = Notification.Builder(this)
        builder.setContentTitle(notification.title)
        builder.setContentText(notification.body)
        builder.setSmallIcon(R.drawable.notification_icon_background)
        val notificacaoGerenciador = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificacaoGerenciador.notify(0, builder.build())
    }

    override fun onDestroy() {
        sendBroadcast(Intent("br.com.bylearn.minhastarefaspush.firebase.acao.start"))
        super.onDestroy()
    }

}