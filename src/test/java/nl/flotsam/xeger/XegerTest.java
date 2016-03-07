/**
 * Copyright 2009 Wilfred Springer
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.flotsam.xeger;

import com.github.dakusui.jcunit.runners.standard.JCUnit;
import com.github.dakusui.jcunit.runners.standard.annotations.FactorField;
import com.github.dakusui.jcunit.runners.standard.annotations.Precondition;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.MessageFormat;
import java.util.Random;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

/**
 * Xeger doesn't support POSIX character classes of Java.
 * Xeger doesn't support java.lang.Character classes of Java.
 * Xeger doesn't support Predefined character classes of Java
 */
@RunWith(JCUnit.class)
public class XegerTest {
  @FactorField(stringLevels = {
      ".",
      "A",
      "[A]",
      "[AB]",
      "[A-C]",
      //      "\\p{javaLowerCase}"
      "\\w"
  })
  public String characterClass1;

  @FactorField(stringLevels = {
      "?",
      "*",
      "+",
      "{1,2}",
      "{1,3}",
      "{2,3}", })
  public String quantifier1;

  @FactorField(stringLevels = {
      "a",
      "[a]",
      "[ab]",
      "[a-c]" })
  public String characterClass2;

  @FactorField(stringLevels = {
      "?",
      "*",
      "+",
      "{1,2}",
      "{1,3}",
      "[a-c]" })
  public String quantifier2;

  /**
   * 0 ... characterClass1
   * 1 ... quantifier1
   * 2 ... characterClass2
   * 3 ... quantifier2
   */
  @FactorField(stringLevels = {
      "{0}{1}{2}{3}",
      " ({0}{1}{2}){3}",
      " ({0}{1}|{2}){3}",
      " (({0}|{2}){1}){3}",
  })
  public String structure;

  @Precondition
  public boolean check() {
    //return "\\p{javaLowerCase}".equals(this.characterClass1);
    return "\\w".equals(this.characterClass1);
  }

  @Test
  public void shouldGenerateTextCorrectly() {
    String regex = this.composeRegex();
    System.out.println(regex);
    Xeger generator = new Xeger(regex, new Random(1));
    for (int i = 0; i < 100; i++) {
      String text = generator.generate().replaceAll("[\\u000a\\u000d]", " ");
      System.out.println("<" + text + ">");
      assertTrue(
          String.format("Generated text '%s' didn't match regex '%s", text, regex),
          Pattern.compile(
              regex,
              Pattern.CANON_EQ//Pattern.UNIX_LINES //Pattern.MULTILINE//Pattern.UNICODE_CASE //Pattern.DOTALL //
          ).matcher(text).matches());
    }
  }

  String composeRegex() {
    return MessageFormat.format(
        this.structure,
        this.characterClass1, this.quantifier1,
        this.characterClass2, this.quantifier2
    );
  }
}
