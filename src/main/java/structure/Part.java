package structure;

import transforms.Col;

public class Part{
    private int startIndex;
    private int count;
    private TopologyType topologyType;
    private Col color;

    public Part(int startIndex, int count, TopologyType topologyType) {
        this.startIndex = startIndex;
        this.count = count;
        this.topologyType = topologyType;
        this.color = new Col(0xFFFFFF);
    }

    public void setColor(Col color) {
        this.color = color;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getCount() {
        return count;
    }

    public TopologyType getTopologyType() {
        return topologyType;
    }

    public Col getColor() {
        return color;
    }
}
