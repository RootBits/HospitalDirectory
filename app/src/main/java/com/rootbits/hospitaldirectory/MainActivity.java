package com.rootbits.hospitaldirectory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rootbits.hospitaldirectory.Database.DistrictInfo;
import com.rootbits.hospitaldirectory.Database.DivisionInfo;
import com.rootbits.widgets.AnimatedExpandableListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<DivisionInfo> division;
    ArrayList<DistrictInfo> district;
    List<GroupItem> items = new ArrayList<GroupItem>();
    ArrayList<String>div=new ArrayList<>();

    @BindView(R.id.toolbarMain)Toolbar toolbar;
    @BindView(R.id.listView)AnimatedExpandableListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


        loadDatabase();
        loadAnimatedExapandListView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//---------------------------------All Function---------------------------------------



    void loadDatabase(){
        DatabaseHandler db=new DatabaseHandler(this);
        try {
            db.createDataBase();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DB", "Data Base found");
        }

    }

    void loadDivisionInfo(){
        DatabaseHandler db=new DatabaseHandler(getApplicationContext());
        try {
            division=db.selectDivision();
        }catch (Exception ex){

        }finally {
            db.close();
        }

    }

    void loadAnimatedExapandListView(){
        loadDivisionInfo();
        div.clear();
        DatabaseHandler db=new DatabaseHandler(getApplicationContext());
        // Populate our list with groups and it's children
        for(int i = 0; i < division.size(); i++) {
            GroupItem item = new GroupItem();

            item.title = division.get(i).getName().toUpperCase().trim()+" DIVISION";
            div.add(division.get(i).getName());
            item.id=division.get(i).getId();
            district=db.selectDistrict(division.get(i).getId().toString());
            for(int j = 0; j < district.size(); j++) {
                ChildItem child = new ChildItem();
                child.title = district.get(j).getName().trim();
                child.id=district.get(j).getId().trim();
                item.items.add(child);
            }
            items.add(item);
        }

        ExampleAdapter adapter = new ExampleAdapter(this);
        adapter.setData(items);

        listView = (AnimatedExpandableListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);



        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(getApplicationContext(),items.get(groupPosition).items.get(childPosition).id.toString(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),ShowHospitalList.class);

                intent.putExtra(IntentFlag.divisionID,items.get(groupPosition).id);
                intent.putExtra(IntentFlag.divisionName,div.get(groupPosition));
                intent.putExtra(IntentFlag.districtId,items.get(groupPosition).items.get(childPosition).id);
                intent.putExtra(IntentFlag.districtName,items.get(groupPosition).items.get(childPosition).title);

                startActivity(intent);
                return false;
            }
        });


    }














//-------------------------------End All Function---------------------------------------


    //-------------------------ExpandListView Adapter--------------------------------


    private static class GroupItem {
        String title;
        String id;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String id;
        String title;
    }

    private static class ChildHolder {
        TextView title;
    }

    private static class GroupHolder {
        TextView title;
        LinearLayout header;
    }

    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.lblListItem);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.list_group, parent, false);
                holder.header=(LinearLayout) convertView.findViewById(R.id.header);
                holder.title = (TextView) convertView.findViewById(R.id.lblListHeader);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }


            //convertView.setPadding(0,20,0,20);
            holder.title.setText(item.title);
            if (isExpanded){
                holder.header.setBackgroundResource(R.drawable.header_expand);
                holder.title.setTextColor(getResources().getColor(R.color.white));
            }else{
                holder.header.setBackgroundResource(R.drawable.header_normal);
                holder.title.setTextColor(getResources().getColor(R.color.ps_text_color));
            }
            holder.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, isExpanded ? R.drawable.list_expand_state_ic : R.drawable.list_collapse_state_ic, 0);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }
}
