package elfak.mosis.petaraplikacija.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.children
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.petaraplikacija.R




class SplashFragment : Fragment() {

private lateinit var auth: FirebaseAuth
private lateinit var navConotroler: NavController



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
            auth= FirebaseAuth.getInstance()
            navConotroler= Navigation.findNavController(view);

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
    /*if(auth.currentUser!=null)
    {
    navConotroler.navigate(R.id.action_splashFragment_to_homeFragment);
    }else
    {*/
        navConotroler.navigate(R.id.action_splashFragment_to_signinFragment);

   // }
        }, 3000)


      /*  val continueButton: Button = view.findViewById(R.id.continueButton)
        continueButton.setOnClickListener {
            Toast.makeText(requireContext(), "petar", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_splashFragment_to_signinFragment)
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {  // da se ne vide 3 tackice
        super.onPrepareOptionsMenu(menu)
        for (x in menu.children) {
            x.isVisible = false
        }
    }

}