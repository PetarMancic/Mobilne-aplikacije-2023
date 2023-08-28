package elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data

data class FrizerskiSalon(
    var name:String = "",
    var description:String = "",
    var MZ: String = "",
    var longitude:String = "",
    var latitude:String = "",
    var autorID:String = "",
    var autorIme:String = "",
    var autorPrezime:String = ""


)
{


    override fun toString(): String
    {
        return name 
    }


}
