package com.shanejansen.devices.ui.adapters;

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
import com.shanejansen.devices.ui.common.mvp.MvpRecyclerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shane Jansen on 3/6/16.
 */
public class DevicesAdapter extends MvpRecyclerAdapter<Device, DevicesAdapter.ViewHolder> {
    // Data
    private Context mContext;
    private DevicesAdapterInterface mDevicesAdapterInterface;
    private LayoutInflater mInflater;

    public DevicesAdapter(Context context, DevicesAdapterInterface devicesAdapterInterface) {
        super();
        this.mContext = context;
        this.mDevicesAdapterInterface = devicesAdapterInterface;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_device, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvName.setText(getData().get(position).getName());
        holder.tvPin.setText(mContext.getResources().getString(R.string.pin, getData().get(position).getPin()));
        holder.swState.setChecked(getData().get(position).isOn());
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
