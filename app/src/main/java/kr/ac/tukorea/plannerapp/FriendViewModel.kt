package kr.ac.tukorea.plannerapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
class FriendViewModel: ViewModel(){

    private var uId = Firebase.auth.currentUser?.uid
    private var friendRepository = FriendRepository.getInstance(uId!!)

    private val _friend = MutableLiveData<Friend>()
    val friend: LiveData<Friend> = _friend
    private val _friends = MutableLiveData<List<Friend>>()
    val friends: LiveData<List<Friend>> = _friends
    private val _friends1 = MutableLiveData<List<Friend>>()
    val friend1: LiveData<List<Friend>> = _friends1

    fun findFriend(friendId : String) {
        friendRepository.findFriend(friendId) { friend ->
            _friend.value = friend
        }
    }

    fun findAllFriends() {
        friendRepository.findAllFriends { friends ->
            _friends.value = friends
        }
    }

    fun isFriendEmailExists(email: String): Boolean {
        val existingFriends = friends.value
        return existingFriends?.any { it.email == email } ?: false
    }
}