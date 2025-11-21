package com.blueventor.network.response

/**
{
  "message": "Attendance details fetched successfully.",
  "status": 1,
  "details": {
    "driver_id": "5",
    "company_id": "1",
    "month": 10,
    "year": 2025,
    "attendance_details": [
      {
        "date": "01-10-2025",
        "login_hours": "11 hrs 51 mins"
      },
      {
        "date": "02-10-2025",
        "login_hours": "18 hrs 5 mins"
      },
      {
        "date": "03-10-2025",
        "login_hours": "24 hrs 0 mins"
      },
      {
        "date": "04-10-2025",
        "login_hours": "18 hrs 23 mins"
      },
      {
        "date": "05-10-2025",
        "login_hours": "1 hrs 43 mins"
      },
      {
        "date": "06-10-2025",
        "login_hours": "2 hrs 38 mins"
      },
      {
        "date": "07-10-2025",
        "login_hours": "0 hrs 25 mins"
      },
      {
        "date": "08-10-2025",
        "login_hours": "16 hrs 54 mins"
      },
      {
        "date": "09-10-2025",
        "login_hours": "24 hrs 0 mins"
      },
      {
        "date": "10-10-2025",
        "login_hours": "21 hrs 39 mins"
      },
      {
        "date": "11-10-2025",
        "login_hours": "1 hrs 30 mins"
      },
      {
        "date": "12-10-2025",
        "login_hours": "0 hrs 0 mins"
      },
      {
        "date": "13-10-2025",
        "login_hours": "0 hrs 0 mins"
      },
      {
        "date": "14-10-2025",
        "login_hours": "8 hrs 24 mins"
      },
      {
        "date": "15-10-2025",
        "login_hours": "8 hrs 20 mins"
      },
      {
        "date": "16-10-2025",
        "login_hours": "0 hrs 0 mins"
      },
      {
        "date": "17-10-2025",
        "login_hours": "1 hrs 31 mins"
      },
      {
        "date": "18-10-2025",
        "login_hours": "0 hrs 0 mins"
      },
      {
        "date": "19-10-2025",
        "login_hours": "8 hrs 10 mins"
      },
      {
        "date": "20-10-2025",
        "login_hours": "14 hrs 55 mins"
      },
      {
        "date": "21-10-2025",
        "login_hours": "0 hrs 51 mins"
      },
      {
        "date": "22-10-2025",
        "login_hours": "0 hrs 0 mins"
      },
      {
        "date": "23-10-2025",
        "login_hours": "0 hrs 0 mins"
      },
      {
        "date": "24-10-2025",
        "login_hours": "0 hrs 0 mins"
      },
      {
        "date": "25-10-2025",
        "login_hours": "0 hrs 22 mins"
      },
      {
        "date": "26-10-2025",
        "login_hours": "0 hrs 16 mins"
      },
      {
        "date": "27-10-2025",
        "login_hours": "0 hrs 3 mins"
      },
      {
        "date": "28-10-2025",
        "login_hours": "0 hrs 22 mins"
      },
      {
        "date": "29-10-2025",
        "login_hours": "0 hrs 3 mins"
      },
      {
        "date": "30-10-2025",
        "login_hours": "0 hrs 0 mins"
      },
      {
        "date": "31-10-2025",
        "login_hours": "0 hrs 0 mins"
      }
    ],
    "total_login_time": "184 hrs 25 mins",
    "average_login_time": "8 hrs 22 mins"
  }
}
*/
data class ResponseDriverAttendanceDetails(
    val details: Details,
    val message: String,
    val status: Int
) {
    data class Details(
        val attendance_details: List<AttendanceDetail>,
        val average_login_time: String,
        val company_id: String,
        val driver_id: String,
        val month: String,
        val total_login_time: String,
        val year: String
    ) {
        data class AttendanceDetail(
            val date: String,
            val login_hours: String
        )
    }
}