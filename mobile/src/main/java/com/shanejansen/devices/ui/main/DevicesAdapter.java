package com.shanejansen.devices.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.shanejansen.devices.R;
import com.shanejansen.devices.data.models.Device;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shane Jansen on 3/6/16.
 */
public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {
    // Data
    private Context mContext;
    private List<Device> mDevices;
    private DevicesAdapterInterface mDevicesAdapterInterface;
    private LayoutInflater mInflater;

    public DevicesAdapter(Context context, DevicesAdapterInterface devicesAdapterInterface) {
        this.mContext = context;
        this.mDevicesAdapterInterface = devicesAdapterInterface;
        mDevices = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_device, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvName.setText(mDevices.get(position).getName());
        holder.tvPin.setText(mContext.getResources().getString(R.string.pin, mDevices.get(position).getPin()));
        holder.swState.setChecked(mDevices.get(position).isOn());
        holder.swState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // We have to check if it is pressed because this method is called even if
                // the user did not manually press the button
                if (buttonView.isPressed()) {
                    mDevicesAdapterInterface.switchToggled(holder.getLayoutPosition(), isChecked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    // TODO: Abstract out. See CounterAdapter in arch-example.
    public void clearAndAddAll(List<Device> devices) {
        mDevices.clear();
        mDevices.addAll(devices);
        notifyDataSetChanged();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvName) TextView tvName;
        @Bind(R.id.tvPin) TextView tvPin;
        @Bind(R.id.swState) Switch swState;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface DevicesAdapterInterface {
        void switchToggled(int index, boolean isChecked);
    }
}
