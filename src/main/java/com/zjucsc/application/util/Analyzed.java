package com.zjucsc.application.util;

import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 21:34
 */
public interface Analyzed {
    Object analyze(Object...objs) throws ProtocolIdNotValidException;
}
