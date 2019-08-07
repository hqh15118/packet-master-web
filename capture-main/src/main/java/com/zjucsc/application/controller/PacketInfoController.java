package com.zjucsc.application.controller;


import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.non_hessian.PacketRealTimeBean;
import com.zjucsc.application.system.service.hessian_iservice.IPacketInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log/")
public class PacketInfoController {

    @Autowired private IPacketInfoService iPacketInfoService;

    @PostMapping("attack_list")
    public BaseResponse selectAttackHistory(@RequestBody AttackHistoryBean attackHistoryBean){
        return BaseResponse.OK(iPacketInfoService.selectAttackHistory(attackHistoryBean));
    }

    @PostMapping("attack_export")
    public BaseResponse exportAttackHistory(@RequestBody AttackInfoExport attackInfoExport){
        return BaseResponse.OK(iPacketInfoService.exportAttackHistory(attackInfoExport));
    }


    @PostMapping("exception_list")
    public BaseResponse selectExceptionHistory(@RequestBody ExceptionHistoryBean exceptionHistoryBean){
        return BaseResponse.OK(iPacketInfoService.selectExceptionHistory(exceptionHistoryBean));
    }

    @PostMapping("exception_export")
    public BaseResponse exportExceptionHistory(@RequestBody ExceptionInfoExport exceptionInfoExport){
        return BaseResponse.OK(iPacketInfoService.exportExceptionHistory(exceptionInfoExport));
    }

    @PostMapping("packet_list")
    public BaseResponse selectPacketHistory(@RequestBody PacketHistoryBean packetHistoryBean){
        return BaseResponse.OK(iPacketInfoService.selectPacketHistory(packetHistoryBean));
    }

    @PostMapping("packet_export")
    public BaseResponse exportPacketHistory(@RequestBody PacketInfoExport packetInfoExport){
        return BaseResponse.OK(iPacketInfoService.exportPacketHistory(packetInfoExport));
    }

    @PostMapping("packet_list2")
    public BaseResponse selectPacketHistory2(@RequestBody PacketHistoryList packetHistoryList){
        return BaseResponse.OK(iPacketInfoService.selectPacketHistoryList(packetHistoryList));
    }

    @PostMapping("packet_list3")
    public BaseResponse selectPacketHistory(@RequestBody PacketHistoryF packetHistoryF){
        return BaseResponse.OK(iPacketInfoService.selectPacketHistoryList(packetHistoryF.getTimeType()));
    }

    @GetMapping("packet_raw_data")
    public BaseResponse selectPacketByTimeStamp(@RequestParam String timeStamp){
        return BaseResponse.OK(iPacketInfoService.selectPacketRawDataByTimeStamp(timeStamp));
    }

    @PostMapping("real_time_packet")
    public BaseResponse selectRealTimePacketByTimeStamp(@RequestBody PacketRealTimeBean packetRealTimeBean){
        return BaseResponse.OK(iPacketInfoService.selectRealTimePacketList(packetRealTimeBean));
    }
    
}
