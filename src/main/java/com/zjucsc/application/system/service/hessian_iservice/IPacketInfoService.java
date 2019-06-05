package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.*;

import java.util.List;

public interface IPacketInfoService {

    List<AttackForSelect> selectAttackHistory(AttackHistoryBean attackHistoryBean);

    List<AttackForSelect> exportAttackHistory(AttackInfoExport attackInfoExport);

    List<ExceptionForSelect> selectExceptionHistory(ExceptionHistoryBean exceptionHistoryBean);

    List<ExceptionForSelect> exportExceptionHistory(ExceptionInfoExport exceptionInfoExport);

    List<PacketForSelect> selectPacketHistory(PacketHistoryBean packetHistoryBean);

    List<PacketForSelect> exportPacketHistory(PacketInfoExport packetInfoExport);

}
