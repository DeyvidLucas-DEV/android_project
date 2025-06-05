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
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
                ListaContatosScreen(
                    contatos = contatos,
                    onAddClick = {
                        startActivity(Intent(this, CadastroContatoActivity::class.java))
                    },
                    onHomeClick = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        contatos = dao.listar()
    }
}

@Composable
fun ListaContatosScreen(
    contatos: List<Contato>,
    onAddClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                Button(
                    onClick = onHomeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Home")
                }
            }
            items(contatos) { contato ->
                ContatoItem(contato)
            }
        }
    }
}

@Composable
fun ContatoItem(contato: Contato) {
    val context = LocalContext.current
    Row(modifier = Modifier.padding(8.dp)) {
        val bitmap = remember(contato.imagem) {
            try {
                if (contato.imagem.isNotBlank()) {
                    val uri = Uri.parse(contato.imagem)
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        BitmapFactory.decodeStream(stream)
                    }
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        } ?: BitmapFactory.decodeResource(context.resources, com.example.agenda.R.drawable.ic_launcher_foreground)

        Image(
            bitmap = bitmap.asImageBitmap(),
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
