package elfak.mosis.petaraplikacija

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.children
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.ListFragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import elfak.mosis.petaraplikacija.databinding.ActivityMainBinding
import androidx.navigation.ui.setupActionBarWithNavController
import elfak.mosis.petaraplikacija.fragments.EditFragment

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)



    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        return super.onCreateView(name, context, attrs)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val navController = this.findNavController(R.id.nav_host_fragment_content_main)



        when (item.itemId) {

            R.id.HomeFragment-> {
                when (navController.currentDestination?.id) {

                    R.id.splashFragment -> { navController.navigate(R.id.action_splashFragment_to_homeFragment)}
                    R.id.mapFragment -> { navController.navigate(R.id.action_mapFragment_to_homeFragment) }
                    R.id.signUpFragment -> { navController.navigate(R.id.action_signUpFragment_to_homeFragment) }
                    R.id.signinFragment -> { navController.navigate(R.id.action_signinFragment_to_homeFragment) }
                    R.id.listFragment -> { navController.navigate(R.id.action_listFragment_to_homeFragment) }
                    R.id.editFragment -> { navController.navigate(R.id.action_editFragment_to_homeFragment) }
                    R.id.filtriranjeFragment->{navController.navigate(R.id.action_filtriranjeFragment_to_homeFragment)}

                }
            }
          R.id.signinFragment -> {
                when (navController.currentDestination?.id) {
                    R.id.signUpFragment -> { navController.navigate(R.id.action_signUpFragment_to_signinFragment) }
                    R.id.homeFragment -> { navController.navigate(R.id.action_homeFragment_to_signinFragment) }
                    R.id.mapFragment -> { navController.navigate(R.id.action_mapFragment_to_signinFragment) }
                    R.id.listFragment -> { navController.navigate(R.id.action_listFragment_to_signinFragment) }
                }
            }
            R.id.signUpFragment->
            {
                when(navController.currentDestination?.id){
                    R.id.signinFragment->{ navController.navigate(R.id.action_signinFragment_to_signUpFragment) }
                    R.id.HomeFragment->{ navController.navigate(R.id.action_homeFragment_to_signUpFragment)}
                    R.id.mapFragment->{navController.navigate(R.id.action_mapFragment_to_signUpFragment)}
                    R.id.listFragment -> { navController.navigate(R.id.action_listFragment_to_signUpFragment) }
                }
            }
            R.id.mapFragment -> {
                when (navController.currentDestination?.id) {
                    R.id.signUpFragment -> { navController.navigate(R.id.action_signUpFragment_to_mapFragment) }
                    R.id.homeFragment -> { navController.navigate(R.id.action_homeFragment_to_mapFragment) }
                    R.id.signinFragment -> { navController.navigate(R.id.action_signinFragment_to_mapFragment) }
                    R.id.listFragment -> { navController.navigate(R.id.action_listFragment_to_mapFragment) }
                    R.id.editFragment -> { navController.navigate(R.id.action_editFragment_to_mapFragment) }
                    R.id.detailFragment -> { navController.navigate(R.id.action_detailFragment_to_mapFragment2) }
                    R.id.filtriranjeFragment->{navController.navigate(R.id.action_filtriranjeFragment_to_mapFragment)}

                }
            }

            R.id.listFragment -> {
                when (navController.currentDestination?.id) {
                    R.id.signUpFragment -> { navController.navigate(R.id.action_signUpFragment_to_listFragment) }
                    R.id.homeFragment -> { navController.navigate(R.id.action_homeFragment_to_listFragment) }
                    R.id.signinFragment -> { navController.navigate(R.id.action_signinFragment_to_listFragment) }
                    R.id.mapFragment->{navController.navigate(R.id.action_mapFragment_to_listFragment)}
                    R.id.editFragment -> { navController.navigate(R.id.action_editFragment_to_listFragment) }
                    R.id.detailFragment -> { navController.navigate(R.id.action_detailFragment_to_listFragment) }
                    R.id.filtriranjeFragment->{navController.navigate(R.id.action_filtriranjeFragment_to_listFragment)}
                }
            }
            R.id.editFragment -> {
                when (navController.currentDestination?.id) {

                    R.id.homeFragment -> { navController.navigate(R.id.action_homeFragment_to_editFragment2) }
                    R.id.mapFragment->{navController.navigate(R.id.action_mapFragment_to_editFragment3)}
                    R.id.listFragment->{navController.navigate(R.id.action_listFragment_to_editFragment)}

                }
            }
            R.id.filtrirajFragment -> {
                when (navController.currentDestination?.id) {

                    R.id.homeFragment -> { navController.navigate(R.id.action_homeFragment_to_filtriranjeFragment) }
                    R.id.listFragment -> { navController.navigate(R.id.action_listFragment_to_filtriranjeFragment) }
                    R.id.mapFragment->{navController.navigate(R.id.action_mapFragment_to_filtriranjeFragment2)}


                }
            }
            R.id.OdjaviSe -> {
                when (navController.currentDestination?.id) {
                    R.id.homeFragment -> { navController.navigate(R.id.action_homeFragment_to_signinFragment) }
                    R.id.mapFragment->{navController.navigate(R.id.action_mapFragment_to_signinFragment)}
                    R.id.editFragment -> { navController.navigate(R.id.action_editFragment_to_signinFragment) }
                    R.id.filtrirajFragment->{navController.navigate(R.id.action_filtriranjeFragment_to_signinFragment)}
                    R.id.listFragment->{navController.navigate(R.id.action_listFragment_to_signinFragment)}
                    R.id.detailFragment -> { navController.navigate(R.id.action_detailFragment_to_signinFragment) }
                    R.id.filtriranjeFragment->{navController.navigate(R.id.action_filtriranjeFragment_to_signinFragment)}

                }
            }



        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        if (!navController.navigateUp()) {
            super.onBackPressed()
        }
    }

    /*override fun passDataComunicator(editTextInput: String) {
       val bundle= Bundle()
        bundle.putString("message", editTextInput)

        val transaction= this.supportFragmentManager.beginTransaction()
        val listFragment= ListFragment()
        listFragment.arguments=bundle

        transaction.replace(R.id.fragmentID, listFragment)
        transaction.commit()

    }*/

}

