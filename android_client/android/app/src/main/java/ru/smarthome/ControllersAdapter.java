package ru.smarthome;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.smarthome.library.BaseController;
import ru.smarthome.library.IotDevice;
import ru.smarthome.library.SmartHome;

class ControllersAdapter extends RecyclerView.Adapter<ControllerViewHolder> {

    private SmartHome smartHome;
    private LayoutInflater inflater;

    public ControllersAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }


    public void setSmartHome(SmartHome smartHome) {
        this.smartHome = smartHome;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ControllerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.controller_item, parent, false);
        return new ControllerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ControllerViewHolder holder, int position) {
        IotDevice device = getDeviceByControllerPosition(position);
        BaseController controller = getControllerByPosition(position);
        holder.bind(device, controller);
    }

    @Override
    public int getItemCount() {
        if (smartHome == null || smartHome.devices == null) {
            return 0;
        }
        return countAllControllers();
    }

    private int countAllControllers() {
        int res = 0;
        for (IotDevice device : smartHome.devices) {
            res += device.controllers.size();
        }
        return res;
    }

    @Nullable
    private IotDevice getDeviceByControllerPosition(int position) {
        int skippedContrCount = 0;
        for (IotDevice device : smartHome.devices) {
            int thisContrCount = device.controllers.size();
            if (position <= skippedContrCount + thisContrCount - 1) {
                return device;
            }
            skippedContrCount += thisContrCount;
        }
        return null;
    }

    // TODO: 12/20/18 need better solution, but not now!
    @Nullable
    private BaseController getControllerByPosition(int position) {
        int skippedContrCount = 0;
        for (IotDevice device : smartHome.devices) {
            int thisContrCount = device.controllers.size();
            if (position <= skippedContrCount + thisContrCount - 1) {
                return device.controllers.get(position - skippedContrCount);
            }
            skippedContrCount += thisContrCount;
        }
        return null;
    }
}