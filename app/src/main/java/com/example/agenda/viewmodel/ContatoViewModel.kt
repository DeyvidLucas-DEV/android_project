package com.example.agenda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.agenda.data.AppDatabase
import com.example.agenda.model.Contato
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ContatoViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val contatoDao = database.contatoDao()

    val allContatos: Flow<List<Contato>> = contatoDao.getAllContatos()

    fun inserir(contato: Contato) = viewModelScope.launch {
        contatoDao.inserir(contato)
    }

    fun atualizar(contato: Contato) = viewModelScope.launch {
        contatoDao.atualizar(contato)
    }

    fun deletar(contato: Contato) = viewModelScope.launch {
        contatoDao.deletar(contato)
    }

    fun buscarContatos(query: String): Flow<List<Contato>> {
        return contatoDao.buscarContatos(query)
    }
} 