package DataMiningLab.dynamicHashing;

public class DynamicHashing {
	private int bits;
	private int identity;
	private int noOfBuckets;
	private int sizeOfBucket;
	private Bucket buckets[];
	private BinaryConversion bc;
	
	public DynamicHashing(int bits, int sizeOfBucket){
		this.bits = bits;
		this.sizeOfBucket = sizeOfBucket;
		identity = 0;
		noOfBuckets = (int)Math.pow(2, bits);
		buckets = new Bucket[1];
		buckets[0] = new Bucket("");
		bc = new BinaryConversion();
	}
	public int h(int num){
		return num % noOfBuckets;
	}
	
	public void insert(int num){
		int index = h(num);
		String id = bc.decToBin(index, bits).substring(0, identity);
		if(identity == 0){
			if(sizeOfBucket == buckets[0].getSize()){
				redistribute(num, buckets,0);
			}
			else
				buckets[0].insertInBucket(num);
		}
		else{
			index = bc.binToDec(id);
			if(sizeOfBucket == buckets[index].getSize()){
				redistribute(num, buckets,index);
			}
			else
				buckets[index].insertInBucket(num);
		}
	}
	public void redistribute(int num, Bucket[] oldBucket, int index){
		if(identity == oldBucket[index].getI()){
			identity++;
			//noOfBuckets = (int)Math.pow(2, identity);
			Bucket[] newBucket = new Bucket[(int)Math.pow(2, identity)];
			for(int i = 0, j =0 ; i < (int)Math.pow(2, identity); i+=2, j++){
				newBucket[i] = newBucket[i+1] = oldBucket[j];
			}
			String idx = oldBucket[index].getHeader();
			String index1 = idx + "0";
			String index2 = idx + "1";
			newBucket[bc.binToDec(index1)] = new Bucket(index1);
			newBucket[bc.binToDec(index2)] = new Bucket(index2);
			for(int i = 0 ; i < oldBucket[index].getSize(); i++){
				int temp = oldBucket[index].getBucketContent(i);
				newBucket[bc
				        .binToDec(bc.decToBin(h(temp), bits)
				        		.substring(0, identity))].insertInBucket(temp);
			}
			buckets = newBucket;
			insert(num);
		}
		else if(identity > oldBucket[index].getI()){
			System.out.println("entered");
			int lowerLimit = 0;
			int upperLimit = (int)Math.pow(2, identity);
			for(int i = index; i >= 0; i--){
				if(oldBucket[index] != oldBucket[i]){
					lowerLimit = i+1;
					break;
				}
			}
			for(int i = index; i < (int)Math.pow(2, identity) ; i++){
				if(oldBucket[index] != oldBucket[i]){
					upperLimit = i-1;
					break;
				}
			}
			int middle = (int)Math.ceil((upperLimit + lowerLimit)/2.0);
			String idx = oldBucket[index].getHeader();
			String index1 = idx + "0";
			String index2 = idx + "1";
			Bucket newBucket1 = new Bucket(index1);
			Bucket newBucket2 = new Bucket(index2);
			for(int i = 0 ; i < oldBucket[index].getSize(); i++){
				int temp = oldBucket[index].getBucketContent(i);
				int tempH = h(temp);
				String tempHBin = bc.decToBin(tempH, bits);
				if(index2.equals(tempHBin.substring(0, index2.length()))){
					newBucket2.insertInBucket(temp);
				}
				else{
					newBucket1.insertInBucket(temp);
				}
			}
			for(int i = lowerLimit ; i < middle ; i++) buckets[i] = newBucket1;
			for(int i = middle; i < upperLimit; i++) buckets[i] = newBucket2;
			insert(num);
		}
		else{
			System.err.println("Error detected");
		}
	}
	
	public void redistribute(Bucket bucket){
		String index = bucket.getHeader();
		String index1 = index + "0";
		String index2 = index + "1";
		buckets[bc.binToDec(index1)] = new Bucket(index1);
		buckets[bc.binToDec(index2)] = new Bucket(index2);
		for(int i = 0 ; i < bucket.getSize(); i++){
			int temp = bucket.getBucketContent(i);
			int idx = h(temp);
			String id = bc.decToBin(idx, bits).substring(0, identity);
			idx = bc.binToDec(id);
			buckets[idx].insertInBucket(temp);
		}
		//bucket.empty();
	}
	
	public void show(){
		for(int i = 0 ; i < (int)Math.pow(2,identity); i++){
			Bucket bucket = buckets[i];
			System.out.println("["+i+"]:");
			System.out.println("bucket (" + bucket.getHeader() + "):");
			for(int j = 0 ; j < bucket.getSize() ; j++){
				System.out.println(bucket.getBucketContent(j));
				
			}
		}
	}
}
