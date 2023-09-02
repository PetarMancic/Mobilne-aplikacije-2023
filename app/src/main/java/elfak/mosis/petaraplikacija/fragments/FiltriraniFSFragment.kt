package elfak.mosis.petaraplikacija.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalonViewModel
import elfak.mosis.petaraplikacija.R


class FiltriraniFSFragment : Fragment() {
    private val FiltriraniSaloni: FrizerskiSalonViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

         val view=inflater.inflate(R.layout.fragment_filtrirani_f_s, container, false)

        var dugme=view.findViewById<Button>(R.id.button);
        dugme.setOnClickListener{
                findNavController().navigate(R.id.action_filtriraniFSFragment_to_mapFragment);
        }

        return view;

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView: ListView = view.findViewById(R.id.listviewFFS);
        val filtriraniSaloni = FiltriraniSaloni.vratiFiltriraneSalone();
        val saloniPoTipu= FiltriraniSaloni.vratiFrizerskeSaloneTip();
        val salonipoDatumu= FiltriraniSaloni.vratiFrizerskiSaloniDatum();

    lateinit var adapter: ArrayAdapter<FrizerskiSalon>;


        if(filtriraniSaloni.isNotEmpty()) {
             adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                filtriraniSaloni
            )
            listView.adapter = adapter
        }

        if(saloniPoTipu.isNotEmpty()) {
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                saloniPoTipu
            )
            listView.adapter = adapter
        }

        if(salonipoDatumu.isNotEmpty()) {
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                salonipoDatumu
            )
            listView.adapter = adapter
        }


        listView.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var kliknutObjekat:FrizerskiSalon=parent?.adapter?.getItem(position) as FrizerskiSalon;
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {  // da se ne vide 3 tackice
        super.onPrepareOptionsMenu(menu)
        for (x in menu.children) {
            x.isVisible = false
        }
    }

    }

