package com.jsontoparsedata;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jsontoparsedata.Contacts.Contacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView list;
    private static String url_to_hit = "http://api.androidhive.info/contacts/";

//    private static final String TAG_CONTACTS = "contacts";
//    private static final String TAG_ID = "id";
//    private static final String TAG_NAME = "name";
//    private static final String TAG_EMAIL = "email";
//    private static final String TAG_ADDRESS = "address";
//    private static final String TAG_GENDER = "gender";
//    private static final String TAG_PHONE = "phone";
//    private static final String TAG_PHONE_MOBILE = "mobile";
//    private static final String TAG_PHONE_HOME = "home";
//    private static final String TAG_PHONE_OFFICE = "office";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.list);
        new GetContacts().execute(url_to_hit);

    }

    public class GetContacts extends AsyncTask<String, String, List<Contacts>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Contacts> doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream steam = httpURLConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(steam));

                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String str = buffer.toString();
                JSONObject jsonRootObject = new JSONObject(str);
                JSONArray jsonArray = jsonRootObject.optJSONArray("contacts");

                List<Contacts> contactsModelList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Contacts contactsModel = new Contacts();
                    contactsModel.setId(jsonObject.getString("id"));
                    contactsModel.setName(jsonObject.getString("name"));
                    contactsModel.setEmail(jsonObject.getString("gmail"));
                    contactsModel.setAddress(jsonObject.getString("address"));
                    contactsModel.setGender(jsonObject.getString("gender"));
                    contactsModel.setPhone(jsonObject.getString("phone"));
//                    for(int j = 0;j < jsonObject.getJSONObject("phone").length();j++) {
//                        JSONObject finalObject = jsonObject.getJSONObject(j);
                    contactsModel.setMobile(jsonObject.getString("mobile"));
                    contactsModel.setHome(jsonObject.getString("home"));
                    contactsModel.setOffice(jsonObject.getString("office"));


                    contactsModelList.add(contactsModel);
                }

                return contactsModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Contacts> s) {
            super.onPostExecute(s);
            ContactsAdapter adapter = new ContactsAdapter(getApplicationContext(), R.layout.list_item, s);
            list.setAdapter(adapter);
        }


        public class ContactsAdapter extends ArrayAdapter {

            private List<Contacts> contactsModelList;
            private int resource;
            private LayoutInflater inflater;

            public ContactsAdapter(Context context, int resource, List<Contacts> objects) {
                super(context, resource, objects);
                contactsModelList = objects;
                this.resource = resource;
                inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                ViewHolder holder;

                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(resource, null);
                    holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
                    holder.name = (TextView) convertView.findViewById(R.id.name);
                    holder.email = (TextView) convertView.findViewById(R.id.email);
                    holder.address = (TextView) convertView.findViewById(R.id.address);
                    holder.gender = (TextView) convertView.findViewById(R.id.gender);
                    holder.phone = (TextView) convertView.findViewById(R.id.phone);
                    holder.mobile = (TextView) convertView.findViewById(R.id.mobile);
                    holder.home = (TextView) convertView.findViewById(R.id.home);
                    holder.office = (TextView) convertView.findViewById(R.id.office);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tv_id.setText(contactsModelList.get(position).getId());
                holder.name.setText(contactsModelList.get(position).getName());
                holder.email.setText(contactsModelList.get(position).getEmail());
                holder.address.setText(contactsModelList.get(position).getAddress());
                holder.gender.setText(contactsModelList.get(position).getGender());
                holder.phone.setText(contactsModelList.get(position).getPhone());
                holder.mobile.setText(contactsModelList.get(position).getMobile());
                holder.home.setText(contactsModelList.get(position).getHome());
                holder.office.setText(contactsModelList.get(position).getOffice());

                return convertView;
            }

            class ViewHolder {
                private TextView tv_id;
                private TextView name;
                private TextView email;
                private TextView address;
                private TextView gender;
                private TextView phone;
                private TextView mobile;
                private TextView home;
                private TextView office;
            }

        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            new GetContacts().execute(url_to_hit);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
