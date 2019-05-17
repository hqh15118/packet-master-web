package com.zjucsc.application.domain.bean;

import java.util.List;

public class Gplot {
    /**
     * source : {"nodes":[{"shape":"diannao","label":"电脑","nodeType":1,"isDevice":true,"address":"192.168.1.1","x":734.4945804681687,"y":78,"id":"b8f260c0"},{"shape":"fuwuqi","label":"服务器","nodeType":2,"address":"192.168.2.1","x":740.4917294985702,"y":229,"id":"9b6b3dd7"},{"shape":"caijiqi","nodeType":4,"x":955.3895697546194,"y":362,"id":"f4d2398d","label":"采集器2","address":"2"},{"shape":"plc","nodeType":5,"x":951.3914704010185,"y":534,"id":"0b88ef35","label":"PLC2","address":"192.168.3.2"},{"shape":"plc","nodeType":5,"x":511.6005415049176,"y":548,"id":"bc4fa4c1","label":"PLC1","address":"192.168.3.1"},{"shape":"caijiqi","nodeType":4,"x":522.59531472732,"y":362,"id":"5a4f4cad","label":"采集器1","address":"1"}],"edges":[{"shape":"line","source":"b8f260c0","target":"9b6b3dd7","id":"60330838"},{"shape":"line","source":"9b6b3dd7","target":"5a4f4cad","id":"ff9f0777"},{"shape":"line","source":"9b6b3dd7","target":"f4d2398d","id":"c647723f"},{"shape":"line","source":"5a4f4cad","target":"bc4fa4c1","id":"89dd6ef9"},{"shape":"line","source":"f4d2398d","target":"0b88ef35","id":"c8dee03a"}]}
     * guides : []
     */

    private Source source;
    private List<?> guides;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public List<?> getGuides() {
        return guides;
    }

    public void setGuides(List<?> guides) {
        this.guides = guides;
    }

    public static class Source {
        private List<Node> nodes;
        private List<Edges> edges;

        public List<Node> getNodes() {
            return nodes;
        }

        public void setNodes(List<Node> nodes) {
            this.nodes = nodes;
        }

        public List<Edges> getEdges() {
            return edges;
        }

        public void setEdges(List<Edges> edges) {
            this.edges = edges;
        }

        public static class Node {
            /**
             * shape : diannao
             * label : 电脑
             * nodeType : 1
             * isDevice : true
             * address : 192.168.1.1
             * x : 734.4945804681687
             * y : 78
             * id : b8f260c0
             */

            private String shape;
            private String label;
            private int nodeType;
            private boolean isDevice;
            private String address;
            private double x;
            private int y;
            private String id;

            public String getShape() {
                return shape;
            }

            public void setShape(String shape) {
                this.shape = shape;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public int getNodeType() {
                return nodeType;
            }

            public void setNodeType(int nodeType) {
                this.nodeType = nodeType;
            }

            public boolean isIsDevice() {
                return isDevice;
            }

            public void setIsDevice(boolean isDevice) {
                this.isDevice = isDevice;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }

            public int getY() {
                return y;
            }

            public void setY(int y) {
                this.y = y;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public static class Edges {
            /**
             * shape : line
             * source : b8f260c0
             * target : 9b6b3dd7
             * id : 60330838
             */

            private String shape;
            private String source;
            private String target;
            private String id;

            public String getShape() {
                return shape;
            }

            public void setShape(String shape) {
                this.shape = shape;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getTarget() {
                return target;
            }

            public void setTarget(String target) {
                this.target = target;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
