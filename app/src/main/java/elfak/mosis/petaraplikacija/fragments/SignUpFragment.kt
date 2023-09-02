package elfak.mosis.petaraplikacija.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Snackbar
import androidx.compose.ui.text.input.ImeAction
import androidx.core.app.ActivityCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import elfak.mosis.petaraplikacija.R
import elfak.mosis.petaraplikacija.databinding.FragmentSignUpBinding
import java.io.ByteArrayOutputStream
import java.util.UUID


class SignUpFragment : Fragment() {

    private lateinit var auth:FirebaseAuth
    private lateinit var navControl:NavController
    private lateinit var binding: FragmentSignUpBinding
   //private var profileImageView= binding.profileImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSignUpBinding.inflate( inflater, container, false);
        return binding.root

    }

    private val CAMERA_REQUEST = 1888
    private lateinit var profileImageView: ImageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()



        profileImageView = view.findViewById(R.id.profilePicture)
        profileImageView.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val photo = data?.extras!!["data"] as Bitmap?
            profileImageView.setImageBitmap(photo)
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
                "brojTelefona" to brojtelefona,
                "brojBodova" to 0,
                "korisnickoIme" to email

            )

            usersReference.child(firebaseUser.uid).setValue(userMap).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Uspesna registracija i dodavanje u bazu!!!", Toast.LENGTH_SHORT).show()
                    navControl.navigate(R.id.action_signUpFragment_to_signinFragment)
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
            val korisnickoIme=binding.editTextTextEmailAddress.text.toString().trim();


            if( email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty() && ime.isNotEmpty() && prezime.isNotEmpty() && brojTelefona.isNotEmpty() )
            {
                if(pass==verifyPass)  // ako se sifre poklapaju
                {


                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(context,"Uspesna registracija!!!", Toast.LENGTH_SHORT).show()
                            updateProfileImage();
                            signUP(email,pass,ime,prezime,brojTelefona);
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




    private fun updateProfileImage() {
        profileImageView.isDrawingCacheEnabled = true
        profileImageView.buildDrawingCache()
        val bitmap = (profileImageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArr = baos.toByteArray()
        val FB=FirebaseStorage.getInstance().getReference("profile_images");
        val imageReference = FB.child("${FirebaseAuth.getInstance().currentUser!!.uid}.jpg")
        imageReference.putBytes(byteArr)

    }
    override fun onPrepareOptionsMenu(menu: Menu) {  // da se ne vide 3 tackice
        super.onPrepareOptionsMenu(menu)
        for (x in menu.children) {
            x.isVisible = false
        }
    }


}