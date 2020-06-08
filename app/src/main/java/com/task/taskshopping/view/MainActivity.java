package com.task.taskshopping.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.task.taskshopping.R;
import com.task.taskshopping.api.APIclient;
import com.task.taskshopping.api.Apiinterface;
import com.task.taskshopping.item.ProductRoomDatabase;
import com.task.taskshopping.item.Productdb;
import com.task.taskshopping.model.Product;
import com.task.taskshopping.model.Productlist;
import com.task.taskshopping.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static ProductRoomDatabase mItemDB;
    private List<Productdb> mList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ArrayList<Productdb> selected = new ArrayList<>();
    private List<Productdb> selectedd = new ArrayList<>();

    //ProductAdapter productAdapter;
    ArrayList<String>spciallist=new ArrayList<>();
    List<String> pricelist = new ArrayList<>();

    String mName= null;
    String special = null;
    int quantity = 0;
    String images= null;
    String price= null;
    ProgressDialog loading = null;
    Productdb note;
    private ProductViewModel productViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loading = new ProgressDialog(MainActivity.this);
        loading.setCancelable(true);
        loading.setMessage("Loading.......");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.dismiss();
        mRecyclerView = findViewById(R.id.products_list);
      //  Toolbar toolbar = findViewById(R.id.toolbar);

        productViewModel= ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.deleteAll();
        productViewModel.getAllProduct().observe(this, new Observer<List<Productdb>>() {
            @Override
            public void onChanged(List<Productdb> notes) {
                loading.show();
                ItemsAdapter adapter = new ItemsAdapter(notes,getApplicationContext());
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        productListt();
        mItemDB = Room.databaseBuilder(getApplicationContext(), ProductRoomDatabase.class, "sample").build();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cart) {
            ArrayList<Productdb> item1 = (ArrayList<Productdb>) selected;
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            Bundle b = new Bundle();
            b.putParcelableArrayList("cart",  item1);
            intent.putExtras(b);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
        private List<Productdb> item;
        private Context mcontext;

        ItemsAdapter(List<Productdb> itemList, Context context) {
            item = new ArrayList<>();
            selected = new ArrayList<>();
            this.item = itemList;
            this.mcontext=context;
        }

        @NonNull
        @Override
        public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemsAdapter.ViewHolder holder, int position) {
            final Productdb itm = item.get(position);
            Log.e("itemlistata",":"+itm.getImages());
            holder.name.setText(itm.getName());
            holder.price.setText(itm.getPrice());
            holder.quantity.setText(String.valueOf(itm.getSpecial()));
            holder.count.setText(String.valueOf(itm.getCount()));

            Glide
                    .with(mcontext)
                    .load(itm.getImages())
                    .centerCrop()
                    .placeholder(R.drawable.ic_local_drink_black_24dp)
                    .into(holder.listimage);

            holder.add.setOnClickListener((v) -> {
                itm.setCount(itm.getCount() + 1);
                holder.count.setText(String.valueOf(itm.getCount()));
                if (!selected.contains(itm))
                    selected.add(itm);
            });

            holder.remove.setOnClickListener(v -> {
                if (!(itm.getCount() <= 0))
                    itm.setCount(itm.getCount() - 1);
                holder.count.setText(String.valueOf(itm.getCount()));
                if (itm.getCount() == 0)
                    selected.remove(itm);
            });
            loading.dismiss();
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
    public void productListt() {
        loading.show();
        Apiinterface apiinterface = APIclient.getapi();
        Call<Productlist> call = apiinterface.getprouctlist();
        call.enqueue(new Callback<Productlist>() {
            @Override
            public void onResponse(Call<Productlist> call, Response<Productlist> response) {

                Log.e("responsedata", ":" + response.body());
                Productlist productlist = response.body();
                Product product=new Product();

                List<Product> products = productlist.getProducts();

                for (int i=0;i<products.size();i++){
                    pricelist.add(products.get(i).getPrice());
                    spciallist.add(products.get(i).getSpecial());
                    mName= products.get(i).getName();
                    special= products.get(i).getSpecial();
                    quantity= products.get(i).getQuantity();
                    images= products.get(i).getImage();
                    price= products.get(i).getPrice();
                    note=new Productdb(mName,special,quantity,images,price,0);
                    productViewModel.insert(note);
                }

            }

            @Override
            public void onFailure(Call<Productlist> call, Throwable t) {
                loading.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productViewModel.deleteAll();

    }
}
