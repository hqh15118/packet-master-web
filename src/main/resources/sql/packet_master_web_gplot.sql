INSERT INTO packet_master_web.gplot (id, name, info, update_time) VALUES (48, 'test', '{
  "source": {
    "nodes": [
      {
        "shape": "diannao",
        "x": 497.96908745326573,
        "y": 128,
        "nodeType": 1,
        "label": "电脑111",
        "address": "10.30.11.120",
        "sendNum": 0,
        "receiveNum": 0,
        "attackNum": 0,
        "id": "bb4fd321"
      },
      {
        "shape": "diannao",
        "x": 294,
        "y": 286,
        "nodeType": 1,
        "label": "电脑222",
        "address": "10.30.11.5",
        "sendNum": 0,
        "receiveNum": 0,
        "attackNum": 0,
        "id": "30c9bc12"
      }
    ],
    "edges": []
  },
  "guides": []
}', 'Wed May 22 11:38:41 CST 2019');
INSERT INTO packet_master_web.gplot (id, name, info, update_time) VALUES (58, '测试', '{
  "source": {
    "nodes": [
      {
        "shape": "plc",
        "x": 196.97390626184722,
        "y": 463,
        "nodeType": 5,
        "label": "PLC1",
        "address": "192.168.0.1",
        "sendNum": 0,
        "receiveNum": 0,
        "attackNum": 0,
        "id": "d49711ab"
      },
      {
        "shape": "caijiqi",
        "delay": 0,
        "x": 390.9059910529563,
        "y": 287,
        "nodeType": 4,
        "id": "9e6d83cd",
        "label": "采集器",
        "address": "3"
      },
      {
        "shape": "plc",
        "x": 727.8277098384979,
        "y": 425,
        "nodeType": 5,
        "label": "PLC11",
        "address": "192.168.0.11",
        "sendNum": 0,
        "receiveNum": 0,
        "attackNum": 0,
        "id": "9b5347c9"
      },
      {
        "shape": "diannao",
        "x": 572.9009867744066,
        "y": 53,
        "nodeType": 1,
        "label": "电脑",
        "address": "192.168.0.121",
        "sendNum": 0,
        "receiveNum": 0,
        "attackNum": 0,
        "id": "e4dc59d4"
      }
    ],
    "edges": [
      {
        "shape": "polyLineFlow",
        "source": "9e6d83cd",
        "target": "d49711ab",
        "id": "5de410d4"
      },
      {
        "shape": "polyLineFlow",
        "source": "9e6d83cd",
        "target": "9b5347c9",
        "id": "2244e396"
      },
      {
        "shape": "polyLineFlow",
        "source": "9e6d83cd",
        "target": "e4dc59d4",
        "id": "7244a611"
      }
    ]
  },
  "guides": []
}', 'Wed May 22 20:50:19 CST 2019');