package com.example.mobsyspr2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobsyspr2.databinding.ActivityHomeBinding
import com.google.firebase.database.*
import android.widget.Toast

class HomeActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityHomeBinding
    private lateinit var itemsAdapter: ItemsAdapter
    private val itemsList = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView setup
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
        itemsAdapter = ItemsAdapter(itemsList)
        binding.recyclerViewItems.adapter = itemsAdapter

        // Firebase setup
        database = FirebaseDatabase.getInstance().getReference("Einkaufsliste")

        // Lade alle Items aus Firebase
        fetchItems()

        // Button zum Hinzufügen neuer Items
        binding.btnHinzufuegen.setOnClickListener {
            val produkt = binding.etProdukt.text.toString()
            val menge = binding.etMenge.text.toString().toIntOrNull() ?: 0
            val notiz = binding.etNotiz.text.toString()
            if (produkt.isNotEmpty() && menge > 0) {
                addItemToFirebase(produkt, menge, notiz)
            } else {
                toast("Bitte fülle alle Felder korrekt aus.")
            }
        }
    }

    private fun fetchItems() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemsList.clear()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Item::class.java)
                    item?.let { itemsList.add(it) }
                }
                itemsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                toast("Fehler beim Laden der Daten: ${error.message}")
            }
        })
    }

    private fun addItemToFirebase(produkt: String, menge: Int, notiz: String) {
        val newItemId = database.child("Einkaufsliste").push().key ?: return
        val newItem = mapOf(
            "id" to newItemId,
            "id_kategorie" to 1,
            "menge" to menge,
            "notizen" to notiz,
            "produkt" to produkt
        )

        FirebaseDatabase.getInstance().getReference("Einkaufsliste")
            .child(newItemId)
            .setValue(newItem)
            .addOnSuccessListener {
                toast("Eintrag erfolgreich hinzugefügt")
            }
            .addOnFailureListener { e ->
                toast("Fehler beim Hinzufügen: ${e.message}")
            }
    }




    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
