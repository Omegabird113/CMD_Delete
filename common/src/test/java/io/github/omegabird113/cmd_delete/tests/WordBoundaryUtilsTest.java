/*
 * Copyright (c) 2026 Omegabird113.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.omegabird113.cmd_delete.tests;

import io.github.omegabird113.cmd_delete.TestLoader;
import io.github.omegabird113.cmd_delete.utils.WordBoundaryUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

final class WordBoundaryUtilsTest {
	private static final String MULTILINE_TEXT = "alpha beta\ngamma";

	@BeforeAll
	static void beforeAll() {
		TestLoader.setup();
	}

	@Test
	void vanillaBoundariesMatchEditBoxAcrossMultilineText() {
		Assertions.assertEquals(6, WordBoundaryUtils.getVanillaBoundary(MULTILINE_TEXT, 5, 1));
		Assertions.assertEquals(MULTILINE_TEXT.length(), WordBoundaryUtils.getVanillaBoundary(MULTILINE_TEXT, 10, 1));
		Assertions.assertEquals(6, WordBoundaryUtils.getVanillaBoundary(MULTILINE_TEXT, 11, -1));
	}

	@Test
	void macBoundariesTraverseMultilineText() {
		Assertions.assertEquals(10, WordBoundaryUtils.getMacBoundary(MULTILINE_TEXT, 5, 1));
		Assertions.assertEquals(MULTILINE_TEXT.length(), WordBoundaryUtils.getMacBoundary(MULTILINE_TEXT, 10, 1));
		Assertions.assertEquals(6, WordBoundaryUtils.getMacBoundary(MULTILINE_TEXT, 11, -1));
	}

	@Test
	void windowsLinuxBoundariesTraverseMultilineText() {
		Assertions.assertEquals(6, WordBoundaryUtils.getWindowsLinuxBoundary(MULTILINE_TEXT, 5, 1));
		Assertions.assertEquals(11, WordBoundaryUtils.getWindowsLinuxBoundary(MULTILINE_TEXT, 10, 1));
		Assertions.assertEquals(6, WordBoundaryUtils.getWindowsLinuxBoundary(MULTILINE_TEXT, 11, -1));
	}

	@Test
	void boundariesRemainWithinTextAndRespectCharacterClasses() {
		final String text = "word, next";
		Assertions.assertEquals(4, WordBoundaryUtils.getMacBoundary(text, 0, 1));
		Assertions.assertEquals(6, WordBoundaryUtils.getVanillaBoundary(text, 4, 1));
		Assertions.assertEquals(text.length(), WordBoundaryUtils.getVanillaBoundary(text, text.length(), 1));
		Assertions.assertEquals(0, WordBoundaryUtils.getMacBoundary(text, 0, -1));
	}

	@Test
	void vanillaBoundariesUseOnlyAsciiSpacesLikeEditBox() {
		final String text = "one\ttwo\nthree";
		Assertions.assertEquals(text.length(), WordBoundaryUtils.getVanillaBoundary(text, 0, 1));
		Assertions.assertEquals(0, WordBoundaryUtils.getVanillaBoundary(text, text.length(), -1));
		Assertions.assertEquals(5, WordBoundaryUtils.getVanillaBoundary("one  two", 3, 1));
	}
}
