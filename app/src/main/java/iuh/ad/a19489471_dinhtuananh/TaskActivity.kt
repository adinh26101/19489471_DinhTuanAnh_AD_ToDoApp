package iuh.ad.a19489471_dinhtuananh

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import iuh.ad.a19489471_dinhtuananh.views.AccountActivity
import kotlinx.android.synthetic.main.dialog_add.*
import iuh.ad.a19489471_dinhtuananh.extensions.Extensions.toast
import iuh.ad.a19489471_dinhtuananh.utils.FirebaseUtils
import java.util.*
import com.google.firebase.database.*
import iuh.ad.a19489471_dinhtuananh.data.Task
import iuh.ad.a19489471_dinhtuananh.views.MyAdapter


class TaskActivity : AppCompatActivity() {
    private lateinit var userData : DatabaseReference
    val database = FirebaseDatabase.getInstance()
    var taskCurrentList = arrayListOf<String>()

    private var itemClicked: String = ""
    private var last_position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        getUserData()

        val addBtn: FloatingActionButton = findViewById(R.id.addTask)
        addBtn.setOnClickListener(){
            DialogAdd()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.accountBtn -> {
            startActivity(Intent(this, AccountActivity::class.java))
            true
        }
        R.id.modifyBtn -> {
            DialogModify()
            itemClicked = ""
            true
        }
        R.id.deleteBtn -> {
            delete()
            itemClicked = ""
            true
        }
        R.id.doneBtn -> {
            done()
            itemClicked = ""
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun delete() {
        val author = FirebaseUtils.firebaseAuth.currentUser?.displayName
        val database = FirebaseDatabase.getInstance()

        val removeTask = database.getReference("$author/task/$itemClicked")

        removeTask.removeValue()
        toast("deleted $itemClicked")
        getUserData()
    }

    private fun DialogAdd() {
        val author = FirebaseUtils.firebaseAuth.currentUser?.displayName
        val dialog = Dialog(this@TaskActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add)
        val edtAddTask = dialog.findViewById<View>(R.id.editAddTask) as EditText
        val edtAddDesc = dialog.findViewById<View>(R.id.editAddDescTask) as EditText
        val edtDeadline = dialog.findViewById<DatePicker>(R.id.editDeadline) as DatePicker
        val addBtn = dialog.findViewById<View>(R.id.addBtn) as Button
        val calcelBtn = dialog.findViewById<View>(R.id.cancelBtn) as Button

        edtDeadline.minDate = System.currentTimeMillis()

        addBtn.setOnClickListener {
            val taskName: String = edtAddTask.text.toString()
            val taskDesc: String = edtAddDesc.text.toString()
            val year: Int = edtDeadline.year
            val month: Int = edtDeadline.month
            val day: Int = edtDeadline.dayOfMonth

            val deadline = "$year-$month-$day"

            if (taskName == "") {
                toast("input task!")
            } else {
                if (taskCurrentList.contains(taskName)){
                    toast("task existing!")
                } else {
                    val setTaskName = database.getReference("$author/task/$taskName/task")
                    val setTaskDesc = database.getReference("$author/task/$taskName/description")
                    val setStatus = database.getReference("$author/task/$taskName/status")
                    val setDeadline = database.getReference("$author/task/$taskName/deadline")

                    setTaskName.setValue(taskName)
                    setTaskDesc.setValue(taskDesc)
                    setStatus.setValue("todo")
                    setDeadline.setValue(deadline)

                    toast("added")
                    dialog.dismiss()
                    getUserData()
                }
            }
        }
        calcelBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun done() {
        val author = FirebaseUtils.firebaseAuth.currentUser?.displayName
        if(itemClicked == ""){
            toast("select task to set status")
        }
        else{
            val doneStatus = database.getReference("$author/task/$itemClicked/status")
            doneStatus.setValue("done")
            getUserData()
            toast("done $itemClicked")
        }
    }


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun DialogModify() {
        val author = FirebaseUtils.firebaseAuth.currentUser?.displayName
        if(itemClicked == ""){
            toast("select task to edit")
        }
        else{
            fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

            val dialog = Dialog(this@TaskActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_modify)
            val edtmodifyTask = dialog.findViewById<View>(R.id.editAddTask) as EditText
            val edtmodifyDesc = dialog.findViewById<View>(R.id.editAddDescTask) as EditText
            val edtDeadline = dialog.findViewById<DatePicker>(R.id.editDeadline) as DatePicker
            val edtStatus = dialog.findViewById<Switch>(R.id.done) as Switch
            val modifyBtn = dialog.findViewById<View>(R.id.modifyBtn) as Button
            val calcelBtn = dialog.findViewById<View>(R.id.cancelBtn) as Button

            edtDeadline.minDate = System.currentTimeMillis()

            val modifyTaskName = database.getReference("$author/task/$itemClicked/task")
            val modifyTaskDesc = database.getReference("$author/task/$itemClicked/description")
            val modifyStatus = database.getReference("$author/task/$itemClicked/status")
            val modifyDeadline = database.getReference("$author/task/$itemClicked/deadline")

            edtmodifyTask.text = itemClicked.toEditable()
            modifyTaskDesc.get().addOnSuccessListener {
                edtmodifyDesc.text = it.value.toString().toEditable()
            }

            modifyStatus.get().addOnSuccessListener {
                if(it.value.toString() == "done"){
                    edtStatus.isChecked = true
                }
            }


            modifyBtn.setOnClickListener {
                val taskName: String = edtmodifyTask.text.toString()
                val taskDesc: String = edtmodifyDesc.text.toString()
                val year: Int = edtDeadline.year
                val month: Int = edtDeadline.month
                val day: Int = edtDeadline.dayOfMonth

                val deadline = "$year-$month-$day"

                var status = "todo"
                if (edtStatus.isChecked) {
                    status = "done"
                }

                if (taskName == "") {
                    toast("input task!")
                } else {
                    if (taskCurrentList.contains(taskName)){
                        toast("task existing!")
                    } else {
                        modifyTaskName.setValue(taskName)
                        modifyTaskDesc.setValue(taskDesc)
                        modifyStatus.setValue(status)
                        modifyDeadline.setValue(deadline)

                        toast("modify successful")
                        dialog.dismiss()
                        getUserData()
                    }
                }
            }

            calcelBtn.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }

    private fun getUserData() {
        val taskRecyclerview: RecyclerView = findViewById(R.id.listTask)
        taskRecyclerview.layoutManager = LinearLayoutManager(this)
        taskRecyclerview.setHasFixedSize(true)

        taskCurrentList.clear()
        val taskArrayList = arrayListOf<Task>()

        val author = FirebaseUtils.firebaseAuth.currentUser?.displayName
        userData = FirebaseDatabase.getInstance().getReference("$author/task/")

        userData.child("")
            .orderByChild("deadline")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (taskSnapshot in snapshot.children){
                        val task = taskSnapshot.getValue(Task::class.java)
                        taskArrayList.add(task!!)
                        taskCurrentList.add(task.task.toString())
                    }

                    val adapter = MyAdapter(taskArrayList)
                    taskRecyclerview.adapter = adapter
                    adapter.setOnItemClickListener(object : MyAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int) {
                            itemClicked = taskArrayList[position].task.toString()
                            taskRecyclerview.findViewHolderForAdapterPosition(last_position)?.itemView?.setBackgroundColor(Color.parseColor("#ffffff"))
                            taskRecyclerview.findViewHolderForAdapterPosition(position)?.itemView?.setBackgroundColor(Color.parseColor("#00ffaa"))
                            last_position = position
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}