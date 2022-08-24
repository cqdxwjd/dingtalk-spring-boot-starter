/*
 * Copyright ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jaemon.dinger.httpclient;

import com.github.jaemon.dinger.dingtalk.entity.DingText;
import com.github.jaemon.dinger.dingtalk.entity.Message;
import com.github.jaemon.dinger.support.sign.SignResult;
import com.github.jaemon.dinger.wetalk.entity.WeText;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * HttpClientDingTalkTemplate
 *
 * @author Jaemon
 * @since 1.0
 */
public class HttpClientDingTalkTemplate {

    public static void main(String[] args) throws Exception {
        String tokenId = "fb84492a906fc57c70e5efb885fef0155b78f12aee42d66e6a7b16f692dfedad";
        StringBuilder url = new StringBuilder("https://oapi.dingtalk.com/robot/send?access_token=");
        String secret = "SECca136bff93886b8be2c132f1190410e87d9c99d1acbcaead97e54e66bbc3f929";
        Long timestamp = System.currentTimeMillis();
        String sign = algorithm(timestamp, secret);
        url.append(tokenId).append("&").append("sign=").append(sign)
                .append("&")
                .append("timestamp=").append(timestamp);

        DingText dingText = new DingText(new DingText.Text("Hello Dinger"));
        dingText.setAt(new Message.At(true));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DingText> request = new HttpEntity<>(dingText, headers);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(url.toString(), request, String.class);
        System.out.println(response);
    }

    private static String algorithm(Long timestamp, String secret) throws Exception {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(
                new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256")
        );
        byte[] signData = mac.doFinal(
                stringToSign.getBytes(StandardCharsets.UTF_8)
        );
        String sign = URLEncoder.encode(
                Base64.getEncoder().encodeToString(signData),
                StandardCharsets.UTF_8.name()
        );
        return sign;
    }
}