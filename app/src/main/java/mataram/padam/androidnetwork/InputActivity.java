package mataram.padam.androidnetwork;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class InputActivity extends Activity {

    private static final String BASE_URL =
            "https://stormy-escarpment-90997.herokuapp.com/api/customer";

    private static AsyncHttpClient client = new AsyncHttpClient();

    private EditText name, address, email, phone;

    private Button btnSave;

    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        name = (EditText)findViewById(R.id.txtNama);
        address = (EditText)findViewById(R.id.txtAddress);
        email = (EditText)findViewById(R.id.txtEmail);
        phone = (EditText)findViewById(R.id.txtPhone);
        btnSave = (Button)findViewById(R.id.btnSave);
        avi = (AVLoadingIndicatorView)findViewById(R.id.aviSave);
        name.requestFocus();
        avi.hide();
    }

    public void save(View view){
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("name", name.getText().toString());
            jsonParams.put("address", address.getText().toString());
            jsonParams.put("email", email.getText().toString());
            jsonParams.put("phone", phone.getText().toString());

            StringEntity entity = new StringEntity(jsonParams.toString());

            client.post(getApplicationContext(), BASE_URL, entity, "application/json",
                    new JsonHttpResponseHandler(){
                        @Override
                        public void onStart() {
                            avi.show();
                            btnSave.setText("Processing");
                            btnSave.setEnabled(false);
                            Log.i("SAVE_CUSTOMER","Send Data");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            name.setText("");
                            address.setText("");
                            email.setText("");
                            phone.setText("");
                            btnSave.setText("Save Customer");
                            btnSave.setEnabled(true);
                            avi.hide();
                            finish();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Toast.makeText(getApplicationContext(),"Fail to save", Toast.LENGTH_LONG).show();
                            btnSave.setText("Save Customer");
                            btnSave.setEnabled(true);
                            avi.hide();
                        }
                    });
        }catch(JSONException ex){
            Log.e("SAVE_CUSTOMER",ex.getMessage());
        }catch(UnsupportedEncodingException ex){
            Log.e("SAVE_CUSTOMER",ex.getMessage());
        }
    }
}
