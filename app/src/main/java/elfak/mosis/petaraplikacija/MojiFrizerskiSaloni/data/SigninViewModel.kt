package elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data

import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage

class SigninViewModel: ViewModel() {
    var korisnici: ArrayList<UserData> = ArrayList<UserData>()

    fun dodajUsera(korisnik: UserData )
    {
        korisnici.add(korisnik);
    }


    fun getUser(): List<UserData> {
        return korisnici
    }


    fun preuzmiSlikuKorisnika(userId: String, onSuccess: (String) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().getReference("profile_images")
        val imageReference = storageReference.child("$userId.jpg")

        imageReference.downloadUrl.addOnSuccessListener {
            val imageUrl = it.toString()
            onSuccess(imageUrl)
        }
    }
}