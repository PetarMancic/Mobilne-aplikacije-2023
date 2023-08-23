package elfak.mosis.petaraplikacija.fragments

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.petaraplikacija.R
import elfak.mosis.petaraplikacija.databinding.FragmentSigninBinding


class SigninFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSigninBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(inflater, container, false);

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
                // ako se sifre poklapaju

                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    hideKeyboard()
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Uspesno ste ste login!!!", Toast.LENGTH_SHORT)
                            .show()
                        navControl.navigate(R.id.action_signinFragment_to_homeFragment);

                    } else {
                        Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }


            }

        }


    }



}