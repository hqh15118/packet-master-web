package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.system.service.hessian_iservice.IPacketInfoService;
import com.zjucsc.application.system.service.hessian_mapper.PacketInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
