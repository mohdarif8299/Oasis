package com.restaurent.swaad;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pd.chocobar.ChocoBar;
import java.util.List;
import java.util.Objects;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private Context ctx;
    private List<Items> list;
    public ItemsAdapter(List<Items> listFeed, Context context) {
        this.ctx = context;
        this.list = listFeed;
    }
    @NonNull
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.items, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ViewHolder holder, int position) {
        Items items = list.get(holder.getAdapterPosition());
        Glide.with(ctx).load(items.getItem_image()).fitCenter().centerCrop().into(holder.item_image);
        holder.item_name.setText(items.getItem_name());
        holder.item_category.setText(items.getItem_category());
        holder.item_price.setText("Rs "+items.getItem_price());
        String cart_status = items.getCart_status();
        if (Objects.equals(cart_status,"added")){
            holder.add_to_cart.setVisibility(View.GONE);
            holder.control_cart.setVisibility(View.VISIBLE);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Snacks");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    holder.counter.setText(""+dataSnapshot.child("item"+position).child("quantity").getValue(Integer.class));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            holder.add_to_cart.setVisibility(View.VISIBLE);
            holder.control_cart.setVisibility(View.GONE);
        }
        holder.add_to_cart.setOnClickListener(v-> {
            holder.add_to_cart.setVisibility(View.GONE);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Snacks").child("item"+position);
            databaseReference.child("cart_status").setValue("added");
            FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference1 = firebaseDatabase1.getReference("Cart");
            Cart_Item cart_item = new Cart_Item(items.getItem_name(),items.getItem_category(),items.getItem_price(),items.getItem_image(),items.getItem_quantity());
            databaseReference1.child(FirebaseAuth.getInstance().getUid()).child(items.getItem_name()).setValue(cart_item);
            holder.control_cart.setVisibility(View.VISIBLE);
            ChocoBar.builder().setView(v)
                    .setActionText("View cart")
                    .setBackgroundColor(Color.parseColor("#2ABE4E"))
                    .setActionTextColor(Color.WHITE)
                    .setActionClickListener(v1-> Toast.makeText(ctx,"You clicked",Toast.LENGTH_LONG).show())
                    .setText("Item added to cart")
                    .setTextSize(12)
                    .setActionTextSize(12)
                    .setDuration(ChocoBar.LENGTH_LONG)
                    .build()
                    .show();
        });
        holder.increment.setOnClickListener(v->
        {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Snacks").child("item"+position);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int quantity = dataSnapshot.child("quantity").getValue(Integer.class);
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
                        databaseReference1.child(items.getItem_name()).child("item_quantity").setValue(items.getItem_quantity());
                        holder.counter.setText("" + items.getItem_quantity());
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
          DatabaseReference databaseReference = firebaseDatabase.getReference("Snacks").child("item"+position);
          databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  int quantity= dataSnapshot.child("quantity").getValue(Integer.class);
                  holder.counter.setText(""+quantity);
                  if(quantity==1) {
                      FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                      DatabaseReference databaseReference = firebaseDatabase.getReference("Snacks").child("item"+position);
                      databaseReference.child("cart_status").setValue("not_added");
                      holder.add_to_cart.setVisibility(View.VISIBLE);
                      holder.control_cart.setVisibility(View.GONE);
                  }
                  else {
                      quantity--;
                   //   items.setItem_quantity(quantity);
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
    public int getItemCount() { return list.size(); }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView item_image;
        public TextView item_name,item_category,item_price,counter;
        public LinearLayout linearLayout;
        public RelativeLayout add_to_cart,control_cart;
        public ImageView increment,decrement;
        public ViewHolder(View itemView) {
            super(itemView);
           this.item_image = itemView.findViewById(R.id.item_image);
            this.item_name = itemView.findViewById(R.id.item_name);
            this.item_category = itemView.findViewById(R.id.item_category);
            this.item_price = itemView.findViewById(R.id.item_price);
            this.linearLayout = itemView.findViewById(R.id.linearLayout);
            add_to_cart = itemView.findViewById(R.id.add_to_cart);
            control_cart = itemView.findViewById(R.id.control_cart);
            counter = itemView.findViewById(R.id.counter);
            increment = itemView.findViewById(R.id.increment);
            decrement = itemView.findViewById(R.id.decrement);
        }
    }
}