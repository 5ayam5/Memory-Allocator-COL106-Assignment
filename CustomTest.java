import java.util.Scanner;

public class CustomTest {

	static void driver(int type) {
		int numTestCases;
		Scanner sc = new Scanner(System.in);
		numTestCases = sc.nextInt();
		int testNo = 0, err = 0;
		while (numTestCases-- > 0) {
			++testNo;
			int size, cmd = 0;
			size = sc.nextInt();
			DynamicMem obj = new A2DynamicMem(size, type);
			while (sc.hasNext()) {
				++cmd;
				String command;
				command = sc.next();
				int argument;
				argument = sc.nextInt();
				switch (command) {
					case "Allocate":
						System.out.println(obj.Allocate(argument));
						break;
					case "Free":
						System.out.println(obj.Free(argument));
						break;
					case "Defragment":
						obj.Defragment();
						break;
					case "Sanity":
						System.out.println(obj.freeBlk.sanity() + ", " + obj.allocBlk.sanity());
						break;
					default:
						break;
				}
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		long time = System.nanoTime();
		driver(1);
		System.err.println((System.nanoTime() - time) / 1e9);
	}
}
