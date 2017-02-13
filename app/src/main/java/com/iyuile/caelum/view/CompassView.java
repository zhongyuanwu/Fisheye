package com.iyuile.caelum.view;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.ImageView;

import com.iyuile.caelum.utils.Log;

/**
 * 罗盘
 */
public class CompassView implements SensorEventListener {
    private static final String TAG = ":::CompassView";

    private SensorManager sensorManager;
    private Sensor gsensor;
    private Sensor msensor;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float azimuth = 0f;
    private float currectAzimuth = 0;

    private OnAzimuthListener onAzimuthListener;

    // compass arrow to rotate
    public ImageView arrowView = null;

    /**
     * 在Android2.3 gingerbread系统中，google提供了11种传感器供应用层使用，具体如下：（Sensor类）
     * Sensor.TYPE_ACCELEROMETER 1 //加速度
     * Sensor.TYPE_MAGNETIC_FIELD 2 //磁力
     * Sensor.TYPE_ORIENTATION 3 //方向
     * Sensor.TYPE_GYROSCOPE 4 //陀螺仪
     * Sensor.TYPE_LIGHT 5 //光线感应
     * Sensor.TYPE_PRESSURE 6 //压力
     * Sensor.TYPE_TEMPERATURE 7 //温度
     * Sensor.TYPE_PROXIMITY 8 //接近
     * Sensor.TYPE_GRAVITY 9 //重力
     * Sensor.TYPE_LINEAR_ACCELERATION 10//线性加速度
     * Sensor.TYPE_ROTATION_VECTOR 11//旋转矢量
     *
     * @param context
     */
    public CompassView(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    /**
     * 在Android编程中 SensorManager的频率总共分为4等，分别是：
     * <p>
     * SENSOR_DELAY_FASTEST 最灵敏，快的然你无语
     * SENSOR_DELAY_GAME 游戏的时候用这个，不过一般用这个就够了
     * SENSOR_DELAY_NORMAL 比较慢。
     * SENSOR_DELAY_UI 最慢的
     */
    public void start() {
        sensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    private void adjustArrow() {
        if (arrowView == null) {
            Log.i(TAG, "arrow view is not set");
            return;
        }
        //:::处理抖动
        float difference = Math.abs(azimuth - currectAzimuth);
        if (difference <= 1)
            return;

        Log.i(TAG, "will set rotation from " + currectAzimuth + " to " + azimuth);

        /*ObjectAnimator anim = ObjectAnimator.ofFloat(arrowView, "rotation", -currectAzimuth, -azimuth);
        anim.setDuration(currectAzimuth - azimuth >300 || azimuth - currectAzimuth > 300 ? 0 : 500);
        anim.setRepeatCount(0);
        anim.start();*/

        currectAzimuth = azimuth;

        arrowView.setRotation(-currectAzimuth);

        if (onAzimuthListener != null)
            onAzimuthListener.onAzimuth(currectAzimuth);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                        * event.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                        * event.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                        * event.values[2];

                // mGravity = event.values;

                // Log.e(TAG, Float.toString(mGravity[0]));
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                // mGeomagnetic = event.values;

                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                        * event.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                        * event.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                        * event.values[2];
                // Log.e(TAG, Float.toString(event.values[0]));

            }

            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                // Log.d(TAG, "azimuth (rad): " + azimuth);
                azimuth = (float) Math.toDegrees(orientation[0]); // orientation
                azimuth = (azimuth + 360) % 360;
                // Log.d(TAG, "azimuth (deg): " + azimuth);
                adjustArrow();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void setOnAzimuthListener(OnAzimuthListener onAzimuthListener) {
        this.onAzimuthListener = onAzimuthListener;
    }

    public interface OnAzimuthListener {
        void onAzimuth(float azimuth);
    }
}