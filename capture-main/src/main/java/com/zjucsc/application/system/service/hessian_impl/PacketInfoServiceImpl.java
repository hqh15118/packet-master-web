package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.system.service.hessian_iservice.IPacketInfoService;
import com.zjucsc.application.system.service.hessian_mapper.PacketInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class PacketInfoServiceImpl  implements IPacketInfoService {

    @Autowired private PacketInfoMapper packetInfoMapper;

    @Override
    public void saveAttackInfo(SavedAttackPacket savedAttackPacket) {
        packetInfoMapper.saveAttackInfo(savedAttackPacket);
    }

    @Override
    public AttackForSelect selectAttackHistory(AttackHistoryBean attackHistoryBean) {
        return packetInfoMapper.selectAttackHistory(attackHistoryBean);
    }

    @Override
    public AttackForSelect exportAttackHistory(AttackInfoExport attackInfoExport) {
        return packetInfoMapper.exportAttackHistory(attackInfoExport);
    }

    @Override
    public ExceptionForSelect selectExceptionHistory(ExceptionHistoryBean exceptionHistoryBean) {
        return packetInfoMapper.selectExceptionHistory(exceptionHistoryBean);
    }

    @Override
    public ExceptionForSelect exportExceptionHistory(ExceptionInfoExport exceptionInfoExport) {
        return packetInfoMapper.exportExceptionHistory(exceptionInfoExport);
    }

    @Override
    public PacketForSelect selectPacketHistory(PacketHistoryBean packetHistoryBean) {
        return packetInfoMapper.selectPacketHistory(packetHistoryBean);
    }

    @Override
    public PacketForSelect exportPacketHistory(PacketInfoExport packetInfoExport) {
        return packetInfoMapper.exportPacketHistory(packetInfoExport);
    }

    @Override
    public List<SavedPacket> selectPacketHistoryList(PacketHistoryList packetHistoryList) {
        return packetInfoMapper.selectPacketHistoryList(packetHistoryList);
    }

    @Override
    public PacketHistoryWrapper selectPacketHistoryList(int type) {
        SimpleDateFormat simpleDateFormat;
        LinkedList<String> time = new LinkedList<>();
        List<Integer> data;
        Calendar tempStart = Calendar.getInstance();
        //tempStart.setTime(new Date());
        if (type == 1){
            simpleDateFormat = new SimpleDateFormat("HH:00");
            //1：24小时
            data = packetInfoMapper.selectPacketHistoryIn24Hours();
            for (int i = 0; i < 24; i++) {
                tempStart.add(Calendar.HOUR_OF_DAY,-1);
                time.addFirst(simpleDateFormat.format(tempStart.getTime()));
            }
        }else{
            simpleDateFormat = new SimpleDateFormat("MM-dd");
            data = packetInfoMapper.selectPacketHistoryIn7Days();
            for (int i = 0; i < 7; i++) {
                tempStart.add(Calendar.DAY_OF_MONTH,-1);
                time.addFirst(simpleDateFormat.format(tempStart.getTime()));
            }
        }
        return new PacketHistoryWrapper(time,data);
    }

    @Override
    public String selectPacketRawDataByTimeStamp(String timeStamp) {
        return packetInfoMapper.selectPacketRawDataByTimeStamp(timeStamp);
    }

}
