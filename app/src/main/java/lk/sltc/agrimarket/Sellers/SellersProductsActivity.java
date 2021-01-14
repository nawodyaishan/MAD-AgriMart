package lk.sltc.agrimarket.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import lk.sltc.agrimarket.Admin.AdminHomeActivity;
import lk.sltc.agrimarket.Admin.CheckNewProductsActivity;
import lk.sltc.agrimarket.Model.Products;
import lk.sltc.agrimarket.Prevalent.Prevalent2;
import lk.sltc.agrimarket.R;
import lk.sltc.agrimarket.ViewHolder.ProductViewHolder;
import lk.sltc.agrimarket.ViewHolder.SellerViewHolder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SellersProductsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference sellersProductsRef, sellersRef;
    private ImageView Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_products);

        sellersProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        sellersRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        recyclerView = findViewById(R.id.sellers_products_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Back = (ImageView) findViewById(R.id.admin_my_products_back);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellersProductsActivity.this, SellerHomeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(sellersProductsRef.orderByChild("sellerMobile").equalTo(Prevalent2.currentOnlineUser.getMobile()), Products.class).build();
        FirebaseRecyclerAdapter<Products, SellerViewHolder> adapter = new FirebaseRecyclerAdapter<Products, SellerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerViewHolder holder, int i, @NonNull final Products products) {
                holder.txtProductName.setText(products.getName());
                holder.txtProductDescription.setText(products.getDescription());
                holder.txtProductPrice.setText("Price = Rs." + products.getPrice());
                holder.txtProductStatus.setText("State : " + products.getProductState());
                Picasso.get().load(products.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String productID = products.getPid();

                        CharSequence options[] = new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(SellersProductsActivity.this);
                        builder.setTitle("Do you want to Delete this product?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0){
                                    sellersProductsRef.child(productID).removeValue();
                                    Toast.makeText(SellersProductsActivity.this, "The product is deleted successfully.", Toast.LENGTH_SHORT).show();
                                }
                                else if (which == 1){

                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false);
                SellerViewHolder holder = new SellerViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}