package teach.meskills.lastfm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import teach.meskills.lastfm.databinding.RecyclerChartFragmentBinding

class ChartFragment : Fragment() {
    private lateinit var binding: RecyclerChartFragmentBinding
    private val pref by lazy {
        CustomPreference(requireContext())
    }
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RecyclerChartFragmentBinding.inflate(inflater, container, false)
//        binding.swipeRefresh.setOnRefreshListener {
//            viewModel.openChart()
//        }

        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.devider))
        binding.recycler.addItemDecoration(dividerItemDecoration)
        viewModel.openChart()
        val adapter = RecyclerAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = layoutManager
        viewModel.trackLiveData.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = false
            adapter.audio = it
        }
        binding.logOut1.setOnClickListener {
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
        fun newInstance(): ChartFragment {
            return ChartFragment()
        }
    }
}