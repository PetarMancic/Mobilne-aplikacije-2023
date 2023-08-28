package elfak.mosis.petaraplikacija.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.requireViewById
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
import elfak.mosis.petaraplikacija.databinding.FragmentSignUpBinding


class EditFragment : Fragment() {

    private val frizerskiSalon: FrizerskiSalonViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentEditBinding

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
        // return inflater.inflate(R.layout.fragment_edit, container, false)
        // val view= inflater.inflate(R.layout.fragment_edit, container, false)

        // Gledas li kvo pises, on dju zakljucil od prethodnutu diskusiju
        //uzas, ceke sad da te pitam, moze li da upalimo negde pricanje
        // Zovi na wa ili fon
// AKO TO MI JE CHAT GPT ZAJEBO JA CU DA MU JEBEM MATER BRE USRANU

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


        /*  dugmeAdd?.setOnClickListener()
        {
            val editName: EditText= requireView().findViewById(R.id.editName);
            val name: String= editName.text.toString()
            val editDesc: EditText= requireView().findViewById(R.id.editDescription);
            val desc:String= editDesc.text.toString();

            val longitude:String= editLongitude.text.toString();
            val latitude:String= editLatitude.text.toString();





            frizerskiSalon.addPlace(FrizerskiSalon(name,desc,latitude,longitude));
            btnSet?.setOnClickListener(){

                locationViewModel.setLocation=true;
                findNavController().navigate(R.id.action_editFragment_to_mapFragment);
            }


*/

        //  findNavController().navigate(R.id.action_editFragment_to_listFragment);
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
        val editName: EditText = requireView().findViewById(R.id.editName);
        val editDescription: EditText = requireView().findViewById(R.id.editDescription);
        val radioGroup: RadioGroup = requireView().findViewById(R.id.radioGroup);
        val numberPicker: EditText = requireView().findViewById(R.id.numberPicker);

        val selectedId = radioGroup.checkedRadioButtonId;

        val longitude = editLongitude.text.toString();
        val latitude = editLatitude.text.toString();
        val name = editName.text.toString();
        val description = editDescription.text.toString();
        val selectedValue = numberPicker.text.toString().toInt();

        if (name.isNotEmpty() && description.isNotEmpty() && !selectedValue.equals(0) && latitude.isNotEmpty() && longitude.isNotEmpty() && (selectedId == R.id.radioMuski || selectedId == R.id.radioZenski)) {
            var MZ = "Z";
            if (selectedId == R.id.radioMuski)
                MZ = "M";

            dodajObjekat(name, description, MZ, selectedValue, longitude, latitude);

        }


    }

    private fun dodajObjekat(
        name: String,
        description: String,
        MZ: String,
        selectedValue: Int,
        Long: String,
        Lat: String
    ) {
//        val currentUser = auth.currentUser
//        val usersReference = FirebaseDatabase.getInstance().getReference("objekti");
//
//        currentUser?.let { firebaseUser ->
//            val userMap = mapOf(
//                "ime" to name,
//                "opis" to description,
//                "tip" to MZ,
//                "ocena" to selectedValue,
//                "longitude" to Long,
//                "latitude" to Lat
//            )
//            usersReference.child(firebaseUser.uid).setValue(userMap).addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(
//                        context,
//                        "Uspesna registracija i dodavanje u bazu!!!",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    //navControl.navigate(R.id.action_signUpFragment_to_signinFragment)
//                } else {
//                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val objektiReference = FirebaseDatabase.getInstance().getReference("objekti").push();

        val userReference = FirebaseDatabase.getInstance().reference.child("users").child(currentUserID);
        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UserData::class.java)
                    val ime = user?.ime
                    val prezime= user?.prezime;



                    if (ime != null && prezime!=null  ) {
                        objektiReference.setValue(FrizerskiSalon(name, description, MZ, Long, Lat,currentUserID,ime,prezime));
                        userReference.child("brojBodova").get().addOnSuccessListener {
                            var brBodova = it.value.toString().toInt()
                            brBodova += 10
                            userReference.child("brojBodova").setValue(brBodova)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Obrada greške
            }
        })


    }
}


