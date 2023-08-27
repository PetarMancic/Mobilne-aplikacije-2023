package elfak.mosis.petaraplikacija.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.navigation.ui.AppBarConfiguration
import elfak.mosis.petaraplikacija.R
import elfak.mosis.petaraplikacija.databinding.FragmentListBinding
import elfak.mosis.petaraplikacija.databinding.FragmentProbaBinding


class ProbaFragment : Fragment() {

//nisam si tija nametal, reko da proradi listView pa onda
    // Daj u list fragment
    private lateinit var places:ArrayList<String>
    private lateinit var binding: FragmentProbaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proba, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Ne ulazi u ovu fju, nzm kvo, ne znam stvarno, ja gleda od lab i pokusva
        places = ArrayList<String>()
        places.add("Tvrdjava")
        places.add("Kale")
        // Tija fragment posle u toku laba postaje ListFragment i ti to imas, kvo oces s ProbaFragment?
        //pa teo sam da mi prikaze sve sto ima u ListFragment
        // Dobro, ali cemu? pa pretposstavljam da ce mi to treba za kasnije jer ga rade na lab
        // Na lab rade u second fragment i to ce preimenuju u ListFragment i to si urabotil
        //a odakle secaju podaci
        // Podaci za sad su hardcoded, pose ce se dobijaju podaci iz Firebase
        // Ti sad posto si prikazal podaciti u ListView, odradi isto s hardcoded, ali da citas listu iz ViewModel klase
        // koja je to klasa
        // To si nadji na lab, ima podosta oko tija, i za sta mi sluzi kad citam iz tu klasu
        //i sta sledece da radim, da pratim li samo lab
        // Sad vidi oko ViewModel klasa, ima to negde na lab
        // Pise sve za kvo sluzi, kvo se koristi...
        //to si je na ovija lab, znaci samo da nastavim, ok

        val myPlacesList: ListView = requireView().findViewById<ListView>(R.id.listView1)

        // Ispravno korištenje ArrayAdapter konstruktora
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, places)
        myPlacesList.adapter = adapter

    }







}