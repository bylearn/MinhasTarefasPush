package br.com.bylearn.minhastarefaspush.data.dao

import br.com.bylearn.minhastarefaspush.data.modelo.Tarefa
import io.realm.Realm


open class TarefasDao {

    private val classeTarefa = Tarefa::class.java
    private val realm: Realm = Realm.getDefaultInstance()

    fun pegaTodasTarefas(): List<Tarefa>{
        try {
            return realm.where(classeTarefa).and().equalTo(Tarefa.EXCLUIDA, false).findAll().toList()
        } catch (e: Exception) {
            throw e
        }
    }

    fun criarTarefa(descricao: String): Tarefa{
        val tarefa = Tarefa()
        tarefa.descricao = descricao
        realm.executeTransaction {
            it.insertOrUpdate(tarefa)
        }
        return tarefa
    }

    fun atualizaTarefa(id: String, acao: TiposAcoesTarefa, retorno: () -> Unit, descricao: String = ""){

        realm.executeTransaction {
            val tarefa = realm.where(classeTarefa).equalTo(Tarefa.ID, id).findFirst()
            if (tarefa != null){
                when(acao){
                    TiposAcoesTarefa.LER ->{
                        if (!tarefa.lida)
                            tarefa.lida = !tarefa.lida
                    }
                    TiposAcoesTarefa.TERMINAR -> {
                        if (!tarefa.concluida)
                            tarefa.concluida = !tarefa.concluida
                    }
                    TiposAcoesTarefa.ATUALIZAR_DESCRICAO -> {
                        tarefa.descricao = descricao
                        if (!tarefa.lida)
                            tarefa.lida = !tarefa.lida
                    }
                    else -> {
                        if (!tarefa.excluida)
                            tarefa.excluida = !tarefa.excluida
                    }
                }

                retorno()
            }
        }

    }

}