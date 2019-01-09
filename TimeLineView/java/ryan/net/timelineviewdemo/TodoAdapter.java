package ryan.net.timelineviewdemo;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private Context context;
    private List<Todo> listOfTodo;

    TodoAdapter(Context context) {
        this.context = context;
        this.listOfTodo = new ArrayList<>();
    }

    void setListOfTodo(List<Todo> listOfTodo) {
        this.listOfTodo = listOfTodo;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setTodo(listOfTodo.get(position));
        holder.setType(position == 0 ? ViewHolder.TYPE_TOP : (position == listOfTodo.size() - 1 ? ViewHolder.TYPE_BOTTOM : ViewHolder.TYPE_MID));

    }

    @Override
    public int getItemCount() {
        return listOfTodo.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        static final int TYPE_TOP = 0;
        static final int TYPE_MID = 1;
        static final int TYPE_BOTTOM = 2;

        TextView tvDate;
        TextView tvTitle;
        TextView tvContent;

        View viewTopLine;
        View imageViewMidCircle;
        View viewBottomLine;

        ViewHolder(View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            viewTopLine = itemView.findViewById(R.id.viewTopLine);
            imageViewMidCircle = itemView.findViewById(R.id.imageViewMidCircle);
            viewBottomLine = itemView.findViewById(R.id.viewBottomLine);
        }

        void setTodo(Todo todo) {
            tvDate.setText(dateToText(todo.getDate()));
            tvTitle.setText(todo.getTitle());
            tvContent.setText(todo.getContent());
        }

        String dateToText(Date date) {
            return new SimpleDateFormat("hh:mm", Locale.getDefault()).format(date);
        }

        void setType(int type) {
            switch (type) {
                case TYPE_TOP:
                    viewTopLine.setBackgroundColor(Color.TRANSPARENT);
                    viewBottomLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    break;
                case TYPE_MID:
                    viewTopLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    viewBottomLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    break;
                case TYPE_BOTTOM:
                    viewTopLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    viewBottomLine.setBackgroundColor(Color.TRANSPARENT);
                    break;
            }
        }
    }
}
