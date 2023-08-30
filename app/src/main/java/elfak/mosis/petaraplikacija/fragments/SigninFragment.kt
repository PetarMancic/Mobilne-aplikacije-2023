package elfak.mosis.petaraplikacija.fragments

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalonViewModel
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.SigninViewModel
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.UserData
import elfak.mosis.petaraplikacija.R
import elfak.mosis.petaraplikacija.databinding.FragmentSigninBinding


class SigninFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSigninBinding

    private val korisnici: SigninViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(inflater, container, false);
       // setHasOptionsMenu(true)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }

    private fun hideKeyboard() {
        val view = view
        if (view != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun registerEvents() {

        binding.signUpLabel.setOnClickListener {
            navControl.navigate(R.id.action_signinFragment_to_signUpFragment);
        }

        binding.loginButtonsignup.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val pass = binding.editTextTextPassword.text.toString().trim()



            if (email.isNotEmpty() && pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    hideKeyboard()
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Uspesno ste ste login!!!", Toast.LENGTH_SHORT)
                            .show()

                        val userId= auth.currentUser?.uid ?: ""
                        val userReference= FirebaseDatabase.getInstance().getReference("users").child(userId);
                        userReference.addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                               if (snapshot.exists()){
                                   Toast.makeText(context, "Snap!!", Toast.LENGTH_SHORT).show()
                                   val userData = snapshot.getValue(UserData::class.java)
                                    if(userData!=null) {
                                        korisnici.dodajUsera(userData);
                                        // Sad raboti, kude je problem dalje
                                        //edit fragment, kad on treba da se prikaze, ili pucaju greske,
                                        //ili se prikaze ali njegov sadrzaj ne bude, nego bude sadrzaj od signUpfragment
                                        navControl.navigate(R.id.action_signinFragment_to_homeFragment);
                                    }
                               }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("FirebaseDatabase", "Greška pri čitanju: ${error.message}")
                            }
                        })
                    //  navControl.navigate(R.id.action_signinFragment_to_homeFragment);

                    } else {
                        Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }


    }


    override fun onPrepareOptionsMenu(menu: Menu) {  // da se ne vide 3 tackice
        super.onPrepareOptionsMenu(menu)
        for (x in menu.children) {
            x.isVisible = false
        }
    }


}