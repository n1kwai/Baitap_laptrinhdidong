package com.example.week3_2;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.week3_2.adapters.NotesAdapter;
import com.example.week3_2.data.DatabaseHandler;
import com.example.week3_2.model.NotesModel;

import android.app.Dialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    ListView listView;
    ArrayList<NotesModel> arrayList;

    EditText editText;

    Button buttonAdd;

    Button buttonHuy;

    Button buttonEdit;

    NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        InitDatabaseSQLite();

        listView = (ListView) findViewById(R.id.listView1);
        arrayList = new ArrayList<>();
        adapter = new NotesAdapter(this, R.layout.row_notes, arrayList);
        listView.setAdapter(adapter);

        DatabaseSQLite();



    }

    private void CreateDatabase() {
        databaseHandler.QueryDatabase("INSERT INTO Notes VALUES (null, 'This is the first note')");
        databaseHandler.QueryDatabase("INSERT INTO Notes VALUES (null, 'This is the second note')");
        databaseHandler.QueryDatabase("INSERT INTO Notes VALUES (null, 'This is the third note')");
    }

    private void InitDatabaseSQLite() {
        databaseHandler = new DatabaseHandler(this, "NotesDB", null, 1);
        databaseHandler.QueryDatabase("CREATE TABLE IF NOT EXISTS Notes (Id INTEGER PRIMARY KEY AUTOINCREMENT, NameNotes VARCHAR(200))");
    }

    private void DatabaseSQLite(){
        Cursor cursor = databaseHandler.GetData("SELECT * FROM Notes");
        try {
            while(cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                arrayList.add(new NotesModel(id, name));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        adapter.notifyDataSetChanged();
    }
    private void DialogThem() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_notes);

        // ÁNH XẠ VIEW TRONG DIALOG
        EditText editText = dialog.findViewById(R.id.editTextName);
        Button buttonAdd = dialog.findViewById(R.id.buttonEdit);
        Button buttonHuy = dialog.findViewById(R.id.buttonHuy);

        // NÚT THÊM
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editText.getText().toString().trim();

                if (name.equals("")) {
                    Toast.makeText(MainActivity.this,
                            "Vui lòng nhập tên Notes",
                            Toast.LENGTH_SHORT).show();
                } else {

                    databaseHandler.QueryDatabase(
                            "INSERT INTO Notes VALUES(null, '" + name + "')"
                    );

                    Toast.makeText(MainActivity.this,
                            "Đã thêm Notes",
                            Toast.LENGTH_SHORT).show();

                    dialog.dismiss();      // đóng dialog
                    DatabaseSQLite();      // gọi hàm load lại dữ liệu
                }
            }
        });

        // NÚT HỦY
        buttonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuAddNotes) {
            DialogThem();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


}