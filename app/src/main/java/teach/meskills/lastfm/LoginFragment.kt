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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignInFragmentBinding.inflate(inflater, container, false)
        binding.signIn.setOnClickListener {
            val userName = binding.login.text.toString()
            val password = binding.password.text.toString()
            if (check(userName, password)) {
                viewModel.onSignInClick(userName, password)
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, ResultFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "Wrong login or password", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return binding.root
    }

    private fun check(userName: String, password: String): Boolean {
        return userName == "Realisttt19" && password == "Zz123123!"
    }
}