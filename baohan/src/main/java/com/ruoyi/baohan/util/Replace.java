
package com.ruoyi.baohan.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import com.ruoyi.baohan.domain.GurtBank;
import com.ruoyi.baohan.domain.GurtOrder;
import com.ruoyi.baohan.domain.GurtProjectType;
import com.ruoyi.baohan.service.IGurtOrderService;
import com.ruoyi.baohan.service.IGurtProjectTypeService;
import com.ruoyi.common.config.ServerConfig;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author zhangdapeng
 * @Date 2014年4月22日
 */

public class Replace {

    public static Map getMap(GurtOrder gurtOrder, IGurtProjectTypeService iGurtProjectTypeService,IGurtOrderService gurtOrderService)throws Exception{
        Map map=new HashMap();
        map.put("warrantee", gurtOrder.getWarrantee());
        map.put("beneficiary", gurtOrder.getBeneficiary());
        List<GurtProjectType> gurtProjectTypeList = iGurtProjectTypeService.selectGurtProjectTypeList(new GurtProjectType());
        for (GurtProjectType type : gurtProjectTypeList) {
            if (type.getId() == gurtOrder.getProjectTypeId())
                gurtOrder.setFenName(type.getName());
        }
        List<GurtBank> gurtBanks=gurtOrderService.getAllBank();
        for (GurtBank bank : gurtBanks) {
            if(bank.getId()==gurtOrder.getBankId())
                gurtOrder.setBankName(bank.getBankName());
        }
        map.put("projectName", gurtOrder.getProjectName());

        map.put("xiao", gurtOrder.getAmount().toString());
        map.put("da", UtilNumber.convert(gurtOrder.getAmount().toString()));
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        if(gurtOrder.getClosingTime()==null)
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(sdf.parse(gurtOrder.getClosingTime()));
        map.put("yy", String.valueOf(calendar.get(Calendar.YEAR)));
        map.put("mm", String.valueOf(calendar.get(Calendar.MONTH) + 1));
        map.put("dd", String.valueOf(calendar.get(Calendar.DATE)));
        //判断有限期类型
        Calendar calendar1 = new GregorianCalendar();

        if(!gurtOrder.getValidityDeadline().contains("-")){
            calendar1.add(calendar.DATE, Integer.valueOf(gurtOrder.getValidityDeadline())); // 负数为提前几天，正数为推迟几天
        }else{
            calendar1.setTime(sdf.parse(gurtOrder.getValidityDeadline()));
        }
        map.put("y2", String.valueOf(calendar1.get(Calendar.YEAR)));
        map.put("m2", String.valueOf(calendar1.get(Calendar.MONTH) + 1));
        map.put("d2", String.valueOf(calendar1.get(Calendar.DATE)));
        map.put("bk", gurtOrder.getBankName());
        return map;
    }

    public static void searchAndReplace(String srcPath, String destPath,
                                        Map<String, String> map) {
        try {
            InputStream input = new FileInputStream(srcPath);
            XWPFDocument document = new XWPFDocument(input);
            // 替换段落中的指定文字
            Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
            while (itPara.hasNext()) {
                XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
                //String s = paragraph.getParagraphText();
                Set<String> set = map.keySet();
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    List<XWPFRun> run=paragraph.getRuns();
                    for(int i=0;i<run.size();i++)
                    {
                        if(run.get(i).getText(run.get(i).getTextPosition())!=null && run.get(i).getText(run.get(i).getTextPosition()).equals(key))
                        {
                            run.get(i).setText(map.get(key),0);
                        }
                    }
                }
            }
            // 替换表格中的指定文字
            Iterator<XWPFTable> itTable = document.getTablesIterator();
            while (itTable.hasNext()) {
                XWPFTable table = (XWPFTable) itTable.next();
                int rcount = table.getNumberOfRows();
                for (int i = 0; i < rcount; i++) {
                    XWPFTableRow row = table.getRow(i);
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        for (Entry<String, String> e : map.entrySet()) {
                            if (cell.getText().equals(e.getKey())) {
                                cell.removeParagraph(0);
                                cell.setText(e.getValue());
                            }
                        }
                    }
                }
            }
            FileOutputStream outStream = null;
            outStream = new FileOutputStream(destPath);
            document.write(outStream);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

    }
}