package com.huawei.mybattera;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnBindViewListener;
import com.timmy.tdialog.listener.OnViewClickListener;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements Message{
    private Unbinder  unbiner;
    private Vibrator vibrator;
    Button bt_getBatter;
    TextView tv_result;
    TextView tv_alarm;
    private AssetManager assetManager;
    private boolean isvibrator = true;
    private BatteryChangedReceiver batteryChangedReceiver;
    private MediaPlayer mMediaPlayer = null;
    MediaPlayer player = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbiner =
        //以前用的是ButterKnife.inject()来绑定Activity,现在用的是ButterKnife.bind()，版本不同相应的方法不同
        ButterKnife.bind(MainActivity.this);
        //初始化数据
        initData();
        // 注册电量监听事件
        registBatter();
    }

    private void registBatter() {
        IntentFilter  intentFilter = getFilter();
        registerReceiver(batteryChangedReceiver,intentFilter);
        batteryChangedReceiver.setMessage(this);
    }

    private void initData() {
        tv_result = findViewById(R.id.tv_result);
        tv_alarm = findViewById(R.id.tv_alarm);
        bt_getBatter = findViewById(R.id.bt_getBatter);
        bt_getBatter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator = (Vibrator)getApplicationContext().getSystemService(VIBRATOR_SERVICE);
                // 等待3秒，震动3秒，从第0个索引开始，一直循环
                vibrator.vibrate(new long[]{500, 500}, 0);
                isvibrator = !isvibrator;
                if (isvibrator){
                    vibrator.cancel();
                    player.stop();
                }else {
                    playRing();
                }

            }
        });
        batteryChangedReceiver = new BatteryChangedReceiver();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbiner.unbind();
        unregisterReceiver(batteryChangedReceiver);
    }

    public IntentFilter getFilter() {
        IntentFilter  filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        return filter;
    }

    @Override
    public void getMsg(String str) {
        Log.i("getMsg",str);
        tv_result.setText(str);
    }
    public  void PlaySound(final Context context) {

        Log.e("ee", "正在响铃");
        // 使用来电铃声的铃声路径
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        // 如果为空，才构造，不为空，说明之前有构造过
        if(mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.setLooping(true); //循环播放
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private MediaPlayer playRing() {

        try {
            player = new MediaPlayer();
            assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("danger.mp3");
            player.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            player.prepare();
            player.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return player;
    }

    public void testDialog(View view) {
     new TDialog.Builder(getSupportFragmentManager())
             .setLayoutRes(R.layout.dialog_vb_convert)
             .setScreenWidthAspect(this,0.85f)
             .setCancelableOutside(false)
             .addOnClickListener(R.id.tv_jiuyuan_desc,R.id.tv_cancel,R.id.tv_confirm)
             .setOnViewClickListener(new OnViewClickListener() {
                 @Override
                 public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                     switch (view.getId()){
                         case R.id.tv_jiuyuan_desc:
                             Toast.makeText(MainActivity.this, "进入说明界面", Toast.LENGTH_SHORT).show();
                             break;
                         case R.id.tv_cancel:
                             tDialog.dismiss();
                             break;
                         case R.id.tv_confirm:
                             Toast.makeText(MainActivity.this, "进入说明界面", Toast.LENGTH_SHORT).show();
                             tDialog.dismiss();
                             break;
                     }
                 }
             })
             .create()
             .show();
    }

    public void showDialog1(){
        new TDialog.Builder(getSupportFragmentManager())
                .setLayoutRes(R.layout.dialog_click2)
//                .setWidth(600)
//                .setHeight(800)
                .setScreenHeightAspect(this, 0.3f)
                .setScreenWidthAspect(this, 0.8f)
                .setGravity(Gravity.CENTER)
                .setTag("DialogTest")
                .setDimAmount(0.8f)
                .setCancelableOutside(true)
                .setCancelable(true)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Toast.makeText(MainActivity.this, "弹窗消失回调", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnBindViewListener(new OnBindViewListener() {
                    @Override
                    public void bindView(BindViewHolder viewHolder) {
                        viewHolder.setText(R.id.tv_content, "红白");
                        viewHolder.setText(R.id.tv_title, "标题");
                    }
                })
                .addOnClickListener(R.id.btn_left, R.id.btn_right, R.id.tv_title)
                .setOnViewClickListener(new OnViewClickListener() {
                    @Override
                    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                        switch (view.getId()) {
                            case R.id.btn_left:
                                Toast.makeText(MainActivity.this, "left clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.btn_right:
                                Toast.makeText(MainActivity.this, "btn_right clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.tv_title:
                                Toast.makeText(MainActivity.this, "tv_title clicked", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                })
                .create()
                .show();
    }


}
