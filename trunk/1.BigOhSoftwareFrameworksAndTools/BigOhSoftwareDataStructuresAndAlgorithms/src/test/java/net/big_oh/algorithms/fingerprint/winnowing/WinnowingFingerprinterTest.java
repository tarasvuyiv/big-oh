package net.big_oh.algorithms.fingerprint.winnowing;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.big_oh.algorithms.fingerprint.winnowing.WinnowingFingerprinter;

import junit.framework.TestCase;

public class WinnowingFingerprinterTest extends TestCase
{

	WinnowingFingerprinter fingerprinter;

	List<String> sourceDocuments;

	@Override
	public void setUp() throws Exception
	{
		fingerprinter = new WinnowingFingerprinter(5, 6);

		sourceDocuments = new ArrayList<String>();
		sourceDocuments.add("The quick brown fox jumped over the lazy dog.");
		sourceDocuments.add("Malt does more than Milton can to justify God's ways to man.");
		sourceDocuments.add("Short.");
		sourceDocuments.add("");
	}

	public void testFingerprint() throws NoSuchAlgorithmException
	{
		String garbageString = "#%&%^&$%@#$@#%$%^*^*&^*&^*";

		// confirm that matches of length k-1 are never detected
		for (String doc : sourceDocuments)
		{

			// test match of length k-1 taken from all locations inside doc
			for (int i = 0; i + fingerprinter.getK() <= doc.length(); i++)
			{

				// fingerprint the original doc
				Set<BigInteger> docFingerprints = fingerprinter.fingerprint(doc);

				// construct & fingerprint a partial copy string.
				String substring = doc.substring(i, i + fingerprinter.getK() - 1);
				String partialCopy = substring + garbageString + substring + garbageString + substring;
				Set<BigInteger> partialCopyFingerprints = fingerprinter.fingerprint(partialCopy);

				// expect no shared values between the docFingerprints &
				// partialCopyFingerprints Sets.
				for (BigInteger matchCandidate : docFingerprints)
				{
					assertTrue(!partialCopyFingerprints.contains(matchCandidate));
				}
				for (BigInteger matchCandidate : partialCopyFingerprints)
				{
					assertTrue(!docFingerprints.contains(matchCandidate));
				}

			}

		}

		// confirm that matches of length t are always detected
		for (String doc : sourceDocuments)
		{
			// test match of length t taken from all locations inside doc
			for (int i = 0; i + fingerprinter.getT() <= doc.length(); i++)
			{

				// fingerprint the original doc
				Set<BigInteger> docFingerprints = fingerprinter.fingerprint(doc);

				// construct & fingerprint a partial copy string.
				String substring = doc.substring(i, i + fingerprinter.getT());
				String partialCopy = garbageString + substring + garbageString;
				Set<BigInteger> partialCopyFingerprints = fingerprinter.fingerprint(partialCopy);

				// expect at least one shared value between the docFingerprints
				// & partialCopyFingerprints Sets.
				docFingerprints.retainAll(partialCopyFingerprints);
				assertTrue(docFingerprints.size() > 0);

			}
		}

	}

	public void testSelectMin()
	{
		// confirm that the minimum value is really selected

		BigInteger[] testIntegers1 = { new BigInteger("0"), new BigInteger("1"), new BigInteger("2"), new BigInteger("3") };
		assertEquals(new BigInteger("0"), fingerprinter.selectMin(Arrays.asList(testIntegers1)));

		BigInteger[] testIntegers2 = { new BigInteger("1"), new BigInteger("2"), new BigInteger("0"), new BigInteger("3") };
		assertEquals(new BigInteger("0"), fingerprinter.selectMin(Arrays.asList(testIntegers2)));

		BigInteger[] testIntegers3 = { new BigInteger("1"), new BigInteger("2"), new BigInteger("3"), new BigInteger("0") };
		assertEquals(new BigInteger("0"), fingerprinter.selectMin(Arrays.asList(testIntegers3)));
	}

	public void testGenerateKGrams()
	{
		// confirm that the number of kGrams generated is correct
		for (String doc : sourceDocuments)
		{
			assertEquals(Math.max(0, (doc.length() - (fingerprinter.getK() - 1))), fingerprinter.generateKGrams(doc).size());
		}
	}

	public void testHashKGram() throws NoSuchAlgorithmException
	{

		String kGram1 = "kGram1";
		String kGram2 = "kGram2";

		// confirm that a given string always hashes to the same value
		assertEquals(fingerprinter.hashKGram(kGram1), fingerprinter.hashKGram(kGram1));

		// confirm that different kGrams hash to different values
		assertTrue(!fingerprinter.hashKGram(kGram1).equals(fingerprinter.hashKGram(kGram2)));

	}

}
