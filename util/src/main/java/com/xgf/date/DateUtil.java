package com.xgf.date;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author strive_day
 * @create 2021-07-18 15:51
 * @description 日期时间工具类
 */
public class DateUtil {

    /**
     * yyyy-MM-dd （精确到天）
     */
    public static final String FORMAT_DAY = "yyyy-MM-dd";
    /**
     * yyyy-MM-dd HH:mm:ss （精确到秒）
     */
    public static final String FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm:ss.SSS （精确到毫秒）
     */
    public static final String FORMAT_MILL = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * yyyy年MM月dd日（精确到天 —— 中文格式）
     */
    public static final String FORMAT_DAY_CN = "yyyy年MM月dd日";
    /**
     * yyyy年MM月dd日 HH时mm分ss秒 （精确到秒 —— 中文格式）
     */
    public static final String FORMAT_SECOND_CN = "yyyy年MM月dd日 HH时mm分ss秒";
    /**
     * yyyy年MM月dd日 HH时mm分ss秒SSS毫秒 （精确到毫秒 —— 中文格式）
     */
    public static final String FORMAT_MILL_CN = "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒";

    /**
     * 默认时间解析格式：yyyy-MM-dd
     */
    public static SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DAY);

    /**
     * 默认日期常量实例
     */
    public static Calendar calendar = Calendar.getInstance();



    /**
     * 将时间转换成汉字形式输出
     * @param date Date时间
     * @return 返回转为中文后的值
     */
    public  static String getChineseDate(Date date, String format){
        if(StringUtils.isBlank(format)){
            format = FORMAT_MILL_CN;
        }
        String[] chinaNumberArray = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        StringBuilder sb = new StringBuilder();
        // 转换为时分秒格式
        String dateStr = dateFormatString(date, format);
        for(char c : dateStr.toCharArray()){
            if(c >= '0' && c <= '9'){
                sb.append(chinaNumberArray[(int)c - 48]);
            }else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * 将String类型的时间转换为Date类型（预制）
     *
     * @param time 传入的String类型时间，支持六种格式
     *             1. yyyy-MM-dd
     *             2. yyyy-MM-dd HH:mm:ss
     *             3. yyyy-MM-dd HH:mm:ss.SSS
     *             4. yyyy年MM月dd日
     *             5. yyyy年MM月dd日 HH时mm分ss秒
     *             6. yyyy年MM月dd日 HH时mm分ss秒SSS毫秒
     * @return 返回对应时间的Date对象
     */
    public static Date stringParseDate(String time) {
        // 这个匹配，复杂的往前放（比如：yyyy-MM-dd HH:mm:ss.SSS的时间格式，也能匹配yyyy-MM-dd）
        // 就散格式异常，只要能够符合前置 FORMAT_DAY 和 FORMAT_DAY_CN 的都会按DAY输出
        try {
            sdf.applyPattern(FORMAT_MILL);
            return sdf.parse(time);
        } catch (ParseException e) {
        }

        try {
            sdf.applyPattern(FORMAT_SECOND);
            return sdf.parse(time);
        } catch (ParseException e) {
        }

        try {
            sdf.applyPattern(FORMAT_DAY);
            return sdf.parse(time);
        } catch (ParseException e) {
        }

        try {
            sdf.applyPattern(FORMAT_MILL_CN);
            return sdf.parse(time);
        } catch (ParseException e) {
        }

        try {
            sdf.applyPattern(FORMAT_SECOND_CN);
            return sdf.parse(time);
        } catch (ParseException e) {
        }

        try {
            sdf.applyPattern(FORMAT_DAY_CN);
            return sdf.parse(time);
        } catch (ParseException e) {
            // 所有转换条件都不满足，抛出异常
            throw new IllegalArgumentException("类型转换异常，您输入的参数：" + time + " 不支持转换为Date");
        }
    }

    /**
     * String -> Date
     *
     * @param time   string类型时间
     * @param format 对应格式
     * @return 返回Date
     */
    public static Date stringParseDate(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据参数格式化日期
     *
     * @param date   日期时间
     * @param format 格式化参数
     * @return 格式化后日期
     */
    public static String dateFormatString(Date date, String format) {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("date param can not be null");
        }
        if (StringUtils.isBlank(format)) {
            // 默认 yyyy-MM-dd
            format = FORMAT_DAY;
        }
        sdf.applyPattern(format);
        return sdf.format(date);
    }

    /**
     * 指定日期增加年
     *
     * @param date 日期
     * @param num  增加数值（正数后推，负数前移）
     * @return 增加后的值
     */
    public static Date addYear(Date date, Integer num) {
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, num);
        return calendar.getTime();
    }

    /**
     * 指定日期增加月
     *
     * @param date 日期
     * @param num  增加数值（正数后推，负数前移）
     * @return 增加后的值
     */
    public static Date addMonth(Date date, Integer num) {
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, num);
        return calendar.getTime();
    }

    /**
     * 指定日期增加天
     *
     * @param date 日期
     * @param num  增加数值（正数后推，负数前移）
     * @return 增加后的值
     */
    public static Date addDay(Date date, Integer num) {
        calendar.setTime(date);
        calendar.add(Calendar.DATE, num);
        return calendar.getTime();
    }

    /**
     * 指定日期增加分
     *
     * @param date 日期
     * @param num  增加数值（正数后推，负数前移）
     * @return 增加后的值
     */
    public static Date addMinute(Date date, Integer num) {
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, num);
        return calendar.getTime();
    }

    /**
     * 指定日期增加秒
     *
     * @param date 日期
     * @param num  增加数值（正数后推，负数前移）
     * @return 增加后的值
     */
    public static Date addSecond(Date date, Integer num) {
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, num);
        return calendar.getTime();
    }

    /**
     * 获取当前日期在这一年的第几周
     * @param date 日期
     * @return 第几周
     */
    public static Integer getWeekOfYear(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取当前日期在这一月的第几周
     * @param date 日期
     * @return 第几周
     */
    public static Integer getWeekOfMonth(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获取当前日期在这一年的第几天
     * @param date 日期
     * @return 第几周
     */
    public static Integer getDayOfYear(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 返回两个日期相差的天数
     * @param date1 日期1
     * @param date2 日期2
     * @return 相差天数
     */
    public static Long differDays(Date date1, Date date2) {
        return Objects.isNull(date1) || Objects.isNull(date2)
                ? null
                : (Math.abs(date1.getTime() - date2.getTime()) / (60 * 60 * 24 * 1000));
    }

    /**
     * 计算两个时间之间的间隔
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回间隔时间相关值
     */
    public static TimeUnit computeIntervalTimeByString(String startTime, String endTime) {
        Date start = stringParseDate(startTime);
        Date end = stringParseDate(endTime);
        return computeIntervalTimeByDate(start, end);
    }

    /**
     * 比较两个时间的时间间隔
     * 支持Date类型数据，和符合条件的六种默认格式的String时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回间隔时间相关值
     */
    public static TimeUnit computeIntervalTimeByObject(Object startTime, Object endTime) {
        Date start = null;
        Date end = null;
        // 通过反射来对比变量类型
        if (String.class.equals(startTime.getClass())) {
            start = stringParseDate(startTime.toString());
        } else if (Date.class.equals(startTime.getClass())) {
            start = (Date) startTime;
        } else {
            throw new IllegalArgumentException("Illegal param ：" + startTime);
        }

        if (String.class.equals(endTime.getClass())) {
            end = stringParseDate(endTime.toString());
        } else if (Date.class.equals(endTime.getClass())) {
            end = (Date) endTime;
        } else {
            throw new IllegalArgumentException("Illegal param ：" + endTime);
        }
        return computeIntervalTimeByDate(start, end);
    }

    /**
     * 获取两个日期时间之间的间隔时间
     * 用java8提供的java.time包下的Duration类来实现
     * 先将Date转成java8提供的java.time.LocalDateTime，然后再使用Duration计算两个时间之间的间隔
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回间隔时间相关值
     */
    public static TimeUnit computeIntervalTimeByDate(Date startTime, Date endTime) {
        if (Objects.isNull(startTime) || Objects.isNull(endTime)) {
            throw new IllegalArgumentException("param can not be null");
        }
        if (startTime == endTime) {
            return new TimeUnit(0L, 0L, 0L, 0L, 0L, "两个时间相同");
        }

        // 比较时间先后，先的为开始时间
        if (endTime.before(startTime)) {
            Date temp = startTime;
            startTime = endTime;
            endTime = temp;
        }

        // 拼接间隔时间
        StringBuilder sb = new StringBuilder("时间间隔：");

        Instant startInstant = startTime.toInstant();
        Instant endInstant = endTime.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime start = startInstant.atZone(zoneId).toLocalDateTime();
        LocalDateTime end = endInstant.atZone(zoneId).toLocalDateTime();
        // 获取两个时间之间的间隔
        long days = Duration.between(start, end).toDays();
        long hours = Duration.between(start, end).toHours();
        long minutes = Duration.between(start, end).toMinutes();
        long seconds = Duration.between(start, end).getSeconds();
        long millis = Duration.between(start, end).toMillis();
//        long nano = Duration.between(start, end).toNanos();

        if (days > 0) {
            sb.append(days).append(" 天 ");
        }
        if (hours % 24 > 0) {
            sb.append(hours % 24).append(" 小时 ");
        }
        if (minutes % 60 > 0) {
            sb.append(minutes % 60).append(" 分钟 ");
        }
        if (seconds % 60 > 0) {
            sb.append(seconds % 60).append(" 秒 ");
        }
        if (millis % 1000 > 0) {
            sb.append(millis % 1000).append(" 毫秒 ");
        }

        if (days > 0) {
            // 天数大于0，显示对应小时分钟数值
            if (hours > 0) {
                sb.append("\n小时总数：").append(hours).append(" 小时");
            }
            if (minutes > 0) {
                sb.append("\n分钟总数：").append(minutes).append(" 分钟");
            }
            if (seconds > 0) {
                sb.append("\n秒钟总数：").append(seconds).append(" 秒");
            }
            if (millis > 0) {
                sb.append("\n毫秒总数：").append(millis).append(" 毫秒");
            }
        }
        return new TimeUnit(days, hours, minutes, seconds, millis, sb.toString());
    }

    /**
     * 获取特殊值 Date 类型认为是 null 空值的情况
     * @return 0000-01-01 00:00:00.000 的 Date 时间
     */
    public static Date getDateNullInstance() {
        try {
            return (new SimpleDateFormat(FORMAT_MILL)).parse("0000-01-01 00:00:00.000");
        } catch (ParseException var1) {
            return null;
        }
    }

    /**
     * 获取当前时间的第二天早上 6 点的时间
     */
    public static Date getNextDaySixClock() {
        Calendar result = Calendar.getInstance();
        result.setTime(DateUtil.addDay(new Date(), 1));
        result.set(Calendar.DATE, result.get(Calendar.DATE) + 1);
        result.set(Calendar.HOUR_OF_DAY, 6);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);
        result.set(Calendar.MILLISECOND, 0);
        return result.getTime();
    }


}

/**
 * 时间单位
 */
@Data
class TimeUnit {
    private Long days;
    private Long hours;
    private Long minutes;
    private Long seconds;
    private Long millis;
    /**
     * 转换为： {} 天 {} 小时 {} 分钟 {} 毫秒  格式显示
     */
    private String description;

    public TimeUnit() {
    }

    public TimeUnit(Long days, Long hours, Long minutes, Long seconds, Long millis, String description) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.millis = millis;
        this.description = description;
    }
}
