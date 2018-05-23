package pcp;

import java.util.ArrayList;
import java.util.List;

public class PcpCard {

	private boolean active;
	private Integer cardSeq;
	
	private Integer[][] rawFeed;
	private List<Integer> bytes;
	
	public PcpCard (int byteCount) {
		this.cardSeq = 0; // @TODO - pass this byte in
		rawFeed = new Integer[byteCount][8];
		bytes = new ArrayList<Integer>();
	}
	
	public void setBit(int x, int y, int bit) {
		this.rawFeed[x][y] = bit;
	}

	public int byteSize() {
		return bytes.size();
	}

	public Integer getByte(int index) {
		return this.bytes.get(index);
	}

	public void setByte(int byteValue) {
		this.bytes.add(byteValue);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Card: ");
		sb.append(this.cardSeq);
		sb.append(System.getProperty("line.separator"));
		for(int x = 0; x < rawFeed.length; x++) {
			for(int y = 0; y < rawFeed[0].length; y++) {
				sb.append(rawFeed[x][y]);	
			}
			sb.append(" = " + bytes.get(x));
			sb.append(System.getProperty("line.separator"));
		}
		sb.append("ACTIVE: ");
		sb.append(this.active);
		return sb.toString();
	}
	
}
