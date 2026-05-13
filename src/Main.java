import java.math.BigInteger;
import java.util.Scanner;

public class Main {


    // 1. GCD (Greatest Common Divisor) - FROM SCRATCH e

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) return a;
        return gcd(b, a.mod(b));
    }


    // 2. POWER (Modular Exponentiation) - FROM SCRATCH
    // Calculates: (base^exp) % mod p/c

    public static BigInteger power(BigInteger base, BigInteger exp, BigInteger mod) {
        BigInteger res = BigInteger.ONE;
        base = base.mod(mod);

        while (exp.compareTo(BigInteger.ZERO) > 0) {
            if (exp.testBit(0)) res = res.multiply(base).mod(mod);
            exp = exp.shiftRight(1);
            base = base.multiply(base).mod(mod);
        }
        return res;
    }


    // 3. MOD INVERSE (Find Private Key d) - FROM SCRATCH
    // Calculates: d such that (d * e) % phi(m) == 1

    public static BigInteger modInverse(BigInteger a, BigInteger m) {
        BigInteger m0 = m;
        BigInteger y = BigInteger.ZERO, x = BigInteger.ONE;
        if (m.equals(BigInteger.ONE)) return BigInteger.ZERO;

        while (a.compareTo(BigInteger.ONE) > 0) {
            BigInteger q = a.divide(m);
            BigInteger t = m;
            m = a.mod(m);
            a = t;
            t = y;
            y = x.subtract(q.multiply(y));
            x = t;
        }
        if (x.compareTo(BigInteger.ZERO) < 0) x = x.add(m0);
        return x;
    }


    // MAIN EXECUTION

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- RSA SYSTEM (With Full Encrypted Output) ---");

        //STEP 1: SETUP KEYS

        BigInteger p = new BigInteger("61");
        BigInteger q = new BigInteger("53");
        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = new BigInteger("17");
        BigInteger d = modInverse(e, phi);

        System.out.println("p=" + p + ", q=" + q);
        System.out.println("n=" + n + " (Modulus)");
        System.out.println("e=" + e + " (Public Key)");
        System.out.println("d=" + d + " (Private Key)");


        //STEP 2: INPUT
        System.out.print("\nEnter your message: ");
        String inputString = scanner.nextLine();


        //STEP 3: ENCRYPTION
        System.out.println("\nEncrypting:");

        BigInteger[] cipherArray = new BigInteger[inputString.length()];
        StringBuilder fullCipherString = new StringBuilder(); // To store the full message

        for (int i = 0; i < inputString.length(); i++) {
            char character = inputString.charAt(i);
            BigInteger m = BigInteger.valueOf((int) character);

            // Encrypt: c = m^e % n
            BigInteger c = power(m, e, n);
            cipherArray[i] = c;

            // Log details
            System.out.println("'" + character + "' -> " + c);

            // Add to full string (separated by spaces)
            fullCipherString.append(c).append(" ");
        }



        System.out.println("FULL ENCRYPTED MESSAGE:");
        System.out.println(fullCipherString.toString());



        //STEP 4: DECRYPTION
        System.out.println("\nDecrypting Back to Text");
        StringBuilder decryptedText = new StringBuilder();

        for (int i = 0; i < cipherArray.length; i++) {
            BigInteger c = cipherArray[i];

            // Decrypt: m = c^d % n
            BigInteger m = power(c, d, n);

            char character = (char) m.intValue();
            decryptedText.append(character);
        }

        System.out.println("Final Result: " + decryptedText.toString());
    }
}