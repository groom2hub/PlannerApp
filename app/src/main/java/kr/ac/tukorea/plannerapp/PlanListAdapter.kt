package kr.ac.tukorea.plannerapp


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.tukorea.plannerapp.databinding.PlanItemListBinding

class PlanListAdapter(var plans: List<Plan>, context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var filteredPlanList: List<Plan> = listOf()

    inner class ViewHolder(itemView: PlanItemListBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val date = itemView.tvDate
        private val rvPlan1 = itemView.rvPlanItems1
        private val rvPlan2 = itemView.rvPlanItems2

        fun bind(position: Int) {
            date.text = filteredPlanList[position].d_day
            rvPlan1.apply {
                adapter = Plan1Adapter(plans, filteredPlanList[position].d_day)
                layoutManager = LinearLayoutManager(rvPlan1.context, LinearLayoutManager.VERTICAL, false)
            }
            rvPlan2.apply {
                adapter = Plan2Adapter(plans, filteredPlanList[position].d_day)
                layoutManager = LinearLayoutManager(rvPlan2.context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    init {
        this.filteredPlanList = plans
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.plan_item_list, viewGroup, false)
        return ViewHolder(PlanItemListBinding.bind(view))
    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        (viewholder as ViewHolder).bind(position)
    }

    override fun getItemCount(): Int = filteredPlanList.size
}