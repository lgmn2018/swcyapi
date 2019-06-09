package com.lgmn.swcyapi.controller;

import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.swcyapi.dto.person.*;
import com.lgmn.swcyapi.service.PersonService;
import com.lgmn.userservices.basic.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Api(description = "我的")
@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    PersonService personService;

    @ApiOperation(value = "获取基本信息与广告")
    @PostMapping("/getPersonAndAdList")
    public Result getPersonAndAdList (@RequestHeader String Authorization, Principal principal) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return personService.getPersonAndAdList(lgmnUserInfo);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取基本信息")
    @PostMapping("/getPersonInfo")
    public Result getPersonInfo (@RequestHeader String Authorization, Principal principal) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return personService.getPersonInfo(lgmnUserInfo);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "修改昵称")
    @PostMapping("/upDateNikeName")
    public Result upDateNikeName (@RequestHeader String Authorization, Principal principal, @RequestBody UpDateNikeNameDto upDateNikeNameDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        return personService.upDateNikeName(lgmnUserInfo, upDateNikeNameDto.getNikeName());
    }

    @ApiOperation(value = "修改头像")
    @PostMapping("/upDateAvatar")
    public Result upDateAvatar (@RequestHeader String Authorization, Principal principal, @RequestBody MultipartFile avatar) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return personService.upDateAvatar(lgmnUserInfo, avatar);
        } catch (IOException e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "修改邮箱")
    @PostMapping("/upDateMailbox")
    public Result upDateMailbox (@RequestHeader String Authorization, Principal principal, @RequestBody UpdateMailboxDto updateMailboxDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return personService.upDateEmail(lgmnUserInfo, updateMailboxDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "实名认证")
    @PostMapping("/authentication")
    public Result authentication (@RequestHeader String Authorization, Principal principal, @RequestBody AuthenticationDto authenticationDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return personService.authentication(lgmnUserInfo, authenticationDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取实名信息")
    @PostMapping("/getAuthenticationInfo")
    public Result getAuthenticationInfo (@RequestHeader String Authorization, Principal principal) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return personService.getAuthenticationInfo(lgmnUserInfo);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/upDatePassword")
    public Result upDatePassword (@RequestHeader String Authorization, Principal principal, @RequestBody UpDatePasswordDto upDatePasswordDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        return personService.upDatePassword(lgmnUserInfo, upDatePasswordDto);
    }

    @ApiOperation(value = "获取我的资产")
    @PostMapping("/getMyAssets")
    public Result getMyAssets (@RequestHeader String Authorization, Principal principal) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return personService.getMyAssets(lgmnUserInfo);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取团队总业绩")
    @PostMapping("/getMyTeamAchievement")
    public Result getMyTeamAchievement (@RequestHeader String Authorization, Principal principal) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return personService.getMyTeamAchievement(lgmnUserInfo);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取团队")
    @PostMapping("/getMyTeam")
    public Result getMyTeam (@RequestHeader String Authorization, Principal principal, @RequestBody MyTeamDto myTeamDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return personService.getMyTeam(lgmnUserInfo, myTeamDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取个人信息/通知公告")
    @PostMapping("/getMessage")
    public Result getMessage (@RequestHeader String Authorization, Principal principal, @RequestBody MessageDto messageDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return personService.getMessage(lgmnUserInfo, messageDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "修改消息未读状态")
    @PostMapping("/upDateMessageHadRead")
    public Result upDateMessageHadRead (@RequestHeader String Authorization, Principal principal, @RequestBody UpDateMessageHadReadDto upDateMessageHadReadDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        return personService.upDateMessageHadRead(upDateMessageHadReadDto);
    }

    @ApiOperation(value = "意见投诉")
    @PostMapping("/complaints")
    public Result complaints (@RequestHeader String Authorization, Principal principal, @RequestBody ComplaintsDto complaintsDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        return personService.complaints(lgmnUserInfo, complaintsDto);
    }
}
