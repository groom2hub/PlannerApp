package kr.ac.tukorea.plannerapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.ac.tukorea.plannerapp.databinding.PlanItem2Binding

class Plan2Adapter(var plans: List<Plan>, days: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var filteredPlanList: List<Plan> = listOf()

    inner class ViewHolder(itemView: PlanItem2Binding) : RecyclerView.ViewHolder(itemView.root) {
        private val planContext = itemView.tvTodo
        fun bind(position: Int) {
            planContext.text = filteredPlanList[position].context
        }
    }

    init {
        for (i in plans) {
            if ((i.d_day == days) and (!i.isImportant)) {
                this.filteredPlanList += i
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.plan_item2, viewGroup, false)
        return ViewHolder(PlanItem2Binding.bind(view))
    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        (viewholder as ViewHolder).bind(position)
    }

    override fun getItemCount(): Int = filteredPlanList.size
}