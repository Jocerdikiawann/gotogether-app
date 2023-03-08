package com.example.livetracking.domain.utils

enum class ErrorStatus {
    OK,
    NOT_FOUND,
    ZERO_RESULT,
    MAX_WAYPOINTS_EXCEEDED,
    MAX_ROUTE_LENGTH_EXCEEDED,
    INVALID_ARGUMENT,
    INVALID_REQUEST,
    OVER_DAILY_LIMIT,
    OVER_QUERY_LIMIT,
    REQUEST_DENIED,
    UNKNOWN_ERROR,
}