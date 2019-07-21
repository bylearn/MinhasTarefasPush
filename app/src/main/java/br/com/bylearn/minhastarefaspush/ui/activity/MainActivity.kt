package br.com.bylearn.minhastarefaspush.ui.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import br.com.bylearn.minhastarefaspush.R
import br.com.bylearn.minhastarefaspush.data.dao.TarefasDao
import br.com.bylearn.minhastarefaspush.data.dao.TiposAcoesTarefa
import br.com.bylearn.minhastarefaspush.data.modelo.Tarefa
import br.com.bylearn.minhastarefaspush.receiver.TarefaNovaEvento
import br.com.bylearn.minhastarefaspush.ui.adapter.TarefaAdapter
import br.com.bylearn.minhastarefaspush.ui.callback.TarefaCallback
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    private val dao = TarefasDao()
    private var dialog: AlertDialog? = null
    private lateinit var adapter: TarefaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.extras != null){
            for (key in intent.extras.keySet()){
                if (key == "mensagem"){
                    val descricao = intent.extras.getString(key)
                    if (descricao != null && descricao.isNotEmpty())
                        dao.criarTarefa(descricao)
                }
            }
        }

        main_fab_add_tarefa.setOnClickListener {
            criarOuEditarTarefa()
        }
    }

    private fun criarOuEditarTarefa(tarefa: Tarefa? = null) {
        val ehEdit = tarefa != null
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage("Escreva a descrição da Tarefa.")
        val editText = EditText(this)
        builder.setView(editText)
        builder.setNegativeButton("Cancelar", null)
        builder.setPositiveButton("OK") { _, _ ->
            val descricao = editText.text.toString().trim()
            if (descricao.isNotEmpty()){
                if (ehEdit){
                    dao.atualizaTarefa(tarefa!!.uuid, TiposAcoesTarefa.ATUALIZAR_DESCRICAO,{
                        criarAdapter()
                    }, descricao)
                }else{
                    val tarefaCriadaBd = dao.criarTarefa(descricao)
                    adapter.addNovaTarefa(tarefaCriadaBd)
                }
            }else{
                Toast.makeText(this, "Não pode ser criada uma Tarefa com descrição vazia.", Toast.LENGTH_LONG).show()
            }
        }
        dialog = builder.create()
        dialog?.show()
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
        criarAdapter()
        val itemTouchHelper = ItemTouchHelper(TarefaCallback(adapter) {
            criarAdapter()
        })
        itemTouchHelper.attachToRecyclerView(main_lista_tarefas)
    }

    override fun onPause() {
        super.onPause()

        if (dialog != null && dialog!!.isShowing){
            dialog!!.dismiss()
            dialog = null
        }
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun eventoNovaTarefa(evento: TarefaNovaEvento){
        criarAdapter()
    }

    private fun criarAdapter() {
        val tarefas = dao.pegaTodasTarefas()
        this.adapter = TarefaAdapter({
            dao.atualizaTarefa(it.uuid, TiposAcoesTarefa.LER, {
                this.adapter.lerTarefa(it.uuid)
            })
        },{
            criarOuEditarTarefa(it)
        })
        this.adapter.adicionaTodasTarefas(tarefas)
        main_lista_tarefas.adapter = adapter
    }
}
