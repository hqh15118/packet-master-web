package com.zjucsc.application.domain.non_hessian;

import lombok.Data;
import org.pcap4j.packet.Packet;

import java.util.Iterator;

@Data
public class CustomPacket implements Packet {

    private byte[] rawData;

    public CustomPacket(){}

    @Override
    public Header getHeader() {
        return null;
    }

    @Override
    public Packet getPayload() {
        return this;
    }

    @Override
    public int length() {
        return rawData.length;
    }

    @Override
    public byte[] getRawData() {
        return rawData;
    }

    @Override
    public <T extends Packet> T get(Class<T> clazz) {
        return null;
    }

    @Override
    public Packet getOuterOf(Class<? extends Packet> clazz) {
        return null;
    }

    @Override
    public <T extends Packet> boolean contains(Class<T> clazz) {
        return false;
    }

    @Override
    public Builder getBuilder() {
        return null;
    }

    @Override
    public Iterator<Packet> iterator() {
        return null;
    }
}
