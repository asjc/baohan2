package com.ruoyi.baohan.util;

import com.ruoyi.baohan.domain.GurtOrder;

import java.util.List;

public class DaoRuExption  {
    public  static void thRun(List<GurtOrder> gurtOrderList){
        for (GurtOrder order : gurtOrderList) {
            if(order.getWarrantee()==null||order.getWarrantee()=="")
                throw new RuntimeException("导入被保证人不能为空");
            if(order.getBeneficiary()==null||order.getBeneficiary()=="")
                throw new RuntimeException("导入受益人不能为空");
            if(order.getProjectName()==null||order.getProjectName()=="")
                throw new RuntimeException("导入项目名称不能为空");
            if(order.getClosingTime()==null||order.getClosingTime()=="")
                throw new RuntimeException("导入截标日期不能为空");
            if(order.getGuaranteeAmount()==null)
                throw new RuntimeException("导入担保金额不能为空");
            if(order.getValidityDeadline()==null||order.getValidityDeadline()=="")
                throw new RuntimeException("导入有效期不能为空");
            /*if(order.getBaoName()==null||order.getBaoName()=="")
                throw new RuntimeException("导入保函格式不能为空");
            if(order.getBankName()==null||order.getBankName()=="")
                throw new RuntimeException("导入银行不能为空");
            if(order.getFenName()==null||order.getFenName()=="")
                throw new RuntimeException("导入项目分类不能为空");*/
           /* if(order.getYifu()==null)
                throw new RuntimeException("导入应付金额不能为空");*/
        }
    }
}
