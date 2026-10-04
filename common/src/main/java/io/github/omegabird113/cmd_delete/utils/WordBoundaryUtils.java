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

package io.github.omegabird113.cmd_delete.utils;

import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import org.jspecify.annotations.NonNull;

public final class WordBoundaryUtils {
	private WordBoundaryUtils () {
	}

	public static int getMacBoundary(final @NonNull String text, int pos, final int dir) {
		final int len = text.length();
		if (dir > 0) {
			while (pos < len) {
				int cp = text.codePointAt(pos);
				if (!Character.isWhitespace(cp))
					break;
				pos += Character.charCount(cp);
			}
			if (pos >= len)
				return len;

			final int cp1 = text.codePointAt(pos);
			final int kind = (Character.isLetterOrDigit(cp1) || cp1 == '_') ? 0 : 1;
			while (pos < len) {
				final int cp = text.codePointAt(pos);
				if (Character.isWhitespace(cp) || ((Character.isLetterOrDigit(cp) || cp == '_') ? 0 : 1) != kind)
					break;
				pos += Character.charCount(cp);
			}
		} else {
			while (pos > 0) {
				final int cp = text.codePointBefore(pos);
				if (!Character.isWhitespace(cp))
					break;
				pos -= Character.charCount(cp);
			}
			if (pos == 0)
				return 0;

			final int cp1 = text.codePointBefore(pos);
			final int kind = (Character.isLetterOrDigit(cp1) || cp1 == '_') ? 0 : 1;
			while (pos > 0) {
				final int cp = text.codePointBefore(pos);
				if (Character.isWhitespace(cp) || ((Character.isLetterOrDigit(cp) || cp == '_') ? 0 : 1) != kind)
					break;
				pos -= Character.charCount(cp);
			}
		}
		return pos;
	}

	public static int getWindowsLinuxBoundary(final @NonNull String text, int pos, final int dir) {
		final int len = text.length();
		if (dir > 0) {
			if (pos < len && !Character.isWhitespace(text.codePointAt(pos))) {
				final int cp1 = text.codePointAt(pos);
				final int kind = (Character.isLetterOrDigit(cp1) || cp1 == '_') ? 0 : 1;
				while (pos < len) {
					final int cp = text.codePointAt(pos);
					if (Character.isWhitespace(cp) || ((Character.isLetterOrDigit(cp) || cp == '_') ? 0 : 1) != kind)
						break;
					pos += Character.charCount(cp);
				}
			}
			while (pos < len) {
				final int cp = text.codePointAt(pos);
				if (!Character.isWhitespace(cp))
					break;
				pos += Character.charCount(cp);
			}
		} else {
			while (pos > 0) {
				final int cp = text.codePointBefore(pos);
				if (!Character.isWhitespace(cp))
					break;
				pos -= Character.charCount(cp);
			}
			if (pos == 0)
				return 0;

			final int cp1 = text.codePointBefore(pos);
			final int kind = (Character.isLetterOrDigit(cp1) || cp1 == '_') ? 0 : 1;
			while (pos > 0) {
				final int cp = text.codePointBefore(pos);
				if (Character.isWhitespace(cp) || ((Character.isLetterOrDigit(cp) || cp == '_') ? 0 : 1) != kind)
					break;
				pos -= Character.charCount(cp);
			}
		}
		return pos;
	}

	public static int getBoundary(final @NonNull String text, final int pos, final int dir) {
		return Boolean.TRUE.equals(NavMappingsManager.getCurrentFeatureFlags().macStyleWordBoundaries())
				? getMacBoundary(text, pos, dir)
				: getWindowsLinuxBoundary(text, pos, dir);
	}
}
