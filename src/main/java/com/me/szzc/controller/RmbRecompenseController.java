package com.me.szzc.controller;

import com.me.szzc.constant.SystemArgsConstant;
import com.me.szzc.pojo.entity.RmbRecompense;
import com.me.szzc.pojo.entity.SwapHouse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 货币补偿协议
 */
@Controller
public class RmbRecompenseController extends BaseController {

    @RequestMapping("ssadmin/RmbRecompense/add")
    public ModelAndView saveRmbRecompense ( RmbRecompense rmbRecompense, HttpServletRequest request)throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/rmbRecompense");
        //创建人
        Long userId = getAdminSession(request).getFid();
        rmbRecompense.setCreateUserId(userId);
        rmbRecompense.setModifiedUserId(userId);
        //征收公司
        rmbRecompense.setCompany(constantMap.getValue(SystemArgsConstant.COMPANY));
        this.rmbRecompenseService.add(rmbRecompense);
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","新增成功");
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/RmbRecompense/detele")
    public ModelAndView dateleRmbRecompense (RmbRecompense rmbRecompense)throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/rmbRecompense");
        int i =  this.rmbRecompenseService.queryOne(rmbRecompense.getId());
        if(i==0){
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","用户不存在此协议");
            return modelAndView;
        }
        this.rmbRecompenseService.detele(rmbRecompense);
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","删除成功");
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/RmbRecompense/update")
    public ModelAndView updateRmbRecompense (RmbRecompense rmbRecompense, HttpServletRequest request)throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        //修改人
        Long userId = getAdminSession(request).getFid();
        rmbRecompense.setModifiedUserId(userId);
        this.rmbRecompenseService.update(rmbRecompense);
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","修改成功");
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/RmbRecompense/query")
    public ModelAndView queryRmbRecompense (String houseOwner, String url)throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(url);
        RmbRecompense recompense = this.rmbRecompenseService.selectByHouseOwner(houseOwner);
        if(recompense != null) {
            modelAndView.addObject("rmbRecom", recompense);
        }
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "查询成功");
        return modelAndView;
    }

}
