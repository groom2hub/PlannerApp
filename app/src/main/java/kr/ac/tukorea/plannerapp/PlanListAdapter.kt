package kr.ac.tukorea.plannerapp


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.tukorea.plannerapp.databinding.PlanItemListBinding

class PlanListAdapter(var plans: List<Plan>, context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {
    var filteredPlanList: List<Plan> = listOf()
    var dateList: List<String> = listOf()

        inner class ViewHolder(itemView: PlanItemListBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val date = itemView.tvDate
        private val rvPlan1 = itemView.rvPlanItems1
        private val rvPlan2 = itemView.rvPlanItems2

        fun bind(position: Int) {
            date.text = dateList[position]
            rvPlan1.apply {
                adapter = Plan1Adapter(plans, dateList[position])
                layoutManager =
                    LinearLayoutManager(rvPlan1.context, LinearLayoutManager.VERTICAL, false)
            }
            rvPlan2.apply {
                adapter = Plan2Adapter(plans, dateList[position])
                layoutManager =
                    LinearLayoutManager(rvPlan2.context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    init {
        this.filteredPlanList = plans
        for (i in plans) {
            if (i.d_day !in dateList) {
                this.dateList += i.d_day
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.plan_item_list, viewGroup, false)
        return ViewHolder(PlanItemListBinding.bind(view))
    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        (viewholder as ViewHolder).bind(position)
    }

    override fun getItemCount(): Int = dateList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredPlanList = plans
                } else {
                    val filteredList = ArrayList<Plan>()
                    for (row in plans) {
                        if (row.d_day.toLowerCase().contains(charString.toLowerCase())) {
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