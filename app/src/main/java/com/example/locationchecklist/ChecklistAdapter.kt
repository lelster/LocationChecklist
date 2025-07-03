import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.locationchecklist.Checklist
import com.example.locationchecklist.R

class ChecklistAdapter(
    private val checklistList: List<Checklist>,
    private val onItemClick: (Checklist) -> Unit
) : RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder>() {

    inner class ChecklistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checklistName: TextView = itemView.findViewById(R.id.checklistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checklist, parent, false)
        return ChecklistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChecklistViewHolder, position: Int) {
        val checklist = checklistList[position]
        holder.checklistName.text = checklist.name
        holder.itemView.setOnClickListener { onItemClick(checklist) }
    }

    override fun getItemCount(): Int = checklistList.size
}
