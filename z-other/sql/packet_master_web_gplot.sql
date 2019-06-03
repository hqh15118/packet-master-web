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
        "shape": "diannao",
        "x": 911.7315055774408,
        "y": 109.13639846743291,
        "nodeType": 1,
        "label": "操作员站",
        "address": "192.168.0.1",
        "sendNum": 5.6,
        "receiveNum": 0,
        "attackNum": 0,
        "id": "d036a9a8"
      },
      {
        "shape": "diannao",
        "x": 297.99801169714476,
        "y": 234.86053639846745,
        "nodeType": 1,
        "label": "电脑",
        "address": "192.168.0.121",
        "sendNum": 0,
        "receiveNum": 0,
        "attackNum": 0,
        "id": "e4dc59d4"
      },
      {
        "shape": "caijiqi",
        "delay": 37.4,
        "x": 584.8375443801884,
        "y": 262.0191570881225,
        "nodeType": 4,
        "id": "9e6d83cd",
        "label": "采集器",
        "address": "3"
      },
      {
        "shape": "plc",
        "x": 983.7373884558763,
        "y": 251.13333333333344,
        "nodeType": 5,
        "label": "PLC11",
        "address": "192.168.0.11",
        "sendNum": 0,
        "receiveNum": 5.6,
        "attackNum": 0,
        "id": "9b5347c9"
      }
    ],
    "edges": [
      {
        "shape": "polyLineFlow",
        "source": "9e6d83cd",
        "target": "e4dc59d4",
        "id": "7244a611"
      },
      {
        "shape": "polyLineFlow",
        "source": "9e6d83cd",
        "target": "d036a9a8",
        "id": "94f4fd7c"
      },
      {
        "shape": "polyLineFlow",
        "source": "9b5347c9",
        "target": "9e6d83cd",
        "id": "637bee54"
      }
    ]
  },
  "guides": []
}', 'Thu May 23 16:23:45 CST 2019');