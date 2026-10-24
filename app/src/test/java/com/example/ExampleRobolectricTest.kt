package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ВРЫВ", appName)
  }

  @Test
  fun `verify initial mock data and repository operations`() {
    val repository = com.example.data.VryvRepository.instance
    val loginResult = repository.login("max.vryv@gmail.com", "TechnoMinsk2026")
    assert(loginResult.first)

    val events = repository.events.value
    assert(events.isNotEmpty())
    
    val firstEvent = events.first()
    val initialBookmark = firstEvent.isBookmarked
    repository.toggleSaveEvent(firstEvent.id)
    val updatedEvent = repository.events.value.first { it.id == firstEvent.id }
    assertEquals(!initialBookmark, updatedEvent.isBookmarked)

    // Test going plan toggle
    val initialGoing = updatedEvent.isGoing
    repository.toggleGoingEvent(firstEvent.id)
    val afterGoing = repository.events.value.first { it.id == firstEvent.id }
    assertEquals(!initialGoing, afterGoing.isGoing)
  }

  @Test
  fun `verify MainActivity launches and initializes successfully`() {
    val controller = org.robolectric.Robolectric.buildActivity(MainActivity::class.java)
    controller.setup()
    assert(controller.get() != null)
  }
}
