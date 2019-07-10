package com.ruoyi.baohan.util;

import com.ruoyi.baohan.domain.GurtStatus;
import com.ruoyi.baohan.service.IGurtOrderService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import org.springframework.ui.ModelMap;

import java.util.List;

public class UtilOrder {
    public static int getRole(){
        int role=0;
        List<SysRole> user= ShiroUtils.getSysUser().getRoles();
        String roles=null;
        for (int i=0;i<user.size();i++){
            roles=roles+user.get(i).getRoleId();
        }
        if(roles.contains("3")) {
            role = 3;
        }
        if(roles.contains("4")) {
            role = 4;
        }
        if(roles.contains("1")) {
            role = 1;
        }
        return role;
    }
    public static void gurtOrder(ModelMap modelMap, IGurtOrderService gurtOrderService) {
        List<GurtStatus> statusList = gurtOrderService.getStatus();
        modelMap.put("statusList", statusList);
        int role = UtilOrder.getRole();
        modelMap.put("role", role);
    }
}
