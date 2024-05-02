package com.nitishsharma.frndcal

import com.nitishsharma.frndcal.main.tasklist.datamodel.TaskListRequestModel
import com.nitishsharma.frndcal.main.tasklist.repository.TaskListRemoteRepository
import kotlinx.coroutines.runBlocking
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
    val taskListJson = """
        {
            "tasks": [
                {
                    "task_id": 2164,
                    "task_detail": {
                        "date": "25-10-2029",
                        "title": "Cc",
                        "description": "Ff"
                    }
                },
                {
                    "task_id": 2165,
                    "task_detail": {
                        "date": "1-5-2024",
                        "title": "Haan Vi, Loat Aa Kamm Ke Nahi!?",
                        "description": "Checking on my friend if he's doing well or not!"
                    }
                },
                {
                    "task_id": 2166,
                    "task_detail": {
                        "date": "10-5-2024",
                        "title": "Set Sutt",
                        "description": "Party poorty plan with friends "
                    }
                },
                {
                    "task_id": 2167,
                    "task_detail": {
                        "date": "10-5-2024",
                        "title": "Dummy Title",
                        "description": "Dummy description"
                    }
                }
            ]
        }
    """.trimIndent()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        taskListApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskListRemoteRepository.TaskListAPIService::class.java)
    }

    @Test
    fun testResponseNotEmpty() = runBlocking {
        val mockResponse = MockResponse().setBody(taskListJson)
        mockWebServer.enqueue(mockResponse)

        val response = taskListApi.getTaskList(TaskListRequestModel(2210))

        val request = mockWebServer.takeRequest()
        Assert.assertEquals("/api/getCalendarTaskList", request.path)

        Assert.assertTrue(!response.taskList.isNullOrEmpty())
    }

    @Test
    fun testResponseSize() = runBlocking {
        val mockResponse = MockResponse().setBody(taskListJson)
        mockWebServer.enqueue(mockResponse)

        val response = taskListApi.getTaskList(TaskListRequestModel(2210))

        Assert.assertEquals(4, response.taskList?.size)
    }

    @Test
    fun testFirstTaskId() = runBlocking {
        val mockResponse = MockResponse().setBody(taskListJson)
        mockWebServer.enqueue(mockResponse)

        val response = taskListApi.getTaskList(TaskListRequestModel(2210))

        Assert.assertEquals(2164, response.taskList?.get(0)?.taskId)
    }

    @Test
    fun testFirstTaskDate() = runBlocking {
        val mockResponse = MockResponse().setBody(taskListJson)
        mockWebServer.enqueue(mockResponse)

        val response = taskListApi.getTaskList(TaskListRequestModel(2210))

        Assert.assertEquals("25-10-2029", response.taskList?.get(0)?.taskDetail?.taskDate)
    }

    @Test
    fun testFirstTaskTitle() = runBlocking {
        val mockResponse = MockResponse().setBody(taskListJson)
        mockWebServer.enqueue(mockResponse)

        val response = taskListApi.getTaskList(TaskListRequestModel(2210))

        Assert.assertEquals("Cc", response.taskList?.get(0)?.taskDetail?.taskTitle)
    }

    @Test
    fun testFirstTaskDescription() = runBlocking {
        val mockResponse = MockResponse().setBody(taskListJson)
        mockWebServer.enqueue(mockResponse)

        val response = taskListApi.getTaskList(TaskListRequestModel(2210))

        Assert.assertEquals("Ff", response.taskList?.get(0)?.taskDetail?.taskDescription)
    }

    @Test
    fun runTests() {
        testFirstTaskDescription()
        testFirstTaskTitle()
        testFirstTaskDate()
        testFirstTaskId()
        testResponseSize()
        testResponseNotEmpty()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}

