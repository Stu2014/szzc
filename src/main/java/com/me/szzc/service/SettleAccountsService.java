package com.me.szzc.service;

import com.alibaba.fastjson.JSONObject;
import com.me.szzc.dao.RmbRecompenseMapper;
import com.me.szzc.dao.SettleAccountsMapper;
import com.me.szzc.dao.SwapHouseMapper;
import com.me.szzc.enums.SigningStatusEnum;
import com.me.szzc.pojo.dto.ChooseHouseDTO;
import com.me.szzc.pojo.entity.RmbRecompense;
import com.me.szzc.pojo.entity.SettleAccounts;
import com.me.szzc.pojo.entity.SwapHouse;
import com.me.szzc.utils.DateHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Service
public class SettleAccountsService {

    @Autowired
    private SettleAccountsMapper settleAccountsMapper;

    @Autowired
    private SwapHouseMapper swapHouseMapper;

    @Autowired
    private RmbRecompenseMapper rmbRecompenseMapper;

    @Autowired
    private RoomChangeService roomChangeService;

    public void add(SettleAccounts settleAccounts) {
        Timestamp date = DateHelper.getTimestamp();
        settleAccounts.setCreateDate(date);
        settleAccounts.setModifiedDate(date);
        settleAccounts.setSigningDate(date);
        settleAccounts.setDeleted(false);
        settleAccounts.setSigningStatus(SigningStatusEnum.NOT_SIGNED.getCode());
        this.settleAccountsMapper.insert(settleAccounts);
    }

    public void delete(SettleAccounts settleAccounts) {
        settleAccounts.setModifiedDate(DateHelper.getTimestamp());
        settleAccounts.setDeleted(true);
        this.settleAccountsMapper.delete(settleAccounts);
    }

    @Transactional
    public void deleteCase(Long userId, Long id, Long rmbId, Long swapId){
        SettleAccounts settleAccounts = this.settleAccountsMapper.selectByPrimaryKey(id);
        //修改人
        settleAccounts.setModifiedUserId(userId);
        //删除结算单
        this.delete(settleAccounts);
        //删除货币补偿协议
        if(rmbId != null && rmbId > 0){
            RmbRecompense recompense = rmbRecompenseMapper.getById(rmbId);
            recompense.setModifiedUserId(userId);
            recompense.setModifiedDate(DateHelper.getTimestamp());
            rmbRecompenseMapper.delete(recompense);
        }

        //删除产权调换协议
        if(swapId != null &&swapId > 0){
            SwapHouse swapHouse = swapHouseMapper.getById(swapId);
            swapHouse.setModifiedUserId(userId);
            swapHouse.setModifiedDate(DateHelper.getTimestamp());
            swapHouseMapper.delete(swapHouse);
        }

    }

    public void update(SettleAccounts settleAccounts) {
        this.settleAccountsMapper.updateByPrimaryKey(settleAccounts);
    }

    public SettleAccounts query(SettleAccounts settleAccounts) {
        return this.settleAccountsMapper.selectByPrimaryKey(settleAccounts.getId());
    }

    public SettleAccounts getById(Long id){
        return this.settleAccountsMapper.selectByPrimaryKey(id);
    }

    public SettleAccounts getByHouseOwnerAddr(String houseOwner, String address) {
        return settleAccountsMapper.getByHouseOwnerAddr(houseOwner, address);
    }


    public List<SettleAccounts> list(int firstResult, int maxResults, boolean isFY, Integer signingStatus, String address,String houseOwner, Long areaId,
                                     List<Long> areaIdList, String startDate, String endDate,Integer compensateType) {
        List<SettleAccounts> listSettleAccounts = this.settleAccountsMapper.list(firstResult, maxResults, isFY, signingStatus, address, houseOwner, areaId, areaIdList, startDate, endDate, compensateType);
        return listSettleAccounts;
    }

    public Integer getCount(Integer signingStatus, String address,String houseOwner, Long areaId, List<Long> areaIdList,
                            String startDate, String endDate, Integer compensateType) {
        return this.settleAccountsMapper.getCount(signingStatus, address, houseOwner, areaId, areaIdList, startDate, endDate, compensateType);
    }

    /**变更签约状态**/
    @Transactional
    public Integer changeSignStatus(SettleAccounts settleAccounts){
        //如果是已签约的，则修改签约时间
        if(settleAccounts.getSigningStatus().equals(SigningStatusEnum.COMPLETE.getCode())){
            settleAccounts.setSigningDate(DateHelper.getTimestamp());
        }
        int result = this.settleAccountsMapper.changeSignStatus(settleAccounts);
        //2019-08-23号，注释，去掉自动点房映射的功能
        /*//签约完成，反向映射点房人
        if (settleAccounts.getSigningStatus().intValue() == SigningStatusEnum.COMPLETE.getCode()) {
            //根据签约地址，获取点房人(仅限产权调换的)，同一个旧地址，可能对应多个新房点方人
            List<ChooseHouseDTO> list = swapHouseMapper.getChooseHouseListBySettleId(settleAccounts.getId());
            if (list == null || list.isEmpty()) {
                return result;
            }

            Map<String, String> map = new HashMap<>();
            for (ChooseHouseDTO dto : list) {
                String value = dto.getHouseOwner();
                //以项目地址为key
                String key = dto.getNewHouseAddress() + "_" + dto.getSeat() + "_" + dto.getUnit() + "_" + dto.getFloors() + "_" + dto.getHouseNumber();
                String temp_value = map.get(key);
                //存在则更新
                if (StringUtils.isNotBlank(temp_value)) {
                    temp_value += "," + value;
                    map.put(key, temp_value);
                    continue;
                }
                map.put(key, value);
            }
            //更新房源中的点房人信息
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String v_key[] = key.split("_");
                String newHouseAddress = v_key[0];
                Integer seat = Integer.valueOf(v_key[1]);
                Integer unit = Integer.valueOf(v_key[2]);
                Integer floors = Integer.valueOf(v_key[3]);
                Integer number = Integer.valueOf(v_key[4]);
                ChooseHouseDTO dto = new ChooseHouseDTO();
                dto.setAddress(settleAccounts.getAddress());
                dto.setHouseOwner(value);
                dto.setNewHouseAddress(newHouseAddress);
                dto.setSeat(seat);
                dto.setUnit(unit);
                dto.setFloors(floors);
                dto.setHouseNumber(number);
                log.info("点方信息dto:{}", JSONObject.toJSONString(dto));
                roomChangeService.chooseRoom(dto);
            }
        }*/
        return result;
    }

    public Integer updateSignDate(SettleAccounts settleAccounts) {
        int result = this.settleAccountsMapper.updateSignDate(settleAccounts);
        return result;
    }


}
