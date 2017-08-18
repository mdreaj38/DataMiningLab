package DataMiningLab.dynamicHashing;

public class BinaryConversion {
	public String decToBin(int dec, int length){
		String bin = Integer.toBinaryString(dec);
		for(int i = bin.length(); i < length ; i++)
			bin = "0" + bin;
		return bin;
	}
	public int binToDec(String bin){
		return Integer.parseInt(bin, 2);
	}
}
