package com.lgmn.swcyapi.service;

import com.alibaba.fastjson.JSONObject;
import com.lgmn.basicservices.basic.entity.LgmnComplaintsEntity;
import com.lgmn.basicservices.basic.entity.LgmnSmsCodeEntity;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.common.utils.ObjectTransfer;
import com.lgmn.juhe.starter.service.JuHe_IdCardQuery_SarterService;
import com.lgmn.qiniu.starter.service.QiNiu_UpLoad_Img_StarterService;
import com.lgmn.swcy.basic.entity.*;
import com.lgmn.swcyapi.dto.login.LoginDto;
import com.lgmn.swcyapi.dto.person.*;
import com.lgmn.swcyapi.service.ad.AdService;
import com.lgmn.swcyapi.service.address.SwcyReceivingAddressApiService;
import com.lgmn.swcyapi.service.appuser.AppUserService;
import com.lgmn.swcyapi.service.assets.AssetsService;
import com.lgmn.swcyapi.service.complaints.ComplaintsService;
import com.lgmn.swcyapi.service.flow.SwcyFlowApiService;
import com.lgmn.swcyapi.service.message.MessageService;
import com.lgmn.swcyapi.service.order.SOrderService;
import com.lgmn.swcyapi.service.sms.SmsCodeService;
import com.lgmn.swcyapi.service.store.SStoreService;
import com.lgmn.swcyapi.service.user.UserService;
import com.lgmn.swcyapi.vo.home.HomeAdVo;
import com.lgmn.swcyapi.vo.person.*;
import com.lgmn.userservices.basic.entity.LgmnUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PersonService {

    static String regxPass = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{8,32}$";
    @Value("${qiniu.service.url}")
    String qiniuUrl;

    @Autowired
    AdService adService;

    @Autowired
    UserService userService;

    @Autowired
    AppUserService appUserService;

    @Autowired
    QiNiu_UpLoad_Img_StarterService qiNiu_upLoad_img_starterService;

    @Autowired
    JuHe_IdCardQuery_SarterService juHe_idCardQuery_sarterService;

    @Autowired
    LoginService loginService;

    @Autowired
    AssetsService assetsService;

    @Autowired
    SOrderService sOrderService;

    @Autowired
    MessageService messageService;

    @Autowired
    ComplaintsService complaintsService;

    @Autowired
    SwcyReceivingAddressApiService swcyReceivingAddressApiService;

    @Autowired
    SmsCodeService smsCodeService;

    @Autowired
    SStoreService sStoreService;

    @Autowired
    SwcyFlowApiService swcyFlowApiService;

    public Result getPersonAndAdList(LgmnUserInfo lgmnUserInfo) throws Exception {
        SwcyAppUserEntity swcyAppUserEntity = appUserService.getAppUserByUid(lgmnUserInfo.getId());
        List<SwcyAdEntity> swcyAdEntities = adService.getAdListByType(3);
        List<SwcyStoreEntity> swcyStoreEntities = sStoreService.getMyStoreListByUid(lgmnUserInfo.getId());
        List<HomeAdVo> homeAdVos = new HomeAdVo().getVoList(swcyAdEntities, HomeAdVo.class);
        PersonAndAdVo personAndAdVo = new PersonAndAdVo();
        personAndAdVo.setHomeAdVo(homeAdVos);
        personAndAdVo.setLgmnUserInfo(lgmnUserInfo);
        personAndAdVo.setStar(swcyAppUserEntity.getStar());
        personAndAdVo.setCredit(swcyAppUserEntity.getCredit());
        personAndAdVo.setNotBusiness(swcyStoreEntities.size() > 0 ? false : true);
        return Result.success(personAndAdVo);
    }

    public Result upDateNikeName(LgmnUserInfo lgmnUserInfo, String nikeName) {
        LgmnUserEntity user = userService.getUserById(lgmnUserInfo.getId());
        user.setNikeName(nikeName);
        userService.save(user);
        return Result.success("修改成功");
    }

    public Result upDateAvatar (LgmnUserInfo lgmnUserInfo, MultipartFile avatar) throws IOException {
        List<String> path = new ArrayList<>();
        path.add("user");
        path.add("avatar");
        String kePath = qiNiu_upLoad_img_starterService.upLoadImg(avatar, path);
        LgmnUserEntity lgmnUserEntity = userService.getUserById(lgmnUserInfo.getId());
        lgmnUserEntity.setAvatar(qiniuUrl + kePath);
        userService.save(lgmnUserEntity);
        return Result.success("修改成功");
    }

    public Result upDateEmail (LgmnUserInfo lgmnUserInfo, UpdateMailboxDto updateMailboxDto) throws Exception {
        SwcyAppUserEntity swcyAppUserEntity = appUserService.getAppUserByUid(lgmnUserInfo.getId());
        swcyAppUserEntity.setEmail(updateMailboxDto.getEmail());
        appUserService.save(swcyAppUserEntity);
        return Result.success("修改成功");
    }

    public Result getPersonInfo (LgmnUserInfo lgmnUserInfo) throws Exception {
        PersonInfoVo personInfoVo = new PersonInfoVo();
        personInfoVo.setId(lgmnUserInfo.getId());
        personInfoVo.setAvatar(lgmnUserInfo.getAvatar());
        personInfoVo.setNikeName(lgmnUserInfo.getNikeName());
        SwcyAppUserEntity swcyAppUserEntity = appUserService.getAppUserByUid(lgmnUserInfo.getId());
        personInfoVo.setAuthentication(!StringUtils.isEmpty(swcyAppUserEntity.getName()));
        personInfoVo.setEmail(swcyAppUserEntity.getEmail());
        personInfoVo.setPhone(swcyAppUserEntity.getPhone());
        return Result.success(personInfoVo);
    }

    public Result authentication (LgmnUserInfo lgmnUserInfo, AuthenticationDto authenticationDto) throws Exception {
        JSONObject jsonObject = juHe_idCardQuery_sarterService.postJuHeIdcard(authenticationDto.getIdNum(), authenticationDto.getName());
        if (Integer.parseInt(jsonObject.get("error_code").toString()) != 0) {
            return Result.error(ResultEnum.NOT_SCHEDULED_ERROR);
        }
        SwcyAppUserEntity swcyAppUserEntity = appUserService.getAppUserByUid(lgmnUserInfo.getId());
        swcyAppUserEntity.setName(authenticationDto.getName());
        swcyAppUserEntity.setIdNum(authenticationDto.getIdNum());
        appUserService.save(swcyAppUserEntity);
        return Result.success("认证成功");
    }

    public Result getAuthenticationInfo (LgmnUserInfo lgmnUserInfo) throws Exception {
        SwcyAppUserEntity swcyAppUserEntity = appUserService.getAppUserByUid(lgmnUserInfo.getId());
        if (StringUtils.isEmpty(swcyAppUserEntity.getName())) {
            return Result.error(ResultEnum.DATA_NOT_EXISTS);
        }
        AuthenticationInfoVo authenticationInfoVo = new AuthenticationInfoVo();
        authenticationInfoVo.setPhone(lgmnUserInfo.getAccount());
        StringBuffer sb = new StringBuffer(swcyAppUserEntity.getIdNum());
        sb.replace(6, 13, "********");
        authenticationInfoVo.setIdNum(sb.toString());
        authenticationInfoVo.setName(swcyAppUserEntity.getName());
        return Result.success(authenticationInfoVo);
    }

    public Result upDatePassword (LgmnUserInfo lgmnUserInfo, UpDatePasswordDto upDatePasswordDto) {
        if (!Pattern.matches(regxPass, upDatePasswordDto.getNewPassword()) || !upDatePasswordDto.getNewPassword().equals(upDatePasswordDto.getConfirmPassword())) {
            return Result.error(ResultEnum.PASS_ERROR);
        }
        LoginDto loginDto = new LoginDto();
        loginDto.setPhone(lgmnUserInfo.getAccount());
        loginDto.setPassword(upDatePasswordDto.getPassword());
        JSONObject responseResult = loginService.restTemplateLogin(loginDto);
        if (responseResult.containsKey("error_description")) {
            return Result.error(ResultEnum.PASS_ERROR);
        }
        LgmnUserEntity lgmnUserEntity = userService.getUserById(lgmnUserInfo.getId());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        lgmnUserEntity.setPassword(bCryptPasswordEncoder.encode(upDatePasswordDto.getNewPassword()));
        userService.save(lgmnUserEntity);
        return Result.success("修改成功");
    }

    public Result getMyAssets (LgmnUserInfo lgmnUserInfo) throws Exception {
        SwcyAssetsEntity swcyAssetsEntity = assetsService.getAsstesByUid(lgmnUserInfo.getId());
        return Result.success(swcyAssetsEntity);
    }

    public Result getMyTeamAchievement (LgmnUserInfo lgmnUserInfo) throws Exception {
        List<SwcyAppUserEntity> swcyAppUserEntities = appUserService.getAppUserListByPuid(lgmnUserInfo.getId());
//        List<SwcyAppUserEntity> swcyAppUserEntities = appUserService.getAppUserListByPuid("2c93808570f857670170f931352c0002");
//        List<String> ids = new ArrayList<>();
//        ids.add(lgmnUserInfo.getId());
//        for (SwcyAppUserEntity swcyAppUserEntity : swcyAppUserEntities) {
//            ids.add(swcyAppUserEntity.getUid());
//        }
//        List<SwcyOrderEntity> swcyOrderEntities = sOrderService.getAllByUid(ids);
        BigDecimal teamAchievement = new BigDecimal(0.0);
//        for (SwcyOrderEntity swcyOrderEntity : swcyOrderEntities) {
//            teamAchievement.add(swcyOrderEntity.getMoney());
//        }

        for (SwcyAppUserEntity swcyAppUserEntity : swcyAppUserEntities) {
            teamAchievement = teamAchievement.add(swcyAppUserEntity.getConsumptionAmount());
        }
        TeamAchievementVo teamAchievementVo = new TeamAchievementVo();
        teamAchievementVo.setTeamSum(swcyAppUserEntities.size());
        teamAchievementVo.setTeamAchievement(teamAchievement);
        return Result.success(teamAchievementVo);
    }

    public Result getMyTeam (LgmnUserInfo lgmnUserInfo, MyTeamDto myTeamDto) throws Exception {
//        SwcyAppUserEntity swcyAppUserEntity = appUserService.getAppUserByUid(lgmnUserInfo.getId());
//        LgmnPage<SwcyAppUserEntity> swcyAppUserEntities = appUserService.getAppUserPageByPuid(lgmnUserInfo.getId(), myTeamDto.getPageNumber(), myTeamDto.getPageSize());
//        swcyAppUserEntities.getList().add(swcyAppUserEntity);
        List<SwcyAppUserEntity> swcyAppUserEntities = appUserService.getAppUserListByPuid(lgmnUserInfo.getId());
//        List<SwcyAppUserEntity> swcyAppUserEntities = appUserService.getAppUserListByPuid("2c93808570f857670170f931352c0002");
        LgmnPage<SwcyAppUserEntity> swcyAppUserEntityPage = new LgmnPage<>();
        swcyAppUserEntityPage.setList(swcyAppUserEntities);
        swcyAppUserEntityPage.setCount(Integer.toUnsignedLong(swcyAppUserEntities.size()));
        swcyAppUserEntityPage.setPageNumber(0);
        swcyAppUserEntityPage.setPageSize(swcyAppUserEntities.size());
        swcyAppUserEntityPage.setTotalPage(1);

        LgmnPage<MyTeamVo> myTeamVoLgmnPage = new MyTeamVo().getVoPage(swcyAppUserEntityPage, MyTeamVo.class);
        for (MyTeamVo myTeamVo : myTeamVoLgmnPage.getList()) {
            LgmnUserEntity lgmnUserEntity = userService.getUserById(myTeamVo.getUid());
            myTeamVo.setAvatar(lgmnUserEntity.getAvatar());
            myTeamVo.setNikeName(lgmnUserEntity.getNikeName());
            myTeamVo.setPhone(myTeamVo.getPhone());
        }

//        for (MyTeamVo myTeamVo : myTeamVoLgmnPage.getList()) {
////            LgmnUserEntity lgmnUserEntity = userService.getUserById(myTeamVo.getUid());
////            List<String> ids = new ArrayList<>();
////            ids.add(myTeamVo.getUid());
////            List<SwcyOrderEntity> swcyOrderEntityList = sOrderService.getAllByUid(ids);
////            BigDecimal achievement = new BigDecimal(0);
////            for (SwcyOrderEntity swcyOrderEntity : swcyOrderEntityList) {
////                achievement.add(swcyOrderEntity.getMoney());
////            }
////            myTeamVo.setAvatar(lgmnUserEntity.getAvatar());
////            myTeamVo.setNikeName(lgmnUserEntity.getNikeName());
////            myTeamVo.setPhone(myTeamVo.getPhone());
////            myTeamVo.setAchievement(achievement);
////
////        }
        return Result.success(myTeamVoLgmnPage);
    }

    public Result getMessage (LgmnUserInfo lgmnUserInfo, MessageDto messageDto) throws Exception {
        if (messageDto.getType() == 0) {
            LgmnPage<SwcyMessageEntity> swcyMessageEntityLgmnPage = messageService.getMyMessagePageByUid(lgmnUserInfo.getId(), messageDto.getType(), messageDto.getPageNumber(), messageDto.getPageSize());
            LgmnPage<MessageVo> messageVoLgmnPage = new MessageVo().getVoPage(swcyMessageEntityLgmnPage, MessageVo.class);
            return Result.success(messageVoLgmnPage);
        } else {
            LgmnPage<SwcyMessageEntity> swcyMessageEntityLgmnPage = messageService.getMessagePage( messageDto.getType(), messageDto.getPageNumber(), messageDto.getPageSize());
            LgmnPage<MessageVo> messageVoLgmnPage = new MessageVo().getVoPage(swcyMessageEntityLgmnPage, MessageVo.class);
            return Result.success(messageVoLgmnPage);
        }
    }

    public Result upDateMessageHadRead (UpDateMessageHadReadDto upDateMessageHadReadDto) {
        SwcyMessageEntity swcyMessageEntity = messageService.getMessageById(upDateMessageHadReadDto.getId());
        swcyMessageEntity.setHadRead(1);
        messageService.save(swcyMessageEntity);
        return Result.success("修改成功");
    }

    public Result complaints (LgmnUserInfo lgmnUserInfo, ComplaintsDto complaintsDto) {
        LgmnComplaintsEntity lgmnComplaintsEntity = new LgmnComplaintsEntity();
        lgmnComplaintsEntity.setContext(complaintsDto.getContext());
        lgmnComplaintsEntity.setUid(lgmnUserInfo.getId());
        lgmnComplaintsEntity.setStatus(0);
        complaintsService.save(lgmnComplaintsEntity);
        return Result.success("投诉成功");
    }

    public Result saveReceivingAddress(LgmnUserInfo lgmnUserInfo, SaveReceivingAddressDto saveReceivingAddressDto) {
        SwcyReceivingAddressEntity swcyReceivingAddressEntity = new SwcyReceivingAddressEntity();
        ObjectTransfer.transValue(saveReceivingAddressDto, swcyReceivingAddressEntity);
        swcyReceivingAddressEntity.setUid(lgmnUserInfo.getId());
        swcyReceivingAddressEntity.setDelFlag(0);
        SwcyReceivingAddressEntity newReceivingAddress = swcyReceivingAddressApiService.saveReceivingAddress(swcyReceivingAddressEntity);
        return Result.success(newReceivingAddress);
    }

    public Result deleteReceivingAddressById(DeleteReceivingAddressDto deleteReceivingAddressDto) {
        swcyReceivingAddressApiService.deleteReceivingAddressById(deleteReceivingAddressDto.getId());
        return Result.success("删除成功");
    }

    public Result updateReceivingAddress(LgmnUserInfo lgmnUserInfo, UpdateReceivingAddressDto updateReceivingAddressDto) {
        SwcyReceivingAddressEntity swcyReceivingAddressEntity = new SwcyReceivingAddressEntity();
        ObjectTransfer.transValue(updateReceivingAddressDto, swcyReceivingAddressEntity);
        swcyReceivingAddressEntity.setUid(lgmnUserInfo.getId());
        swcyReceivingAddressEntity.setDelFlag(0);
        swcyReceivingAddressApiService.updateReceivingAddress(swcyReceivingAddressEntity);
        return Result.success("修改成功");
    }

    public Result getReceivingAddressListByUId(LgmnUserInfo lgmnUserInfo) throws Exception {
        List<SwcyReceivingAddressEntity> list = swcyReceivingAddressApiService.getReceivingAddressListByUId(lgmnUserInfo.getId());
        return Result.success(list);
    }

    public Result authenticationPhone(LgmnUserInfo lgmnUserInfo, AuthenticationPhoneDto authenticationPhoneDto) throws Exception {
        List<SwcyAppUserEntity> userList = appUserService.getAppUserByPhone(authenticationPhoneDto.getPhone());
        if (userList.size() > 0) {
            return Result.serverError("此手机号已认证，请更换手机号");
        }
        List<LgmnSmsCodeEntity> lgmnSmsCodes = smsCodeService.getByPhone(authenticationPhoneDto.getPhone());
        if (lgmnSmsCodes.size() <= 0) return Result.error(ResultEnum.MSG_CODE_ERROR);
        LgmnSmsCodeEntity lgmnSmsCode = lgmnSmsCodes.get(0);
        if (lgmnSmsCode.getCode().equals(authenticationPhoneDto.getSmsCode()) && lgmnSmsCode.getIsExprie() == 1 || !new Timestamp(System.currentTimeMillis()).before(lgmnSmsCodes.get(0).getExpireTime())) return Result.error(ResultEnum.MSG_CODE_ERROR);

        // 认证手机号
        SwcyAppUserEntity swcyAppUserEntity = appUserService.getAppUserByUid(lgmnUserInfo.getId());
        swcyAppUserEntity.setPhone(authenticationPhoneDto.getPhone());

        // 修改验证码状态
        lgmnSmsCode.setIsExprie(1);
        smsCodeService.saveBySmsCode(lgmnSmsCode);
        appUserService.save(swcyAppUserEntity);
        return Result.success("认证成功");
    }

    public Result getStoreFlowing(LgmnUserInfo lgmnUserInfo, StoreFlowingDto storeFlowingDto) throws Exception {
        return Result.success(swcyFlowApiService.getStoreFlowingByPayeeId(lgmnUserInfo.getId(), storeFlowingDto.getPageNumber(), storeFlowingDto.getPageSize()));
    }
}
