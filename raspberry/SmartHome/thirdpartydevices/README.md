# Third party devices library

## Supported devices:
* Xiaomi gateway lumi (v3)
* Xiaomi door-window sensor
* Xiaomi motion sensor
* Xiaomi smart plug
* Xiaomi smoke sensor
* Xiaomi water leak sensor
* Xiaomi temerature-humidity sensor
* Xiaomi weather sensor (temperature + humidity + pressure)
* Xiaomi wired single wall switch
* Xiaomi wired dual wall switch
* Xiaomi wireless switch

## Usage
### Discovering gateway and devices:
```
GatewayEnv env = GatewayEnv.builder()
                .setGatewayPassword("<password>")
                .build();

Gateway gateway = env.getGateway();
List<Device> devices = env.getDevices();
```

### Getting string with information about device
```
device.toString();

Example output:
        --- Xiaomi gateway device ---
    type: gateway, sid: 7c49ebb09a69, name:
    rgb: 0, illumination: 595, proto version: 1.1.2
```

### Subscribing for the device's state updates
subscription occurs via device's nested interface
```
doorWindowSensor.setListener(System.out::println);
```

## Devices features

Devices, powered by batteries have `float getVoltage()` method

### Gateway
methods:
```
// illumination range: 300 - 1300
enableLight(byte r, byte g, byte b, int illumination)

disableLight()
```

getters:
```
String getIp()

String getRgb()

String getIllumination()

String getProtoVersion()
```

### Smart plug
methods:
```
on()

off()
```

getters:
```
int getInUse()

int getPowerConsumed()

float getLoadPower()
```

### Wired single wall switch
methods:
```
on()

off()
```

### Wired dual wall switch
methods:
```
onLeft()

offLeft()

onRight()

offRight()
```

### Wired dual wall switch
methods:
```
click()

doubleClick()

longPress()
```
