package mataram.padam.androidnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hendro Steven on 20/03/2017.
 */

public class CustomerAdapter extends ArrayAdapter<Customer> {

    private final Context context;
    private final List<Customer> customers;

    public CustomerAdapter(Context context, List<Customer> customers) {
        super(context, R.layout.customer_list, customers);
        this.context = context;
        this.customers = customers;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.customer_list, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView address = (TextView) rowView.findViewById(R.id.address);

        Customer customer = customers.get(position);

        name.setText(customer.getName());
        address.setText(customer.getAddress());

        return rowView;
    }
}
