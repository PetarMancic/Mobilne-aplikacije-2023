package elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {
    private val _longitude= MutableLiveData<String>("")
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


}