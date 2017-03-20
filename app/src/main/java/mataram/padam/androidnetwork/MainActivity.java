package mataram.padam.androidnetwork;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends ListActivity {

    private static final String BASE_URL =
            "https://stormy-escarpment-90997.herokuapp.com/api/customer/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);
        loadAll();

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Customer cust = (Customer) getListView().getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(),cust.getName(),Toast.LENGTH_LONG).show();
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.mnuDetail :
                                Toast.makeText(getApplicationContext(),"Detail Click",Toast.LENGTH_LONG).show();
                                break;
                            case R.id.mnuDelete:
                                delete(cust);
                                break;
                            default: break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }


    private void delete(Customer customer){
        client.delete(BASE_URL+customer.getId(), null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                avi.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Toast.makeText(getApplicationContext(),"Delete",Toast.LENGTH_LONG).show();
                loadAll();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.mnuAdd://
                Intent intent = new Intent(this, InputActivity.class);
                startActivity(intent);
                break;
            case R.id.mnuRefresh:
                Toast.makeText(getApplicationContext(),"Reload Data",Toast.LENGTH_LONG).show();
                loadAll();
                break;
            default:
                break;
        }

        return true;
    }

    private void loadAll() {
       final List<Customer> customers = new ArrayList<Customer>();
        client.get(BASE_URL, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {

                avi.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        customers.add(toCustomer(obj));
                    } catch (JSONException ex) {
                        Log.e("ERROR", ex.getMessage());
                    }
                }
                Log.i("CUSTOMERS-X", customers.size()+"");
                setListAdapter(new CustomerAdapter(getApplicationContext(),customers));
                avi.hide();
            }
        });
    }

    private Customer toCustomer(JSONObject obj) {
        Customer customer = new Customer();
        try {
            customer.setId(obj.getString("id"));
            customer.setName(obj.getString("name"));
            customer.setAddress(obj.getString("address"));
            customer.setEmail(obj.getString("email"));
            customer.setPhone(obj.getString("phone"));
            Log.i("CUSTOMER",customer.toString());
        }catch(JSONException ex){
            Log.e("CONVERT",ex.getMessage());
        }
        return customer;
    }


}
