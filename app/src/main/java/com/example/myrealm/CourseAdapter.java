package com.example.myrealm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    private List<DataModel> courses;

    public CourseAdapter(List<DataModel> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_course,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindData(this.courses.get(position));
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public View view;
        public TextView tvName;
        public TextView tvDuration;
        public TextView tvDescription;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bindData(DataModel data){

            tvName.setText(data.getCourseName());
            tvDuration.setText(data.getCourseDuration());
            tvDescription.setText(data.getCourseDescription());

        }

    }

    @Override
    public int getItemCount() {
        return this.courses.size();
    }
}
