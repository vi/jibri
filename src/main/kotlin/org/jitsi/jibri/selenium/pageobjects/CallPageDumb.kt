/*
 * Copyright @ 2018 Atlassian Pty Ltd
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
 *
 */

package org.jitsi.jibri.selenium.pageobjects

import org.jitsi.jibri.util.extensions.debug
import org.jitsi.jibri.util.extensions.error
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.WebDriverWait
import java.util.logging.Logger

/**
 * Page object representing a dumb, non-jitsimeet page
 */
class CallPageDumb(driver: RemoteWebDriver) : AbstractPageObject(driver), ICallPage {
    private val logger = Logger.getLogger(this::class.qualifiedName)

    init {
        PageFactory.initElements(driver, this)
    }

    override fun visit(url: String): Boolean {
        logger.debug("Visiting url $url")
        if (!super.visit(url)) {
            return false
        }
        val start = System.currentTimeMillis()
        return try {
            WebDriverWait(driver, 30).until {
                val result = driver.executeScript("""
                    return true;
                    """.trimMargin()
                )
                when (result) {
                    is Boolean -> result
                    else -> false
                }
            }
            val totalTime = System.currentTimeMillis() - start
            logger.info("Waited $totalTime milliseconds for call page to load")
            true
        } catch (t: TimeoutException) {
            logger.error("Timed out waiting for call page to load")
            false
        }
    }

    override fun getNumParticipants(): Long = 2

    override fun injectParticipantTrackerScript(): Boolean = true

    override fun getParticipants(): List<Map<String, Any>> = emptyList()

    /**
     * Add the given key, value pair to the presence map and send a new presence
     * message
     */
    @Suppress("UNUSED_PARAMETER")
    override fun addToPresence(key: String, value: String): Boolean = true

    override fun sendPresence(): Boolean = true

    override fun leave(): Boolean = true
}
