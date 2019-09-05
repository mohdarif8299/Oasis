package com.restaurent.swaad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class Cart_ItemAdapter extends RecyclerView.Adapter<Cart_ItemAdapter.ViewHolder> {
    private Context ctx;
    private List<Cart_Item> list;
    public Cart_ItemAdapter(List<Cart_Item> listFeed, Context context) {
        this.ctx = context;
        this.list = listFeed;
    }
    @NonNull
    @Override
    public Cart_ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.cart_item, parent, false);
       Cart_ItemAdapter.ViewHolder viewHolder = new Cart_ItemAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Cart_ItemAdapter.ViewHolder holder, int position) {
        Cart_Item items = list.get(holder.getAdapterPosition());
        Glide.with(ctx).load(items.getItem_image()).fitCenter().centerCrop().into(holder.item_image);
        holder.item_name.setText(items.getItem_name());
        holder.item_category.setText(items.getItem_category());
        holder.item_price.setText("Rs "+items.getItem_price());
        holder.increment.setOnClickListener(v->
                {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Cart").child(FirebaseAuth.getInstance().getUid()).child(items.getItem_name());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int quantity = dataSnapshot.child("item_quantity").getValue(Integer.class);
                            quantity++;
                            if (quantity > 6) {
                                return;
                            } else {
                                items.setItem_quantity(quantity);
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference("Snacks").child("item" + position);
                                databaseReference.child("quantity").setValue(items.getItem_quantity());
                                FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference1 = firebaseDatabase1.getReference("Cart").child(FirebaseAuth.getInstance().getUid());
                                databaseReference1.child(items.getItem_name()).child("item_quantity").setValue(quantity);
                                holder.counter.setText("" +quantity);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
        );
        holder.decrement.setOnClickListener(v-> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Cart").child(FirebaseAuth.getInstance().getUid()).child(items.getItem_name());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int quantity= dataSnapshot.child("item_quantity").getValue(Integer.class);
                    holder.counter.setText(""+quantity);
                    if(quantity<=1) {
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Snacks").child("item"+position);
                        databaseReference.child("cart_status").setValue("not_added");
                        holder.control_cart.setVisibility(View.GONE);
                    }
                    else {
                        quantity--;
                        //items.setItem_quantity(quantity);
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Snacks").child("item"+position);
                        databaseReference.child("quantity").setValue(quantity);
                        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference1 = firebaseDatabase1.getReference("Cart").child(FirebaseAuth.getInstance().getUid());
                        databaseReference1.child(items.getItem_name()).child("item_quantity").setValue(quantity);
                        holder.counter.setText(""+quantity);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView item_image;
        public TextView item_name,item_category,item_price,counter;
        public LinearLayout linearLayout;
        public RelativeLayout control_cart;
        public ImageView increment,decrement;
        public ViewHolder(View itemView) {
            super(itemView);
            this.item_image = itemView.findViewById(R.id.item_image);
            this.item_name = itemView.findViewById(R.id.item_name);
            this.item_category = itemView.findViewById(R.id.item_category);
            this.item_price = itemView.findViewById(R.id.item_price);
            this.linearLayout = itemView.findViewById(R.id.linearLayout);
            control_cart = itemView.findViewById(R.id.control_cart);
            counter = itemView.findViewById(R.id.counter);
            increment = itemView.findViewById(R.id.increment);
            decrement = itemView.findViewById(R.id.decrement);
        }
    }
}
