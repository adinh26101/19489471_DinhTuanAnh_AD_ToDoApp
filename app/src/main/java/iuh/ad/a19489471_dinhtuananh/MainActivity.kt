package iuh.ad.a19489471_dinhtuananh

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
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


class MainActivity : AppCompatActivity() {
    private lateinit var userData : DatabaseReference

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
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun DialogAdd() {
        val dialog = Dialog(this@MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add)
        val edtAddTask = dialog.findViewById<View>(R.id.editAddTask) as EditText
        val edtAddDesc = dialog.findViewById<View>(R.id.editAddDescTask) as EditText
        val addBtn = dialog.findViewById<View>(R.id.addBtn) as Button
        val calcelBtn = dialog.findViewById<View>(R.id.cancelBtn) as Button

        addBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val taskName: String = edtAddTask.text.toString()
                val taskDesc: String = edtAddDesc.text.toString()

                val c = Calendar.getInstance()
                val year: Int = c.get(Calendar.YEAR)
                val month: Int = c.get(Calendar.MONTH)
                val day: Int = c.get(Calendar.DAY_OF_MONTH)
                val addedDay: String = "${year}/${month}/${day}"

                if (taskName == "") {
                    toast("input task!" )
                } else {
                    val author = FirebaseUtils.firebaseAuth.currentUser?.displayName
                    val database = FirebaseDatabase.getInstance()

                    val setTaskName = database.getReference("$author/$taskName/task")
                    val setTaskDesc = database.getReference("$author/$taskName/description")
                    val setAddedDay = database.getReference("$author/$taskName/added_time")
//                    val setDeadline = database.getReference("$author/$taskName/deadline")
                    setTaskName.setValue("$taskName")
                    setTaskDesc.setValue("$taskDesc")
                    setAddedDay.setValue("$addedDay")
                    // setDeadline.setValue("${deadline}")

                    toast("added")
                    dialog.dismiss()
                    getUserData()
                }
            }
        })
        calcelBtn.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        dialog.show()
    }

    private fun getUserData() {
        val taskRecyclerview: RecyclerView = findViewById(R.id.listTask)
        taskRecyclerview.layoutManager = LinearLayoutManager(this)
        taskRecyclerview.setHasFixedSize(true)

        val taskArrayList = arrayListOf<Task>()

        val author = FirebaseUtils.firebaseAuth.currentUser?.displayName
        userData = FirebaseDatabase.getInstance().getReference("$author")

        userData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (taskSnapshot in snapshot.children){
                        val task = taskSnapshot.getValue(Task::class.java)
                        taskArrayList.add(task!!)
                    }
                    taskRecyclerview.adapter = MyAdapter(taskArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}