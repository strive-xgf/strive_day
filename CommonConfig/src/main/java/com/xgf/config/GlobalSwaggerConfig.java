package com.xgf.config;

import com.xgf.constant.StringConstantUtil;
import com.xgf.constant.reqrep.header.RequestDeviceUtil;
import com.xgf.constant.reqrep.header.RequestVersionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xgf
 * @create 2022-04-22 23:53
 * @description swagger 配置类 开启Swagger2的自动配置
 **/


@Slf4j
@Configuration
@EnableSwagger2
public class GlobalSwaggerConfig {

    /**
     * 自定义页面实现展示效果
     * 配置 Swagger 的 Docket 的 bean 实例
     *
     * @param environment
     * @return docket实例
     */
    @Bean
    public Docket docketCustom(Environment environment){

//        return new Docket(DocumentationType.SWAGGER_2);

        // 自定义配置
        // 设置显示当前 Swagger 页面的环境【 (application.yml取值spring.profiles.active)】不满足则不显示分组选择
        Profiles profiles = Profiles.of("dev", "test", "uat", "online");
        // 获取项目的环境  通过 environment.acceptsProfiles 判断是否处在自己的设定的环境当中 (application.yml取值spring.profiles.active)
        boolean enable = environment.acceptsProfiles(profiles);

        log.info("====== SwaggerConfig enable = {}" , enable);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("swagger_0_custom")
                // enable: 是否启动Swagger 如果为false 则 Swagger 不在浏览器中显示（多个分组则不在分组中显示）
                .enable(enable)
                .select()
                // 配置扫描接口方式：只扫描类上有 @RestController 注解的方法
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                // 过滤路径 (ant指定包. any 全部, none: 无）
//                .paths(PathSelectors.ant("/com/xgf/mvc/**"))
                .build()
                .globalOperationParameters(getParameterList());
    }

    /**
     * 联系信息
     */
    private static final Contact DEFAULT_CONTACT = new Contact("strive_day","https://striveday.blog.csdn.net/","861221293@qq.com");

    /**
     * 修改 swagger 信息
     * @return
     */
    private ApiInfo apiInfo(){
        return new ApiInfo("strive_day swagger api document", "WebPage swagger Api Documentation",
                "2.0", "urn:tos", DEFAULT_CONTACT,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0");
    }

    /**
     * 【分组】配置 多个 swagger 分组，自定义页面，可在页面切换
     * 默认展示以 groupName 分组名称字符排序
     * @return 分组 docket
     */
    @Bean
    public Docket docket1(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("swagger_1");
    }

    @Bean
    public Docket docket2(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("swagger_2");
    }

    /**
     * header 参数配置方法
     */
    private List<Parameter> getParameterList() {
        List<Parameter> pars = new ArrayList<>();

        // token
        Parameter tokenParameter = new ParameterBuilder()
                .name(StringConstantUtil.TOKEN_HERDER_KEY)
                .description("token令牌")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                // false，表示 clientId 参数，是非必填
                .required(false)
                .build();
        pars.add(tokenParameter);

        // 设备端口
        Parameter deviceParameter = new ParameterBuilder()
                .name(RequestDeviceUtil.EXTEND_DEVICE_TYPE_HEADER_KEY)
                .description("device type")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                // false，表示 clientId 参数，是非必填
                .required(false)
                .build();
        pars.add(deviceParameter);

        // 访问版本号
        Parameter versionParameter = new ParameterBuilder()
                .name(RequestVersionUtil.EXTEND_VERSION_HEADER_KEY)
                .description("version")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                // false，表示 clientId 参数，是非必填
                .required(false)
                .build();
        pars.add(versionParameter);

        return pars;
    }

}
