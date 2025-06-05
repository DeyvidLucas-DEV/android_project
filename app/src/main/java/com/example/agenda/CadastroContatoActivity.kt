package com.example.agenda

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.graphics.BitmapFactory
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
                                imagem = imagem
                            )
                        )
                        finish()
                    },
                    onHomeClick = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },
                    onBackClick = {
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
    onHomeClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var imagem by remember { mutableStateOf("") }
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri.value = uri
        imagem = uri?.toString() ?: ""
    }

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
        imageUri.value?.let { uri ->
            val bitmap = remember(uri) {
                try {
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        BitmapFactory.decodeStream(stream)
                    }
                } catch (e: Exception) {
                    null
                }
            }
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .padding(bottom = 8.dp)
                )
            }
        }
        Button(onClick = { launcher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
            Text(text = if (imageUri.value == null) "Selecionar Imagem" else "Alterar Imagem")
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = { onSalvar(nome, telefone, email, imagem) }, modifier = Modifier.fillMaxWidth()) {
            Text("Salvar")
        }
        Spacer(modifier = Modifier.size(8.dp))
        Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar")
        }
        Spacer(modifier = Modifier.size(8.dp))
        Button(onClick = onHomeClick, modifier = Modifier.fillMaxWidth()) {
            Text("Home")
        }
    }
}
