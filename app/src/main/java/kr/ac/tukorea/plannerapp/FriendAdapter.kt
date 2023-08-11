package kr.ac.tukorea.plannerapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.FriendItemBinding

class FriendAdapter(var friends: List<Friend>, var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var filteredFriendList: List<Friend> = listOf()
    private val friendRepository: FriendRepository = FriendRepository.getInstance(Firebase.auth.currentUser!!.uid)

    inner class ViewHolder(itemView: FriendItemBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var friendName = itemView.name
        private var friendEmail = itemView.email

        fun bind(position: Int) {
            friendName.text = filteredFriendList[position].name
            friendEmail.text = filteredFriendList[position].email

            itemView.setOnClickListener {
                val friend = filteredFriendList[position]

                var builder = AlertDialog.Builder(context)
                builder.setTitle("친구 삭제")

                val alertDialog = builder.create()

                alertDialog.apply {
                    setButton(AlertDialog.BUTTON_POSITIVE, "확인") { _, _ ->
                    }

                    setButton(AlertDialog.BUTTON_NEGATIVE, "취소") { dialog, _ ->
                        dialog.dismiss()
                    }

                    show()

                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        friendRepository.deleteFriend(friend.id)
                        alertDialog.dismiss()
                    }
                }
            }
        }
    }

    init {
        for (i in friends) {
            this.filteredFriendList += i
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.friend_item, viewGroup, false)
        return ViewHolder(FriendItemBinding.bind(view))
    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        (viewholder as ViewHolder).bind(position)
    }

    override fun getItemCount(): Int = filteredFriendList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredFriendList = friends
                } else {
                    val filteredList = ArrayList<Friend>()
                    for (row in friends) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredFriendList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredFriendList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredFriendList = filterResults.values as ArrayList<Friend>
                notifyDataSetChanged()
            }
        }
    }
}