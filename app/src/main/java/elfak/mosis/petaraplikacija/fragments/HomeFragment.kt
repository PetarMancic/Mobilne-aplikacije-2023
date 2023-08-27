package elfak.mosis.petaraplikacija.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user: List<UserData> =SigninViewModel.getUser();

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
            }

        var imgProfile=view.findViewById<ImageView>(R.id.imgProfile);



        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val storageReference = FirebaseStorage.getInstance().reference
        val imageReference = storageReference.child("profile_images/$userId.jpg")
    imageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener {
        val bmp=BitmapFactory.decodeByteArray(it,0,it.size)
        imgProfile.setImageBitmap(bmp);
    }

     //   val storageReference = FirebaseStorage.getInstance().getReference("gs://petar-aplikacija.appspot.com/$userId.jpg")
       /* Glide.with(this)
            .load(imageReference)
            .into(imgProfile)
*/

        }



    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.signinFragment)
        item.isVisible = false
    }
}