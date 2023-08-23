package elfak.mosis.petaraplikacija.fragments

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.petaraplikacija.R
import elfak.mosis.petaraplikacija.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {

    private lateinit var auth:FirebaseAuth
    private lateinit var navControl:NavController
    private lateinit var binding: FragmentSignUpBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSignUpBinding.inflate( inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
        binding.slikaHide.setOnClickListener{
            togglePasswordVisibility(binding.editTextTextPassword,binding.slikaHide2)
        }

    }
    private fun init (view: View)
    {
        navControl= Navigation.findNavController(view)
        auth= FirebaseAuth.getInstance()
    }

    private fun registerEvents(){

        binding.signUpLabel.setOnClickListener{
            navControl.navigate(R.id.action_signUpFragment_to_signinFragment);
        }

        binding.loginButtonsignup.setOnClickListener{
            val email= binding.editTextTextEmailAddress.text.toString().trim()
            val pass=binding.editTextTextPassword.text.toString().trim()
            val verifyPass= binding.editTextConfirmpass.text.toString().trim()

            if( email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty())
            {
                if(pass==verifyPass)  // ako se sifre poklapaju
                {
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(context,"Uspesna registracija!!!", Toast.LENGTH_SHORT).show()
                            navControl.navigate(R.id.action_signUpFragment_to_homeFragment);

                        }
                        else
                        {
                            Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }



                }
            }

        }
    }

    private fun togglePasswordVisibility(editText: EditText, imageView: ImageView) {

        Toast.makeText(context, "Petar", Toast.LENGTH_SHORT).show();
        val inputPass= binding.editTextTextPassword;
        if(inputPass.inputType==InputType.TYPE_TEXT_VARIATION_PASSWORD)
        {
            inputPass.inputType=InputType.TYPE_CLASS_TEXT
            imageView.setImageResource(R.drawable.visible)
        }
        else
        {
            inputPass.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            imageView.setImageResource(R.drawable.hide)
        }

    }

}