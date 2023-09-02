package elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data

import androidx.lifecycle.ViewModel
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon

class FrizerskiSalonViewModel : ViewModel(){
    var FrizerskiSaloni: ArrayList<FrizerskiSalon> = ArrayList<FrizerskiSalon>();
    var FiltriraniFrizerskiSaloni: ArrayList<FrizerskiSalon> = ArrayList<FrizerskiSalon>();

    var FrizerskiSaloniTip :ArrayList<FrizerskiSalon> = ArrayList<FrizerskiSalon>();
    var FrizerskiSaloniDatum :ArrayList<FrizerskiSalon> = ArrayList<FrizerskiSalon>();
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

    fun dodajFiltriranSalon(FrizerskiSalon:FrizerskiSalon)
    {
        FiltriraniFrizerskiSaloni.add(FrizerskiSalon)
    }

    fun vratiFiltriraneSalone():ArrayList<FrizerskiSalon>
    {
         return FiltriraniFrizerskiSaloni;
    }


    fun dodajFrizerskiSaloniTip(FrizerskiSalon:FrizerskiSalon)
    {
        FrizerskiSaloniTip.add(FrizerskiSalon);
    }

    fun vratiFrizerskeSaloneTip ():ArrayList<FrizerskiSalon>
    {
        return FrizerskiSaloniTip;
    }

    fun dodajFrizerskiSaloniDatum(FrizerskiSalon:FrizerskiSalon)
    {
        FrizerskiSaloniDatum.add(FrizerskiSalon);

    }
    fun vratiFrizerskiSaloniDatum(): ArrayList<FrizerskiSalon>
    {
        return FrizerskiSaloniDatum;
    }

}