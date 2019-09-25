package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyHolders>{

    Context context;
    List<ModelUser> userList;

    //constructor


    public AdapterUser(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout(row_user.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, viewGroup,false);

        return new MyHolders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolders myHolders, int i) {
        //get data
        String userImage = userList.get(i).getImage();
        String userName = userList.get(i).getName();
        final String userEmail = userList.get(i).getEmail();

        //set data
        myHolders.mnameTv.setText(userName);
        myHolders.memailTv.setText(userEmail);
        try {
            Picasso.get().load(userImage)
                    .placeholder(R.drawable.ic_defaut_img)
                    .into(myHolders.mimgTv);

        }catch (Exception e){

        }
        //handle item click
        myHolders.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+userEmail, Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolders extends RecyclerView.ViewHolder{

        ImageView mimgTv;
        TextView mnameTv,memailTv;


        public MyHolders(@NonNull View itemView) {
            super(itemView);

            mimgTv = itemView.findViewById(R.id.imgTv);
            mnameTv = itemView.findViewById(R.id.nameTv);
            memailTv = itemView.findViewById(R.id.emailTv);
        }
    }
}
