package com.example.agenda

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

import androidx.compose.ui.unit.dp
import com.example.agenda.ui.theme.AgendaFuncionalPROJETOBASETheme

class ListaContatosActivity : ComponentActivity() {
    private lateinit var dao: ContatoDao
    private var contatos by mutableStateOf(listOf<Contato>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dao = ContatoDao(this)
        contatos = dao.listar()
        enableEdgeToEdge()
        setContent {
            AgendaFuncionalPROJETOBASETheme {

            }
        }
    }

    override fun onResume() {
        super.onResume()
        contatos = dao.listar()
    }
}

@Composable

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {

            items(contatos) { contato ->
                ContatoItem(contato)
            }
        }
    }
}

@Composable
fun ContatoItem(contato: Contato) {

            contentDescription = null,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = contato.nome, style = MaterialTheme.typography.titleMedium)
            Text(text = contato.telefone)
            Text(text = contato.email)
        }
    }
}
