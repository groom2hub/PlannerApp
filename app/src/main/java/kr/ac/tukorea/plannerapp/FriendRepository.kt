package kr.ac.tukorea.plannerapp

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
class FriendRepository(uId: String) {

    private var friendPathString = "users/$uId/friends"
    private var friendDatabaseReference: DatabaseReference = Firebase.database.reference.child(friendPathString)

    companion object {

        private var INSTANCE: FriendRepository? = null

        fun getInstance(uId: String): FriendRepository {
            INSTANCE = FriendRepository(uId)
            return INSTANCE!!
        }
    }

    fun deleteFriend(friendId: String) {
        friendDatabaseReference.child("friend/${friendId}").removeValue()
    }

    fun saveFriend(callback: (DatabaseReference) -> Task<Void>) {
        callback(friendDatabaseReference.child("friend").push())
    }

    fun findFriend(postId: String, callback: (Friend) -> Unit) {
        friendDatabaseReference
            .child("friend")
            .child(postId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val friend = snapshot.getValue(Friend::class.java)!!
                    callback(friend)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun findAllFriends(callback: (List<Friend>) -> Unit) {
        friendDatabaseReference
            .child("friend")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val friendList = mutableListOf<Friend>()
                    for (data in snapshot.children) {
                        val friend = data.getValue(Friend::class.java)
                        if (friend != null){
                            friend.id = data.key.toString()
                            friendList.add(friend)
                        }
                    }
                    callback(friendList.sortedBy { it.name })
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}
