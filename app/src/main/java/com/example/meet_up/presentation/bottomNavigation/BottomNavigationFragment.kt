package com.example.meet_up.presentation.bottomNavigation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.MainApplication
import com.example.meet_up.R
import com.example.meet_up.databinding.BottomNavigationFragmentBinding
import javax.inject.Inject

class BottomNavigationFragment: Fragment(R.layout.bottom_navigation_fragment) {

    @Inject
    lateinit var mainViewModelFactory: MainViewModel.MainViewModelFactory
    private val viewModel by viewModels<MainViewModel> { mainViewModelFactory }

    private val binding by viewBinding<BottomNavigationFragmentBinding>()

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.mainComponent.inflate(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = (childFragmentManager.findFragmentById(R.id.menu_container) as NavHostFragment).navController
        binding.bottomMenu.setupWithNavController(navController)

        viewModel.synchronize()
    }
}