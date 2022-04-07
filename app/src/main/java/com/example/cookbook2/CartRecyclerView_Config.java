package com.example.cookbook2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartRecyclerView_Config {
    private Context mContext;
    private CartAdapter mCartAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<Cart> carts, List<String> keys){
        mContext = context;
        mCartAdapter = new CartAdapter(carts,keys);
        LinearLayoutManager layout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(mCartAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),layout.getOrientation());
        recyclerView.addItemDecoration(divider);
    }

    // to inflate the layout cart_items
    class CartItemView extends RecyclerView.ViewHolder{
        private TextView mTitle;
        private TextView mQuantity;

        private String key;

        public CartItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.cart_items, parent, false));
            mTitle = (TextView) itemView.findViewById(R.id.cart_txtview);
            mQuantity = (TextView) itemView.findViewById(R.id.cart_quantity);


        }

        public void bind(Cart carts, String key){
            mTitle.setText(carts.getItem_name());
            mQuantity.setText(carts.getQuantity());
            this.key = key;
        }
    }

    class CartAdapter extends RecyclerView.Adapter<CartItemView>{
        private List<Cart> mCart;
        private List<String> mKey;

        @NonNull
        @Override
        public CartItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CartItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CartItemView holder, int position) {
            holder.bind(mCart.get(position), mKey.get(position));
        }

        @Override
        public int getItemCount() {
            return mCart.size();
        }

        public CartAdapter(List<Cart> mCart, List<String> mKey) {
            this.mCart = mCart;
            this.mKey = mKey;
        }
    }
}
