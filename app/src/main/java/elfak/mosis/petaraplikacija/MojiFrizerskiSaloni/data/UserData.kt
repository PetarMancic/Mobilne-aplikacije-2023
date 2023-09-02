package elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data

data class UserData(
    val id: String,
    val ime:String = "",
    val prezime:String = "",
   // val profilnaSlika:String,
    val brojBodova: Int = 0,
    val korisnickoIme:String=""
)
{
    constructor() : this("", "", "", 0,"")

    override fun toString(): String
    {
       // return "Naziv:$name  Ocena:$trenutnaOcena Autor:$autorIme $autorPrezime"
        return ime;
    }

    fun prikaziKoirniske():String
    {
        return "Ime $ime $prezime  korisnicko ime: $korisnickoIme broj bodova: $brojBodova"
    }

}
