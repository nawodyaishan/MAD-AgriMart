package lk.sltc.agrimarket.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lk.sltc.agrimarket.Interface.ItemClickListner;
import lk.sltc.agrimarket.R;

public class SellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName, txtProductDescription, txtProductPrice, txtProductStatus;
    public ImageView imageView;
    public ItemClickListner listner;

    public SellerViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.seller_product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.seller_product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.seller_product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.seller_product_price);
        txtProductStatus = (TextView) itemView.findViewById(R.id.seller_product_status);

    }

    public void setItemClickListner(ItemClickListner listner){
        this.listner = listner;
    }

    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false);

    }
}
