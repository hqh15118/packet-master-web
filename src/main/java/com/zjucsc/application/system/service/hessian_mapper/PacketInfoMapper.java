package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.*;

import java.util.List;

public interface PacketInfoMapper  {
    void saveAttackInfo(SavedAttackPacket savedAttackPacket);
    /**
     * 分页选取历史
     * @param attackHistoryBean
     * @return
     */
    AttackForSelect selectAttackHistory(AttackHistoryBean attackHistoryBean);

    /**
     * 不分页，导出全部记录
     * @param attackInfoExport
     * @return
     */
    AttackForSelect exportAttackHistory(AttackInfoExport attackInfoExport);

    ExceptionForSelect selectExceptionHistory(ExceptionHistoryBean exceptionHistoryBean);

    ExceptionForSelect exportExceptionHistory(ExceptionInfoExport exceptionInfoExport);

    PacketForSelect selectPacketHistory(PacketHistoryBean packetHistoryBean);

    PacketForSelect exportPacketHistory(PacketInfoExport packetInfoExport);

    int handleAttackPacket(List<String> attackPacketTimeStamps);
}
