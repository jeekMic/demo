package com.huawei.mybattera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;


/**
 * Created by 软件组01 on 2018/1/8.
 */

public class BatteryChangedReceiver extends BroadcastReceiver{
    public Message message;
    public StringBuilder  builder = new StringBuilder();
    @Override
    public void onReceive(Context context, Intent intent) {
        builder.setLength(0);
        final String action = intent.getAction();
        if (action.equals(Intent.ACTION_BATTERY_CHANGED)){
            //当前电池的电压
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            //电池的健康状态
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);

            switch (health){

                case BatteryManager.BATTERY_HEALTH_GOOD:
                    builder.append("电池状态:良好");
                    Log.i("电池信息","良好");
                case BatteryManager.BATTERY_HEALTH_COLD:
                    Log.i("电池信息","BATTERY_HEALTH_COLD");
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    Log.i("电池信息","BATTERY_HEALTH_DEAD");
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    Log.i("电池信息","BATTERY_HEALTH_OVERHEAT");
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    Log.i("电池信息","BATTERY_HEALTH_OVER_VOLTAGE");
                    break;
                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                    Log.i("电池信息","BATTERY_HEALTH_UNKNOWN");
                    break;
                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    Log.i("电池信息","BATTERY_HEALTH_UNSPECIFIED_FAILURE");
                    break;
                default:
                    break;
            }
            //当前电池电量
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            Log.i("level",""+level);
            builder.append("\n电量:"+level);
            //电池电量的最大值
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
            Log.i("scale",""+scale);
            builder.append("\n电量的最大值:"+scale);
            //当前手机使用的是哪里的电源
            int pluged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1);
            Log.i("pluged",""+pluged);
            switch (pluged){
                case BatteryManager.BATTERY_PLUGGED_AC:
                    // 电源是AC charger.[应该是指充电器]
                    Log.i("电源类型:","电源是AC charger");
                    break;
                case BatteryManager.BATTERY_PLUGGED_USB:
                    // 电源是USB port
                    Log.i("电源类型:","电源是USB port");
                    break;
                default:
                    break;

            }
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    // 正在充电

                    Log.i("充电状态:","正在充电");
                    builder.append("\n充电状态:正在充电");
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:

                    Log.i("充电状态:","BATTERY_STATUS_DISCHARGING");
                    builder.append("\n充电状态:DISCHARGING");
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    // 充满
                    Log.i("充电状态:","充满");
                    builder.append("\n充电状态:充满");
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    // 没有充电
                    builder.append("\n充电状态:没有充电");
                    Log.i("充电状态:","没有充电");
                    break;
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    // 未知状态
                    Log.i("充电状态:","未知状态");
                    builder.append("\n充电状态:未知状态");
                    break;
                default:
                    break;
            }
            // 电池使用的技术。比如，对于锂电池是Li-ion
            String technology = intent.
                    getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            // 当前电池的温度
            int temperature = intent.
                    getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            String str= "voltage = " + voltage + " technology = "
                    + technology + " temperature = " + temperature;
            message.getMsg(builder.toString());
            Log.i("电池状态",""+str);

        }else if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_LOW)) {
            // 表示当前电池电量低
        } else if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_OKAY)) {
            // 表示当前电池已经从电量低恢复为正常
            System.out.println(
                    "BatteryChangedReceiver ACTION_BATTERY_OKAY---");
        }
    }
    public void setMessage(Message message){
        this.message = message;
    }
}
