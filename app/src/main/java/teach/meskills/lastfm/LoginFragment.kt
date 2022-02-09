package teach.meskills.lastfm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import teach.meskills.lastfm.databinding.SignInFragmentBinding

class LoginFragment : Fragment() {
    private lateinit var binding: SignInFragmentBinding
    private val viewModel: UserViewModel by viewModels()
    private val pref by lazy {
        CustomPreference(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignInFragmentBinding.inflate(inflater, container, false)
        val autoLogin = binding.login.setText(pref.login)
        val autoPass = binding.password.setText(pref.password)
        if (pref.login.isNotEmpty() && pref.password.isNotEmpty()) {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, ChartFragment.newInstance())
                .commit()
        } else {
            viewModel.isSuccessfullyEnter.observe(viewLifecycleOwner) { v ->
                if (v!!) {
                    viewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                        if (user != null) {
                            pref.login = user.userName
                            pref.password = user.password
                        }
                    }
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, ChartFragment.newInstance())
                        .commit()
                } else {
                    Toast.makeText(requireContext(), "Wrong login or password", Toast.LENGTH_SHORT)
                        .show()
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
