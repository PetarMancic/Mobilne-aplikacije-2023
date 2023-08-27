package elfak.mosis.petaraplikacija.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalonViewModel
import elfak.mosis.petaraplikacija.R



class EditFragment : Fragment() {

   private val frizerskiSalon: FrizerskiSalonViewModel by activityViewModels()


   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


       // return inflater.inflate(R.layout.fragment_edit, container, false)
        val view= inflater.inflate(R.layout.fragment_edit, container, false)


        return view;
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.editFragment)
        item.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dugmeAdd: Button? = requireView().findViewById<Button>(R.id.btnAdd);
        dugmeAdd?.setOnClickListener()
        {
            val editName: EditText= requireView().findViewById(R.id.editName);
            val name: String= editName.text.toString()
            val editDesc: EditText= requireView().findViewById(R.id.editDescription);
            val desc:String= editDesc.text.toString();

            Toast.makeText(context, "${name}",Toast.LENGTH_SHORT ).show();
            Toast.makeText(context, "${desc}",Toast.LENGTH_SHORT ).show();


            frizerskiSalon.addPlace(FrizerskiSalon(name,desc));





          findNavController().navigate(R.id.action_editFragment_to_listFragment);
        }
        val cancelButton: Button= requireView().findViewById<Button>(R.id.btnCancel);
        cancelButton.setOnClickListener()
        {
            findNavController().navigate(R.id.action_editFragment_to_listFragment);
        }
        super.onViewCreated(view, savedInstanceState)
    }

}