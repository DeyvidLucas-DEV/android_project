package com.example.agenda

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.agenda.ui.theme.AgendaFuncionalPROJETOBASETheme

class CadastroContatoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgendaFuncionalPROJETOBASETheme {
                CadastroContatoScreen(
                    onSalvar = { nome, telefone, email, imagem ->
                        val dao = ContatoDao(this)
                        dao.inserir(
                            Contato(
                                nome = nome,
                                telefone = telefone,
                                email = email,
                                imagem = if (imagem.isBlank()) "ic_launcher_foreground" else imagem
                            )
                        )
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },
                    onHomeClick = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun CadastroContatoScreen(
    onSalvar: (String, String, String, String) -> Unit,
    onHomeClick: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var imagem by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = telefone,
            onValueChange = { telefone = it },
            label = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = imagem,
            onValueChange = { imagem = it },
            label = { Text("Imagem (drawable)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = { onSalvar(nome, telefone, email, imagem) }, modifier = Modifier.fillMaxWidth()) {
            Text("Salvar")
        }
        Spacer(modifier = Modifier.size(8.dp))
        Button(onClick = onHomeClick, modifier = Modifier.fillMaxWidth()) {
            Text("Home")
        }
    }
}
