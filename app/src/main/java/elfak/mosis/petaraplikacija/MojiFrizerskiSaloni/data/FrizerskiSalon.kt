package elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data

import com.google.firebase.database.Exclude

data class FrizerskiSalon(
    var name:String = "",
    var description:String = "",
    var MZ: String = "",
    var trenutnaOcena:Int=0,
    var longitude:String = "",
    var latitude:String = "",
    var autorID:String = "",
    var autorIme:String = "",
    var autorPrezime:String = ""



)
{
    @get:Exclude
    public var key: String = "";

    override fun toString(): String
    {
        return "Naziv:$name  Ocena:$trenutnaOcena Autor:$autorIme $autorPrezime"
    }


}
