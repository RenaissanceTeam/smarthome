package smarthome.library.common;

import java.util.List;

public interface DeviceDataSource<T extends IotDevice>{

    List<T> getAll();

    /**
     * @return result of addition - was the device added to the data source
     */
    boolean add(T device);

    boolean contains(T device);

    IotDevice get(Long guid);

    void update(T device);

    void delete(T device);

    void clearAll();
}
