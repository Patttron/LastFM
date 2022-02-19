package teach.meskills.lastfm.chartTracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import teach.meskills.lastfm.*
import teach.meskills.lastfm.data.AppDatabase
import teach.meskills.lastfm.data.ContentRepositoryOkhttp
import teach.meskills.lastfm.data.DownloadWorker
import teach.meskills.lastfm.data.DownloadWorker.Companion.TAG
import teach.meskills.lastfm.databinding.RecyclerChartFragmentBinding
import teach.meskills.lastfm.login.CustomPreference
import teach.meskills.lastfm.login.LoginFragment
import teach.meskills.lastfm.login.LoginManager
import java.util.concurrent.TimeUnit

class ChartFragment : Fragment() {
    private lateinit var binding: RecyclerChartFragmentBinding
    private val pref by lazy {
        CustomPreference(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RecyclerChartFragmentBinding.inflate(inflater, container, false)
        val loginManager = LoginManager(pref)
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.devider))
        binding.recycler.addItemDecoration(dividerItemDecoration)
        val viewModel = getViewModel {
            ChartViewModel(ContentRepositoryOkhttp(AppDatabase.build(requireContext())))
        }
        WorkManager.getInstance(requireContext()).cancelAllWorkByTag(TAG)
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()
        val myWorkRequest =
            PeriodicWorkRequestBuilder<DownloadWorker>(12, TimeUnit.HOURS, 11, TimeUnit.HOURS)
                .addTag(TAG)
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.openChart()
        }
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
            loginManager.logOut()
        }
        return binding.root
    }

    companion object {
        fun newInstance() = ChartFragment()
    }
}

