package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.bean.RightPacketInfo;

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

    AttackForSelect selectAttackBybadTypeAndLevel(AttackF attackF);

    List<SavedPacket> selectPacketHistoryList(PacketHistoryList packetHistoryList);

    List<Integer> selectPacketHistoryIn7Days();

    List<Integer> selectPacketHistoryIn24Hours();

    /**
     * 存入配置，返回主键ID到data里面
     * @param artAttackConfig
     * @return
     */
    BaseResponse saveOrUpdateArtAttackConfig(ArtAttackConfig artAttackConfig);

    /**
     * 修改工艺参数工艺配置使能状态
     * @param id
     * @param enable
     */
    void updateArtAttackConfigState(int id,boolean enable);

    void deleteArtAttackConfig(int id);

    List<ArtAttackConfigDB> selectArtAttackConfigPaged(int limit,int page);

    String selectPacketRawDataByTimeStamp(String timeStamp);

    void addNormalPacket(RightPacketInfo rightPacketInfo , int gplotId);
}
