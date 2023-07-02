package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)

        val asteroidListAdapter = AsteroidAdapter(AsteroidClickListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })

        binding.asteroidRecycler.adapter = asteroidListAdapter
        binding.asteroidRecycler.setHasFixedSize(true)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.apply {
            asteroids.observe(viewLifecycleOwner) { asteroids ->
                if (asteroids.isNotEmpty()) {
                    binding.statusLoadingWheel.visibility = View.GONE
                }
            }

            navigatingToDetailView.observe(viewLifecycleOwner) { asteroid ->
                asteroid?.let {
                    this@MainFragment.findNavController().navigate(
                        MainFragmentDirections.actionShowDetail(asteroid)
                    )
                    doneNavigatingToDetailView()
                }
            }
        }


        setHasOptionsMenu(true)

        return binding.root
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
