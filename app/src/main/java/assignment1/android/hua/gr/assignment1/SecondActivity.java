package assignment1.android.hua.gr.assignment1;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ListView rssListView = (ListView) findViewById(R.id.rssListView);

        //get the bundle with data from MainActivity
        Bundle bundle = getIntent().getExtras();

        //store object in to Arraylist
        ArrayList<RssItems> rowItems = (ArrayList<RssItems>) bundle.getSerializable("rssBundel");

        //debug
        System.out.println("----------------------------------------------------");
        System.out.println("Size of array in SecondActivity = " + rowItems.size());

        //create tow list to save titles and descriptions
        List<String> muTitle = new ArrayList<>();
        final List<String> mDescriptio = new ArrayList<>();

        //store the titles separately from descriptions
        for(int i = 0; i< rowItems.size();i++){

            muTitle.add(rowItems.get(i).getPostTitle());
            mDescriptio.add(rowItems.get(i).getPostDescription());
            System.out.println();
        }


        //use the default array adapter
//      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, muTitle);

        //use my custom adapter to display titles in the listView
        CustomAdapter adapter =  new CustomAdapter(this, R.layout.listview_item, muTitle);

        // Set list adapter for the ListView
        rssListView.setAdapter(adapter);



        //on title click display a toast with description
        rssListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Toast.makeText(getApplicationContext(), (CharSequence) mDescriptio.get(arg2), Toast.LENGTH_LONG).show();



            }
        });
//
    }




}

