package elfak.mosis.petaraplikacija.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import elfak.mosis.petaraplikacija.R
import elfak.mosis.petaraplikacija.databinding.FragmentSignUpBinding
import java.io.ByteArrayOutputStream
import java.util.UUID


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

    private fun signUP(email: String, pass: String, ime: String, prezime: String, brojtelefona: String) {
        val currentUser = auth.currentUser
        val usersReference = FirebaseDatabase.getInstance().getReference("users")

        currentUser?.let { firebaseUser ->
            val userMap = mapOf(
                "ime" to ime,
                "prezime" to prezime,
                "brojTelefona" to brojtelefona
            )

            usersReference.child(firebaseUser.uid).setValue(userMap).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Uspesna registracija i dodavanje u bazu!!!", Toast.LENGTH_SHORT).show()
                    navControl.navigate(R.id.action_signUpFragment_to_homeFragment)
                } else {
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




    private fun registerEvents(){

        binding.signUpLabel.setOnClickListener{
            navControl.navigate(R.id.action_signUpFragment_to_signinFragment);
        }


        binding.loginButtonsignup.setOnClickListener{
            val email= binding.editTextTextEmailAddress.text.toString().trim()
            val pass=binding.editTextTextPassword.text.toString().trim()
            val verifyPass= binding.editTextConfirmpass.text.toString().trim()

            val ime= binding.editTextTextFirstName.text.toString().trim();
            val prezime=binding.editTextTextLastName.text.toString().trim()
            val brojTelefona=binding.editTextPhoneNumber.text.toString().trim()



            if( email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty() && ime.isNotEmpty() && prezime.isNotEmpty() && brojTelefona.isNotEmpty() )
            {
                if(pass==verifyPass)  // ako se sifre poklapaju
                {


                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(context,"Uspesna registracija!!!", Toast.LENGTH_SHORT).show()
                            signUP(email,pass,ime,prezime,brojTelefona);
                           // navControl.navigate(R.id.action_signUpFragment_to_homeFragment);

                        }
                        else
                        {
                            Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }



                }
                else
                {
                    Toast.makeText(context, "Sifre se ne podudaraju!", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(context, "Nisu popunjena sva polja!", Toast.LENGTH_SHORT).show()
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

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageData = baos.toByteArray()

            val uploadTask = imageRef.putBytes(imageData)
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Slika je uspesno dodata !", Toast.LENGTH_SHORT).show()
                } else {
                    // Neuspeh pri dodavanju slike u Firebase Storage
                    Toast.makeText(context, "Greska pri dodavanju slike !", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
*/

}