public class Obx extends HEX{
	private int value = 2;
	private int m = 0;
	private int n =0;
	public Obx(){
//		this.m = 9;
//		super.m = 10;
		this.m =10;
		this.n = 2;
		System.out.println(this.m+"**********"+super.m);
		System.out.println(this.add()+"----"+this.add(m,n));
	}
}