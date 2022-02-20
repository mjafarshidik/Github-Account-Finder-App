package com.androidfundamental.githubuserapp.ui.activities

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidfundamental.githubuserapp.R
import com.androidfundamental.githubuserapp.adapter.MainAdapter
import com.androidfundamental.githubuserapp.data.peferences.SettingPreferences
import com.androidfundamental.githubuserapp.databinding.ActivityMainBinding
import com.androidfundamental.githubuserapp.ui.viewmodels.MainViewModel
import com.androidfundamental.githubuserapp.ui.viewmodels.ModeViewModel
import com.androidfundamental.githubuserapp.ui.viewmodels.ViewModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                MainViewModel::class.java
            )
        searchUser()
        getUserData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val switchMode =
            menu.findItem(R.id.switchMode).actionView.findViewById(R.id.switchButton) as SwitchMaterial

        val pref = SettingPreferences.getInstance(dataStore)
        val modeViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            ModeViewModel::class.java
        )
        modeViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchMode.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchMode.isChecked = false
                }
            })
        switchMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            modeViewModel.saveThemeSetting(isChecked)
        }
        return true
    }


    override fun onOptionsItemSelected(id: MenuItem): Boolean {
        when (id.itemId) {
            R.id.favorite_menu -> startActivity(Intent(this, FavoriteActivity::class.java))
        }
        return super.onOptionsItemSelected(id)
    }

    private fun showLoading(state: Boolean) {
        binding.mainProgressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun getUserData() {
        with(binding) {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            mainAdapter = MainAdapter()
            rvUser.adapter = mainAdapter

            mainViewModel.getUserSearch().observe(this@MainActivity) {
                it?.let {
                    if (it.size == 0) {
                        showImageVisibility(true)
                        tvWaiting.text = getString(R.string.not_found)
                        showLoading(false)
                    } else {
                        showImageVisibility(false)
                        mainAdapter.setListUser(it)
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showImageVisibility(state: Boolean) {
        if (state) {
            binding.imgWaiting.setImageResource(R.drawable.ic_people)
            binding.imgWaiting.visibility = View.VISIBLE
            binding.tvWaiting.visibility = View.VISIBLE
        } else {
            binding.imgWaiting.visibility = View.GONE
            binding.tvWaiting.visibility = View.GONE
        }
    }

    private fun searchUser() {
        binding.apply {
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

            searchViewUser.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchViewUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mainAdapter.listUser.clear()
                    query?.let {
                        showLoading(true)
                        mainViewModel.setSearchUser(it)
                        showImageVisibility(false)
                    }
                    val inputMethodManager =
                        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(window.currentFocus?.windowToken, 0)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false

            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
