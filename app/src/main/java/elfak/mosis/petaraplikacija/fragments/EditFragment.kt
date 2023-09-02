package elfak.mosis.petaraplikacija.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalonViewModel
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.LocationViewModel
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.UserData
import elfak.mosis.petaraplikacija.R
import elfak.mosis.petaraplikacija.databinding.FragmentEditBinding


class EditFragment : Fragment() {

    private val frizerskiSalon: FrizerskiSalonViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentEditBinding


   // private lateinit var  IDFS:String;  // sluzi nam da pronadjemo koji FS cemo da editujemo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        auth = FirebaseAuth.getInstance()
        binding = FragmentEditBinding.inflate(inflater, container, false);


        return binding.root;
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.editFragment)
        item.isVisible = false

        val item1 = menu.findItem(R.id.signinFragment)
        item1.isVisible = false

        val item2 = menu.findItem(R.id.signUpFragment)
        item2.isVisible = false




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dugmeAdd = view.findViewById<Button>(R.id.btnAdd);
        dugmeAdd?.setOnClickListener {
            proveraInputa() // Poziv funkcije za proveru inputa
        }
        if(frizerskiSalon.vratiID().isNotEmpty()) {
            postaviVrednosti();
        }



        val editLatitude: EditText = requireView().findViewById(R.id.editLatitude);
        val editLongitude: EditText = requireView().findViewById(R.id.editLongitude);
        val btnSet: Button? = requireView().findViewById<Button>(R.id.btnSet);


        val lonObserver = Observer<String> { newValue ->
            editLongitude.setText(newValue)
        }
        locationViewModel.longitude.observe(viewLifecycleOwner, lonObserver)

        val latObserver = Observer<String> { newValue ->
            editLatitude.setText(newValue)
        }
        locationViewModel.latitude.observe(viewLifecycleOwner, latObserver)


        var dugmeCancel=view.findViewById<Button>(R.id.btnCancel)
        dugmeCancel.setOnClickListener()
        {
            findNavController().navigate(R.id.action_editFragment_to_mapFragment);
        }


    }

    private fun postaviVrednosti() {
        val editLongitude: EditText = requireView().findViewById(R.id.editLongitude);
        val editLatitude: EditText = requireView().findViewById(R.id.editLatitude);
        val editName: EditText = requireView().findViewById(R.id.ime);
        val editDescription: EditText = requireView().findViewById(R.id.editDescription);
        val radioGroup: RadioGroup = requireView().findViewById(R.id.radioGroup);
      //  val numberPicker: EditText = requireView().findViewById(R.id.numberPicker);

        val selectedId = radioGroup.checkedRadioButtonId;

            var MZ = "Z";
            if (selectedId == R.id.radioMuski)
                MZ = "M";

            val IDFS= frizerskiSalon.vratiID();
            val objekatReference= FirebaseDatabase.getInstance().reference.child("objekti").child(IDFS);

            objekatReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val frizerskiSalon = snapshot.getValue(FrizerskiSalon::class.java)

                        editName.setText(frizerskiSalon?.name.toString());
                        editDescription.setText(frizerskiSalon?.description.toString());
                        if (frizerskiSalon?.MZ == "M") {
                            radioGroup.check(R.id.radioMuski) // Zamenjate sa stvarnim ID-om radio dugmeta
                        }
                        else {
                            radioGroup.check(R.id.radioZenski)
                        }
                            editLongitude.setText(frizerskiSalon?.longitude);
                            editLatitude.setText(frizerskiSalon?.latitude);
                       //    numberPicker.setText(frizerskiSalon?.trenutnaOcena.toString());



                        //val trenutnaOcena= frizerskiSalon?
                        if (selectedId == R.id.radioMuski)
                            MZ = "M";

                        val dugmeIzmeni= requireView().findViewById<Button>(R.id.btnSet);
                   //     var ocena=numberPicker.text.toString().toDouble();
                        dugmeIzmeni.setOnClickListener()
                        {
                            izmeniUBazi(
                                editName.text.toString(),
                                editDescription.text.toString(), MZ,
                               // ocena,
                                editLongitude.text.toString(),
                                editLatitude.text.toString()
                            )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Obrada greške
                }
            })


    }

    private fun izmeniUBazi(ime:String,desc:String,tip:String,/*ocena:Double*/ long:String,lat:String,) {
        val IDFS = frizerskiSalon.vratiID()
        val objekatReference = FirebaseDatabase.getInstance().reference.child("objekti").child(IDFS);
        val noviPodaci: Map<String, Any> = mapOf(
            "name" to ime,
            "description" to desc,
            "MZ" to tip,
           // "trenutnaOcena" to ocena,
            "longitude" to long,
            "latitude" to lat
        )

        // Ažurirajte podatke u bazi
        objekatReference.updateChildren(noviPodaci)
            .addOnSuccessListener {
                // Uspesno ažurirano
                Toast.makeText(context, "Podaci su uspešno ažurirani.", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.action_editFragment_to_listFragment);

            }
            .addOnFailureListener {
                // Greška pri ažuriranju
                Toast.makeText(context, "Greška pri ažuriranju podataka.", Toast.LENGTH_SHORT).show()
            }

















        }


    /* val cancelButton: Button= requireView().findViewById<Button>(R.id.btnCancel);
        cancelButton.setOnClickListener()
        {

    frizerskiSalon.postaviNaNull();
            locationViewModel.setLocation("","");
            findNavController().popBackStack();
            //findNavController().navigate(R.id.action_editFragment_to_listFragment);
        }*/


    private fun proveraInputa() {
        val editLongitude: EditText = requireView().findViewById(R.id.editLongitude);
        val editLatitude: EditText = requireView().findViewById(R.id.editLatitude);
        val editName: EditText = requireView().findViewById(R.id.ime);
        val editDescription: EditText = requireView().findViewById(R.id.editDescription);
        val radioGroup: RadioGroup = requireView().findViewById(R.id.radioGroup);
      // val numberPicker: EditText = requireView().findViewById(R.id.numberPicker);

        val selectedId = radioGroup.checkedRadioButtonId;

        val longitude = editLongitude.text.toString();
        val latitude = editLatitude.text.toString();
        val name = editName.text.toString();
        val description = editDescription.text.toString();
      //  val selectedValue = numberPicker.text.toString().toDouble();

        if (name.isNotEmpty() && description.isNotEmpty()  && latitude.isNotEmpty() && longitude.isNotEmpty() && (selectedId == R.id.radioMuski || selectedId == R.id.radioZenski)) {
            var MZ = "Z";
            if (selectedId == R.id.radioMuski)
                MZ = "M";

            dodajObjekat(name, description, MZ,0.0,  longitude, latitude);

        }


    }

    private fun dodajObjekat(
        name: String,
        description: String,
        MZ: String,
        selectedValue: Double,
        Long: String,
        Lat: String
    ) {

        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid

        val objektiReference = FirebaseDatabase.getInstance().getReference("objekti")
        var kljuc= System.currentTimeMillis().toString();
        val datumKreiranja = System.currentTimeMillis();

        val userReference = FirebaseDatabase.getInstance().reference.child("users").child(currentUserID);
        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UserData::class.java)
                    val ime = user?.ime
                    val prezime= user?.prezime;
                    val korisnickoIme=user?.korisnickoIme;




                    if (ime != null && prezime!=null  ) {
                        objektiReference.child(kljuc).setValue(FrizerskiSalon(name, description, MZ,0.0, Long, Lat,currentUserID,ime,prezime,korisnickoIme!!,datumKreiranja,kljuc));
                        userReference.child("brojBodova").get().addOnSuccessListener {
                            var brBodova = it.value.toString().toInt()
                            brBodova += 10
                            userReference.child("brojBodova").setValue(brBodova)

                        }
                        //ocistiPolja();
                        Toast.makeText(context,"Uspesno dodat objekat",Toast.LENGTH_SHORT).show();
                        findNavController().navigate(R.id.action_editFragment_to_mapFragment)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Obrada greške
            }
        })


    }

    override fun onDestroyView() {
        frizerskiSalon.dodajID("");
        super.onDestroyView()
    }
}


