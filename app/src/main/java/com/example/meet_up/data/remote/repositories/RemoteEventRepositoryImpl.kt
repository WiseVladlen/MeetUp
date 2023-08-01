package com.example.meet_up.data.remote.repositories

import android.util.Log
import at.bitfire.dav4jvm.DavCollection
import biweekly.ICalendar
import biweekly.component.VEvent
import biweekly.component.VTimezone
import biweekly.io.TimezoneAssignment
import biweekly.property.Attendee
import biweekly.property.DateEnd
import biweekly.property.DateStart
import biweekly.property.LastModified
import biweekly.property.Location
import biweekly.property.Organizer
import biweekly.property.Summary
import com.example.meet_up.data.local.UserStorage
import com.example.meet_up.data.remote.HttpClientFactory
import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.domain.repositories.RemoteEventRepository
import com.example.meet_up.tools.toFilename
import com.example.meet_up.tools.toUsername
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedReader
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

private val TAG = RemoteEventRepositoryImpl::class.java.simpleName

class RemoteEventRepositoryImpl @Inject constructor(
    private val httpClientFactory: HttpClientFactory,
) : RemoteEventRepository {

    override fun putEvent(eventModel: EventModel) {
        val event = VEvent().apply {
            dateStart = DateStart(eventModel.startDate)
            dateEnd = DateEnd(eventModel.endDate)

            summary = Summary(eventModel.title)
            location = Location(eventModel.room.title)
            lastModified = LastModified(Calendar.getInstance().time)
            organizer = Organizer(UserStorage.user.login.toUsername(), UserStorage.user.login)

            eventModel.users.forEach { attendee ->
                addAttendee(Attendee(attendee.login.toUsername(), attendee.login))
            }
        }

        val calendar = ICalendar().apply {
            addEvent(event)

            removeProperty(productId)

            val timeZone = TimeZone.getDefault()

            timezoneInfo.defaultTimezone = TimezoneAssignment(timeZone, VTimezone(timeZone.id))
        }

        DavCollection(httpClientFactory.client, getFileLocation(eventModel.id).toHttpUrl()).run {
            put(calendar.write().toRequestBody()) { response ->
                Log.i(TAG, response.message)
            }
        }
    }

    override fun deleteEvent(eventId: String) {
        DavCollection(httpClientFactory.client, getFileLocation(eventId).toHttpUrl()).run {
            delete { response ->
                Log.i(TAG, response.message)
            }
        }
    }

    override fun getEventIdList(callback: (pathList: List<String>) -> Unit) {
        DavCollection(httpClientFactory.client, getFolderLocation().toHttpUrl()).run {
            get("/", null) { response ->
                if (response.isSuccessful) {
                    val body = response.body ?: return@get

                    val eventPathList = BufferedReader(body.byteStream().reader()).readLines().filter { line ->
                        line.contains(CALENDAR_FILE_EXTENSION)
                    }

                    val result = eventPathList.map { path ->
                        path.toFilename(CALENDAR_FILE_EXTENSION)
                    }

                    callback(result)
                }
            }
        }
    }

    private fun getFileLocation(eventId: String): String {
        return "${YANDEX_CALENDAR_SERVER_URI}/${UserStorage.user.login}/${YANDEX_CALENDAR_ID}/${eventId}${CALENDAR_FILE_EXTENSION}"
    }

    private fun getFolderLocation(): String {
        return "${YANDEX_CALENDAR_SERVER_URI}/${UserStorage.user.login}/${YANDEX_CALENDAR_ID}"
    }

    companion object {
        private const val YANDEX_CALENDAR_SERVER_URI = "https://caldav.yandex.ru/calendars"

        private const val YANDEX_CALENDAR_ID = "events-24483602"

        private const val CALENDAR_FILE_EXTENSION = ".ics"
    }
}