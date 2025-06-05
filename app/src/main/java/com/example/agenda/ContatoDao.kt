import android.content.ContentValues
import android.content.Context

class ContatoDao(context: Context) {
    private val dbHelper = ContatoDbHelper(context)

    fun inserir(contato: Contato) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nome", contato.nome)
            put("telefone", contato.telefone)
            put("email", contato.email)
            put("imagem", contato.imagem)
        }
        db.insert("contato", null, values)
        db.close()
    }

    fun listar(): List<Contato> {
        val lista = mutableListOf<Contato>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM contato", null)

        if (cursor.moveToFirst()) {
            do {
                val contato = Contato(
                    id = cursor.getInt(0),
                    nome = cursor.getString(1),
                    telefone = cursor.getString(2),
                    email = cursor.getString(3),
                    imagem = cursor.getString(4)
                )
                lista.add(contato)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}

