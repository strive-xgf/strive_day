package com.xgf.file.internal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author strive_day
 * @create 2023-04-08 12:32
 * @description 图片缩放工具类
 */
public class ImageScaleUtil {

    /**
     * 定义默认的最优插值算法映射表（根据图像类型自动选择最优插值算法，以获得最佳效果）
     * <p>
     * 主要是三种插值算法：
     * 1. RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR（最近邻插值算法），特点: 像素值直接采用原来最近的像素值，优势和场景: 速度快，适用于图像放大倍数不超过2倍的情况
     * 2. RenderingHints.VALUE_INTERPOLATION_BILINEAR（双线性插值算法），特点：根据周围4个像素计算新像素值， 优势和场景：速度快，适用于图像放大倍数不超过2倍的情况
     * 3. RenderingHints.VALUE_INTERPOLATION_BICUBIC（双三次插值算法）， 特点：根据周围16个像素计算新像素值，图像质量高，适用于图像放大倍数较大的情况
     */
    private static final Map<Integer, Object> DEFAULT_INTERPOLATION_MAP = new HashMap<>();

    static {
        // 将 BufferedImage.TYPE_BYTE_GRAY（表示一个 8 位灰度图像，每个像素值存储在 8 位无符号字节中） 类型的图像对应的最优插值算法设置为 NEAREST_NEIGHBOR（最近邻插值）
        DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_BYTE_GRAY, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        // 将 BufferedImage.TYPE_USHORT_GRAY（表示一个 16 位灰度图像，每个像素值存储在 16 位无符号 short 类型中） 类型的图像对应的最优插值算法设置为 NEAREST_NEIGHBOR（最近邻插值）
        DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_USHORT_GRAY, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        // 将 BufferedImage.TYPE_3BYTE_BGR（表示一个 8 位 BGR 颜色分量的图像，颜色存储在 24 位字节数组中） 类型的图像对应的最优插值算法设置为 BILINEAR（双线性插值）
        DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_3BYTE_BGR, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // 将 BufferedImage.TYPE_4BYTE_ABGR（表示一个 8 位 ABGR 颜色分量的图像，颜色存储在 32 位字节数组中） 类型的图像对应的最优插值算法设置为 BILINEAR（双线性插值）
        DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_4BYTE_ABGR, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // 将 BufferedImage.TYPE_INT_ARGB（表示一个 8 位 RGBA 颜色分量的图像，颜色存储在 32 位整数中） 类型的图像对应的最优插值算法设置为 BICUBIC（双三次插值）
        DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_INT_ARGB, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        // 将 BufferedImage.TYPE_INT_RGB（表示一个 8 位 RGB 颜色分量的图像，颜色存储在 32 位整数中） 类型的图像对应的最优插值算法设置为 BICUBIC（双三次插值）
        DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_INT_RGB, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        // 将 BufferedImage.TYPE_CUSTOM（表示一个自定义类型的图像，其颜色模型由开发人员指定） 类型的图像对应的最优插值算法设置为 BICUBIC（双三次插值）
        DEFAULT_INTERPOLATION_MAP.put(BufferedImage.TYPE_CUSTOM, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    /**
     * 对图像进行最优缩放，返回缩放后的图片
     *
     * @param sourceImg 原始图像 BufferedImage
     * @param width     缩放后的宽度
     * @param height    缩放后的高度
     * @return 缩放后的图像 BufferedImage
     */
    public static BufferedImage scale(BufferedImage sourceImg, int width, int height) {
        // 根据图像类型选择最优插值算法，默认为 RenderingHints.VALUE_INTERPOLATION_BICUBICC（双三次插值）
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                DEFAULT_INTERPOLATION_MAP.getOrDefault(sourceImg.getType(), RenderingHints.VALUE_INTERPOLATION_BICUBIC));

        // 创建 BufferedImage，对图片宽高进行缩放，并使用原始图像相同的颜色模型，参数分别为：图像宽度、图像高度、图像的颜色模型类型（这里使用原图像类型）
        BufferedImage scaledImage = new BufferedImage(width, height, sourceImg.getType());

        // 创建缩放图像上进行绘制操作的 Graphics2D 对象（图形上下文）
        Graphics2D graphics2D = scaledImage.createGraphics();
        try {
            // 设置 最优插值算法
            graphics2D.setRenderingHints(renderingHints);
            // 缩放裁剪图片，将指定的图像绘制在Graphics2D对象的当前坐标系中，在目标矩形区域内进行缩放和裁剪。
            // 就是：如果目标矩形区域和源图像的宽高比不同，则源图像将按照目标区域的宽高比例进行缩放。如果目标矩形区域超出了源图像的边界，则会进行裁剪
            // observer为null，表示使用默认的图像观察者（java.awt.image.ImageObserver）
            graphics2D.drawImage(sourceImg, 0, 0, width, height, null);
        } finally {
            // 释放 Graphics2D 资源
            graphics2D.dispose();
        }

        // 返回缩放完成后的图片内容
        return scaledImage;
    }

}
