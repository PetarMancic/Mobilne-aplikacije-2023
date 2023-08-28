package elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data

import androidx.lifecycle.ViewModel
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon

class FrizerskiSalonViewModel : ViewModel(){
    var FrizerskiSaloni: ArrayList<FrizerskiSalon> = ArrayList<FrizerskiSalon>()

   public  fun addPlace(placeName: FrizerskiSalon) {
        FrizerskiSaloni.add(placeName)
    }

    fun getFrizerskiSaloni(): List<FrizerskiSalon> {
        return FrizerskiSaloni
    }

    fun postaviNaNull()
    {
        FrizerskiSaloni.clear();

    }

}