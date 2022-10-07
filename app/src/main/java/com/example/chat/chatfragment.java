package com.example.chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class chatfragment extends Fragment {

    public FirebaseFirestore firebaseFirestore;
    public LinearLayoutManager linearLayoutManager;
    public FirebaseAuth firebaseAuth;
    public ImageView imageview;
    public FirestoreRecyclerAdapter<firebasemodel, NotView> chatAdpater;
    public RecyclerView mrecycleview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.chatfragment, container, false);
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseFirestore = FirebaseFirestore.getInstance();
            mrecycleview = v.findViewById(R.id.recycleview);
            //Query query=firebaseFirestore.collection("Users");
            Query query = firebaseFirestore.collection("Users").whereNotEqualTo("uid", firebaseAuth.getUid());
            FirestoreRecyclerOptions<firebasemodel> allusername = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query, firebasemodel.class).build();
            chatAdpater = new FirestoreRecyclerAdapter<firebasemodel, NotView>(allusername) {
                @Override
                protected void onBindViewHolder(@NonNull NotView holder, int position, @NonNull firebasemodel model) {
                    holder.paticularusername.setText(model.getName());
                    String uri = model.getImage();
                    Picasso.get().load(uri).into(imageview);
                    if (model.getStatus().equals("Online")) {
                        holder.statusofuser.setText(model.getStatus());
                        holder.statusofuser.setTextColor(Color.BLACK);
                    } else {
                        holder.statusofuser.setText(model.getStatus());
                        assert container != null;
                        holder.statusofuser.setTextColor(ContextCompat.getColor(container.getContext(), R.color.font));
                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            assert container != null;
                            view.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.silver));
//                        Toast.makeText(getActivity(),"Item is clicked",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), specifichat.class);
                            intent.putExtra("name", model.getName());
                            intent.putExtra("reciveseruid", model.getUid());
                            intent.putExtra("imageuri", model.getImage());
                            intent.putExtra("token", model.getToken());
//                            intent.putExtra("nname",)
//                        Toast.makeText(getContext(), model.getToken(), Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    view.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.font));
                                    startActivity(intent);

                                }
                            }, 100);


                        }
                    });
                }
                @NonNull
                @Override
                public NotView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout, parent, false);
                    return new NotView(view);
                }
            };
            mrecycleview.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            mrecycleview.setLayoutManager(linearLayoutManager);
            mrecycleview.setAdapter(chatAdpater);
            return v;
        }
        public class NotView extends RecyclerView.ViewHolder {
            public TextView paticularusername;
            public TextView statusofuser;

            public NotView(@NonNull View itemView) {
                super(itemView);
                paticularusername = itemView.findViewById(R.id.nameofuser);
                statusofuser = itemView.findViewById(R.id.statusofuser);
                imageview = itemView.findViewById(R.id.imageviewofuser);
            }
        }
        @Override
        public void onStart () {
            super.onStart();
            chatAdpater.startListening();
        }

        @Override
        public void onStop () {
            super.onStop();
            if (chatAdpater != null) {
                chatAdpater.startListening();
            }
        }
    }
