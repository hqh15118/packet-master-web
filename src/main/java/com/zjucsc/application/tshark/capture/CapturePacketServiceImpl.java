package com.zjucsc.application.tshark.capture;

import com.zjucsc.application.handler.ThreadExceptionHandler;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.decode.DefaultPipeLine;
import com.zjucsc.application.tshark.domain.packets_2.FvDimensionLayer;
import com.zjucsc.application.tshark.handler_2.BadPacketAnalyzeHandler;
import com.zjucsc.application.tshark.pre_processor.BasePreProcessor;
import com.zjucsc.application.tshark.pre_processor.ModbusPreProcessor;
import com.zjucsc.application.tshark.pre_processor.S7CommPreProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class CapturePacketServiceImpl implements CapturePacketService<String,String>{

    private List<BasePreProcessor<?>> processorList = new ArrayList<>();
    private ProcessCallback<String,String> callback;
    private BadPacketAnalyzeHandler badPacketAnalyzeHandler = new BadPacketAnalyzeHandler(Executors.newFixedThreadPool(5,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("bad packet analyze handler - ");
                thread.setUncaughtExceptionHandler(new ThreadExceptionHandler());
                return thread;
            }));

    @Override
    public void start(ProcessCallback<String,String> callback) {
        this.callback = callback;
        /**
         * 接收协议解析好的五元组，并作五元组发送、报文流量统计处理
         */
        AbstractAsyncHandler<FvDimensionLayer> fvDimensionLayerAbstractAsyncHandler
                = new AbstractAsyncHandler<FvDimensionLayer>(Executors.newFixedThreadPool(10, r -> {
                    Thread thread = new Thread(r);
                    thread.setName("fv_dimension_handler_thread");
                    return thread;
                })) {
            @Override
            public FvDimensionLayer handle(Object t) {
                FvDimensionLayer fvDimensionLayer = ((FvDimensionLayer) t);
                sendFvDimensionPacket(fvDimensionLayer);    //五元组所有报文
                sendPacketStasticsEvent(fvDimensionLayer);  //统计信息
                return fvDimensionLayer;
            }
        };

        callback.start(doStart(fvDimensionLayerAbstractAsyncHandler , new ModbusPreProcessor() ,
                               new S7CommPreProcessor()
                               ));
    }

    @Override
    public void stop() {
        for (BasePreProcessor<?> basePreProcessor : processorList) {
            basePreProcessor.stopProcess();
            callback.end("end " + basePreProcessor.getClass().getName());
        }
    }

    private String doStart(AbstractAsyncHandler<FvDimensionLayer> fvDimensionHandler ,
                           BasePreProcessor<?>... packetPreProcessor){
        StringBuilder sb = new StringBuilder();
        for (BasePreProcessor<?> basePreProcessor : packetPreProcessor) {
            String processName = basePreProcessor.getClass().getName();
            processorList.add(basePreProcessor);
            DefaultPipeLine pipeLine = new DefaultPipeLine(processName);
            pipeLine.addLast(fvDimensionHandler);
            pipeLine.addLast(badPacketAnalyzeHandler);
            basePreProcessor.setPipeLine(pipeLine);
            Thread processThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    basePreProcessor.execCommand();
                }
            });
            processThread.setName(processName + " - thread");
            processThread.start();
            sb.append(basePreProcessor.getClass().getName()).append(" start");
        }
        return sb.toString();
    }


    private void sendFvDimensionPacket(FvDimensionLayer fvDimensionLayer){

    }

    private void sendPacketStasticsEvent(FvDimensionLayer fvDimensionLayer){

    }
}
