package elfak.mosis.petaraplikacija.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.ui.AppBarConfiguration
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalonViewModel
import elfak.mosis.petaraplikacija.R

import elfak.mosis.petaraplikacija.databinding.FragmentListBinding


class ListFragment : Fragment() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: FragmentListBinding
   // private val frizerskiSalon: FrizerskiSalon by activityViewModels()
    private val FrizerskiSaloniViewModel: FrizerskiSalonViewModel by activityViewModels()


var displayMessage: String? = "";

    private var listView: ListView? = null
    private var adapter: ArrayAdapter<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= FragmentListBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)


    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_list, container, false)
        val listView = view.findViewById<ListView>(R.id.listView1)




        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            FrizerskiSaloniViewModel.getFrizerskiSaloni()
        )

        listView.adapter = adapter


        Toast.makeText(context,"Uspesno", Toast.LENGTH_SHORT).show()
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val FrizerskiSaloni:ListView= requireView().findViewById<ListView>(R.id.listView1);
        FrizerskiSaloni.adapter=ArrayAdapter<FrizerskiSalon>(view.context, android.R.layout.simple_list_item_1,FrizerskiSaloniViewModel.FrizerskiSaloni);

        val name= arguments?.getString("name");
        val desc= arguments?.getString("desc");



    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(elfak.mosis.petaraplikacija.R.id.listFragment)
        item.isVisible = false
    }
}