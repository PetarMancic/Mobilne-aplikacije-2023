package elfak.mosis.petaraplikacija.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalonViewModel
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.LocationViewModel
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.UserData
import elfak.mosis.petaraplikacija.R
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapFragment : Fragment() {

    lateinit var map:MapView

    private val locationViewModel: LocationViewModel by activityViewModels()
    private val frizerskiSalon: FrizerskiSalonViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_map, container, false)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ctx: Context = requireContext().applicationContext
        Configuration.getInstance().load(ctx,PreferenceManager.getDefaultSharedPreferences(ctx))
        map= requireView().findViewById<MapView>(R.id.map);
        map.setMultiTouchControls(true)
        if(ActivityCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }else
        {
            setMyLocationOverlay()
            setOnMapClickOveralay()
        }

        map.controller.setZoom(15.0)
        val startPoint= GeoPoint(43.151833, 22.585)
        map.controller.setCenter(startPoint)
        //map.invalidate()

        var dugmePlus= view.findViewById<Button>(R.id.buttonDodaj);
        dugmePlus.setOnClickListener()
        {
            val myLocationOverlay = map.overlays.find { it is MyLocationNewOverlay } as MyLocationNewOverlay?
            if (myLocationOverlay != null && myLocationOverlay.myLocation != null) {
                val currentLocation = myLocationOverlay.myLocation
                val latitude = currentLocation.latitude
                val longitude = currentLocation.longitude

                locationViewModel.setLocation(longitude.toString(), latitude.toString());

                //findNavController().popBackStack();
                findNavController().navigate(R.id.action_mapFragment_to_editFragment2);
                // Sada možete koristiti latitude i longitude za dalje manipulacije
            }
        }

            ucitajSveFrizerskeSalone();
    }

    private fun setOnMapClickOveralay() {
        var receive= object:  MapEventsReceiver{
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                var lon= p?.longitude.toString();
                var lat=p?.latitude.toString();
                locationViewModel.setLocation(lon, lat)
                //findNavController().popBackStack();
                findNavController().navigate(R.id.action_mapFragment_to_editFragment2);
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false;
            }
        }
        var overlayEvents= MapEventsOverlay(receive);
        map.overlays.add(overlayEvents);
    }


    private fun setMyLocationOverlay() {
       var myLocationOverlay= MyLocationNewOverlay(GpsMyLocationProvider(activity),map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            setMyLocationOverlay()
            setOnMapClickOveralay()
        }
    }


    override fun onResume()
    {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.mapFragment)
        item.isVisible = false

        val item1= menu.findItem(R.id.signUpFragment);
        val item2= menu.findItem(R.id.signinFragment);
        item1.isVisible=false;
        item2.isVisible=false;

    }

    fun ucitajSveFrizerskeSalone()
    {
        val db = FirebaseDatabase.getInstance().getReference("objekti");

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

        for(jedanObjekat in snapshot.children)
        {
            val jedanFrizerskiSalon = jedanObjekat.getValue(FrizerskiSalon::class.java)
            createMarker(jedanFrizerskiSalon!!);
        }
                    map.invalidate();


                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Obrada greške
            }
        })

    }
    fun createMarker(frizerskiSalon: FrizerskiSalon)
    {
        val marker = Marker(map)
        map.overlays?.add(marker)
        marker.position = GeoPoint(frizerskiSalon.latitude.toDouble(), frizerskiSalon.longitude.toDouble())
        marker.title = frizerskiSalon.toString()
    }

}
