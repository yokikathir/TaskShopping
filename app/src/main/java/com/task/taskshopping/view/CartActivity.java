package com.task.taskshopping.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.task.taskshopping.R;
import com.task.taskshopping.cart.Cart;
import com.task.taskshopping.cart.CartDB;
import com.task.taskshopping.item.Productdb;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class CartActivity extends AppCompatActivity {
    private ArrayList<Productdb> items;
    private RecyclerView mRecyclerView;
    private String mPhoneNumber;
    private CartDB mCartDb;
    private TextView mPriceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Cart");
        items = Objects.requireNonNull(this.getIntent().getExtras()).getParcelableArrayList("cart");
        mRecyclerView = findViewById(R.id.cart_list);
        mPriceText = findViewById(R.id.price);
        mCartDb = Room.databaseBuilder(getApplicationContext(), CartDB.class, "sample-db").build();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemsAdapter adapter = new ItemsAdapter(items,getApplicationContext());
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        int price = 0;
         for (Productdb i : items) {
             String pricef= (i.getPrice().replace("₹","").trim());
             String ppp=pricef.replace(",","");

             double doubleValue = Double.parseDouble(ppp.trim());
             int intValue = (int)doubleValue;
            price += intValue * i.getCount();
             Log.e("priceof",":"+price);
        }
        mPriceText.setText("₹ " + String.valueOf(price));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.done) {
            if (items.size() == 0) {
                Toast.makeText(this, "Add some items to checkout", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to checkout?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            new InsertCartItem().execute();
                        })
                        .setNegativeButton(android.R.string.no, (dialog, which) -> {
                            dialog.cancel();
                        })
                        .show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertCartItem extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (Productdb i: items) {
                Cart c = new Cart();
                c.setQuantity(i.getQuantity());
                c.setName(i.getName());
                c.setCount(i.getCount());
                c.setSpecial(i.getSpecial());
                c.setPrice(i.getPrice());
                c.setImages(i.getImages());
                mCartDb.getCartDao().insert(c);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(CartActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
           // CartActivity.this.finish();
        }
    }

    private class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
        private List<Productdb> item;
        Context context;

        ItemsAdapter(List<Productdb> itemList, Context context) {
            item = new ArrayList<>();
            this.item = itemList;
            this.context=context;
        }

        @NonNull
        @Override
        public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            return new ItemsAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemsAdapter.ViewHolder holder, final int position) {
            final Productdb itm = item.get(position);
            holder.name.setText(itm.getName());
            holder.price.setText(String.valueOf(itm.getPrice()));
            holder.quantity.setText(String.valueOf(itm.getSpecial()));
            holder.count.setText(String.valueOf(itm.getCount()));
            Glide
                    .with(context)
                    .load(itm.getImages())
                    .centerCrop()
                    .placeholder(R.drawable.ic_local_drink_black_24dp)
                    .into(holder.listimage);

            holder.add.setOnClickListener(v -> {
                itm.setCount(itm.getCount() + 1);
                holder.count.setText(String.valueOf(itm.getCount()));
            });

            holder.remove.setOnClickListener(v -> {
                if (!(itm.getCount() <= 0))
                    itm.setCount(itm.getCount() - 1);
                if (itm.getCount() == 0) {
                    item.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                } else holder.count.setText(String.valueOf(itm.getCount()));
            });

        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name, price, quantity, count;
            private ImageView add, remove,listimage;


            ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.from_name);
                price = itemView.findViewById(R.id.price_text);
                quantity = itemView.findViewById(R.id.weight_text);
                count = itemView.findViewById(R.id.cart_product_quantity_tv);
                add = itemView.findViewById(R.id.cart_plus_img);
                remove = itemView.findViewById(R.id.cart_minus_img);
                listimage = itemView.findViewById(R.id.list_image);
            }
        }
    }

}
