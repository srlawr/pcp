package pcp;

public class PcpCard {

	private boolean active;
	private Integer cardSeq;
	
	private Integer[][] rawFeed;
	private Integer[][] bytes;
	
	public PcpCard (int byteCount) {
		this.cardSeq = 0; // @TODO - pass this byte in
		rawFeed = new Integer[byteCount][8];
	}
	
	public void setBit(int x, int y, int bit) {
		this.rawFeed[x][y] = bit;
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
			sb.append(System.getProperty("line.separator"));
		}
		sb.append("ACTIVE: ");
		sb.append(this.active);
		return sb.toString();
	}
	
}
