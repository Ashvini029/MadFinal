package com.example.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class adapterPost extends RecyclerView.Adapter<adapterPost.MyHolder> {

    Context context;
    List<ModelPost> postList;
    String myUid;

    private DatabaseReference likesRef;
    private DatabaseReference postRef1;

    boolean mProcessLike = false;

    public adapterPost(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postRef1 = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.row, viewType,false);

        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        final String uid = postList.get(position).getUid();
        //String uEmail = postList.get(position).getuEmail();
        String uName = postList.get(position).getuName();
        String uDp = postList.get(position).getuDp();
        final String pId = postList.get(position).getpId();
        String pDesc = postList.get(position).getpDiesc();
        final String pImage = postList.get(position).getpImage();
        String pTimeStamp = postList.get(position).getpTime();
        String pLikes = postList.get(position).getpLikes();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        holder.uName.setText(uName);
        holder.pTime.setText(pTime);
        holder.Pdesc.setText(pDesc);
        holder.Plike.setText(pLikes +"Likes");

        setLikes(holder, pId);

        try {
            Picasso.get().load(uDp).placeholder(R.drawable.ic_action_image).into(holder.uPicture);
        }
        catch (Exception e)
        {

        }
        if (pImage.equals("noImage")){
            holder.pImage.setVisibility(View.GONE);

        }
        else{
            holder.pImage.setVisibility(View.VISIBLE);


            try {
                Picasso.get().load(pImage).into(holder.pImage);
            }
            catch (Exception e)
            {

            }}

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOptions(holder.moreBtn, uid, myUid, pId, pImage);
            }
        });
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pLikes = Integer.parseInt(postList.get(position).getpLikes());
                mProcessLike = true;

                final String postIde = postList.get(position).getpId();

                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (mProcessLike){
                            if ( dataSnapshot.child(postIde).hasChild(myUid)){
                                //remove liked
                                postRef1.child(postIde).child("pLikes").setValue(""+(pLikes-1));
                                likesRef.child(postIde).child(myUid).removeValue();
                                mProcessLike = false;

                            }
                            else {
                                //like
                                postRef1.child(postIde).child("pLikes").setValue(""+(pLikes+1));
                                likesRef.child(postIde).child(myUid).setValue("Liked");
                                mProcessLike = false;


                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        holder.comBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Comment", Toast.LENGTH_SHORT).show();

            }
        });
        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setLikes(final MyHolder myholder, final String postkey) {
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ( dataSnapshot.child(postkey).hasChild(myUid)){

                    //user has liked this post
                    myholder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_liked, 0,0,0);
                    myholder.likeBtn.setText("Liked");

                }
                else{
                    //user has not liked this post
                    myholder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_like, 0,0,0);
                    myholder.likeBtn.setText("Like");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showMoreOptions(ImageButton moreBtn, String uid, String myUid, final String pId, final String pImage) {

        PopupMenu popupMenu = new PopupMenu(context, moreBtn, Gravity.END);

       if (uid.equals(myUid)){

        popupMenu.getMenu().add(Menu.NONE,0,0, "Delete" );
        popupMenu.getMenu().add(Menu.NONE,1,0, "Edit" );

    }
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
           int id = item.getItemId();
           if (id == 0){
               
               beginDelete(pId, pImage);

           }
           else if (id ==1){
               //edit
               Intent intent = new Intent(context, Post.class);
               intent.putExtra("key", "editPost");
               intent.putExtra("editPostId", pId);
               context.startActivity(intent);


           }

            return false;
        }
    });

       popupMenu.show();

    }

    private void beginDelete(String pId, String pImage) {
        if (pImage.equals("noImage")){

            deleteWithImage(pId,pImage);

        }
        else{

            deleteWithImage(pId,pImage);

        }

    }

    private void deleteWithoutImage(String pId) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting..");
        Query fquery = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
        fquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ds.getRef().removeValue();

                }

                Toast.makeText(context, "Deleted Successfully..", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteWithImage(final String pId, String pImage) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting..");

        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Query fquery = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
                        fquery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds: dataSnapshot.getChildren()){
                                    ds.getRef().removeValue();

                                }

                                Toast.makeText(context, "Deleted Successfully..", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView uPicture, pImage;
        TextView uName, pTime,Pdesc, Plike;
        ImageButton moreBtn;
        Button likeBtn, comBtn, shareBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            uPicture = itemView.findViewById(R.id.profile_image);
            pImage = itemView.findViewById(R.id.pImage);
            uName = itemView.findViewById(R.id.uName);
            pTime = itemView.findViewById(R.id.pTime);
            Pdesc = itemView.findViewById(R.id.Pdes);
            Plike = itemView.findViewById(R.id.like);
            moreBtn = itemView.findViewById(R.id.morebtn);
            likeBtn = itemView.findViewById(R.id.likeBtn);
            comBtn = itemView.findViewById(R.id.comBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);


        }
    }



}
