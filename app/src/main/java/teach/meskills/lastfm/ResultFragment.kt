package teach.meskills.lastfm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import teach.meskills.lastfm.databinding.ResultFragmentBinding
import teach.meskills.lastfm.login.CustomPreference
import teach.meskills.lastfm.login.LoginFragment

class ResultFragment : Fragment() {
    private lateinit var binding: ResultFragmentBinding
    private val pref by lazy {
        CustomPreference(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ResultFragmentBinding.inflate(inflater, container, false)
        binding.result.text = SUCCESS

        binding.logOut.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, LoginFragment.newInstance())
                .commit()
            pref.login = ""
            pref.password = ""
        }
        return binding.root
    }

    companion object {
        const val SUCCESS = "Congratulations, you are logged in!"
        fun newInstance(): ResultFragment {
            return ResultFragment()
        }
    }
}
