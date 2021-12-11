package iuh.ad.a19489471_dinhtuananh.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iuh.ad.a19489471_dinhtuananh.R
import iuh.ad.a19489471_dinhtuananh.data.Task

class MyAdapter(private val taskList : ArrayList<Task>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_view,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = taskList[position]

        holder.nametask.text = currentitem.task
        holder.descTask.text = currentitem.description

    }

    override fun getItemCount(): Int {

        return taskList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val nametask : TextView = itemView.findViewById(R.id.nameTask)
        val descTask : TextView = itemView.findViewById(R.id.descTask)

    }

}