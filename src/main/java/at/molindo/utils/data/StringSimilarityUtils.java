/**
 * Copyright 2016 Molindo GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.molindo.utils.data;

/**
 * implementation of the Needleman-Wunsch algorithm for string similarity
 */
public class StringSimilarityUtils {

	private StringSimilarityUtils() {
	}

	private static final int GAP_COST = 2;

	public static float similarity(final String string1, final String string2) {
		return similarity(string1, string2, 0.0f);
	}

	public static float similarity(final String string1, final String string2, final float min) {
		if (string1.equals(string2)) {
			return 1.0f;
		}

		final int l1 = string1.length();
		final int l2 = string2.length();

		// max value is to create longest string from ""
		final int maxValue = Math.max(l1, l2) * GAP_COST;

		final float maxGap = (1.0f - min) * maxValue;

		// min value is length difference - abort if this is already > maxGap
		if (Math.abs(l1 - l2) * GAP_COST > maxGap) {
			return 0.0f;
		}

		// calc
		final int needlemanWunch = unnormalisedSimilarity(string1, string2, maxGap);

		// return actual / possible NeedlemanWunch distance to get 0-1 range
		final float normalised = 1.0f - needlemanWunch / (float) maxValue;
		return normalised < min ? 0.0f : normalised;

	}

	public static int unnormalisedSimilarity(final String string1, final String string2) {
		return unnormalisedSimilarity(string1, string2, Float.MAX_VALUE);
	}

	/**
	 * implements the NeedlemanWunch distance function.
	 * 
	 * @param maxGap
	 * 
	 * @param s
	 * @param t
	 * @param maxGap
	 * @return the NeedlemanWunch distance for the given strings
	 */
	public static int unnormalisedSimilarity(final String string1, final String string2, final float maxGap) {
		final char[] s = string1.toCharArray();
		final char[] t = string2.toCharArray();

		final int[][] d; // matrix
		final int n; // length of s
		final int m; // length of t
		int i; // iterates through s
		int j; // iterates through t
		int cost; // cost

		// check for zero length input
		n = s.length;
		m = t.length;
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}

		// create matrix (n+1)x(m+1)
		d = new int[n + 1][m + 1];

		// put row and column numbers in place
		for (i = 0; i <= n; i++) {
			d[i][0] = i;
		}
		for (j = 0; j <= m; j++) {
			d[0][j] = j;
		}

		// cycle through rest of table filling values from the lowest cost value
		// of the three part cost function
		for (i = 1; i <= n; i++) {
			int rowMin = 0;
			for (j = 1; j <= m; j++) {
				// get the substution cost
				cost = cost(s, i - 1, t, j - 1);

				// find lowest cost at point from three possible
				d[i][j] = min3(d[i - 1][j] + GAP_COST, d[i][j - 1] + GAP_COST, d[i - 1][j - 1] + cost);

				if (d[i][j] < rowMin) {
					rowMin = d[i][j];
				}
			}
			if (rowMin > maxGap) {
				// break - it will exceed maxGap
				return rowMin;
			}
		}

		// return bottom right of matrix as holds the maximum edit score
		return d[n][m];
	}

	private static int min3(final int x, final int y, final int z) {
		final int min = y <= z ? y : z;
		return x <= min ? x : min;
	}

	/**
	 * get cost between characters where d(i,j) = 1 if i does not equals j, 0 if
	 * i equals j.
	 * 
	 * @param str1
	 *            - the string1 to evaluate the cost
	 * @param string1Index
	 *            - the index within the string1 to test
	 * @param str2
	 *            - the string2 to evaluate the cost
	 * @param string2Index
	 *            - the index within the string2 to test
	 * @return the cost of a given substitution d(i,j) where d(i,j) = 1 if i!=j,
	 *         0 if i==j
	 */
	public static final int cost(final char[] str1, final int string1Index, final char[] str2, final int string2Index) {
		if (str1[string1Index] == str2[string2Index]) {
			return 0;
		} else {
			return 1;
		}
	}

}
