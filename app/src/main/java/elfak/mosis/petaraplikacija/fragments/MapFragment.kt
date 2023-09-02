package elfak.mosis.petaraplikacija.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalon
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.FrizerskiSalonViewModel
import elfak.mosis.petaraplikacija.MojiFrizerskiSaloni.data.LocationViewModel
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

    lateinit var map: MapView

    private val locationViewModel: LocationViewModel by activityViewModels()
    private val frizerskiSalon: FrizerskiSalonViewModel by activityViewModels()
   private var filtriraniSaloni: ArrayList<FrizerskiSalon> = ArrayList();

    private  var nizSalona:ArrayList<FrizerskiSalon> = ArrayList();

      var radijusKM=1.0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ctx: Context = requireContext().applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        map = requireView().findViewById<MapView>(R.id.map);
        map.setMultiTouchControls(true)
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            setMyLocationOverlay()
            setOnMapClickOveralay();
            val nizObserver= Observer<ArrayList<FrizerskiSalon>>{
                    newValue->
                nizSalona=newValue;


                if(frizerskiSalon.vratiFiltriraneSalone().isEmpty()&&
                    frizerskiSalon.vratiFrizerskiSaloniDatum().isEmpty()&&
                    frizerskiSalon.vratiFrizerskeSaloneTip().isEmpty() )
                {
                    ucitajSveFrizerskeSalone(100.0)
                }
                else
                {
//pravimo tip overlay
                    val myLocationOverlay =
                        map.overlays.find { it is MyLocationNewOverlay } as MyLocationNewOverlay?



                    myLocationOverlay?.runOnFirstFix {
                        ucitajFiltriraneSalone(100.0,myLocationOverlay);
                    }
                }


            }
            locationViewModel.liveNizSalona.observe(viewLifecycleOwner, nizObserver);

        }


        map.controller.setZoom(15.0)
        val startPoint = GeoPoint(43.151834, 22.585)
        map.controller.setCenter(startPoint)





        var dugmeSearch=view.findViewById<Button>(R.id.buttonSearch);
        dugmeSearch.setOnClickListener()
        {
            var radijus=view.findViewById<EditText>(R.id.editTextText2).text.toString();

            if(radijus.isNotEmpty()) {
                radijusKM = radijus.toDouble();
                ucitajSveFrizerskeSalone(radijusKM);
            }
            else
            {
                Toast.makeText(context,"Niste uneli vrednost za radius!",Toast.LENGTH_SHORT).show();
            }
        }

        var dugmePlus = view.findViewById<Button>(R.id.buttonDodaj);
        dugmePlus.setOnClickListener()
        {
            val myLocationOverlay =
                map.overlays.find { it is MyLocationNewOverlay } as MyLocationNewOverlay?
            if (myLocationOverlay != null && myLocationOverlay.myLocation != null) {
                val currentLocation = myLocationOverlay.myLocation
                val latitude = currentLocation.latitude
                val longitude = currentLocation.longitude

                locationViewModel.setLocation(longitude.toString(), latitude.toString());


                findNavController().navigate(R.id.action_mapFragment_to_editFragment2);

            }
        }



    }

    private fun ucitajFiltriraneSalone(radijusKilometri: Double,myLocationOverlay:MyLocationNewOverlay) {

       if(frizerskiSalon.vratiFiltriraneSalone().isNotEmpty())
       {
           filtriraniSaloni=frizerskiSalon.vratiFiltriraneSalone();
       }
        else if(frizerskiSalon.vratiFrizerskeSaloneTip().isNotEmpty() )
       {
           filtriraniSaloni=frizerskiSalon.vratiFrizerskeSaloneTip();
       }
        else
       {
           filtriraniSaloni=frizerskiSalon.vratiFrizerskiSaloniDatum();
       }



//poslednje zabelezeno

        if (myLocationOverlay != null && myLocationOverlay.lastFix != null) {

            val currentLocation = myLocationOverlay.lastFix
            val latitude = currentLocation.latitude.toDouble()
            val longitude = currentLocation.longitude.toDouble()




            for(marker in locationViewModel.markeri )
            {
                map.overlays.remove(marker);
            }
            locationViewModel.markeri.clear();

            map.invalidate();


            for (jedanObjekat in filtriraniSaloni) {

                if (jedanObjekat != null) {
                    val salonLatitude = jedanObjekat.latitude.toDouble()
                    val salonLongitude = jedanObjekat.longitude.toDouble()


                    val udaljenost = calculateDistance(
                        latitude, longitude, salonLatitude, salonLongitude
                    )

                    if (udaljenost <= radijusKilometri) {
                        createMarker(jedanObjekat)
                    }
                }
            }
            map.invalidate()
        }


    }

//    private fun postaviViewModel() {
//        val myLocationOverlay =
//            map.overlays.find { it is MyLocationNewOverlay } as MyLocationNewOverlay?
//        if (myLocationOverlay != null && myLocationOverlay.myLocation != null) {
//            val currentLocation = myLocationOverlay.myLocation
//            val latitude = currentLocation.latitude
//            val longitude = currentLocation.longitude
//
//            locationViewModel.setLocation(longitude.toString(), latitude.toString());
//
//        }
//    }

    private fun setOnMapClickOveralay() {
        var receive = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                var lon = p?.longitude.toString();
                var lat = p?.latitude.toString();

                var latitudeCurrent = 0.0;
                var longitudeCurrent = 0.0;
                val myLocationOverlay =
                    map.overlays.find { it is MyLocationNewOverlay } as MyLocationNewOverlay?
                if (myLocationOverlay != null && myLocationOverlay.myLocation != null) {
                    val currentLocation = myLocationOverlay.myLocation
                    latitudeCurrent = currentLocation.latitude
                    longitudeCurrent = currentLocation.longitude
                }
                var startPoint = GeoPoint(latitudeCurrent, longitudeCurrent);
                var endPoint = GeoPoint(p!!.latitude, p!!.longitude);


                if (startPoint.distanceToAsDouble(endPoint) < 200) {
                    locationViewModel.setLocation(lon, lat)
                    //findNavController().popBackStack();
                    findNavController().navigate(R.id.action_mapFragment_to_editFragment2);
                    return true
                    }
                else
                {
                    Toast.makeText(context,"Ne mozete dodati objekat koji nije u radiusu od 100m",Toast.LENGTH_SHORT).show();
                    return false;
                }

                }

                override fun longPressHelper(p: GeoPoint?): Boolean {
                    return false;
                }
            }

            var overlayEvents = MapEventsOverlay(receive);
            map.overlays.add(overlayEvents);

    }


    private fun setMyLocationOverlay() {
        var myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)
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


    override fun onResume() {
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

        val item1 = menu.findItem(R.id.signUpFragment);
        val item2 = menu.findItem(R.id.signinFragment);
        item1.isVisible = false;
        item2.isVisible = false;
        val item3= menu.findItem(R.id.editFragment);
        item3.isVisible=false;

    }

   fun ucitajSveFrizerskeSalone(radijus:Double) {

       val myLocationOverlay =
           map.overlays.find { it is MyLocationNewOverlay } as MyLocationNewOverlay?

       myLocationOverlay?.runOnFirstFix {
           ucitajFrizerskeSaloneURadijusu(radijus, myLocationOverlay);
       }

    }

    fun createMarker(frizerskiSalon: FrizerskiSalon) {
        if(map != null) {
            val marker = Marker(map)
            map.overlays?.add(marker)
            marker.position =
                GeoPoint(frizerskiSalon.latitude.toDouble(), frizerskiSalon.longitude.toDouble())
            marker.title = frizerskiSalon.toString()
           ;
            locationViewModel.markeri.add(marker);
        }
    }

    fun ucitajFrizerskeSaloneURadijusu(radijusKilometri: Double, myLocationOverlay: MyLocationNewOverlay) {



        if (myLocationOverlay != null && myLocationOverlay.lastFix != null) {

            val currentLocation = myLocationOverlay.myLocation
            val latitude = currentLocation.latitude.toDouble();
            val longitude = currentLocation.longitude.toDouble()

            for(marker in locationViewModel.markeri )
            {
                map.overlays.remove(marker);
            }
            locationViewModel.markeri.clear();


                        for (jedanObjekat in nizSalona) {

                            if (jedanObjekat != null) {
                                val salonLatitude = jedanObjekat.latitude.toDouble()
                                val salonLongitude = jedanObjekat.longitude.toDouble()


                                val udaljenost = calculateDistance(
                                    latitude, longitude, salonLatitude, salonLongitude
                                )

                                if (udaljenost <= radijusKilometri) {
                                    createMarker(jedanObjekat)
                                }
                            }
                        }
                       map.invalidate()
                    }
                }





        // Funkcija za računanje udaljenosti između tačaka koristeći Haversine formulu
        fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val R = 6371.0 // Earth's radius in kilometers
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                    Math.sin(dLon / 2) * Math.sin(dLon / 2)
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            return R * c
        }

    override fun onDestroyView() {
        frizerskiSalon.vratiFiltriraneSalone().clear();
        frizerskiSalon.vratiFrizerskiSaloniDatum().clear();
        frizerskiSalon.vratiFrizerskeSaloneTip().clear();

        locationViewModel.markeri.clear();
        super.onDestroyView()
    }
}
