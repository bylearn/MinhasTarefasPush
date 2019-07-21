package br.com.bylearn.minhastarefaspush.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import br.com.bylearn.minhastarefaspush.R
import br.com.bylearn.minhastarefaspush.data.modelo.Tarefa
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class TarefaAdapter(
    private val onClick: (Tarefa) -> Unit,
    private val onLongClick: (Tarefa) -> Unit
) : RecyclerView.Adapter<TarefaAdapter.ViewHolder>() {

    private val listaTarefas = HashMap<String, Tarefa>()

    fun adicionaTodasTarefas(tarefas: List<Tarefa>) {
        this.listaTarefas.clear()
        for (tarefa in tarefas) {
            listaTarefas[tarefa.uuid] = tarefa
        }
        notifyDataSetChanged()
    }

    fun addNovaTarefa(tarefa: Tarefa) {
        listaTarefas[tarefa.uuid] = tarefa
        notifyDataSetChanged()
    }

    fun lerTarefa(uuid: String) {
        val tarefa = listaTarefas[uuid]
        if (tarefa != null)
            tarefa.lida = true

        notifyDataSetChanged()
    }

    fun getTarefa(position: Int) = listaTarefas.values.toList()[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarefa_layout, parent, false)

        val viewHolder = ViewHolder(view)
        viewHolder.descricao = view.findViewById(R.id.item_tarefa_descricao)
        viewHolder.dataCriacao = view.findViewById(R.id.item_tarefa_data)
        viewHolder.lida = view.findViewById(R.id.item_tarefa_lida)
        viewHolder.finalizada = view.findViewById(R.id.item_tarefa_terminada)
        viewHolder.cartao = view.findViewById(R.id.item_tarefa_card)

        return viewHolder
    }

    override fun getItemCount() = listaTarefas.values.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tarefa = getTarefa(position)

        holder.cartao.setOnClickListener {
            onClick(tarefa)
        }
        holder.cartao.setOnLongClickListener {
            onLongClick(tarefa)
            true
        }

        holder.descricao.text = tarefa.descricao

        val data = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(tarefa.dataCriacao)
        holder.dataCriacao.text = "Criada em: $data"

        if (tarefa.lida)
            holder.lida.visibility = View.GONE
        else{
            holder.lida.visibility = View.VISIBLE
            holder.finalizada.visibility = View.GONE
        }

        if (tarefa.concluida){
            holder.finalizada.visibility = View.VISIBLE
            holder.lida.visibility = View.GONE
        }else
            holder.finalizada.visibility = View.GONE


    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        lateinit var descricao: TextView
        lateinit var dataCriacao: TextView
        lateinit var lida: ImageView
        lateinit var finalizada: ImageView
        lateinit var cartao: CardView
    }
}
