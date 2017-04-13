import misc.MagicSquare;


public class MagicSquareTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MagicSquare square = new MagicSquare(6);
		
		System.out.println(square);
		square.rotate();
		System.out.println(square);
		System.out.println(MagicSquare.isMagic(square));
	}

}
