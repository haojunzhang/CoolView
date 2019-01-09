package ryan.net.timelineviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TodoAdapter adapter = new TodoAdapter(this);
        adapter.setListOfTodo(getTodoData());
        recyclerView.setAdapter(adapter);


    }

    private List<Todo> getTodoData() {
        List<Todo> listOfTodo = new ArrayList<>();

        String str = "content ";
        for (int i = 12; i <= 21; i++) {
            listOfTodo.add(new Todo(textToDate(String.format(Locale.getDefault(), "2019/01/01 %d:00:00", i)), "Title", str));
            str += "content ";
        }


        return listOfTodo;
    }

    private Date textToDate(String text){
        try {
            return new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.getDefault()).parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
