package com.example.meet_up.presentation.authorization

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.MainApplication
import com.example.meet_up.R
import com.example.meet_up.databinding.FragmentLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationFragment : Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var viewModelFactory: AuthorizationViewModel.AuthorizationViewModelFactory
    private val viewModel: AuthorizationViewModel by viewModels { viewModelFactory }

    private val binding by viewBinding<FragmentLoginBinding>()

    private val authorizationErrorMessage by lazy { requireContext().getString(R.string.authorization_error_message) }

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.mainComponent.inflate(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        observeModel()
    }

    private fun initializeViews() {
        with(binding) {
            buttonLogIn.setOnClickListener {
                viewModel.authorizationProcess(
                    loginText.text.toString(),
                    passwordText.text.toString(),
                )
            }
        }
    }

    private fun observeModel() {
        viewModel.authorizationProcessLiveData.observe(viewLifecycleOwner) { authResult ->
            if (authResult) {
                navigateToMainScreen()
            } else {
                Toast.makeText(requireContext(), authorizationErrorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMainScreen() {
        binding.root.findNavController().navigate(
            AuthorizationFragmentDirections.actionAuthorizationFragmentToBottomNavigationFragment()
        )
    }
}