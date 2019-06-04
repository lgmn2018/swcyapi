package com.lgmn.swcyapi.service.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.lgmn.basicservices.basic.entity.LgmnSmsCodeEntity;
import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.swcyapi.dto.login.ExitLoginDto;
import com.lgmn.swcyapi.dto.login.LoginDto;
import com.lgmn.swcyapi.dto.login.RegisterDto;
import com.lgmn.swcyapi.service.sms.SmsCodeService;
import com.lgmn.userservices.basic.dto.LgmnUserDto;
import com.lgmn.userservices.basic.entity.LgmnUserEntity;
import com.lgmn.userservices.basic.service.LgmnUserEntityService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserService {

    @Reference(version = "${demo.service.version}")
    private LgmnUserEntityService lgmnUserEntityService;

    @Autowired
    SmsCodeService smsCodeService;

    @Value("${lgmn.token-url}")
    String tokenUrl;

    @Value("${lgmn.exitLogin-url}")
    String exitLoginUrl;

    static String regxPhone = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    static String regxPass = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{8,32}$";


    public Result register (RegisterDto registerDto) throws Exception {
        String phone = registerDto.getPhone();
        String password = registerDto.getPassword();
        String confirmPassword = registerDto.getConfirmPassword();
        if (!Pattern.matches(regxPhone, phone)) return Result.error(ResultEnum.PHONE_ERROR);
        if (!Pattern.matches(regxPass, password) || !password.equals(confirmPassword)) return Result.error(ResultEnum.PASS_ERROR);

        List<LgmnSmsCodeEntity> lgmnSmsCodeEntities = smsCodeService.getByPhone(phone);
        if (lgmnSmsCodeEntities.size() <= 0) return Result.error(ResultEnum.MSG_CODE_ERROR);
        LgmnSmsCodeEntity lgmnSmsCodeEntity = lgmnSmsCodeEntities.get(0);
        if (lgmnSmsCodeEntity.getCode().equals(registerDto.getSmsCode()) && lgmnSmsCodeEntity.getIsExprie() == 1 || !new Timestamp(System.currentTimeMillis()).before(lgmnSmsCodeEntities.get(0).getExpireTime())) return Result.error(ResultEnum.MSG_CODE_ERROR);

        List<LgmnUserEntity> lgmnUserEntities = getUserByPhone(phone);
        LgmnUserEntity lgmnUserEntity = null;
        String msg = "注册成功";
        if (lgmnUserEntities.size() > 0) {
            if (registerDto.getType() == 0) {
                return Result.error(ResultEnum.DATA_EXISTS);
            } else {
                lgmnUserEntity = lgmnUserEntities.get(0);
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                lgmnUserEntity.setPassword(bCryptPasswordEncoder.encode(password));
                msg = "重置成功";
            }
        } else {
            lgmnUserEntity = getUser(phone, password);
        }
        // 修改验证码状态并保存用户
        lgmnSmsCodeEntity.setIsExprie(1);
        saveUserAndUpdateSmsCode(lgmnUserEntity, lgmnSmsCodeEntity);
        return Result.success(msg);
    }

    public Result login (LoginDto loginDto) throws Exception {
        List<LgmnUserEntity> lgmnUserEntities = getUserByPhone(loginDto.getPhone());
        if (lgmnUserEntities.size() <= 0) return Result.error(ResultEnum.DATA_NOT_EXISTS);

        // 不能用Map,
        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("grant_type", "password");
        postParameters.add("username", loginDto.getPhone());
        postParameters.add("password", loginDto.getPassword());

        RestTemplate restTemplate = getRestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth("android", "android", StandardCharsets.UTF_8);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        JSONObject responseResult = restTemplate.exchange(tokenUrl, HttpMethod.POST, httpEntity, JSONObject.class).getBody();
        if (responseResult.containsKey("error_description")) {
            return Result.error(ResultEnum.PASS_ERROR);
        }
        return Result.success(responseResult);
    }

    public Result exitLogin (ExitLoginDto exitLoginDto) {
        RestTemplate restTemplate = getRestTemplate();
        ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(exitLoginUrl + "?access_token=" + exitLoginDto.getAccessToken(), JSONObject.class);
        JSONObject responseResult = responseEntity.getBody();
        String msg = "";
        if (responseResult.containsKey("message")) {
            msg = responseResult.getString("message");
        } else if (responseResult.containsKey("error")) {
            msg = "token无效";
        }
        Result result = new Result();
        result.setMessage(msg);
        result.setCode(String.valueOf(responseEntity.getStatusCodeValue()));
        return result;
    }


    public List<LgmnUserEntity> getUserByPhone (String phone) throws Exception {
        LgmnUserDto lgmnUserDto = new LgmnUserDto();
        lgmnUserDto.setAccount(phone);
        return lgmnUserEntityService.getListByDto(lgmnUserDto);
    }

    private RestTemplate getRestTemplate () {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401 && response.getRawStatusCode() != 402 && response.getRawStatusCode() != 403 && response.getRawStatusCode() != 405 && response.getRawStatusCode() != 500) {
                    super.handleError(response);
                }
            }
        });
        return restTemplate;
    }

    private LgmnUserEntity getUser (String phone, String password) {
        LgmnUserEntity lgmnUserEntity = new LgmnUserEntity();
        lgmnUserEntity.setAvatar("http://qncdn.gdsdec.com/default/avatar/man.png");
        lgmnUserEntity.setAccount(phone);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        lgmnUserEntity.setPassword(bCryptPasswordEncoder.encode(password));
        lgmnUserEntity.setSalt("123456");
        lgmnUserEntity.setUserType(0);
        lgmnUserEntity.setRegTime(new Timestamp(System.currentTimeMillis()));
        return lgmnUserEntity;
    }

    @GlobalTransactional
    private void saveUserAndUpdateSmsCode (LgmnUserEntity lgmnUserEntity, LgmnSmsCodeEntity lgmnSmsCodeEntity) {
        smsCodeService.saveBySmsCode(lgmnSmsCodeEntity);
        lgmnUserEntityService.saveEntity(lgmnUserEntity);
    }
}
