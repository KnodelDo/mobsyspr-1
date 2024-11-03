package com.example.mobsyspr2


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.ComponentActivity
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var einkaufsListeRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        einkaufsListeRef = FirebaseDatabase.getInstance().getReference("Einkaufsliste")
        addTestEintrag()
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
    data class EinkaufsItem(
        val id: Int = 0,
        val idKategorie: Int = 0,
        val produkt: String = "",
        val menge: Int = 0,
        val notizen: String = ""
    )
    private fun readEinkaufsListe() {
        einkaufsListeRef.get()
            .addOnSuccessListener { dataSnapshot ->
                for (snapshot in dataSnapshot.children) {
                    val item = snapshot.getValue(EinkaufsItem::class.java)
                    item?.let {
                        Log.d("Einkaufsliste", "Produkt: ${it.produkt}, Menge: ${it.menge}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Fehler bei der Verbindung zur Datenbank", exception)
            }
    }

    private fun addEinkaufsItem(id: Int, idKategorie: Int, produkt: String, menge: Int, notizen: String) {
        val item = EinkaufsItem(id, idKategorie, produkt, menge, notizen)
        einkaufsListeRef.child(id.toString()).setValue(item)

    }
    private fun addTestEintrag() {
        val testEintrag = EinkaufsItem(id = 999, idKategorie = 1, produkt = "TestProdukt", menge = 1, notizen = "Dies ist ein Testeintrag")

        einkaufsListeRef.push().setValue(testEintrag)
    }

}