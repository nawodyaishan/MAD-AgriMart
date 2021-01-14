package lk.sltc.agrimarket.Sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import lk.sltc.agrimarket.MainActivity;
import lk.sltc.agrimarket.R;

public class SellerHomeActivity extends AppCompatActivity {
    private Button myProducts, addNew, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        myProducts = (Button) findViewById(R.id.seller_my_btn);
        addNew = (Button) findViewById(R.id.seller_add_btn);
        logout = (Button) findViewById(R.id.seller_logout_btn);

        myProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellerHomeActivity.this, SellersProductsActivity.class);
                startActivity(intent);
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellerHomeActivity.this, SellerAddNewActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
