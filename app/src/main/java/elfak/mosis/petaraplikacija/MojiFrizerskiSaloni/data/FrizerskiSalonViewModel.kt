package elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data

import androidx.lifecycle.ViewModel
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon

class FrizerskiSalonViewModel : ViewModel(){
    var FrizerskiSaloni: ArrayList<FrizerskiSalon> = ArrayList<FrizerskiSalon>()

     var IDfrizerskogSalona:String="";
   public  fun addPlace(placeName: FrizerskiSalon) {
        FrizerskiSaloni.add(placeName)
    }

    fun getFrizerskiSaloni(): List<FrizerskiSalon> {
        return FrizerskiSaloni
    }
    fun dodajID(ID:String)
    {
        IDfrizerskogSalona=ID;

    }

    fun vratiID():String
    {
        return IDfrizerskogSalona;
    }

    fun postaviNaNull()
    {
        FrizerskiSaloni.clear();

    }

}