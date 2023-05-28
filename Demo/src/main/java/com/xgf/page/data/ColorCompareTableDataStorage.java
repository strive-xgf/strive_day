package com.xgf.page.data;

import com.xgf.page.bean.resp.ColorCompareRespDTO;
import com.xgf.system.SystemUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author strive_day
 * @create 2023-05-28 20:41
 * @description 颜色对照表，临时数据存放
 */

public class ColorCompareTableDataStorage {

    public static List<ColorCompareRespDTO> colorCompareRespDTOList;


    static {
        colorCompareRespDTOList = Arrays.asList(
                ColorCompareRespDTO.valueOf("black", "纯黑", "#000000", 0, 0, 0),
                ColorCompareRespDTO.valueOf("navy", "海军蓝", "#000080", 0, 0, 128),
                ColorCompareRespDTO.valueOf("darkblue", "暗蓝色", "#00008b", 0, 0, 139),
                ColorCompareRespDTO.valueOf("mediumblue", "中蓝色", "#0000cd", 0, 0, 205),
                ColorCompareRespDTO.valueOf("blue", "纯蓝", "#0000ff", 0, 0, 255),
                ColorCompareRespDTO.valueOf("darkgreen", "暗绿色", "#006400", 0, 100, 0),
                ColorCompareRespDTO.valueOf("green", "纯绿", "#008000", 0, 128, 0),
                ColorCompareRespDTO.valueOf("teal", "水鸭色", "#008080", 0, 128, 128),
                ColorCompareRespDTO.valueOf("darkcyan", "暗青色", "#008b8b", 0, 139, 139),
                ColorCompareRespDTO.valueOf("deepskyblue", "深天蓝", "#00bfff", 0, 191, 255),
                ColorCompareRespDTO.valueOf("darkturquoise", "暗绿宝石", "#00ced1", 0, 206, 209),
                ColorCompareRespDTO.valueOf("mediumspringgreen", "中春绿色", "#00fa9a", 0, 250, 154),
                ColorCompareRespDTO.valueOf("lime", "闪光绿", "#00ff00", 0, 255, 0),
                ColorCompareRespDTO.valueOf("springgreen", "春绿色", "#00ff7f", 0, 255, 127),
                ColorCompareRespDTO.valueOf("cyan", "青色", "#00ffff", 0, 255, 255),
                ColorCompareRespDTO.valueOf("aqua", "浅绿色(水色)", "#00ffff", 0, 255, 255),
                ColorCompareRespDTO.valueOf("midnightblue", "午夜蓝", "#191970", 25, 25, 112),
                ColorCompareRespDTO.valueOf("dodgerblue", "闪兰色(道奇蓝)", "#1e90ff", 30, 144, 255),
                ColorCompareRespDTO.valueOf("lightseagreen", "浅海洋绿", "#20b2aa", 32, 178, 170),
                ColorCompareRespDTO.valueOf("forestgreen", "森林绿", "#228b22", 34, 139, 34),
                ColorCompareRespDTO.valueOf("seagreen", "海洋绿", "#2e8b57", 46, 139, 87),
                ColorCompareRespDTO.valueOf("darkslategray", "暗瓦灰色(暗石板灰)", "#2f4f4f", 47, 79, 79),
                ColorCompareRespDTO.valueOf("limegreen", "闪光深绿", "#32cd32", 50, 205, 50),
                ColorCompareRespDTO.valueOf("mediumseagreen", "中海洋绿", "#3cb371", 60, 179, 113),
                ColorCompareRespDTO.valueOf("turquoise", "绿宝石", "#40e0d0", 64, 224, 208),
                ColorCompareRespDTO.valueOf("royalblue", "皇家蓝/宝蓝", "#4169e1", 65, 105, 225),
                ColorCompareRespDTO.valueOf("steelblue", "钢蓝/铁青", "#4682b4", 70, 130, 180),
                ColorCompareRespDTO.valueOf("darkslateblue", "暗灰蓝色(暗板岩蓝)", "#483d8b", 72, 61, 139),
                ColorCompareRespDTO.valueOf("mediumturquoise", "中绿宝石", "#48d1cc", 72, 209, 204),
                ColorCompareRespDTO.valueOf("indigo", "靛青/紫兰色", "#4b0082", 75, 0, 130),
                ColorCompareRespDTO.valueOf("darkolivegreen", "暗橄榄绿", "#556b2f", 85, 107, 47),
                ColorCompareRespDTO.valueOf("cadetblue", "军兰色(军服蓝)", "#5f9ea0", 95, 158, 160),
                ColorCompareRespDTO.valueOf("cornflowerblue", "矢车菊蓝", "#6495ed", 100, 149, 237),
                ColorCompareRespDTO.valueOf("mediumaquamarine", "中宝石碧绿", "#66cdaa", 102, 205, 170),
                ColorCompareRespDTO.valueOf("dimgray", "暗淡的灰色", "#696969", 105, 105, 105),
                ColorCompareRespDTO.valueOf("slateblue", "石蓝色(板岩蓝)", "#6a5acd", 106, 90, 205),
                ColorCompareRespDTO.valueOf("olivedrab", "橄榄褐色", "#6b8e23", 107, 142, 35),
                ColorCompareRespDTO.valueOf("slategray", "灰石色(石板灰)", "#708090", 112, 128, 144),
                ColorCompareRespDTO.valueOf("lightslategray", "亮蓝灰(亮石板灰)", "#778899", 119, 136, 153),
                ColorCompareRespDTO.valueOf("mediumslateblue", "中暗蓝色(中板岩蓝)", "#7b68ee", 123, 104, 238),
                ColorCompareRespDTO.valueOf("lawngreen", "草绿色(草坪绿)", "#7cfc00", 124, 252, 0),
                ColorCompareRespDTO.valueOf("chartreuse", "黄绿色(查特酒绿)", "#7fff00", 127, 255, 0),
                ColorCompareRespDTO.valueOf("aquamarine", "宝石碧绿", "#7fffd4", 127, 255, 212),
                ColorCompareRespDTO.valueOf("maroon", "栗色", "#800000", 128, 0, 0),
                ColorCompareRespDTO.valueOf("purple", "紫色", "#800080", 128, 0, 128),
                ColorCompareRespDTO.valueOf("olive", "橄榄", "#808000", 128, 128, 0),
                ColorCompareRespDTO.valueOf("gray", "灰色", "#808080", 128, 128, 128),
                ColorCompareRespDTO.valueOf("skyblue", "天蓝色", "#87ceeb", 135, 206, 235),
                ColorCompareRespDTO.valueOf("lightskyblue", "亮天蓝色", "#87cefa", 135, 206, 250),
                ColorCompareRespDTO.valueOf("blueviolet", "蓝紫罗兰", "#8a2be2", 138, 43, 226),
                ColorCompareRespDTO.valueOf("darkred", "深红色", "#8b0000", 139, 0, 0),
                ColorCompareRespDTO.valueOf("darkmagenta", "深洋红", "#8b008b", 139, 0, 139),
                ColorCompareRespDTO.valueOf("saddlebrown", "重褐色(马鞍棕色)", "#8b4513", 139, 69, 19),
                ColorCompareRespDTO.valueOf("darkseagreen", "暗海洋绿", "#8fbc8f", 143, 188, 143),
                ColorCompareRespDTO.valueOf("lightgreen", "淡绿色", "#90ee90", 144, 238, 144),
                ColorCompareRespDTO.valueOf("mediumpurple", "中紫色", "#9370db", 147, 112, 219),
                ColorCompareRespDTO.valueOf("darkviolet", "暗紫罗兰", "#9400d3", 148, 0, 211),
                ColorCompareRespDTO.valueOf("palegreen", "弱绿色", "#98fb98", 152, 251, 152),
                ColorCompareRespDTO.valueOf("darkorchid", "暗兰花紫", "#9932cc", 153, 50, 204),
                ColorCompareRespDTO.valueOf("yellowgreen", "黄绿色", "#9acd32", 154, 205, 50),
                ColorCompareRespDTO.valueOf("sienna", "黄土赭色", "#a0522d", 160, 82, 45),
                ColorCompareRespDTO.valueOf("brown", "棕色", "#a52a2a", 165, 42, 42),
                ColorCompareRespDTO.valueOf("darkgray", "深灰色", "#a9a9a9", 169, 169, 169),
                ColorCompareRespDTO.valueOf("lightblue", "亮蓝", "#add8e6", 173, 216, 230),
                ColorCompareRespDTO.valueOf("greenyellow", "绿黄色", "#adff2f", 173, 255, 47),
                ColorCompareRespDTO.valueOf("paleturquoise", "弱绿宝石", "#afeeee", 175, 238, 238),
                ColorCompareRespDTO.valueOf("lightsteelblue", "亮钢蓝", "#b0c4de", 176, 196, 222),
                ColorCompareRespDTO.valueOf("powderblue", "粉蓝色(火药青)", "#b0e0e6", 176, 224, 230),
                ColorCompareRespDTO.valueOf("firebrick", "火砖色(耐火砖)", "#b22222", 178, 34, 34),
                ColorCompareRespDTO.valueOf("darkgoldenrod", "暗金菊黄", "#b8860b", 184, 134, 11),
                ColorCompareRespDTO.valueOf("mediumorchid", "中兰花紫", "#ba55d3", 186, 85, 211),
                ColorCompareRespDTO.valueOf("rosybrown", "玫瑰棕色", "#bc8f8f", 188, 143, 143),
                ColorCompareRespDTO.valueOf("darkkhaki", "暗黄褐色(深卡叽布)", "#bdb76b", 189, 183, 107),
                ColorCompareRespDTO.valueOf("silver", "银灰色", "#c0c0c0", 192, 192, 192),
                ColorCompareRespDTO.valueOf("mediumvioletred", "中紫罗兰红", "#c71585", 199, 21, 133),
                ColorCompareRespDTO.valueOf("indianred", "印度红", "#cd5c5c", 205, 92, 92),
                ColorCompareRespDTO.valueOf("peru", "秘鲁色", "#cd853f", 205, 133, 63),
                ColorCompareRespDTO.valueOf("chocolate", "巧克力色", "#d2691e", 210, 105, 30),
                ColorCompareRespDTO.valueOf("tan", "茶色", "#d2b48c", 210, 180, 140),
                ColorCompareRespDTO.valueOf("lightgrey", "浅灰色", "#d3d3d3", 211, 211, 211),
                ColorCompareRespDTO.valueOf("thistle", "蓟色", "#d8bfd8", 216, 191, 216),
                ColorCompareRespDTO.valueOf("orchid", "暗紫色(兰花紫)", "#da70d6", 218, 112, 214),
                ColorCompareRespDTO.valueOf("goldenrod", "金菊黄", "#daa520", 218, 165, 32),
                ColorCompareRespDTO.valueOf("palevioletred", "弱紫罗兰红", "#db7093", 219, 112, 147),
                ColorCompareRespDTO.valueOf("crimson", "深红(猩红)", "#dc143c", 220, 20, 60),
                ColorCompareRespDTO.valueOf("gainsboro", "淡灰色(庚斯博罗灰)", "#dcdcdc", 220, 220, 220),
                ColorCompareRespDTO.valueOf("plum", "洋李色(李子紫)", "#dda0dd", 221, 160, 221),
                ColorCompareRespDTO.valueOf("burlywood", "硬木色", "#deb887", 222, 184, 135),
                ColorCompareRespDTO.valueOf("lightcyan", "淡青色", "#e0ffff", 224, 255, 255),
                ColorCompareRespDTO.valueOf("lavender", "淡紫色(熏衣草淡紫)", "#e6e6fa", 230, 230, 250),
                ColorCompareRespDTO.valueOf("darksalmon", "深鲜肉/鲑鱼色", "#e9967a", 233, 150, 122),
                ColorCompareRespDTO.valueOf("violet", "紫罗兰", "#ee82ee", 238, 130, 238),
                ColorCompareRespDTO.valueOf("palegoldenrod", "灰菊黄(苍麒麟色)", "#eee8aa", 238, 232, 170),
                ColorCompareRespDTO.valueOf("lightcoral", "淡珊瑚色", "#f08080", 240, 128, 128),
                ColorCompareRespDTO.valueOf("khaki", "黄褐色(卡叽布)", "#f0e68c", 240, 230, 140),
                ColorCompareRespDTO.valueOf("aliceblue", "爱丽丝蓝", "#f0f8ff", 240, 248, 255),
                ColorCompareRespDTO.valueOf("honeydew", "蜜色(蜜瓜色)", "#f0fff0", 240, 255, 240),
                ColorCompareRespDTO.valueOf("azure", "蔚蓝色", "#f0ffff", 240, 255, 255),
                ColorCompareRespDTO.valueOf("sandybrown", "沙棕色", "#f4a460", 244, 164, 96),
                ColorCompareRespDTO.valueOf("wheat", "浅黄色(小麦色)", "#f5deb3", 245, 222, 179),
                ColorCompareRespDTO.valueOf("beige", "米色/灰棕色", "#f5f5dc", 245, 245, 220),
                ColorCompareRespDTO.valueOf("whitesmoke", "白烟", "#f5f5f5", 245, 245, 245),
                ColorCompareRespDTO.valueOf("mintcream", "薄荷奶油", "#f5fffa", 245, 255, 250),
                ColorCompareRespDTO.valueOf("ghostwhite", "幽灵白", "#f8f8ff", 248, 248, 255),
                ColorCompareRespDTO.valueOf("salmon", "鲜肉/鲑鱼色", "#fa8072", 250, 128, 114),
                ColorCompareRespDTO.valueOf("antiquewhite", "古董白", "#faebd7", 250, 235, 215),
                ColorCompareRespDTO.valueOf("linen", "亚麻布", "#faf0e6", 250, 240, 230),
                ColorCompareRespDTO.valueOf("lightgoldenrodyellow", "亮菊黄", "#fafad2", 250, 250, 210),
                ColorCompareRespDTO.valueOf("oldlace", "老花色(旧蕾丝)", "#fdf5e6", 253, 245, 230),
                ColorCompareRespDTO.valueOf("red", "纯红", "#ff0000", 255, 0, 0),
                ColorCompareRespDTO.valueOf("magenta", "洋红(玫瑰红)", "#ff00ff", 255, 0, 255),
                ColorCompareRespDTO.valueOf("fuchsia", "紫红(灯笼海棠)", "#ff00ff", 255, 0, 255),
                ColorCompareRespDTO.valueOf("deeppink", "深粉红", "#ff1493", 255, 20, 147),
                ColorCompareRespDTO.valueOf("orangered", "橙红色", "#ff4500", 255, 69, 0),
                ColorCompareRespDTO.valueOf("tomato", "番茄红", "#ff6347", 255, 99, 71),
                ColorCompareRespDTO.valueOf("hotpink", "热情的粉红", "#ff69b4", 255, 105, 180),
                ColorCompareRespDTO.valueOf("coral", "珊瑚", "#ff7f50", 255, 127, 80),
                ColorCompareRespDTO.valueOf("darkorange", "深橙色", "#ff8c00", 255, 140, 0),
                ColorCompareRespDTO.valueOf("lightsalmon", "浅鲑鱼肉色", "#ffa07a", 255, 160, 122),
                ColorCompareRespDTO.valueOf("orange", "橙色", "#ffa500", 255, 165, 0),
                ColorCompareRespDTO.valueOf("lightpink", "浅粉红", "#ffb6c1", 255, 182, 193),
                ColorCompareRespDTO.valueOf("pink", "粉红", "#ffc0cb", 255, 192, 203),
                ColorCompareRespDTO.valueOf("gold", "金色", "#ffd700", 255, 215, 0),
                ColorCompareRespDTO.valueOf("peachpuff", "桃肉色", "#ffdab9", 255, 218, 185),
                ColorCompareRespDTO.valueOf("navajowhite", "纳瓦白(土著白)", "#ffdead", 255, 222, 173),
                ColorCompareRespDTO.valueOf("moccasin", "鹿皮色(鹿皮靴)", "#ffe4b5", 255, 228, 181),
                ColorCompareRespDTO.valueOf("bisque", "陶坯黄", "#ffe4c4", 255, 228, 196),
                ColorCompareRespDTO.valueOf("mistyrose", "浅玫瑰色(薄雾玫瑰)", "#ffe4e1", 255, 228, 225),
                ColorCompareRespDTO.valueOf("blanchedalmond", "白杏色", "#ffebcd", 255, 235, 205),
                ColorCompareRespDTO.valueOf("papayawhip", "番木色(番木瓜)", "#ffefd5", 255, 239, 213),
                ColorCompareRespDTO.valueOf("lavenderblush", "淡紫红", "#fff0f5", 255, 240, 245),
                ColorCompareRespDTO.valueOf("seashell", "海贝壳", "#fff5ee", 255, 245, 238),
                ColorCompareRespDTO.valueOf("cornsilk", "玉米丝色", "#fff8dc", 255, 248, 220),
                ColorCompareRespDTO.valueOf("lemonchiffon", "柠檬绸", "#fffacd", 255, 250, 205),
                ColorCompareRespDTO.valueOf("floralwhite", "花的白色", "#fffaf0", 255, 250, 240),
                ColorCompareRespDTO.valueOf("snow", "雪白色", "#fffafa", 255, 250, 250),
                ColorCompareRespDTO.valueOf("yellow", "纯黄", "#ffff00", 255, 255, 0),
                ColorCompareRespDTO.valueOf("lightyellow", "浅黄色", "#ffffe0", 255, 255, 224),
                ColorCompareRespDTO.valueOf("ivory", "象牙色", "#fffff0", 255, 255, 240),
                ColorCompareRespDTO.valueOf("white", "纯白", "#ffffff", 255, 255, 255)
        );
    }

    public static void main(String[] args) {
        // 基于 rgb 值排序，转换为数据写入
        colorCompareRespDTOList.sort(
                Comparator.comparing(ColorCompareRespDTO::getColorR, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(ColorCompareRespDTO::getColorG, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(ColorCompareRespDTO::getColorB, Comparator.nullsLast(Comparator.naturalOrder())));

        StringBuilder sb = new StringBuilder();
        colorCompareRespDTOList.forEach(p ->
                sb.append("ColorCompareRespDTO.valueOf(\"")
                        .append(p.getColorEnName())
                        .append("\", \"")
                        .append(p.getColorCnName())
                        .append("\", \"").append(p.getColorValue())
                        .append("\", ").append(p.getColorR()).append(", ")
                        .append(p.getColorG()).append(", ")
                        .append(p.getColorB()).append("),")
                        .append(SystemUtil.getLineSeparator()));

        System.out.println(sb.toString());

    }


}
