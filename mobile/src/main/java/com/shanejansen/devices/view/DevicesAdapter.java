package com.shanejansen.devices.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.shanejansen.devices.R;
import com.shanejansen.devices.data.Device;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shane Jansen on 3/6/16.
 */
public class DevicesAdapter extends ArrayAdapter<Device> {
    // Data
    private Context mContext;
    private List<Device> mDevices;
    private DevicesAdapterInterface mDevicesAdapterInterface;
    private LayoutInflater mInflater;

    public DevicesAdapter(Context context, List<Device> devices, DevicesAdapterInterface devicesAdapterInterface) {
        super(context, R.layout.item_device, devices);
        this.mContext = context;
        this.mDevices = devices;
        this.mDevicesAdapterInterface = devicesAdapterInterface;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_device, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tvName.setText(mDevices.get(position).getName());
        viewHolder.tvPin.setText(mContext.getResources().getString(R.string.pin, mDevices.get(position).getPin()));
        viewHolder.swState.setChecked(mDevices.get(position).isOn());
        viewHolder.swState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // We have to check if it is pressed because this method is called even if
                // the user did not manually press the button
                if (buttonView.isPressed()) mDevicesAdapterInterface.switchToggled(position, isChecked);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tvName) TextView tvName;
        @Bind(R.id.tvPin) TextView tvPin;
        @Bind(R.id.swState) Switch swState;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface DevicesAdapterInterface {
        void switchToggled(int index, boolean isChecked);
    }
}
