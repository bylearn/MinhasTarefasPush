package br.com.bylearn.minhastarefaspush

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import io.realm.Realm
import io.realm.RealmConfiguration
import java.lang.Exception

class MinhaAplicacao: Application() {

    override fun onCreate() {
        super.onCreate()
        iniciarFirebase()
        iniciarRealm()
    }

    private fun iniciarFirebase() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener {task ->
                if (!task.isSuccessful)
                    return@OnCompleteListener

                val token = task.result?.token
                Log.d("TOKEN", token)
            })

        FirebaseMessaging.getInstance().isAutoInitEnabled = true
    }

    private fun iniciarRealm() {
        try {
            Realm.init(this)
            val realmConfiguracao = RealmConfiguration.Builder()
                .name("minhasTarefas.realm")
                .schemaVersion(0).build()
            Realm.setDefaultConfiguration(realmConfiguracao)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

}