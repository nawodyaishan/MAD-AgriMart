package lk.sltc.agrimarket.Sellers;

import androidx.appcompat.app.AppCompatActivity;
import lk.sltc.agrimarket.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SellerAddNewActivity extends AppCompatActivity {
    private ImageView vegitables, fruits, seeds;
    private ImageView fertilizer, equipments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new);

        vegitables = (ImageView) findViewById(R.id.vegitables);
        fruits = (ImageView) findViewById(R.id.fruits);
        seeds = (ImageView) findViewById(R.id.seeds);
        fertilizer = (ImageView) findViewById(R.id.fertilizer);
        equipments = (ImageView) findViewById(R.id.equipments);

        vegitables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerAddNewActivity.this, SellersAddNewProductActivity.class);
                intent.putExtra("category", "vegitables");
                startActivity(intent);
            }
        });

        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerAddNewActivity.this, SellersAddNewProductActivity.class);
                intent.putExtra("category", "fruits");
                startActivity(intent);
            }
        });

        seeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerAddNewActivity.this, SellersAddNewProductActivity.class);
                intent.putExtra("category", "seeds");
                startActivity(intent);
            }
        });

        fertilizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerAddNewActivity.this, SellersAddNewProductActivity.class);
                intent.putExtra("category", "fertilizer");
                startActivity(intent);
            }
        });

        equipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerAddNewActivity.this, SellersAddNewProductActivity.class);
                intent.putExtra("category", "equipments");
                startActivity(intent);
            }
        });
    }
}