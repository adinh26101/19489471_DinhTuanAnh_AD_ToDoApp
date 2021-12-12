package iuh.ad.a19489471_dinhtuananh.views

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iuh.ad.a19489471_dinhtuananh.R
import iuh.ad.a19489471_dinhtuananh.data.Task
import android.view.animation.Animation

import android.view.animation.ScaleAnimation




class MyAdapter(private val taskList : ArrayList<Task>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var lastPosition = -1
    var row_index = -1
    private lateinit var Listener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        Listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_view,
            parent,false)
        return MyViewHolder(itemView, Listener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = taskList[position]

        holder.nametask.text = currentitem.task
        holder.descTask.text = currentitem.description

        holder.linearLayout.setOnClickListener(View.OnClickListener {
            row_index = position
            notifyDataSetChanged()
        })

        if (row_index === position) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#adffc5"))
        } else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#87c498"))
        }


        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int {

        return taskList.size
    }


    class MyViewHolder(itemView : View, listener : onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val nametask : TextView = itemView.findViewById(R.id.nameTask)
        val descTask : TextView = itemView.findViewById(R.id.descTask)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.LinearLayout)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            //TranslateAnimation anim = new TranslateAnimation(0,-1000,0,-1000);
            val anim = ScaleAnimation(
                0.0f,
                1.0f,
                0.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            //anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            anim.duration = 550 //to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim)
            lastPosition = position
        }
    }

}