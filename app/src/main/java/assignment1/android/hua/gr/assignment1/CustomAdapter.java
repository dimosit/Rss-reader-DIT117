package assignment1.android.hua.gr.assignment1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

/**
 * Created by d1 on 1/12/2015.
 */
public class CustomAdapter extends ArrayAdapter<String>{
    //input an XML file and builds the View objects from it(LayoutInflater) ST
    private LayoutInflater inflater;
    private List<String> rssDatas;

    /* here we must override the constructor for ArrayAdapter
	* the only variable we care about now is ArrayList<Item> objects,
	* because it is the list of objects we want to display.
	*/
    public CustomAdapter(Context context, int textViewResourceId,
                         List<String> objects) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        inflater = ((Activity) context).getLayoutInflater();
        rssDatas = objects;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitleView;
        TextView txtDescView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        //present the titles in the listView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item, null);

            viewHolder = new ViewHolder();
            viewHolder.txtTitleView = (TextView) convertView.findViewById(R.id.textView1);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtTitleView.setText(rssDatas.get(position).toString());


        return convertView;
    }

}
