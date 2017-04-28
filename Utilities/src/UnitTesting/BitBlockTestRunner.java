package UnitTesting;

import ksk.math.BitBlock;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class BitBlockTestRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(BitBlockTest.class);

		System.out.println("Running BitBlock Unit Tests");

		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		int passed = result.getRunCount() - result.getFailureCount();

		System.out.println(passed + " of " + result.getRunCount() + " tests passed.");

	}


}
