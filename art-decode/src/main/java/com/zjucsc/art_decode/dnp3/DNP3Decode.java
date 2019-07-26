package com.zjucsc.art_decode.dnp3;

import com.zjucsc.art_decode.artconfig.DNP3Config;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import java.util.*;

public class DNP3Decode extends BaseArtDecode<DNP3Config> {


    public Map<String, Float> decode_tech(DNP3Config DNP3tech, Map<String, Float> tech_map, byte[] load, FvDimensionLayer fvDimensionLayer) {
        byte[] dnp3load = load;
        int start = 0;
        int stop = 0;
        int infobyte = 0;
        int length, length1, bytes;
        float output = 0f;
        length = Byte.toUnsignedInt(dnp3load[2]);
        length1 = length + 5;
        byte[] editedload = new byte[length1];
        int i = 0, j = 10, n = 0, m = 0, x;
        length = (length - 5) / 16 * 2 + length + 2 + 3;
        if ((length - 5) % 16 > 0) {
            length = length + 2;
        }
        for (i = 0; i <= 9; i++) {
            editedload[i] = dnp3load[i];
        }
        for (i = 10; i <= (length - 3); i++) {
            if (((i - 8) % 18 != 0) && ((i - 9) % 18 != 0)) {
                editedload[j] = dnp3load[i];
                j = j + 1;
            }
        }
        if ((editedload[3] & 0x8f) == 0x04) {
            if ((editedload[11] & 0x20) == 0x20) {
                if (Byte.toUnsignedInt(editedload[12]) == 0x81) {
                    if (j >= 23) {
                        infobyte = 15;
                        do {
                            if (editedload[infobyte] == 0x1e) {
                                if (editedload[(infobyte + 1)] == 0x04) {
                                    if (editedload[(infobyte + 2)] == 0x01) {
                                        start = Byte.toUnsignedInt(editedload[(infobyte + 3)]) + (Byte.toUnsignedInt(editedload[(infobyte + 4)]) << 8);
                                        stop = Byte.toUnsignedInt(editedload[(infobyte + 5)]) + (Byte.toUnsignedInt(editedload[(infobyte + 6)]) << 8);
                                        for (n = 0; n <= (stop - start); n++) {
                                            if ((n + start) == DNP3tech.getindex() && DNP3tech.getObjGroup() == 0x1e) {
                                                output = (float) (Byte.toUnsignedInt(editedload[(infobyte + 7 + 2 * n)]) + (Byte.toUnsignedInt(editedload[(infobyte + 8 + 2 * n)]) << 8));
                                                tech_map.put(DNP3tech.getTag(), output);
                                                callback(DNP3tech.getTag(), output, fvDimensionLayer);
                                            }
                                        }
                                        infobyte = infobyte + 6 + (stop - start + 1) * 2;
                                    }
                                }
                                else if (editedload[(infobyte + 1)] == 0x02) {
                                    if (editedload[(infobyte + 2)] == 0x01) {
                                        start = Byte.toUnsignedInt(editedload[(infobyte + 3)]) + (Byte.toUnsignedInt(editedload[(infobyte + 4)]) << 8);
                                        stop = Byte.toUnsignedInt(editedload[(infobyte + 5)]) + (Byte.toUnsignedInt(editedload[(infobyte + 6)]) << 8);
                                        for (n = 0; n <= (stop - start); n++) {
                                            if ((n + start) == DNP3tech.getindex() && DNP3tech.getObjGroup() == 0x1e) {
                                                output = (float) (Byte.toUnsignedInt(editedload[(infobyte + 7 + 3 * n)]) + (Byte.toUnsignedInt(editedload[(infobyte + 8 + 3 * n)]) << 8));
                                                tech_map.put(DNP3tech.getTag(), output);
                                                callback(DNP3tech.getTag(), output, fvDimensionLayer);
                                            }
                                        }
                                        infobyte = infobyte + 6 + (stop - start + 1) * 3;
                                    }
                                }
                            }
                            infobyte = infobyte + 1;
                        } while (infobyte < j);
                    }
                }
            } else {
                if (Byte.toUnsignedInt(editedload[12]) == 0x81) {
                    if (j >= 22) {
                        infobyte = 15;
                        do {
                            if (editedload[(infobyte)] == 0x01 && editedload[(infobyte + 1)] == 0x01) {
                                start = Byte.toUnsignedInt(editedload[(infobyte + 3)]) + (Byte.toUnsignedInt(editedload[(infobyte + 4)]) << 8);
                                stop = Byte.toUnsignedInt(editedload[(infobyte + 5)]) + (Byte.toUnsignedInt(editedload[(infobyte + 6)]) << 8);
                                bytes = (stop - start + 1) / 8;
                                if ((stop - start + 1) % 8 > 0) {
                                    bytes = bytes + 1;
                                }
                                for (n = 0; n < bytes; n++) {
                                    for (m = 7; m >= 0; m--) {
                                        x = 8 * n + 7 - m;
                                        if ((8 * n + 7 - m) == DNP3tech.getindex() && DNP3tech.getObjGroup() == 0x01) {
                                            output = (editedload[(infobyte + 7 + n)] >> (7 - m)) & 0x01;
                                            tech_map.put(DNP3tech.getTag(), output);
                                            callback(DNP3tech.getTag(), output, fvDimensionLayer);
                                        }
                                    }
                                }
                                infobyte = infobyte + 6 + bytes;
                            }
                            infobyte = infobyte + 1;
                        } while (infobyte < j);
                    }
                }
            }
        }
        return tech_map;
    }

    @Override
    public Map<String, Float> decode(DNP3Config dnp3Config, Map<String, Float> globalMap, byte[] payload, FvDimensionLayer fvDimensionLayer, Object... obj) {
        return decode_tech(dnp3Config, globalMap, payload, fvDimensionLayer);
    }

    @Override
    public String protocol() {
        return "DNP3.0";
    }
}

