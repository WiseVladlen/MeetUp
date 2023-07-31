package com.example.meet_up.presentation.bottomNavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.R
import com.example.meet_up.databinding.BottomNavigationFragmentBinding

class BottomNavigationFragment: Fragment(R.layout.bottom_navigation_fragment) {

    private val binding by viewBinding<BottomNavigationFragmentBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = (childFragmentManager.findFragmentById(R.id.menu_container) as NavHostFragment).navController
        binding.bottomMenu.setupWithNavController(navController)
    }
}