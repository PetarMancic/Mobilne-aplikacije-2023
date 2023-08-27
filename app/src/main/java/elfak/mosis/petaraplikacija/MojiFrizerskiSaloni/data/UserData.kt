package elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data

data class UserData(
    val id: String,
    val ime:String = "",
    val prezime:String = "",
   // val profilnaSlika:String,
    val brojBodova: Int = 0
)
{
    constructor() : this("", "", "", 0)
}
