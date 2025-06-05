package com.example.agenda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.agenda.ui.theme.AgendaFuncionalPROJETOBASETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgendaFuncionalPROJETOBASETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        onOpenList = {
                            startActivity(
                                Intent(this@MainActivity, ListaContatosActivity::class.java)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, onOpenList: () -> Unit) {
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text(text = "Agenda")
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = onOpenList) {
            Text("Ver Contatos")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AgendaFuncionalPROJETOBASETheme {
        MainScreen(onOpenList = {})
    }
}