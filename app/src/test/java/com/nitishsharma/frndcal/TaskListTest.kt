package com.nitishsharma.frndcal

import com.nitishsharma.frndcal.main.tasklist.datamodel.TaskListRequestModel
import com.nitishsharma.frndcal.main.tasklist.repository.TaskListRemoteRepository
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TaskListTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var taskListApi: TaskListRemoteRepository.TaskListAPIService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        taskListApi = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(TaskListRemoteRepository.TaskListAPIService::class.java)
    }

    @Test
    suspend fun testGetTask(): Unit {
        val mockResponse = MockResponse()
        mockResponse.setBody("[]")
        mockWebServer.enqueue(mockResponse)
        val response = taskListApi.getTaskList(TaskListRequestModel(2210))
        mockWebServer.takeRequest()
        Assert.assertEquals(true, response.taskList?.isEmpty())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}