package elfak.mosis.petaraplikacija.fragments

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalonViewModel
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.SigninViewModel
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.UserData
import elfak.mosis.petaraplikacija.R



class HomeFragment : Fragment() {

    private val SigninViewModel: SigninViewModel by activityViewModels()
    private lateinit var txtFirstName: TextView
    private lateinit var txtLastName: TextView
    private lateinit var txtScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        txtFirstName = rootView.findViewById(R.id.txtFirstName)
        txtLastName = rootView.findViewById(R.id.txtLastName)
        txtScore = rootView.findViewById(R.id.txtScore)



        // Inflate the layout for this fragment
        return  rootView;
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var navControl = Navigation.findNavController(view)

        var dugmeOdjavise=view.findViewById<Button>(R.id.buttonOdjavise);
        var dugmeFiltriraj=view.findViewById<Button>(R.id.buttonFiltiraj);
        var dugmePrikaziKorisnike= view.findViewById<Button>(R.id.buttonPrikaziUsers);
        val textViewIme= view.findViewById<TextView>(R.id.txtFirstName);
        val textViewPrezime= view.findViewById<TextView>(R.id.txtLastName);
        val textViewBrojBodova= view.findViewById<TextView>(R.id.txtScore);


        dugmeFiltriraj.setOnClickListener()
        {
            navControl.navigate(R.id.action_homeFragment_to_filtriranjeFragment);
        }
        dugmeOdjavise.setOnClickListener()
        {
            navControl.navigate(R.id.action_homeFragment_to_signinFragment);
        }
        dugmePrikaziKorisnike.setOnClickListener()
        {
            navControl.navigate(R.id.korisniciFragment);
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val userReference = FirebaseDatabase.getInstance().reference.child("users").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(UserData::class.java)
                        val ime = user?.ime
                        val prezime= user?.prezime;
                        val brojBodova=user?.brojBodova;


                        if (ime != null && prezime!=null && brojBodova!=null ) {

                            textViewIme.text=ime;
                            textViewPrezime.text=prezime;
                            textViewBrojBodova.text=brojBodova.toString();
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Obrada greške
                }
            })
        }


       // val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        val usersReference = FirebaseDatabase.getInstance().getReference("users")
       /* usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)
                    if (userData != null) {
                        val ime = userData.ime
                        val prezime = userData.prezime
                        val brojBodova = userData.brojBodova

                        val textViewIme= view.findViewById<TextView>(R.id.txtFirstName);
                        val textViewPrezime= view.findViewById<TextView>(R.id.txtLastName);
                        val textViewBrojBodova= view.findViewById<TextView>(R.id.txtScore);
                        textViewIme.text=ime;
                        textViewPrezime.text=prezime;
                        textViewBrojBodova.text=brojBodova.toString();
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Obrada grešaka
            }
        })*/

       /* val user: List<UserData> =SigninViewModel.getUser();

            if(user.isNotEmpty())
            {
                val ime=user.get(0).ime;
                val prezime=user.get(0).prezime;
                val brojBodova=user.get(0).brojBodova;

               val textViewIme= view.findViewById<TextView>(R.id.txtFirstName);
                val textViewPrezime= view.findViewById<TextView>(R.id.txtLastName);
                val textViewBrojBodova= view.findViewById<TextView>(R.id.txtScore);
                textViewIme.text=ime;
                textViewPrezime.text=prezime;
                textViewBrojBodova.text=brojBodova.toString();
            }*/

        var imgProfile=view.findViewById<ImageView>(R.id.imgProfile);




        val storageReference = FirebaseStorage.getInstance().reference
        val imageReference = storageReference.child("profile_images/$userId.jpg")
    imageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener {
        val bmp=BitmapFactory.decodeByteArray(it,0,it.size)
        imgProfile.setImageBitmap(bmp);
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

    }
}