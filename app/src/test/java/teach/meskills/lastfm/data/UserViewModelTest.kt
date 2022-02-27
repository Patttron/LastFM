package teach.meskills.lastfm.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import teach.meskills.lastfm.data.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import teach.meskills.lastfm.login.UserViewModel

@RunWith(AndroidJUnit4::class)
class UserViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var viewModel: UserViewModel

    @Before
    fun init(){
        viewModel = UserViewModel(object : ContentRepository {
            override suspend fun getMedia(): List<AudioEntity> = emptyList()

            override suspend fun signIn(login: String, password: String): Boolean = true

        }, Dispatchers.Unconfined, Dispatchers.Unconfined)
    }

    @Test
    fun checkResponseSignIn() {
        viewModel.onSignInClick("","")
        assertEquals(viewModel.isSuccessfullyEnter.getOrAwaitValue(), true)
    }
}
//@Test
//fun test_right_login_password() {
//    val mock = mockk<LoginRepository>()
////        для каждого вызов getAuthorization возвращаем true
//    coEvery { mock.getAuthorization("", "") } returns true
//
//    val viewModel = LoginViewModel(
//        mock,
//        Dispatchers.Unconfined,
//        Dispatchers.Unconfined
//    )
//    viewModel.onSignInClick("", "")
//    assertEquals(viewModel.authIsSuccessful.getOrAwaitValue(), true)
//}