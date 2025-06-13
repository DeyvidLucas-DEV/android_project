// ListaContatosActivity.kt
package com.example.agenda

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Ícone de voltar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.agenda.ui.theme.AgendaFuncionalPROJETOBASETheme

class ListaContatosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgendaFuncionalPROJETOBASETheme {
                // Usaremos um Scaffold para ter uma TopAppBar para o botão de voltar
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Lista de Contatos") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    // Finaliza a atividade atual para voltar à anterior
                                    finish()
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Voltar"
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    // Conteúdo da sua lista de contatos aqui
                    ContactListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ContactListScreen(modifier: Modifier = Modifier) {
    // Exemplo de conteúdo para a lista de contatos
    // Substitua isso pela sua lógica real de exibição da lista
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Aqui será exibida a lista de contatos.")
        // Você usaria LazyColumn ou similar para exibir sua lista de contatos
    }
}

@Preview(showBackground = true)
@Composable
fun ListaContatosActivityPreview() {
    AgendaFuncionalPROJETOBASETheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Lista de Contatos") },
                    navigationIcon = {
                        IconButton(onClick = { /* Ação de voltar para preview */ }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            ContactListScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}