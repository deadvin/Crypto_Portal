package com.example.cryptoterminal.main;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.cryptoterminal.R;
import java.util.ArrayList;



public class SymbolAdapter extends ArrayAdapter<symbol> {

    Context mContext;
    private int mResource;

    private static class ViewHolder {
        TextView name;
        TextView val1;
        TextView val2;
        TextView val3;

    }

    public SymbolAdapter(Context context, int resource, ArrayList<symbol> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String name = getItem(position).getName();
        String val1 = getItem(position).getVal1();
        String val2 = getItem(position).getVal2();
        String val3 = getItem(position).getVal3();
        int  seen = getItem(position).getAlarm();

        symbol symbol = new symbol(name,val1,val2,val3,seen);

        final View result;

        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.name =  convertView.findViewById(R.id.textView);
            holder.val1 =  convertView.findViewById(R.id.textView1);
            holder.val2 =  convertView.findViewById(R.id.textView2);
            holder.val3 =  convertView.findViewById(R.id.textView3);
//
//            holder.open.setOnClickListener( new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    String hhh = "https://twitter.com/twitter/statuses/" +  post.getLink();
//
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hhh));
//                    mContext.startActivity(browserIntent);
//
//                }
//            });

            result = convertView;

            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.name.setText(symbol.getName());
        holder.val1.setText(symbol.getVal1());
        holder.val2.setText(symbol.getVal2());
        holder.val3.setText(symbol.getVal3());


        return convertView;
    }
}

























