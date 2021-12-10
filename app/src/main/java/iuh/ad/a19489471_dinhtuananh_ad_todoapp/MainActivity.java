package iuh.ad.a19489471_dinhtuananh_ad_todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DataBase dataBase;
    ListView lvTask;
    ArrayList<Task> arrayTask;
    TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTask = (ListView) findViewById(R.id.listTask);
        arrayTask = new ArrayList<>();

        adapter = new TaskAdapter(MainActivity.this, R.layout.row_task, arrayTask);
        lvTask.setAdapter(adapter);

        dataBase = new DataBase(MainActivity.this, "note.sqlite", null, 1);

        dataBase.QueryData("CREATE TABLE IF NOT EXISTS Task(Id INTEGER PRIMARY KEY AUTOINCREMENT, NameTask VARCHAR(200))");

        GetDataCongViec();
    }

    public void DialogDeleteTask(String tenCV, int id){
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this);
        dialogDelete.setMessage("do you want to delete this task?");

        dialogDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataBase.QueryData("DELETE FROM Task WHERE Id = '"+ id +"'");
                Toast.makeText(MainActivity.this, "deleted" + tenCV, Toast.LENGTH_SHORT).show();
                GetDataCongViec();
            }
        });

        dialogDelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogDelete.show();
    }

    public void DialogEditTask(String ten, int id){
        Dialog dialog= new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_task);

        EditText edtNameEdit = (EditText) dialog.findViewById(R.id.editTaskName);
        Button btnXacNhanEdit = (Button) dialog.findViewById(R.id.saveEditBtn);
        Button btnHuyEdit = (Button) dialog.findViewById(R.id.cancelEditBtn);

        edtNameEdit.setText(ten);

        btnXacNhanEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenMoi = edtNameEdit.getText().toString().trim();
                dataBase.QueryData("UPDATE Task SET NameTask = '"+ tenMoi +"' WHERE Id = '"+ id +"'");
                Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                GetDataCongViec();
            }
        });

        btnHuyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void GetDataCongViec(){

        Cursor dataTask = dataBase.GetData("SELECT * FROM Task");
        arrayTask.clear();
        while (dataTask.moveToNext()){
            String name = dataTask.getString(1);
            int id = dataTask.getInt(0);
            arrayTask.add(new Task(id, name));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menuAdd){
            DialogThem();
        }

        return super.onOptionsItemSelected(item);
    }

    private void DialogThem(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_task);

        EditText edtName = (EditText) dialog.findViewById(R.id.editTextTaskName);
        Button btnAdd = (Button) dialog.findViewById(R.id.saveBtn);
        Button btnCancel = (Button) dialog.findViewById(R.id.cancelBtn);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTask = edtName.getText().toString();
                if (nameTask.equals("")){
                    Toast.makeText(MainActivity.this, "input task!", Toast.LENGTH_SHORT).show();
                }else {
                    dataBase.QueryData("INSERT INTO Task VALUES (null, '"+ nameTask +"')");
                    Toast.makeText(MainActivity.this, "added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataCongViec();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}