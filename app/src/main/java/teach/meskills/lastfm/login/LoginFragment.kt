package teach.meskills.lastfm.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import teach.meskills.lastfm.R
import teach.meskills.lastfm.chart_tracks.ChartFragment
import teach.meskills.lastfm.databinding.SignInFragmentBinding

class LoginFragment : Fragment() {
    private lateinit var binding: SignInFragmentBinding
    private val pref by lazy {
        CustomPreference(requireContext())
    }

    private val viewModel by viewModel<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignInFragmentBinding.inflate(inflater, container, false)
        val loginManager = LoginManager(pref)
        Log.d("prefer", pref.login)
        if (loginManager.isLoggedIn == true) {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, ChartFragment.newInstance())
                .commit()
        } else {
            viewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    loginManager.logIn(user)
                }
            }
            viewModel.errorMessage.observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(requireContext(), "Wrong login or password", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.errorMessage.value = false
                }
            }
            viewModel.isSuccessfullyEnter.observe(viewLifecycleOwner) {
                if (it) {
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, ChartFragment.newInstance())
                        .commit()
                }
            }
            binding.signIn.setOnClickListener {
                val userName = binding.login.text.toString()
                val password = binding.password.text.toString()
                viewModel.onSignInClick(userName, password)
            }
        }
        return binding.root
    }

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}
//userName == "Realisttt19" && password == "Zz123123!"
