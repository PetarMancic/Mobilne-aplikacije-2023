package elfak.mosis.petaraplikacija.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalonViewModel
import elfak.mosis.petaraplikacija.R
import java.util.Calendar


class FiltriranjeFragment : Fragment() {

    private val FiltriraniFS: FrizerskiSalonViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        FiltriraniFS.FrizerskiSaloniDatum.clear();
        FiltriraniFS.FrizerskiSaloniTip.clear();
        FiltriraniFS.FiltriraniFrizerskiSaloni.clear();

        return inflater.inflate(R.layout.fragment_filtriranje, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filtriraj();
    }

    private fun filtriraj() {
       val buttonFiltriraj= requireView().findViewById<Button>(R.id.buttonPretrazi);




        buttonFiltriraj.setOnClickListener()
        {
            val radioGrupa= requireView().findViewById<RadioGroup>(R.id.radiogrupa);
            val selectedId = radioGrupa.checkedRadioButtonId;

           when (selectedId) {
               R.id.poAutoru->
               {

                   val korisnickoImePolje=requireView().findViewById<EditText>(R.id.editTextText);
                    val korisnickoIme= korisnickoImePolje.text.toString();


                    if(korisnickoIme.isNotEmpty())
                    {
                        //sad treba da korisnickoIme pretrazimo u referenci objekti, i
                        val usersRef=FirebaseDatabase.getInstance().reference.child("objekti");
                        usersRef.addListenerForSingleValueEvent(object :ValueEventListener
                        {

                            override fun onDataChange(snapshot: DataSnapshot){
                                FiltriraniFS.FiltriraniFrizerskiSaloni.clear();
                                for( FS in snapshot.children)
                                {
                                    val frizerskiSalon= FS.getValue(FrizerskiSalon::class.java);

                                    if(frizerskiSalon!!.korisnickoIme== korisnickoIme)
                                    {
                                        //to znaci da smo pronasli objekat ciji je autor isti onaj cije je
                                        //korisnicko ime uneto u editText
                                        //i treba ga dodati u listi
                                        FiltriraniFS.dodajFiltriranSalon(frizerskiSalon);

                                    }
                                }
                                findNavController().navigate(R.id.action_filtriranjeFragment_to_filtriraniFSFragment);
                            }

                            override fun onCancelled(error: DatabaseError) {
                              Toast.makeText(context,"Nesto ne valja",Toast.LENGTH_SHORT).show();
                            }
                        })

                    }
                   else
                    {
                        Toast.makeText(context,"Niste uneli korisnicko ime",Toast.LENGTH_SHORT).show();
                    }
                }
               R.id.poTipu->
               {
                  var MZ=requireView().findViewById<EditText>(R.id.editTextText).text.toString();

                   if(MZ.isNotEmpty())
                   {

                       val usersRef=FirebaseDatabase.getInstance().reference.child("objekti");
                       usersRef.addListenerForSingleValueEvent(object :ValueEventListener
                       {

                           override fun onDataChange(snapshot: DataSnapshot){
                               FiltriraniFS.FrizerskiSaloniTip.clear();

                               for( FS in snapshot.children)
                               {
                                   val frizerskiSalon= FS.getValue(FrizerskiSalon::class.java);
                                   Toast.makeText(context," Vrednost iz firebase :${frizerskiSalon!!.MZ}", Toast.LENGTH_SHORT).show();
                                   if(frizerskiSalon!!.MZ==MZ)
                                   {
                                       //to znaci da smo pronasli objekat ciji je autor isti onaj cije je
                                       //korisnicko ime uneto u editText
                                       //i treba ga dodati u listi
                                       FiltriraniFS.dodajFrizerskiSaloniTip(frizerskiSalon);

                                   }
                               }
                               findNavController().navigate(R.id.action_filtriranjeFragment_to_filtriraniFSFragment);
                           }

                           override fun onCancelled(error: DatabaseError) {
                               Toast.makeText(context,"Nesto ne valja",Toast.LENGTH_SHORT).show();
                           }
                       })

                   }
                   else{
                       Toast.makeText(context,"Niste uneli tip frizerskog salona",Toast.LENGTH_SHORT).show();
                   }
               }
               R.id.poDatumu->
               {

                   val datePickerOd = requireView().findViewById<DatePicker>(R.id.datePickerOd)
                   val datePickerDo = requireView().findViewById<DatePicker>(R.id.datePickerDo)

                   val godinaOd = datePickerOd.year
                   val mesecOd = datePickerOd.month
                   val danOd = datePickerOd.dayOfMonth

                   val godinaDo = datePickerDo.year
                   val mesecDo = datePickerDo.month
                   val danDo = datePickerDo.dayOfMonth

// Napravite instance Calendar objekata za datume "od" i "do"
                   val calendarOd = Calendar.getInstance()
                   calendarOd.set(godinaOd, mesecOd, danOd, 0, 0, 0)
                   val datumOd = calendarOd.timeInMillis

                   val calendarDo = Calendar.getInstance()
                   calendarDo.set(godinaDo, mesecDo, danDo, 23, 59, 59)
                   val datumDo = calendarDo.timeInMillis


                   if(datumOd!=null && datumDo!=null)
                   {

                       val usersRef=FirebaseDatabase.getInstance().reference.child("objekti");
                       usersRef.addListenerForSingleValueEvent(object :ValueEventListener
                       {

                           override fun onDataChange(snapshot: DataSnapshot){
                               FiltriraniFS.FrizerskiSaloniDatum.clear();

                               for( FS in snapshot.children)
                               {
                                   val frizerskiSalon= FS.getValue(FrizerskiSalon::class.java);

                                   val datumKreiranja = frizerskiSalon?.DatumKreiranja ?: 0

                                   if (datumKreiranja in datumOd..datumDo) {
                                       // Objekat spada u željeni raspon datuma, dodajte ga u listu
                                       FiltriraniFS.dodajFrizerskiSaloniDatum(frizerskiSalon!!);
                                   }

                               }
                               findNavController().navigate(R.id.action_filtriranjeFragment_to_filtriraniFSFragment);
                           }

                           override fun onCancelled(error: DatabaseError) {
                               Toast.makeText(context,"Nesto ne valja",Toast.LENGTH_SHORT).show();
                           }
                       })

                   }

               }


            }


        }

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.editFragment)
        item.isVisible = false

        val item1 = menu.findItem(R.id.signinFragment)
        item1.isVisible = false

        val item2 = menu.findItem(R.id.signUpFragment)
        item2.isVisible = false

        val item3= menu.findItem(R.id.filtrirajFragment)
        item3.isVisible=false;


    }

}