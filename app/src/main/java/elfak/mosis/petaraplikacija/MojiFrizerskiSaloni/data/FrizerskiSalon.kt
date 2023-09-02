package elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data

import com.google.firebase.database.Exclude

data class FrizerskiSalon(
    var name:String = "",
    var description:String = "",
    var MZ: String = "",
    var trenutnaOcena:Double= 0.0,
    var longitude:String = "",
    var latitude:String = "",
    var autorID:String = "",
    var autorIme:String = "",
    var autorPrezime:String = "",
    var korisnickoIme:String="",
    var DatumKreiranja:Long=0,
    var kljuc:String=""



)
{
    @get:Exclude
    public var key: String = "";

    override fun toString(): String
    {
        val formatiranaOcena = String.format("%.2f", trenutnaOcena)
        val naziv = "<font color='red'>$name</font>"
        val ocena = "<font color='red'>$trenutnaOcena</font>"
        val autor = "<font color='red'>$korisnickoIme</font>"
        var tip:String="Muski";
        if(MZ=="Z")
        {
            tip = "Zenski"
        }
        return "Naziv:$name    Ocena: $trenutnaOcena Tip:$tip   Autor:$korisnickoIme  "
    }


}
