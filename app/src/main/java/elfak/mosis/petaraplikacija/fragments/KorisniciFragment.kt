package elfak.mosis.petaraplikacija.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat.requireViewById
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.UserData
import elfak.mosis.petaraplikacija.R


class KorisniciFragment : Fragment() {

         var sviKorisnici:ArrayList<UserData> = ArrayList();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_korisnici, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userReference=FirebaseDatabase.getInstance().getReference("users");
        userReference.addValueEventListener(object : ValueEventListener
        {

            override fun onDataChange(snapshot: DataSnapshot){

                for( korisnik in snapshot.children)
                {
                    val jedanKorisnik= korisnik.getValue(UserData::class.java);
                        if(jedanKorisnik!=null){
                        sviKorisnici.add(jedanKorisnik);
                        }
                        else
                        {

                        }
                }

            sortiraj();
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Nesto ne valja", Toast.LENGTH_SHORT).show();
            }
        })

    }

    private fun sortiraj() {
   //    Toast.makeText(context,"Usao sam u sortiraj", Toast.LENGTH_SHORT).show();
        if(sviKorisnici.isNotEmpty())
        {
            Toast.makeText(context,"Lista nije prazna!", Toast.LENGTH_SHORT).show();
        }
        val sortiranaLista = sviKorisnici.sortedByDescending { it.brojBodova }
        val listaKorisnika:ListView = requireView().findViewById<ListView>(R.id.listViewKorisnici);
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, sortiranaLista.map{it.prikaziKoirniske()})
        listaKorisnika.adapter=adapter;


    }
    override fun onPrepareOptionsMenu(menu: Menu) {  // da se ne vide 3 tackice
        super.onPrepareOptionsMenu(menu)
        for (x in menu.children) {
            x.isVisible = false
        }
    }
}