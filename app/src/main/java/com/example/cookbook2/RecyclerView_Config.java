package com.example.cookbook2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private ProductAdapter mProductAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<Product> products, List<String> keys){
        mContext = context;
        mProductAdapter = new ProductAdapter(products, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mProductAdapter);
    }

    // to inflate the layout product_items
    class ProductItemView extends RecyclerView.ViewHolder{
        private TextView mTitle;
        private TextView mQuantity;
        private TextView mDate;

        private String key;

        public ProductItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.product_items, parent, false));
            mTitle = (TextView) itemView.findViewById(R.id.title_txtview);
            mQuantity = (TextView) itemView.findViewById(R.id.quantity);
            mDate  = (TextView) itemView.findViewById(R.id.date_txtview);
        }

        public void bind(Product product, String key){
            mTitle.setText(product.getProduct_name());
            mQuantity.setText(product.getQuantity());
            mDate.setText(product.getDatetime());
            this.key = key;
        }
    }

    class ProductAdapter extends RecyclerView.Adapter<ProductItemView>{
        private List<Product> mProduct;
        private List<String> mKey;

        @NonNull
        @Override
        public ProductItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductItemView holder, int position) {
            holder.bind(mProduct.get(position), mKey.get(position));
        }

        @Override
        public int getItemCount() {
            return mProduct.size();
        }

        public ProductAdapter(List<Product> mProduct, List<String> mKey) {
            this.mProduct = mProduct;
            this.mKey = mKey;
        }
    }
}
