package elfak.mosis.petaraplikacija.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalonViewModel
import elfak.mosis.petaraplikacija.R



class KomentariFragment : Fragment() {

    private val frizerskiSalon: FrizerskiSalonViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_komentari, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pronadjiIme();

    }

    private fun pronadjiIme() {
        val IDFS= frizerskiSalon.vratiID();
        val referencaFS= FirebaseDatabase.getInstance().reference.child("objekti").child(IDFS);
        lateinit var  name:String;
        referencaFS.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val frizerskiSalon = snapshot.getValue(FrizerskiSalon::class.java)
                    name = frizerskiSalon?.name.toString();
    if(name!=null)
        ispisiKomentare(name);
                    else
                            {
                                    Toast.makeText(context,"Nije pronadjeno ime", Toast.LENGTH_SHORT).show();
                            }
                    // Sada možete koristiti vrednost "name" kako vam odgovara
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Obrada greške
            }
        })
    }

    private fun ispisiKomentare(name:String) {


        Log.d("KomentariFragment", "Početak metode ispisiKomentare()")



        val db = FirebaseDatabase.getInstance().reference
        val query = db.child("recenzije").orderByChild("nazivFrizerskogSalona").equalTo(name)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviews: MutableList<Pair<String, String>> = mutableListOf()

                for (objekat in snapshot.children) {
                    val komentar = objekat.child("opis").getValue(String::class.java)
                    val ocena = objekat.child("ocena").getValue(String::class.java)

                    if (komentar != null && ocena != null) {
                        reviews.add(Pair(komentar, ocena))
                    }
                }

                // Ovde možete postaviti listu reviews u ListView adapter
                val listView: ListView = requireView().findViewById(R.id.listViewKomentari)
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    reviews.map { "Komentar: ${it.first}, Ocena: ${it.second}" }
                )
                listView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Obrada greške
            }
        })



    }

}