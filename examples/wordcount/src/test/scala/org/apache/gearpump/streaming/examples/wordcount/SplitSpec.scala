/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.gearpump.streaming.examples.wordcount

import akka.actor.ActorSystem
import org.apache.gearpump.Message
import org.apache.gearpump.cluster.{UserConfig, TestUtil}
import org.apache.gearpump.streaming.StreamingTestUtil
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration._

class SplitSpec extends WordSpec with Matchers {

  "Split" should {
    "split the text and deliver to next task" in {
      val system1 = ActorSystem("Split", TestUtil.DEFAULT_CONFIG)
      val system2 = ActorSystem("Reporter", TestUtil.DEFAULT_CONFIG)
      val (_, echo) = StreamingTestUtil.createEchoForTaskActor(classOf[Split].getName, UserConfig.empty, system1, system2)
      Split.TEXT_TO_SPLIT.lines.foreach { line =>
        line.split(" ").foreach { msg =>
          echo.expectMsg(10 seconds, Message(msg))
        }
      }
      system1.shutdown()
      system2.shutdown()
    }
  }
}
