package elfak.mosis.petaraplikacija.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalonViewModel
import elfak.mosis.petaraplikacija.R

import elfak.mosis.petaraplikacija.databinding.FragmentListBinding


class ListFragment : Fragment() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: FragmentListBinding
   // private val frizerskiSalon: FrizerskiSalon by activityViewModels()
    private val FrizerskiSaloniViewModel: FrizerskiSalonViewModel by activityViewModels()
    val frizerskiSalonList: ArrayList<FrizerskiSalon> = ArrayList()

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

        return view


    }

      //   fun pronadjiIme(idFS: String, callback: (String?) -> Unit) {

      //  }


        private fun izracunajProsecnuOcenu(id:String) {

            val IDFS = id;
            val db = FirebaseDatabase.getInstance().reference

            val referencaFS = FirebaseDatabase.getInstance().reference.child("objekti").child(IDFS)
            val objektiReference = db.child("objekti").child(id)
            objektiReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val frizerskiSalon = snapshot.getValue(FrizerskiSalon::class.java)
                        val ime = frizerskiSalon?.name

                        if (ime != null) {
                            val recenzijeQuery = db.child("recenzije").orderByChild("nazivFrizerskogSalona").equalTo(ime)

                            recenzijeQuery.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(recenzijeSnapshot: DataSnapshot) {
                                    val ratings: MutableList<Int> = mutableListOf()

                                    for (recenzija in recenzijeSnapshot.children) {
                                        val ocenaValue = recenzija.child("ocena").getValue(String::class.java);
                                        val ocena= ocenaValue?.toInt();
                                        if (ocena != null) {
                                            ratings.add(ocena)
                                        }

                                    }

                                    // Izracunavanje prosečne ocene
                                    val sum = ratings.sumBy { it }
                                    val averageRating = if (ratings.isNotEmpty()) {
                                        sum.toDouble() / ratings.size
                                    } else {
                                        0.0
                                    }
                                    referencaFS.child("trenutnaOcena").setValue(averageRating).addOnCompleteListener()
                                    {task->
                                        if(task.isSuccessful)
                                        {


                                            // Osvežavanje liste
                                            adapter?.notifyDataSetChanged()
                                        }

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show();
                                }
                            })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Obrada greške
                }
            })


            adapter?.notifyDataSetChanged();

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val FrizerskiSaloni:ListView= requireView().findViewById<ListView>(R.id.listView1);
        FrizerskiSaloni.adapter=ArrayAdapter<FrizerskiSalon>(view.context, android.R.layout.simple_list_item_1,FrizerskiSaloniViewModel.FrizerskiSaloni);

        val name= arguments?.getString("name");
        val desc= arguments?.getString("desc");
        ucitajSveFrizerskeSalone();

        val listView = view.findViewById<ListView>(R.id.listView1);
        listView.setOnItemClickListener(){ parent,view,position,id->
            showFloatingContextMenu(view, position);

            /*val selectedSalon= parent.adapter.getItem(position) as FrizerskiSalon
            FrizerskiSaloniViewModel.dodajID(selectedSalon.key);
           findNavController().navigate(R.id.action_listFragment_to_detailFragment);*/

           //Toast.makeText(context, "${selectedSalon.key}", Toast.LENGTH_SHORT).show()
           // FrizerskiSaloniViewModel.dodajID(selectedSalon.)

        }

    }

    private fun showFloatingContextMenu(view: View?, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.example_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.dodavanje -> {
                    val selectedSalon = (view?.parent as ListView).adapter.getItem(position) as FrizerskiSalon
                    FrizerskiSaloniViewModel.dodajID(selectedSalon.key)
                    findNavController().navigate(R.id.action_listFragment_to_detailFragment)
                    true
                }
                R.id.Edit -> {
                    val selectedSalon = (view?.parent as ListView).adapter.getItem(position) as FrizerskiSalon
                    FrizerskiSaloniViewModel.dodajID(selectedSalon.key)
                    findNavController().navigate(R.id.action_listFragment_to_editFragment);
                    true
                }
                R.id.komentari -> {
                    val selectedSalon = (view?.parent as ListView).adapter.getItem(position) as FrizerskiSalon
                    FrizerskiSaloniViewModel.dodajID(selectedSalon.key)
                    findNavController().navigate(R.id.action_listFragment_to_komentariFragment);
                    true
                }

                // Dodajte više opcija prema potrebi
                else -> false
            }
        }

        popupMenu.show()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(elfak.mosis.petaraplikacija.R.id.listFragment)
        item.isVisible = false

        val item1 = menu.findItem(R.id.signinFragment)
        item1.isVisible = false

        val item2 = menu.findItem(R.id.signUpFragment)
        item2.isVisible = false
        val item3= menu.findItem(R.id.editFragment)
        item3.isVisible=false;
    }

    fun ucitajSveFrizerskeSalone()
    {
        val db = FirebaseDatabase.getInstance().getReference("objekti");
        val listView: ListView = requireView().findViewById(R.id.listView1)
      //  val frizerskiSalonList: ArrayList<FrizerskiSalon> = ArrayList()

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    frizerskiSalonList.clear()

                    for(jedanObjekat in snapshot.children)
                    {
                        val jedanFrizerskiSalon = jedanObjekat.getValue(FrizerskiSalon::class.java)
                        jedanFrizerskiSalon?.key = jedanObjekat.key!!
                        izracunajProsecnuOcenu(jedanFrizerskiSalon!!.key);
                        adapter?.notifyDataSetChanged();
                        frizerskiSalonList.add(jedanFrizerskiSalon!!)
                    }
                }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, frizerskiSalonList)
                listView.adapter = adapter

                // Osvežavanje adaptera
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Nesto ne valja ", Toast.LENGTH_SHORT).show()
            }
        })
    }
}