package iuh.ad.a19489471_dinhtuananh

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
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
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker





class MainActivity : AppCompatActivity() {
    private lateinit var userData : DatabaseReference
    private val author = FirebaseUtils.firebaseAuth.currentUser?.displayName
    val database = FirebaseDatabase.getInstance()
    private var itemClicked: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            itemClicked == ""
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun delete() {
        val author = FirebaseUtils.firebaseAuth.currentUser?.displayName
        val database = FirebaseDatabase.getInstance()

        val removeTask = database.getReference("$author/$itemClicked")

        removeTask.removeValue()
        toast("deleted $itemClicked")
        getUserData()
    }

    private fun DialogAdd() {
        val dialog = Dialog(this@MainActivity)
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


                val setTaskName = database.getReference("$author/$taskName/task")
                val setTaskDesc = database.getReference("$author/$taskName/description")
                val setStatus = database.getReference("$author/$taskName/status")
                val setDeadline = database.getReference("$author/$taskName/deadline")

                setTaskName.setValue(taskName)
                setTaskDesc.setValue(taskDesc)
                setStatus.setValue("todo")
                setDeadline.setValue(deadline)

                toast("added")
                dialog.dismiss()
                getUserData()
            }
        }
        calcelBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun done() {
        if(itemClicked == ""){
            toast("select task to set status")
        }
        else{
            val doneStatus = database.getReference("$author/$itemClicked/status")
            doneStatus.setValue("done")
            getUserData()
            toast("done $itemClicked")
        }
    }


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun DialogModify() {
        if(itemClicked == ""){
            toast("select task to edit")
        }
        else{
            fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

            val dialog = Dialog(this@MainActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_modify)
            val edtmodifyTask = dialog.findViewById<View>(R.id.editAddTask) as EditText
            val edtmodifyDesc = dialog.findViewById<View>(R.id.editAddDescTask) as EditText
            val edtDeadline = dialog.findViewById<DatePicker>(R.id.editDeadline) as DatePicker
            val edtStatus = dialog.findViewById<Switch>(R.id.done) as Switch
            val modifyBtn = dialog.findViewById<View>(R.id.modifyBtn) as Button
            val calcelBtn = dialog.findViewById<View>(R.id.cancelBtn) as Button

            edtDeadline.minDate = System.currentTimeMillis()

            val modifyTaskName = database.getReference("$author/$itemClicked/task")
            val modifyTaskDesc = database.getReference("$author/$itemClicked/description")
            val modifyStatus = database.getReference("$author/$itemClicked/status")
            val modifyDeadline = database.getReference("$author/$itemClicked/deadline")

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

                    modifyTaskName.setValue(taskName)
                    modifyTaskDesc.setValue(taskDesc)
                    modifyStatus.setValue(status)
                    modifyDeadline.setValue(deadline)

                    toast("modify successful")
                    dialog.dismiss()
                    getUserData()
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
        val taskSelected = findViewById<TextView>(R.id.taskSelected)

        val taskArrayList = arrayListOf<Task>()

        val author = FirebaseUtils.firebaseAuth.currentUser?.displayName
        userData = FirebaseDatabase.getInstance().getReference("$author")

        userData.child("")
            .orderByChild("deadline")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (taskSnapshot in snapshot.children){
                        val task = taskSnapshot.getValue(Task::class.java)
                        taskArrayList.add(task!!)
                    }

                    val adapter = MyAdapter(taskArrayList)
                    taskRecyclerview.adapter = adapter
                    adapter.setOnItemClickListener(object : MyAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int) {
                            itemClicked = taskArrayList[position].task.toString()
                            taskSelected.text = itemClicked
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