package com.example.haijun.mymobilemanager;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.haijun.javabean.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends ActionBarActivity {

    private ListView lv_contactllist_content;
    private List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        contactList = new ArrayList<>();
        initContentValuse();

        lv_contactllist_content = (ListView) findViewById(R.id.lv_contactllist_content);

        lv_contactllist_content.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return contactList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Contact contact = contactList.get(position);
                MyHander myHander = null;
                View inflate ;
                if (convertView !=null){
                    inflate = convertView;
                    myHander = (MyHander) convertView.getTag();
                }
                else {
                    myHander = new MyHander();
                    inflate = View.inflate(ContactListActivity.this, R.layout.conatctlist_item, null);
                    convertView = inflate;
                    myHander.tv_contactlist_name = (TextView) inflate.findViewById(R.id.tv_contactlist_name);
                    myHander.tv_contactlist_number = (TextView) inflate.findViewById(R.id.tv_contactlist_number);
                    convertView.setTag(myHander);
                }
                myHander.tv_contactlist_name.setText(contact.getName());
                myHander.tv_contactlist_number.setText(contact.getPhonenum());
                return inflate;
            }

            class MyHander{
                TextView tv_contactlist_name;
                TextView tv_contactlist_number;
            }
        });

        lv_contactllist_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = contactList.get(position);
                String phonenum = contact.getPhonenum();
                Intent intent = new Intent();
                intent.putExtra("safePhoneNum",phonenum);

                setResult(1000, intent);
                finish();
            }
        });
    }

    private void initContentValuse() {
        Cursor query = getContentResolver().query(Uri.parse("content://com.android.contacts/raw_contacts"), new String[]{"contact_id"}, null, null, null);
        while (query.moveToNext()){
            Contact contact = new Contact();
            int contact_id = query.getInt(0);
            Cursor query2 = getContentResolver().query(Uri.parse("content://com.android.contacts/data"),
                    new String[]{"data1", "mimetype"}, "raw_contact_id =?", new String[]{contact_id + ""}, null);
            while (query2.moveToNext()){
                String data1 = query2.getString(0);
                String mimitype = query2.getString(1);
                if ("vnd.android.cursor.item/phone_v2".equals(mimitype)){
                    contact.setPhonenum(data1);
                }
                else if ("vnd.android.cursor.item/name".equals(mimitype)){
                    contact.setName(data1);
                }
            }
            contactList.add(contact);
        }
    }
}
