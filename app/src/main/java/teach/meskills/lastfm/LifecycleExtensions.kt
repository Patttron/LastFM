package teach.meskills.lastfm

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


inline fun <reified VIEW_MODEL : ViewModel> Fragment.getViewModel(crossinline factoryBlock: () -> VIEW_MODEL): VIEW_MODEL {
    return ViewModelProvider(this, object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factoryBlock() as T
        }
    })[VIEW_MODEL::class.java]
}
