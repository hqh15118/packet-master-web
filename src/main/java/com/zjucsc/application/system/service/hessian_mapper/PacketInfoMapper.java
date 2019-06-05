package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.*;

import java.util.List;

public interface PacketInfoMapper  {
    /**
     * 分页选取历史
     * @param attackHistoryBean
     * @return
     */
    List<AttackForSelect> selectAttackHistory(AttackHistoryBean attackHistoryBean);

    /**
     * 不分页，导出全部记录
     * @param attackInfoExport
     * @return
     */
    List<AttackForSelect> exportAttackHistory(AttackInfoExport attackInfoExport);

    List<ExceptionForSelect> selectExceptionHistory(ExceptionHistoryBean exceptionHistoryBean);

    List<ExceptionForSelect> exportExceptionHistory(ExceptionInfoExport exceptionInfoExport);

    List<PacketForSelect> selectPacketHistory(PacketHistoryBean packetHistoryBean);

    List<PacketForSelect> exportPacketHistory(PacketInfoExport packetInfoExport);
}
