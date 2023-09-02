package elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class LocationViewModel : ViewModel() {
    private val _longitude= MutableLiveData<String>("")
    private var  nizSalona:ArrayList<FrizerskiSalon> = ArrayList();
    private val _nizSalona=MutableLiveData<ArrayList<FrizerskiSalon>>();

    var markeri:ArrayList<Marker> = ArrayList();
    val liveNizSalona:LiveData<ArrayList<FrizerskiSalon>>
        get()=_nizSalona;

    val longitude:LiveData<String>
        get()=_longitude

    private val _latitude= MutableLiveData<String>("")
        val latitude:LiveData<String>
        get()=_latitude;

    var setLocation:Boolean=false
    fun setLocation(lon:String, lat:String)
    {
        _longitude.value=lon
        _latitude.value=lat
        setLocation=false;
    }

    fun getLocation(): LocationData {
        return LocationData(_longitude.value ?: "", _latitude.value ?: "")

    }

fun setFrizerskiSaloni(saloni:ArrayList<FrizerskiSalon>)
{
    nizSalona=saloni;
}

    fun getfrizerskiSaloni() : ArrayList<FrizerskiSalon>
    {
        return nizSalona;
    }

    fun ucitajFrizerskeSaloneURadijusu(  ) {







            //Toast.makeText(context, "${latitude}", Toast.LENGTH_SHORT).show();
            val db = FirebaseDatabase.getInstance().getReference("objekti")


            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        nizSalona.clear();
                        for (jedanObjekat in snapshot.children) {
                            val jedanFrizerskiSalon =
                                jedanObjekat.getValue(FrizerskiSalon::class.java)
                            if (jedanFrizerskiSalon != null) {
                                nizSalona.add(jedanFrizerskiSalon);

                            }
                        }
                        _nizSalona.postValue(nizSalona);

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Obrada greške
                }
            })

    }

    init {
        ucitajFrizerskeSaloneURadijusu();
    }

}