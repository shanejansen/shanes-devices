package com.shanejansen.devices;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Shane Jansen on 3/11/16.
 */
public class WearDevicesAdapter extends WearableListView.Adapter {
    private Context mContext;
    private List<WearDevice> mDevices;
    private DevicesAdapterInterface mDevicesAdapterInterface;
    private LayoutInflater mInflater;

    public WearDevicesAdapter(Context context, List<WearDevice> devices, DevicesAdapterInterface devicesAdapterInterface) {
        this.mContext = context;
        this.mDevices = devices;
        this.mDevicesAdapterInterface = devicesAdapterInterface;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mInflater.inflate(R.layout.item_device, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, final int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;
        itemHolder.tvName.setText(mDevices.get(position).getName());
        itemHolder.swState.setChecked(mDevices.get(position).isOn());
        itemHolder.swState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) mDevicesAdapterInterface.switchToggled(position, isChecked);
            }
        });

        ((ItemViewHolder) holder).itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView tvName;
        private Switch swState;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            swState = (Switch) itemView.findViewById(R.id.swState);
        }
    }

    public interface DevicesAdapterInterface {
        void switchToggled(int index, boolean isChecked);
    }
}
