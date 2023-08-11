package kr.ac.tukorea.plannerapp


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kr.ac.tukorea.plannerapp.databinding.PlanItem1Binding

class Plan1Adapter(var plans: List<Plan>, var days: String, var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    var filteredPlanList: List<Plan> = listOf()

    inner class ViewHolder(itemView: PlanItem1Binding) : RecyclerView.ViewHolder(itemView.root) {
        private var planContext = itemView.tvTodo
        fun bind(position: Int) {
            planContext.text = filteredPlanList[position].context
            itemView.setOnClickListener {
                val intent = Intent(context, ModifyPlanActivity::class.java)
                intent.putExtra("plan_item", filteredPlanList[position])
                context.startActivity(intent)
            }
        }
    }

    init {
        for (i in plans) {
            if ((i.dateStart == days) and (!i.isImportant)) {
                this.filteredPlanList += i
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.plan_item1, viewGroup, false)
        return ViewHolder(PlanItem1Binding.bind(view))
    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        (viewholder as ViewHolder).bind(position)
    }

    override fun getItemCount(): Int = filteredPlanList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredPlanList = plans
                } else {
                    val filteredList = ArrayList<Plan>()
                    for (row in plans) {
                        if (row.timeStart.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredPlanList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredPlanList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredPlanList = filterResults.values as ArrayList<Plan>
                notifyDataSetChanged()
            }
        }
    }
}