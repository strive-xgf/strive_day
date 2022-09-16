package com.xgf.runutil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xgf.common.LogUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.date.DateUtil;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.file.FileUtil;
import com.xgf.mq.rabbitmq.RabbitMqCommonUtil;
import com.xgf.system.SystemUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xgf
 * @create 2022-09-10 13:02
 * @description 目标任务计划工具
 */


public class TargetPlanTimeUtil {

    private final static transient Logger log = LoggerFactory.getLogger(TargetPlanTimeUtil.class);

    /**
     * 关闭退出（全局退出程序使用）【String类型】
     */
    private static final String EXIT_STR = "exit_finishPlan_2e6288aabb736a29baf928b189fb71c6";

    /**
     * 关闭退出（全局退出程序使用）【int类型】
     */
    private static final Integer EXIT_INT = -999999;

    /**
     * 最后一次执行统计结果
     */
    private static String afterOnceResultStr = "";


    public static void main(String[] args) throws Exception {

        executePlanTime();

    }






    public static void executePlanTime() throws Exception{

        // JVM 程序中断保存日志
        executeJvmExitHook();

        // 创建连接
        Connection connection = RabbitMqCommonUtil.createChannelDefault();
        //通过Connection 创建一个 Channel通道/频道
        Channel channel = connection.createChannel();

        //日期时间格式化
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_SECOND);
        // 程序执行开始时间
        Date runInitTime = new Date();
        // 当前程序执行过程中，用户输入的计划耗时统计（任务计划的耗时统计）
        int totalPlanTime = 0;
        // 执行次数统计
        int executeCount = 0;
        // 每次执行结果保存（用于弹框展示）
        StringBuilder allExecuteResultSb = new StringBuilder();
        // 当前任务目标 / 描述
        String inputTargetStr = getInputTarget();
        // 获取页面输入目标计时时间值
        int inputTargetTime = 0;

        if (!EXIT_STR.equalsIgnoreCase(inputTargetStr)) {
            // 不退出，输入时间
            inputTargetTime = getInputTargetTime();
        }

        // 记录开始使用运行时间
        saveLog("########## 开始使用plan time，目标: " + inputTargetStr + ", 时间: " + sdf.format(runInitTime) + "\n");

        while (!EXIT_STR.equalsIgnoreCase(inputTargetStr)
                && inputTargetTime != EXIT_INT) {
            // 每次循环执行开始时间（包含非任务时间）
            Date onceStartTime = new Date();
            System.out.println("====== execute inputTargetStr " + inputTargetStr + ", inputTargetTime = " + inputTargetTime);

            // 异步线程调用倒计时时钟（实时倒计时面板）
            new Thread(new TimeJFrameUtil(inputTargetTime * 1000L)).start();
            // 休眠
            Thread.sleep(inputTargetTime * 1000L);
            // todo 时间到了转发消息，钉钉/短信通知

            // 每次循环执行结束时间（包含非任务时间）
            Date onceEndTime = new Date();

            // 执行次数+1
            executeCount++;
            // 共使用时间【最后一次执行结束时间 - 程序运行开始时间】
            long totalUseTime = (onceEndTime.getTime() - runInitTime.getTime()) / 1000;
            // 使用计时增加（单位: 秒）
            totalPlanTime += inputTargetTime;

            // 单次统计结果
            afterOnceResultStr = "\n开始使用时间: " + sdf.format(runInitTime) +
                    "\n最后一次结束使用时间: " + sdf.format(onceEndTime) +
                    "\n使用本工具的时间: " + DateUtil.getDayHourMinuteSecondBySecond(totalUseTime) +
                    "\n共利用计时: " + DateUtil.getDayHourMinuteSecondBySecond((long) totalPlanTime) +
                    "\n时间利用率: " + String.format("%.2f", ((double) totalPlanTime / (double) totalUseTime) * 100) + "%";

            // 获取完成百分比
            String selectPercent = getSelectPercent("请输入【" + inputTargetStr + "】完成进度（百分比数）: ");

            // 本次记录
            String current = "第 " + executeCount + " 次计时，开始时间: " + sdf.format(onceStartTime) + "，结束时间: " + sdf.format(onceEndTime) +
                    "，耗时: " + inputTargetTime + " 秒，目标: 【" + inputTargetStr + "】，完成度: " + selectPercent + "\n";
            // 加入历史归档记录中
            allExecuteResultSb.append(current);
            // 记录到文件中
            // todo 存储 excel
            saveLog(current);

            if (StringUtils.isEmpty(selectPercent)) {
                // 输入百分比的时候选择退出返回null值（在保存文件后返回）
                break;
            }

            // 展示内容
            String showPanel = "<html>当前为第 <font color='red'>" + executeCount + " </font>次计时，开始时间: <font color='red'>" + sdf.format(onceStartTime) + "</font>，结束时间: <font color='red'>" + sdf.format(onceEndTime) + "</font></html>" +
                    "\n>>>>>>本次计时时间: " + inputTargetTime + "秒，目标: " + inputTargetStr + "，完成度: " + selectPercent + "\n" +
                    "\n======历史记录======\n" +
                    allExecuteResultSb +
                    "\n==================" +
                    afterOnceResultStr +
                    "\n是否需要延迟？";

            // 展示面板
            int confirm = getYesOrNoShowPlane(showPanel, "时间到啦");
            if (confirm == EXIT_INT) {
                // 设为 EXIT_INT 退出循环
                inputTargetTime = EXIT_INT;
            } else if (confirm == 1) {
                // 1: 否，延迟继续
                inputTargetStr = getInputTarget();
                inputTargetTime = getInputTargetTime();
            } else {
                // 0: 是
                inputTargetTime = getInputTargetTime();
            }
        }

        //记录统计结果
        // todo
        saveLog("\n============================" + afterOnceResultStr + "\n============================\n\n");
//        //关闭相关的连接
        channel.close();
        connection.close();
    }


    /**
     * JVM 关闭执行钩子函数，保存最后一次很自信记录
     */
    private static void executeJvmExitHook() {
        // jvm关闭线程执行钩子函数
        Thread threadStopExecute = new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                if (StringUtils.isNotBlank(afterOnceResultStr)) {
                    // 记录统计结果
                    saveLog("\n============================  threadExit ============================ " + afterOnceResultStr + "\n============================ \n\n");
                } else {
                    saveLog("\n**************************** 开启后直接退出 ****************************\n\n");
                }
            }
        };

        // jvm关闭的时候先执行该线程钩子
        // 这个方法的意思就是在jvm中增加一个关闭的钩子，当jvm关闭的时候，
        // 会执行系统中已经设置的所有通过方法addShutdownHook添加的钩子，当系统执行完这些钩子后，jvm才会关闭。所以这些钩子可以在jvm关闭的时候进行内存清理、对象销毁等操作。
        Runtime.getRuntime().addShutdownHook(threadStopExecute);
    }


    private static int getInputTargetTime() {
        return getInputTargetTime(null);
    }
    
    /**
     * 输入目标计划时间
     *
     * @param message 提示信息
     * @return 获取解析输入框的输入时间值
     */
    private static int getInputTargetTime(String message) {
        message = StringUtils.defaultString(message, "请输入您需要计时的时间（单位/秒)【支持运算表达式】\n");

        // 输入选择框，返回你的输入值
        String targetTimeStr = JOptionPane.showInputDialog(null, message, "输入时间（单位/秒）", JOptionPane.PLAIN_MESSAGE);

        // 点击取消，返回null
        if (targetTimeStr == null) {
            if (judgeExit()) {
                // 关闭退出
                return EXIT_INT;
            }
            // 不关闭，继续输入
            return getInputTargetTime();
        }

        // 表达式/时间的 double 结果
        double expressionResult;
        try {
            // 时间转换
            expressionResult = getExpressionResult(targetTimeStr);
        } catch (Exception exception) {
            LogUtil.warn(">>>>>> exception 输入非法字符异常，inputTime = " + targetTimeStr);
            // 错误提示框
//            JOptionPane.showMessageDialog(null, "您输入的时间有误，请输入数字。", "输入错误", JOptionPane.ERROR_MESSAGE);
            // 再调用方法，并返回输入值转换，展示提示信息
            return getInputTargetTime("<html>您输入的时间: [ <font color='red'>" + targetTimeStr + "</font> ] 格式错误，请重新输入【支持运算表达式，单位/秒】: <html>");
        }

        if (expressionResult < 1) {
            LogUtil.warn(">>>>>> exception 输入时间小于1，inputTime = " + targetTimeStr);
            // 警告提示框
//            JOptionPane.showMessageDialog(null, "您输入的时间不能小于1，请重新输入", "输入错误", JOptionPane.WARNING_MESSAGE);
            // 再调用方法，并返回输入值转换
            return getInputTargetTime("<html>您输入的时间: [ <font color='red'>" + targetTimeStr + " = " + expressionResult + "</font> ] 长度小于1s，请重新输入【支持运算表达式，单位/秒】: <html>");
        }

        System.out.println("====== 目标执行时间 : " + targetTimeStr);
        return (int) expressionResult;
    }


    /**
     * 输入算术表达式的字符串，计算出结果返回【ScriptEngineManager】
     *
     * @param expression 输入的算术表达式字符串
     * @return 返回运算结果
     */
    private static Double getExpressionResult(String expression) {

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension("js");
        Bindings bindings = engine.createBindings();

        bindings.put("expression", expression);
        Object expressionResult;
        double result;
        try {
            expressionResult = engine.eval("eval(expression)", bindings);
            if (expressionResult instanceof Integer) {
                //转为Double类型
                result = ((Integer) expressionResult).doubleValue();
            } else if (expressionResult instanceof Double) {
                result = (Double) expressionResult;
            } else {
                throw CustomExceptionEnum.DATA_CONVERT_EXCEPTION.generateCustomMessageException("计算结果非int、double类型");
            }
            System.out.println(expression + " = " + result);
            return result;
        } catch (ScriptException e) {
            System.err.println("输入算术表达式错误，" + e.getMessage());
            throw CustomExceptionEnum.DATA_CONVERT_EXCEPTION.generateCustomMessageException("输入算术表达式错误: " + e.getLocalizedMessage());
        }
    }


    private static String getInputTarget() {
        return getInputTarget(null);
    }

    /**
     * 输入计划内容
     *
     * @param message 提示信息
     * @return 目标
     */
    private static String getInputTarget(String message) {
        message = StringUtils.defaultString(message, "请输入您本次目标/计划\n");

        // 输入选择框，返回你的输入值
        String targetStr = JOptionPane.showInputDialog(null, message, "计划信息描述", JOptionPane.PLAIN_MESSAGE);
        // 点击X关闭，或者取消按钮，返回null
        if (targetStr == null) {
            if (judgeExit()) {
                // 退出直接返回（EXIT: 退出使用）
                return EXIT_STR;
            }
            // 不关闭，继续输入
            return getInputTarget();
        }

        if (StringUtils.isBlank(targetStr)) {
            return getInputTarget("目标/计划不能为空（或空字符串），请重新输入请输入您本次目标/计划\n");
        }

        System.out.println("执行目标: " + targetStr);
        return targetStr;
    }


    /**
     * 将指定内容保存到指定的路径文件下
     *
     * @param str 需要存储的内容
     */
    private static void saveLog(String str) {
        saveLog(str, null);
    }

    /**
     * 将指定内容保存到指定的路径文件下
     *
     * @param content 需要存储的内容
     * @param path    存储的路径
     */
    private static void saveLog(String content, String path) {

        String saveContent = "save Log >>【" + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_MILL) + "】\n\t" + content;

        // 文件目录为空，则取当前项目路径
        FileUtil.fileAppendData(
                StringUtils.defaultString(path, SystemUtil.getCurrentDir() + SystemUtil.getFileSeparator() + "studyPlan" + SystemUtil.getFileSeparator() + "studyLog.txt"),
                saveContent);
    }


    /**
     * 完成百分比选择
     *
     * @param message 提示框提示消息
     * @return 返回输入的百分比数值
     */
    private static String getSelectPercent(String message) {
        // 提示信息
        message = StringUtils.defaultString(message, "请输入目标完成进度（百分比数）: \n");

//        Object[] percentList = {"0%", "10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"};
//        // 下拉框
//        String percentStr = (String) JOptionPane.showInputDialog(null, message, "完成百分比",
//        JOptionPane.PLAIN_MESSAGE, new ImageIcon("icon.png"), percentList, "百分比");

        String percentStr = JOptionPane.showInputDialog(null, message, "完成百分比", JOptionPane.PLAIN_MESSAGE);

        // 点击X关闭，或者取消按钮，返回null，返回空的时候弹框，是否退出
        if (percentStr == null) {
            if (judgeExit()) {
                // 关闭退出
                return null;
            }
            // 为空，不关闭，则继续输入完成百分比
            return getSelectPercent(null);
        }

        if (StringConstantUtil.checkStrIsFloatNumber(percentStr)) {
            // 匹配浮点数（正常float）返回百分比数
            return percentStr + "%";

        } else if (StringConstantUtil.checkStrIsPercentNumber(percentStr)) {
            // 匹配百分数，直接返回
            return percentStr;

        }

        // 输入非百分比数或浮点数，重新输入
        return getSelectPercent("<html>您输入的完成百分比数:  [  <font color='red'>" + percentStr + "</font>  ] 有误，请重新输入（百分数)</html>");
    }


    /**
     * 是否退出使用判断弹框
     *
     * @return true: 退出，false: 继续使用
     */
    private static boolean judgeExit() {
        // 选择按钮（两个: 是/否；返回1或0）0代表是，1代表否, -1点x关闭
        int confirmFlag = JOptionPane.showConfirmDialog(null, "是否退出使用？", "结束提示", JOptionPane.YES_NO_OPTION);
        if (confirmFlag == 0 || confirmFlag == -1) {
            //普通提示框
            JOptionPane.showMessageDialog(null, "感谢您的使用欢迎您下次继续使用", "感谢提示", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    /**
     * 获取展示面板（是/否）
     *
     * @param message 内容
     * @param title 标题
     * @return 1 : 否, 0 : 是, EXIT_INT : 退出x
     */
    private static int getYesOrNoShowPlane(String message, String title) {

        int confirm = -1;

        // 循环直到退出或继续使用
        while (confirm == -1) {
            // 1 : 否, 0 : 是, -1 : 退出x
            confirm = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
            if (confirm == -1) {
                if (judgeExit()) {
                    // 退出
                    return EXIT_INT;
                }
                // 继续展示弹框判断
                getYesOrNoShowPlane(message, title);
            }
        }

        return confirm;
    }

}

