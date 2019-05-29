package com.me.szzc.controller;

import com.me.szzc.constant.SystemArgsConstant;
import com.me.szzc.pojo.entity.Fadmin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author luwei
 * @date 2019-01-25
 */
public class BaseController extends BaseServiceCtrl{



    public static String getIpAddr(HttpServletRequest request) {
        try {
            String ip = request.getHeader("X-Forwarded-For");
            try {
                if(ip != null && ip.trim().length() >0){
                    return ip.split(",")[0];
                }
            } catch (Exception e) {}

            try {
                ip = request.getHeader("X-Real-IP");
                if ((ip != null && ip.trim().length() >0) && (!"unKnown".equalsIgnoreCase(ip))) {
                    return ip;
                }
            } catch (Exception e) {}

            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("http_client_ip");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            // 如果是多级代理，那么取第一个ip为客户ip
            if (ip != null && ip.indexOf(",") != -1) {
                ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
            }
            return ip;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return request.getRemoteAddr();
        }
    }


    public HttpSession getSession(HttpServletRequest request){
        return request.getSession() ;
    }


    /**此方法会在每个controller前执行**/
    @ModelAttribute
    public void addConstant(HttpServletRequest request){
        //前端常量
        request.setAttribute("constant", constantMap.getMap()) ;
        String ossURL = "";
        //静态文件在项目里面，无需配置全局oss_url
        request.setAttribute("oss_url", ossURL) ;

    }


    //获得管理员session
    public Fadmin getAdminSession(HttpServletRequest request){
        Object session = getSession(request).getAttribute("login_admin");
        Fadmin fadmin = null;
        if (session != null) {
            fadmin = (Fadmin) session;
        }
        return fadmin;
    }



    /**初始化水电表参数**/
    public void initWaterAmmerParam(ModelAndView modelAndView){
        //查询空调
        String airConditionerCabinet = systemArgsService.getValue(SystemArgsConstant.AIR_CONDITIONER_CABINET);
        String airConditionerHang = systemArgsService.getValue(SystemArgsConstant.AIR_CONDITIONER_HANG);
        String airConditionerShutter = systemArgsService.getValue(SystemArgsConstant.AIR_CONDITIONER_SHUTTER);
        modelAndView.addObject("airConditionerCabinet", airConditionerCabinet);
        modelAndView.addObject("airConditionerHang", airConditionerHang);
        modelAndView.addObject("airConditionerShutter", airConditionerShutter);

        //查询水表
        String waterMeterMain = systemArgsService.getValue(SystemArgsConstant.WATER_METER_MAIN);
        String waterMeterSub = systemArgsService.getValue(SystemArgsConstant.WATER_METER_SUB);
        modelAndView.addObject("waterMeterMain", waterMeterMain);
        modelAndView.addObject("waterMeterSub", waterMeterSub);

        //查询电表
        String ammeterMain = systemArgsService.getValue(SystemArgsConstant.AMMETER_MAIN);
        String ammeterSub = systemArgsService.getValue(SystemArgsConstant.AMMETER_SUB);
        modelAndView.addObject("ammeterMain", ammeterMain);
        modelAndView.addObject("ammeterSub", ammeterSub);

        //查询搬家费
        String moveHouseRmb = systemArgsService.getValue(SystemArgsConstant.MOVE_HOUSE_RMB);
        String moveHouseSwap = systemArgsService.getValue(SystemArgsConstant.MOVE_HOUSE_SWAP);
        modelAndView.addObject("moveHouseRmb", moveHouseRmb);
        modelAndView.addObject("moveHouseSwap", moveHouseSwap);

        //查询生活困难补助
        String diseaseSubsidy = systemArgsService.getValue(SystemArgsConstant.DISEASE_SUBSIDY);
        String disabilitySubsidy = systemArgsService.getValue(SystemArgsConstant.DISABILITY_SUBSIDY);
        String basicLivingSubsidy = systemArgsService.getValue(SystemArgsConstant.BASIC_LIVING_SUBSIDY);
        modelAndView.addObject("diseaseSubsidy", diseaseSubsidy);
        modelAndView.addObject("disabilitySubsidy", disabilitySubsidy);
        modelAndView.addObject("basicLivingSubsidy", basicLivingSubsidy);

        //搬迁奖励
        String oneMoveReward = systemArgsService.getValue(SystemArgsConstant.ONE_MOVE_REWARD);
        String twoMoveReward = systemArgsService.getValue(SystemArgsConstant.TWO_MOVE_REWARD);
        String threeMoveReward = systemArgsService.getValue(SystemArgsConstant.THREE_MOVE_REWARD);
        modelAndView.addObject("oneMoveReward", oneMoveReward);
        modelAndView.addObject("twoMoveReward", twoMoveReward);
        modelAndView.addObject("threeMoveReward", threeMoveReward);

        //停产停业损失补偿比例
        String suspendBusinessProportion = systemArgsService.getValue(SystemArgsConstant.SUSPEND_BUSINESS_PROPORTION);
        modelAndView.addObject("suspendBusinessProportion", suspendBusinessProportion);


        //货币补偿补助
        String rmbCompensateProportion = systemArgsService.getValue(SystemArgsConstant.RMB_COMPENSATE_PROPORTION);
        modelAndView.addObject("rmbCompensateProportion", rmbCompensateProportion);

        //货币搬迁奖励
        String rmbMoveRewardProportion = systemArgsService.getValue(SystemArgsConstant.RMB_MOVE_REWARD_PROPORTION);
        modelAndView.addObject("rmbMoveRewardProportion", rmbMoveRewardProportion);

        /**无烟灶台**/
        String smokeFreeStove = systemArgsService.getValue(SystemArgsConstant.SMOKE_FREE_STOVE);
        modelAndView.addObject("smokeFreeStove", smokeFreeStove);

        /**电热水器**/
        String waterHeater = systemArgsService.getValue(SystemArgsConstant.WATER_HEATER);
        modelAndView.addObject("waterHeater", waterHeater);

        /**太阳能热水器**/
        String solarWaterHeaters = systemArgsService.getValue(SystemArgsConstant.SOLAR_WATER_HEATERS);
        modelAndView.addObject("solarWaterHeaters", solarWaterHeaters);


    }


    protected String getOwnerOnlyMsg(String houseOwner, String address) {
        String message = "操作失败，用户:" + houseOwner + ",地址:" + address + " 记录已存在，请核对后再操作";
        return message;
    }


}
