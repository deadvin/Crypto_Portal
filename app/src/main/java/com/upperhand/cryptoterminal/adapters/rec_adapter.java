//package com.upperhand.cryptoterminal.adapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.TimeZone;
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.core.content.ContextCompat;
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.formatter.ValueFormatter;
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
//import com.upperhand.cryptoterminal.R;
//import com.upperhand.cryptoterminal.objects.div;
//import com.upperhand.cryptoterminal.objects.last_price;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//
//public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
//
//    Context mContext;
//    private int mResource;
//    int count = 0;
//    int num_elements = 0;
//    public boolean isAnimate;
//    ArrayList<String> list = new ArrayList<>();
//    ArrayList<last_price> last_prices;
//    String name_alt;
//    float interval_size;
//    int hours;
//    int mins;
//    int visible_elements;
//    boolean half;
//    String selected;
//    public boolean price_update;
//    Calendar calendar = Calendar.getInstance();
//    String[] month_names = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
//    private LayoutInflater mInflater;
//
//
//    public MyRecyclerViewAdapter(Context context, int resource, ArrayList<div> objects,  ArrayList<last_price> last_prices) {
//
//        this.mInflater = LayoutInflater.from(context);
//        this.mContext = context;
//        this.last_prices = last_prices;
//        mResource = resource;
//
//    }
//
//    @NotNull
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.layout_div, parent, false);
//        return new ViewHolder(view);
//    }
//
//    // binds the data to the TextView in each row
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        String animal = mData.get(position);
//        holder.myTextView.setText(animal);
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return last_prices.size();
//    }
//
//
//    // stores and recycles views as they are scrolled off screen
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        TextView myTextView;
//
//        ViewHolder(View itemView) {
//            super(itemView);
//            myTextView = itemView.findViewById(R.id.tvAnimalName);
//            itemView.setOnClickListener(this);
//        }
//
//
//
//
//        @Override
//        public void onClick(View view) {
//
//        }
//    }
//
//
//}