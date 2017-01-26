/*
 * Copyright 2001-2013 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalatest.prop

import org.scalactic.anyvals._
import org.scalatest._

class ConfigurationSpec extends FunSpec with Matchers with GeneratorDrivenPropertyChecks {

  describe("Configuration.Parameter") {
    import Generator._
    implicit def configGen: Generator[Configuration.Parameter] =
      for {
        minSuccessful <- posIntGenerator
        maxDiscardedFactor <- posZDoubleGenerator
        minSize <- posZIntGenerator
        sizeRange <- posZIntsBetween(0, PosZInt.ensuringValid(PosZInt.MaxValue - minSize))
        workers <- posIntGenerator
      } yield {

        Configuration.Parameter(minSuccessful, maxDiscardedFactor, minSize, sizeRange, workers)
      }

    it("should offer a maxSize method that is minSize + SizeRange") {
      forAll { (param: Configuration.Parameter) =>
        param.maxSize.value shouldEqual (param.minSize + param.sizeRange)
      }
    }

    it("should throw IllegalArgumentException when the result of minSize + sizeRange goes out of range") {
      assertThrows[IllegalArgumentException] {
        Configuration.Parameter(PosInt(5), PosZDouble(0.5), PosZInt.MaxValue, PosZInt(1), PosInt(1))
      }
    }

  }

}