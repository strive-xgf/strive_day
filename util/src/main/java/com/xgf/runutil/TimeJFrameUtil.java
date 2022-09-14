package com.xgf.runutil;

/**
 * @author xgf
 * @create 2022-09-12 17:02
 * @description JFrame 时钟（倒计时）
 **/

import com.xgf.date.DateUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class TimeJFrameUtil extends JFrame implements Runnable{

    /**
     * 日期 JLabel
     */
    private JLabel dateJLabel;

    /**
     * 时间 JLabel
     */
    private JLabel timeJLabel;

    /**
     * 倒计时时间 JLabel
     */
    private JLabel countDownJLabel;

    /**
     * 倒计时终止时间戳，单位ms：毫秒
     */
    private Long endTime;

    /**
     * 初始化图形界面
     * @param millis 倒计时毫秒数
     */
    public TimeJFrameUtil(Long millis) {
        this.setVisible(true);
        this.setTitle("计时器");

        int widthLen = 360;
        int heightLen = 200;
        this.setSize(widthLen, heightLen);
        // 获取屏幕大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 窗体位置坐标（居中显示）要减去setSize的宽高
        this.setLocation((screenSize.width - widthLen)/2, (screenSize.height - heightLen)/2);
        this.setResizable(true);
        // 点击 X(窗口关闭) 同时停止程序
        // EXIT_ON_CLOSE：使用 system exit 方法退出应用程序，DISPOSE_ON_CLOSE: 调用任意已注册 windowlistener 的对象后自动隐藏并释放该窗体
        // do_nothing_on_close(在 windowconstants 中定义)：不执行任何操作；要求程序在已注册的 windowlistener 对象的 windowclosing方法中处理该操作。
        // hide_on_close(在 windowconstants 中定义)：调用任意已注册的 windowlistener 对象后自动隐藏该窗体。
        // dispose_on_close(在 windowconstants 中定义)：调用任意已注册 windowlistener 的对象后自动隐藏并释放该窗体。
        // exit_on_close(在 jframe 中定义)：使用 system exit 方法退出应用程序。仅在应用程序中使用。
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 容器
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        // 日期
        dateJLabel = new JLabel();
        dateJLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        dateJLabel.setBounds(60, 10, 180, 18);
        panel.add(dateJLabel);

        // 时间
        timeJLabel = new JLabel();
        timeJLabel.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        timeJLabel.setBounds(100, 12, 196, 60);
        panel.add(timeJLabel);

        // 终止时间初始化 = 当前时间错 + 倒计时时间
        if (millis != null) {
            endTime = System.currentTimeMillis() + millis;

            // 倒计时
            countDownJLabel = new JLabel();
            countDownJLabel.setFont(new Font("微软雅黑", Font.PLAIN, 30));
            countDownJLabel.setBounds(20, 58, 10000, 72);
            panel.add(countDownJLabel);
        }

    }

    /**
     * 线程更新时间信息
     */
    @Override
    public void run() {
        while(true){
            try{
                dateJLabel.setText(DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_DAY_WEEK_CN));
                timeJLabel.setText(DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_TIME_SECOND));

                if (endTime != null) {
                    // 剩余时间毫秒数（实时计算） = 终止时间和当前时间比较
                    long endTimeCount = endTime - System.currentTimeMillis();
                    if (endTimeCount >= 0) {
                        countDownJLabel.setText(DateUtil.getTimeUnitByMillis(endTimeCount).assembleDescription(DateUtil.TimeUnitEnum.MILLIS));
                    } else {
                        countDownJLabel.setText("时间到啦，终止时间: " + DateUtil.dateFormatString(new Date(endTime), DateUtil.FORMAT_SECOND) + ", 10 秒钟之后关闭该窗口");
                        // todo 消息通知，异常提醒？
                        Thread.sleep(10 * 1000);
                        // JFrame关闭
                        dispose();
                        // 程序关闭
//                        System.exit(0);
                        break;
                    }
                }

                // 睡眠1s有几毫秒误差
//                Thread.sleep(1000);

            }catch(Throwable t){
                t.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
//        new Thread(new DemoTimePlan(null)).start();
        new Thread(new TimeJFrameUtil(6666L)).start();
//        TaskUtil.waitFirstException(TaskUtil.runAsync(() -> new DemoTimePlan(6666L)));
    }
}