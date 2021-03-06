package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.non_hessian.PacketRealTimeBean;

import java.util.List;

public interface IPacketInfoService {

    AttackForSelect selectAttackHistory(AttackHistoryBean attackHistoryBean);

    AttackForSelect exportAttackHistory(AttackInfoExport attackInfoExport);

    ExceptionForSelect selectExceptionHistory(ExceptionHistoryBean exceptionHistoryBean);

    ExceptionForSelect exportExceptionHistory(ExceptionInfoExport exceptionInfoExport);

    PacketForSelect selectPacketHistory(PacketHistoryBean packetHistoryBean);

    PacketForSelect exportPacketHistory(PacketInfoExport packetInfoExport);

    List<SavedPacket> selectPacketHistoryList(PacketHistoryList packetHistoryList);

    PacketHistoryWrapper selectPacketHistoryList(int type);

    String selectPacketRawDataByTimeStamp(String timeStamp);

    List<SavedPacket> selectRealTimePacketList(PacketRealTimeBean packetRealTimeBean);

    List<RightPacketInfo> selectAllRightPacketInfo();
}
