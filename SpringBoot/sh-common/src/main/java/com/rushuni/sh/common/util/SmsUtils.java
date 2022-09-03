package com.rushuni.sh.common.util;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Marshall
 * @date 2022/8/8
 */
public class SmsUtils {
    /**
     * 预约模板
     */
    public static final String TEMPLATE_CODE_1 = "SMS_154950909";
    /**
     * 预约成功模板
     */
    public static final String TEMPLATE_CODE_2 = "SMS_154950909";

    /**
     * 发送短信
     *
     * @param templateCode
     * @param phoneNumbers
     * @param templateParam
     * @throws ExecutionException
     */
    public static String sendShortMessage(String templateCode, String phoneNumbers, String templateParam) {
        // Configure Credentials authentication information, including ak, secret, token
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId("youacceskey")
                .accessKeySecret("youacceskey")
                //.securityToken("<your-token>") // use STS token
                .build());

        // Configure the Client
        AsyncClient client = AsyncClient.builder()
                .region("cn-shenzhen") // Region ID
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                        //.setReadTimeout(Duration.ofSeconds(30))
                )
                .build();

        // Parameter settings for API request
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName("阿里云短信测试")
                .templateCode(templateCode)
                .phoneNumbers(phoneNumbers)
                .templateParam("{\"code\":\"" + templateParam + "\"}")
                // Request-level configuration rewrite, can set Http request parameters, etc.
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();

        // Asynchronously get the return value of the API request
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        // Synchronously get the return value of the API request
        SendSmsResponse resp = null;
        try {
            resp = response.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println(new Gson().toJson(resp));

        // Finally, close the client
        client.close();

        return resp.getBody().getCode();

//      下面是旧版本
//        // 设置超时时间-可自行调整
//        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//        // 初始化ascClient需要的几个参数
//        // 短信API产品名称（短信产品名固定，无需修改）
//        final String product = "Dysmsapi";
//        // 短信API产品域名（接口地址固定，无需修改）
//        final String domain = "dysmsapi.aliyuncs.com";
//        // 你的accessKeyId,参考本文档步骤 2
//        final String accessKeyId = "LTAI5tRdz1zTj6kVF2BRbFPS";
//        //你的accessKeySecret，参考本文档步骤 2
//        final String accessKeySecret = "LN4I7NoHfJTKeA1MrV6fR07y9obB6V";
//        // 初始化ascClient,暂时不支持多region（请勿修改）
//        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
//        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
//        IAcsClient acsClient = new DefaultAcsClient(profile);
//
//        // 组装请求对象
//        SendSmsRequest request = new SendSmsRequest();
//        // 使用post提交
//        request.setMethod(MethodType.POST);
//        // 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
//        request.setPhoneNumbers(phoneNumbers);
//        // 必填:短信签名-可在短信控制台中找到
//        request.setSignName("码云社");
//        // 必填:短信模板-可在短信控制台中找到
//        request.setTemplateCode(templateCode);
//        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
//        // 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
//        request.setTemplateParam("{\"code\":\""+param+"\"}");
//        // 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
//        // request.setSmsUpExtendCode("");
//        // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        // request.setOutId("yourOutId");
//        // 请求失败这里会抛ClientException异常
//        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
//        if (sendSmsResponse.getCode() != null && "OK".equals(sendSmsResponse.getCode())) {
//            // 请求成功
//            System.out.println("请求成功");
//        }
    }

    public static String getSmsValidKey(String redisKey, String phoneNumber) {
        return redisKey + ":" + phoneNumber;
    }


    public static void main(String[] args)throws Exception {
        SmsUtils.sendShortMessage("SMS_154950909","13760309524","123321");
    }
}
