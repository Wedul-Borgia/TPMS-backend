package com.tpms.common.web.bean;

import com.tpms.common.web.bean.sys.Power;
import com.tpms.common.web.bean.sys.Role;
import com.tpms.common.web.bean.sys.User;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * @author wld
 * @date 2023/5/13 - 22:19
 */
@Setter
@Getter
public class ProfileResult implements Serializable {
    private String userId;
    private String username;
    private String officeName;
    private String officeId;
    private String level;
    private Map<String,Object> roles = new HashMap<>();

    /**
     *
     * @param user
     */
    public ProfileResult(User user, List<Power> list) {
        this.username = user.getUsername();
        this.officeName = user.getOfficeName();
        this.officeId = user.getOfficeId();
        this.userId = user.getUserId();
        this.level = user.getLevel();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        for (Power power : list) {
            String code = power.getPowerCode();
            String parent = power.getParentCode();
            if("1".equals(power.getPowerType())) {
                if(StringUtils.isNotBlank(parent)&&!menus.contains(parent)){
                    menus.add(parent);
                }
                menus.add(code);
            }else if("2".equals(power.getPowerType())) {
                points.add(code);
            }else {
                apis.add(code);
            }
        }
        this.roles.put("menus",menus);
        this.roles.put("points",points);
        this.roles.put("apis",apis);
    }


    public ProfileResult(User user) {
        this.username = user.getUsername();
        this.officeName = user.getOfficeName();
        this.officeId = user.getOfficeId();
        this.level = user.getLevel();
        this.userId = user.getUserId();
        Set<Role> roles = user.getRoles();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        for (Role role : roles) {
            Set<Power> powers = role.getPowers();
            for (Power power : powers) {
                String code = power.getPowerCode();
                String parent = power.getParentCode();
                if("1".equals(power.getPowerType())) {
                    if(StringUtils.isNotBlank(parent)&&!menus.contains(parent)){
                        menus.add(parent);
                    }
                    menus.add(code);
                }else if("2".equals(power.getPowerType())) {
                    points.add(code);
                }else {
                    apis.add(code);
                }
            }
        }
        this.roles.put("menus",menus);
        this.roles.put("points",points);
        this.roles.put("apis",apis);
    }
}
