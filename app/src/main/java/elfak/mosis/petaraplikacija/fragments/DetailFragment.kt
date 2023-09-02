package elfak.mosis.petaraplikacija.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalonViewModel
import elfak.mosis.petaraplikacija.R


class DetailFragment : Fragment() {
    private val FrizerskiSaloniViewModel: FrizerskiSalonViewModel by activityViewModels()
    private lateinit var  IDFS:String;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
       postaviVrednosti();


        return inflater.inflate(R.layout.fragment_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dugmeAzuriraj= view?.findViewById<Button>(R.id.buttonAzuriraj);

        dugmeAzuriraj!!.setOnClickListener()
        {
            azurirajAtribute();
        }
    }
    private fun postaviVrednosti() {
         val IDFS= FrizerskiSaloniViewModel.vratiID();
        val objekatReference= FirebaseDatabase.getInstance().reference.child("objekti").child(IDFS);

        objekatReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val frizerskiSalon = snapshot.getValue(FrizerskiSalon::class.java)
                    val ime = frizerskiSalon?.name
                    //val trenutnaOcena= frizerskiSalon?


                    if (ime != null  ) {

                       requireView().findViewById<TextView>(R.id.ime).text=ime;


                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Obrada greške
            }
        })

    }

    private fun azurirajAtribute() {

        val opisPolje=requireView().findViewById<EditText>(R.id.editDescription);
        val ocenaPolje= requireView().findViewById<EditText>(R.id.numberPicker);
        val nazivFSpolje= requireView().findViewById<TextView>(R.id.ime)

        val opis= opisPolje.text.toString();
        val ocena= ocenaPolje.text.toString();
        val nazivFS= nazivFSpolje.text.toString();

        //pravimo novu referencu
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val objektiReference = FirebaseDatabase.getInstance().getReference("recenzije").push();
        val userReference = FirebaseDatabase.getInstance().reference.child("users").child(currentUserID);
        //userReference.child("br")


        if(opis.isNotEmpty() && ocena.isNotEmpty())
        {
                val mapiranjeAtributa = hashMapOf(
                    "opis" to opis,
                    "ocena" to ocena,
                    "nazivFrizerskogSalona" to nazivFS

                )

                objektiReference.setValue(mapiranjeAtributa).addOnSuccessListener {
                    Toast.makeText(context, "Uspesno dodato u recenzijama ", Toast.LENGTH_SHORT).show()
                    userReference.child("brojBodova").get().addOnSuccessListener {
                        var brBodova = it.value.toString().toInt()
                        brBodova += 5
                        userReference.child("brojBodova").setValue(brBodova)

                    }
                    findNavController().navigate(R.id.action_detailFragment_to_listFragment);

                }.addOnFailureListener()
                {
                    Toast.makeText(context, "Nesupesno dodavanje u bazi", Toast.LENGTH_SHORT).show()
                }
         }
        else
        {
            Toast.makeText(context, "Nisu popunjena sva polja ", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.signinFragment)
        item.isVisible = false

        val item1=menu.findItem(R.id.HomeFragment)
        item1.isVisible=false;

        val item2 = menu.findItem(R.id.signUpFragment)
        item2.isVisible = false

        val item3 = menu.findItem(R.id.editFragment)
        item3.isVisible = false

        val item4= menu.findItem(R.id.filtrirajFragment);
        item4.isVisible=false;

    }

}